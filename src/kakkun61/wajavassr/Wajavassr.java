package kakkun61.wajavassr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Wajavassr {
    private final String wassr = "http://api.wassr.jp";
    private String auth = null;
    private static final String DEFAULT_USER_AGENT = "Wajavassr/00.01";
    private String userAgent = DEFAULT_USER_AGENT;
    private static final String DEFAULT_CLIENT_NAME = "Wajavassr";
    private String clientName = DEFAULT_CLIENT_NAME;
    private final JSONParser parser = new JSONParser();

    /**
     * クライアントを作る。認証が必要な時は、先に必ず {@link #setUser(String, String)} を呼び出すこと。
     */
    public Wajavassr() {
    }

    /**
     * ログイン名・パスワードを指定してクライアントを作る。
     * 
     * @param user ログイン名
     * @param password パスワード or 認証トークン
     */
    public Wajavassr(String user, String password) {
        setUser(user, password);
    }

    /**
     * ログイン名・パスワードを設定。
     * 
     * @param user ログイン名
     * @param password パスワード or 認証トークン
     */
    public void setUser(String user, String password) {
        auth = createAuthentication(user, password);
    }

    /**
     * User Agent を得る。
     * 
     * @return
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * User Agent を設定する。引数 {@code userAgent} が {@code null} の場合デフォルト値に設定。
     * 
     * @param userAgent 新たに設定する User Agent。{@code null} の場合デフォルト値に設定。
     */
    public void setUserAgent(String userAgent) {
        if (userAgent == null)
            this.userAgent = DEFAULT_USER_AGENT;
        else this.userAgent = userAgent;
    }

    /**
     * クライアント名（via ***）を得る。
     * 
     * @return
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * クライアント名を設定する。クライアント名は、Wassr 上で via *** と表示されるもの。引数 {@code clentName} が {@code null} の場合デフォルト値に設定。
     * 
     * @param clientName
     */
    public void setClientName(String clientName) {
        if (clientName == null)
            this.clientName = DEFAULT_CLIENT_NAME;
        this.clientName = clientName;
    }

    /**
     * 自分への返信を得る。{@code fromPage} から {@code toPage}-1 までのページを読み込む。
     * 
     * @param fromPage 読み込むページの上端点。
     * @param toPage 読み込むページの下端点。（これを含まない）
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException
     * @throws ParseException
     */
    public List<FriendHitokoto> getReplies(int fromPage, int toPage) throws IOException, ParseException {
        if (fromPage <= 0)
            throw new IllegalArgumentException("引数 fromPage は正でないといけません。: " + fromPage);
        if (toPage <= 0)
            throw new IllegalArgumentException("引数 toPage は正でないといけません。: " + toPage);
        if (toPage <= fromPage)
            throw new IllegalArgumentException("fromPage < toPage でないといけません。" + fromPage + " < " + toPage);
        return getFriendHitokotos("/statuses/replies.json", null, fromPage, toPage, true);
    }

    /**
     * 友達タイムラインを得る。普通、単に「タイムライン」と呼ばれるもの。{@code fromPage} から {@code toPage}-1 までのページを読み込む。
     * 
     * @param fromPage 読み込むページの上端点。
     * @param toPage 読み込むページの下端点。（これを含まない）
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException 読み込みに失敗。
     * @throws ParseException パースに失敗。{@link org.json.simple.parser.JSONParser#parse(java.io.Reader)} による例外。
     */
    public List<FriendHitokoto> getFriendTimeline(int fromPage, int toPage) throws IOException, ParseException {
        if (fromPage <= 0)
            throw new IllegalArgumentException("引数 fromPage は正でないといけません。: " + fromPage);
        if (toPage <= 0)
            throw new IllegalArgumentException("引数 toPage は正でないといけません。: " + toPage);
        if (toPage <= fromPage)
            throw new IllegalArgumentException("fromPage < toPage でないといけません。" + fromPage + " < " + toPage);
        return getFriendHitokotos("/statuses/friends_timeline.json", null, fromPage, toPage, true);
    }

    /**
     * ユーザタイムラインを得る。
     * 
     * @param id ユーザID。
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException 読み込みに失敗。
     * @throws ParseException パースに失敗。{@link org.json.simple.parser.JSONParser#parse(java.io.Reader)} による例外。指定したユーザが存在しない場合もこれが投げられる。
     */
    public List<FriendHitokoto> getUserTimeline(String id) throws IOException, ParseException {
        if (id == null)
            throw new IllegalArgumentException("引数 id に null は指定できません。");
        if (id.isEmpty())
            throw new IllegalArgumentException("引数 id に空文字列は指定できません。");
        // 購読関係にあっても、鍵っ子のヒトコトは読めないので、認証はしない。ページ指定をしては、有効ではないのでページ指定しない。
        return getFriendHitokotos("/statuses/user_timeline.json", id, 0, 0, false);
    }

    /**
     * 友達ヒトコト形式で得られるタイムライン用の取得メソッド。{@code fromPage} から {@code toPage}-1 までのページを読み込む。ページの指定をしないときは、{@code fromPage}, {@code toPage} ともに {@code 0}。
     * 
     * @param path ルート以下のパス。“http://api.wassr.jp/statuses/friends_timeline.json” の場合、“/statuses/friends_timeline.json”。
     * @param id ユーザID。指定しない時は {@code null}。
     * @param fromPage 読み込むページの上端点。（非負)
     * @param toPage 読み込むページの下端点。（これを含まない）（非負)
     * @param authorization 認証が必要かどうか。必要なら、{@code true}。
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException 読み込みに失敗。
     * @throws ParseException パースに失敗。{@link org.json.simple.parser.JSONParser#parse(java.io.Reader)} による例外。
     */
    protected List<FriendHitokoto> getFriendHitokotos(String path, String id, int fromPage, int toPage, boolean authorization) throws IOException, ParseException {
        // ページ指定なし
        if (fromPage == 0 && toPage == 0)
            return parseJsonFriendHitokoto(createConnectedReader(path, id, 0, authorization));

        List<FriendHitokoto> hitokotos = new ArrayList<FriendHitokoto>(20*(toPage-fromPage));
        for (int page=fromPage; page<toPage; page++) {
            List<FriendHitokoto> hs = parseJsonFriendHitokoto(createConnectedReader(path, id, page, authorization) );
            if(hs.size() == 0)
                break;
            // ダブるヒトコトを省いて結合
            int i;
            for (i=0; i<hitokotos.size(); i++) {
                if (hitokotos.get(i).equals(hs.get(0)))
                    break;
            }
            hitokotos.addAll(hs.subList(hitokotos.size()-i, hs.size()));
        }
        return hitokotos;
    }

    /**
     * データ取得用リーダを作る。
     * 
     * @param path path ルート以下のパス。“http://api.wassr.jp/statuses/friends_timeline.json” の場合、“/statuses/friends_timeline.json”。
     * @param id id ユーザID。指定しない時は {@code null}。
     * @param page 読み込むページ。指定しない時は {@code 0}。
     * @param authorization authorization 認証が必要かどうか。必要なら、{@code true}。
     * @return データ取得用リーダ。
     * @throws IOException コネクションの確立または読み込みに失敗。
     */
    protected Reader createConnectedReader(String path, String id, int page, boolean authorization) throws IOException {
        if (id != null)
        {
            if (page != 0)
                path = path + "?id=" + id + "&page=" + page;
            else
                path = path + "?id=" + id;
        } else {
            if (page != 0)
                path = path + "?page=" + page;
        }
        HttpURLConnection c = createHttpURLConnection("GET", path, authorization);
        c.connect();
        System.err.println("connect: " + path + " -> " + c.getResponseCode() + " " + c.getResponseMessage());
        if(c.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new IOException("Server returned HTTP response code: \"" + c.getResponseCode() + " " + c.getResponseMessage() + "\" for URL: " + c.getURL());
        return new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));
    }

    /**
     * Wassr に対し、指定されたパスに指定された HTTP メソッドでコネクションを張る。
     * 
     * @param method HTTP メソッド。GET、POST など。
     * @param path ルート以下のパス。“http://api.wassr.jp/statuses/friends_timeline.json” の場合、“/statuses/friends_timeline.json”。
     * @param authorization 認証が必要かどうか。必要なら、{@code true}。
     * @return 確立したコネクション
     * @throws IOException コネクションの確立に失敗。
     */
    protected HttpURLConnection createHttpURLConnection(String method, String path, boolean authorization) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(wassr + path).openConnection();
        c.setRequestMethod(method);
        c.setRequestProperty("User-Agent", userAgent);
        if (authorization)
            c.setRequestProperty("Authorization", "Basic " + auth);
        return c;
    }

//    /**
//     * パラメータを文字列にし、パスを作る。
//     * 
//     * @param path
//     * @param params URL に付加するパラメータ。n×2 の2次元配列。“new String[][]{{"page", "2"}, {"id", "xxx"}}” を渡した場合、{@code path} パラメータの後ろに、“?page=2&id=xxx” が付加される。パラメータなしの場合は {@code null}。URL エンコード（Percent-Encoding）は実装していない。
//     * @return
//     */
//    protected String makePath(String path, String[][] params) {
//        if(params != null) {
//            StringBuilder sb = new StringBuilder(path + "?");
//            for(int i=0; i<params.length; i++) {
//                if(params[i].length != 2)
//                    throw new IllegalArgumentException("第2引数 params は、n×2 の2次元配列でないといけません。: " + Arrays.deepToString(params));
//                if(i != 0)
//                    sb.append("&");
//                sb.append(params[i][0] + "=" + params[i][1]);
//            }
//            path = sb.toString();
//        }
//        return path;
//    }

    /**
     * 認証文字列の作成
     * 
     * @param user ログイン名
     * @param password パスワード or 認証トークン
     * @return authentication 認証文字列
     */
    protected static String createAuthentication(String user, String password) {
        return new String(Base64.encodeBase64((user + ":" + password).getBytes()));
    }

    /**
     * JSON を読み出すリーダから {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >} を作る。
     * 
     * @param r
     * @return
     * @throws IOException
     * @throws ParseException
     */
    protected List<FriendHitokoto> parseJsonFriendHitokoto(Reader r) throws IOException, ParseException {
        JSONArray jhs; // Json HitokotoS の略
        try {
            jhs = (JSONArray) parser.parse(r);
        } finally {
            r.close();
        }
        List<FriendHitokoto> hitokotos = new ArrayList<FriendHitokoto>(20);
        for (Object o : jhs) {
            JSONObject jh = (JSONObject) o;
            JSONObject user = (JSONObject) jh.get("user");
            Object[] favorites = ((JSONArray) jh.get("favorites")).toArray();
            hitokotos.add(new FriendHitokoto(
                    (String) jh.get("html"),
                    (String) jh.get("text"),
                    (String) jh.get("rid"),
                    Long.parseLong((String) jh.get("id")),
                    (String) jh.get("link"),
                    (String) jh.get("user_login_id"),
                    (String) user.get("screen_name"),
                    (String) user.get("profile_image_url"),
                    (Boolean) user.get("protected"),
                    (String) jh.get("photo_url"),
                    (String) jh.get("photo_thombnail_url"),
                    Arrays.copyOf(favorites, favorites.length, String[].class),
                    (String) jh.get("reply_message"),
                    (String) jh.get("reply_status_url"),
                    (String) jh.get("reply_user_login_id"),
                    (String) jh.get("reply_user_nick"),
                    (String) jh.get("areaname"),
                    (String) jh.get("areacode"),
                    (Long) jh.get("epoch"),
                    (String) jh.get("slurl")));
        }
        return hitokotos;
    }

    /**
     * Wassr の認証用 URL 文字列の生成。
     * 
     * @param appKey アプリケーションキー
     * @param secretKey 秘密鍵
     * @return 認証用 URL の文字列
     * @throws NoSuchAlgorithmException 指定したアルゴリズムの MacSpi 実装をサポートするプロバイダが存在しない場合。{@link javax.crypto.Mac.getInstance(String)} による例外。
     * @throws InvalidKeyException 指定された鍵がこの MAC の初期化に不適切な場合。{@link javax.crypto.Mac.init(Key)} による例外。
     */
    public static String createAuthUrl(String appKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec sk = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sk);
        byte[] result = mac.doFinal(("app_key" + appKey).getBytes());
        String sig = new String(Hex.encodeHex(result));
        return "http://wassr.jp/auth/?app_key=" + appKey + "&sig=" + sig;
    }
}
