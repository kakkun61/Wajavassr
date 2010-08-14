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
    protected final String wassr = "http://api.wassr.jp";
    protected String auth = null;
    protected static final String DEFAULT_USER_AGENT = "Wajavassr/00.03";
    protected String userAgent = DEFAULT_USER_AGENT;
    protected final JSONParser parser = new JSONParser();

    /**
     * クライアントを作る。後で必ず {@link #setUser(String, String)} を呼び出すこと。
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
        if (userAgent == null) this.userAgent = DEFAULT_USER_AGENT;
        else this.userAgent = userAgent;
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
    protected URLConnection createURLConnection(String method, String path, boolean authorization) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(wassr + path).openConnection();
        c.setRequestMethod(method);
        c.setRequestProperty("User-Agent", userAgent);
        if (authorization)
            c.setRequestProperty("Authorization", "Basic " + auth);
        return c;
    }

    /**
     * 自分への返信を得る。
     * 
     * @param page 何頁目を読み込むか。正数。
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException
     * @throws ParseException
     */
    public List<FriendHitokoto> getReplies(int page) throws IOException, ParseException {
        if(page <= 0)
            throw new IllegalArgumentException("引数 page は正でないといけません。: " + page);
        return getFriendHitokotos("/statuses/replies.json", new String[][]{{"page", String.valueOf(page)}}, true);
    }

    /**
     * 友達タイムラインを得る。普通、単に「タイムライン」と呼ばれるもの。
     * 
     * @param page 何頁目を読み込むか。正数。
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException 読み込みに失敗。
     * @throws ParseException パースに失敗。{@link org.json.simple.parser.JSONParser#parse(java.io.Reader)} による例外。
     */
    public List<FriendHitokoto> getFriendTimeline(int page) throws IOException, ParseException {
        if(page <= 0)
            throw new IllegalArgumentException("引数 page は正でないといけません。: " + page);
        return getFriendHitokotos("/statuses/friends_timeline.json", new String[][]{{"page", String.valueOf(page)}}, true);
    }

    /**
     * ユーザタイムラインを得る。
     * 
     * @param page 何頁目を読み込むか。正数。
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException 読み込みに失敗。
     * @throws ParseException パースに失敗。{@link org.json.simple.parser.JSONParser#parse(java.io.Reader)} による例外。
     */
    public List<FriendHitokoto> getUserTimeline(int page, String id) throws IOException, ParseException {
        if(page <= 0)
            throw new IllegalArgumentException("引数 page は正でないといけません。: " + page);
        // 購読関係にあっても、鍵っ子のヒトコトは読めないので、認証は false。
        return getFriendHitokotos("/statuses/user_timeline.json", new String[][]{{"page", String.valueOf(page)}, {"id", id}}, false);
    }

    /**
     * 友達ヒトコト形式で得られるタイムライン用の取得メソッド。
     * 
     * @param path ルート以下のパス。“http://api.wassr.jp/statuses/friends_timeline.json” の場合、“/statuses/friends_timeline.json”。{@link #createURLConnection(String, String, boolean)} の第2引数へ渡される。
     * @param params URL に付加するパラメータ。n×2 の2次元配列。“new String[][]{{"page", "2"}, {"id", "xxx"}}” を渡した場合、{@code path} パラメータの後ろに、“?page=2&id=xxx” が付加される。パラメータなしの場合は {@code null}。URL エンコード（Percent-Encoding）は実装していない。
     * @param authorization 認証が必要かどうか。必要なら、{@code true}。
     * @return 得られたヒトコトをパースした {@link java.util.List List}{@code <}{@link FriendHitokoto}{@code >}。
     * @throws IOException 読み込みに失敗。
     * @throws ParseException パースに失敗。{@link org.json.simple.parser.JSONParser#parse(java.io.Reader)} による例外。
     */
    protected List<FriendHitokoto> getFriendHitokotos(String path, String[][] params, boolean authorization) throws IOException, ParseException {
        return parseJsonFriendHitokoto(createConnectedReader(path, params, authorization));
    }

    /**
     * データ取得用リーダを作る。
     * 
     * @param path
     * @param params
     * @param authorization
     * @return
     * @throws IOException
     */
    protected Reader createConnectedReader(String path, String[][] params, boolean authorization) throws IOException {
        path = makePath(path, params);
        URLConnection c = createURLConnection("GET", path, authorization);
        return new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));
    }

    /**
     * パラメータを文字列にし、パスを作る。
     * 
     * @param path
     * @param params
     * @return
     */
    protected String makePath(String path, String[][] params) {
        if(params != null) {
            StringBuilder sb = new StringBuilder(path + "?");
            for(int i=0; i<params.length; i++) {
                if(params[i].length != 2)
                    throw new IllegalArgumentException("第2引数 params は、n×2 の2次元配列でないといけません。: " + Arrays.deepToString(params));
                if(i != 0)
                    sb.append("&");
                sb.append(params[i][0] + "=" + params[i][1]);
            }
            path = sb.toString();
        }
        return path;
    }

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
