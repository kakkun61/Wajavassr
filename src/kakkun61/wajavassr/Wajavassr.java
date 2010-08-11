package kakkun61.wajavassr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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
    public Wajavassr(){}

    /**
     * ログイン名・パスワードを指定してクライアントを作る。
     * @param user ログイン名
     * @param password パスワード or 認証トークン
     */
    public Wajavassr( String user, String password ) {
        setUser( user, password );
    }

    /**
     * ログイン名・パスワードを設定。
     * @param user ログイン名
     * @param password パスワード or 認証トークン
     */
    public void setUser( String user, String password ) {
        auth = createAuthentication( user, password );
    }

    /**
     * 認証文字列の作成
     * @param user ログイン名
     * @param password パスワード or 認証トークン
     * @return authentication 認証文字列
     */
    protected static String createAuthentication( String user, String password ) {
        return new String( Base64.encodeBase64( ( user + ":" + password ).getBytes() ) );
    }

    /**
     * User Agent を得る。
     * @return
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * User Agent を設定する。引数 {@code userAgent} が {@code null} の場合デフォルト値に設定。
     * @param userAgent 新たに設定する User Agent。{@code null} の場合デフォルト値に設定。
     */
    public void setUserAgent( String userAgent ) {
        if( userAgent == null )
            this.userAgent = DEFAULT_USER_AGENT;
        else
            this.userAgent = userAgent;
    }

    /**
     * Wassr に対し、指定されたパスに指定された HTTP メソッドでコネクションを張る。
     * @param method HTTP メソッド。GET、POST など。
     * @param path ルート以下のパス。“http://api.wassr.jp/statuses/friends_timeline.json” の場合、“/statuses/friends_timeline.json”
     * @return 確立したコネクション
     * @throws IOException コネクションの確立に失敗。
     */
    protected URLConnection createURLConnection( String method, String path, boolean authorization ) throws IOException {
        HttpURLConnection c = (HttpURLConnection)new URL( wassr + path ).openConnection();
        c.setRequestMethod( method );
        c.setRequestProperty( "User-Agent", userAgent );
        if(authorization)
            c.setRequestProperty( "Authorization", "Basic " + auth );
        return c;
    }

    /**
     * 自分への返信を得る。
     * @return 
     * @throws IOException
     * @throws ParseException
     */
    public JSONArray getReplies() throws IOException, ParseException {
        URLConnection c = createURLConnection( "GET", "/statuses/replies.json", true );
        c.connect();
        BufferedReader r = new BufferedReader( new InputStreamReader( c.getInputStream(), "UTF-8" ) );
        JSONArray json;
        try {
            json = (JSONArray)parser.parse( r );
        } finally {
            r.close();
        }
        return json;
    }

    /**
     *
     * @return
     * @throws IOException コネクションの確立に失敗、または読み込みの失敗。
     * @throws ParseException JSON のパースに失敗。
     */
    public List<FriendHitokoto> getFriendTimelie() throws IOException, ParseException {
        List<FriendHitokoto> hitokotos = new ArrayList<FriendHitokoto>();
        URLConnection c = createURLConnection( "GET", "/statuses/friends_timeline.json", true );
        BufferedReader r = new BufferedReader( new InputStreamReader( c.getInputStream(), "UTF-8" ) );
        JSONArray jhs; // Json Hitokotos
        try {
            jhs = (JSONArray)parser.parse( r );
        } finally {
            r.close();
        }
        for( Object o : jhs ) {
            JSONObject jh = (JSONObject)o;
            JSONObject user = (JSONObject)jh.get( "user" );
            Object[] favorites = ( (JSONArray)jh.get( "favorites" ) ).toArray();
            hitokotos.add(
                    new FriendHitokoto(
                            (String)jh.get( "html" ),
                            (String)jh.get( "text" ),
                            (String)jh.get("rid"),
                            Long.parseLong((String)jh.get( "id" )),
                            (String)jh.get( "link" ),
                            (String)jh.get( "user_login_id" ),
                            (String)user.get( "screen_name" ),
                            (String)user.get( "profile_image_url" ),
                            (Boolean)user.get( "protected" ),
                            (String)jh.get( "photo_url" ),
                            (String)jh.get( "photo_thombnail_url" ),
                            Arrays.copyOf( favorites, favorites.length, String[].class ),
                            (String)jh.get( "reply_message" ),
                            (String)jh.get( "reply_status_url" ),
                            (String)jh.get( "reply_user_login_id" ),
                            (String)jh.get( "reply_user_nick" ),
                            (String)jh.get( "areaname" ),
                            (String)jh.get( "areacode" ),
                            (Long)jh.get( "epoch" ),
                            (String)jh.get( "slurl" )
                    )
            );
        }
        return hitokotos;
    }
}
