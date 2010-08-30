package kakkun61.wajavassr;

public class FriendHitokoto {
    /** 本文（HTML版） */
    private String html;

    /** 本文（text版） */
    private String text;

    /** このヒトコトのRID（連番） */
    private String rid;

    /** このヒトコトのID */
    private long id;

    /** このヒトコトのURL つまり、http://wassr.jp/user/{@linkplain #userLoginId}/statuses/{@linkplain #rid} */
    private String link;

    /** ユーザID */
    private String userLoginId;

    /** ユーザ名 */
    private String userScreenName;

    /** プロフィール画像のURL */
    private String userProfileImageUrl;

    /** 友達のみに公開かどうか */
    private boolean userProtected;

    /** 添付画像のURL */
    private String photoUrl;

    /** 添付画像のサムネイルのURL */
    private String photoThombnailUrl;

    /** イイネした人のユーザID */
    private String[] favorites;

    /** リプライ先ヒトコト（text版） */
    private String replyMessage;

    /** リプライ先ヒトコトのURL */
    private String replyStatusUrl;

    /** リプライ先ヒトコトの投稿者のログイン名 */
    private String replyUserLoginId;

    /** リプライ先ヒトコトの投稿者のユーザ名 */
    private String replyUserNick;

    /** 位置情報の名前? */
    private String areaname;

    /** 位置情報のコード? */
    private String areacode;

    /** エポック（秒） */
    long epoch;

    /** Second Life URL? */
    private String slurl;

    public FriendHitokoto(
            String html,
            String text,
            String rid,
            long id,
            String link,
            String userLoginId,
            String userScreenName,
            String userProfileImageUrl,
            boolean userProtected,
            String photoUrl,
            String photoThombnailUrl,
            String[] favorites,
            String replyMessage,
            String replyStatusUrl,
            String replyUserLoginId,
            String replyUserNick,
            String areaname,
            String areacode,
            long epoch,
            String slurl) {
        super();
        this.html = html;
        this.text = text;
        this.rid = rid;
        this.id = id;
        this.link = link;
        this.userLoginId = userLoginId;
        this.userScreenName = userScreenName;
        this.userProfileImageUrl = userProfileImageUrl;
        this.userProtected = userProtected;
        this.photoUrl = photoUrl;
        this.photoThombnailUrl = photoThombnailUrl;
        this.favorites = favorites;
        this.replyMessage = replyMessage;
        this.replyStatusUrl = replyStatusUrl;
        this.replyUserLoginId = replyUserLoginId;
        this.replyUserNick = replyUserNick;
        this.areaname = areaname;
        this.areacode = areacode;
        this.epoch = epoch;
        this.slurl = slurl;
    }

    /**
     * 本文（HTML版）を得る。
     * @return
     */
    public String getHtml() {
        return html;
    }

    /**
     * 本文（text版）を得る。
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * このヒトコトのRID（連番）を得る。
     * @return
     */
    public String getRid() {
        return rid;
    }

    /**
     * このヒトコトのIDを得る。
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * このヒトコトのURL つまり、http://wassr.jp/user/{@linkplain #userLoginId}/statuses/{@linkplain #rid}を得る。
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     * ユーザIDを得る。
     * @return
     */
    public String getUserLoginId() {
        return userLoginId;
    }

    /**
     * ユーザ名を得る。
     * @return
     */
    public String getUserScreenName() {
        return userScreenName;
    }

    /**
     * プロフィール画像のURLを得る。
     * @return
     */
    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    /**
     * 友達のみに公開かどうかを得る。
     * @return
     */
    public boolean getUserProtected() {
        return userProtected;
    }

    /**
     * 添付画像のURLを得る。
     * @return
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * 添付画像のサムネイルのURLを得る。
     * @return
     */
    public String getPhotoThombnailUrl() {
        return photoThombnailUrl;
    }

    /**
     * イイネした人のユーザIDを得る。
     * @return
     */
    public String[] getFavorites() {
        return favorites;
    }

    /**
     * リプライ先ヒトコト（text版）を得る。
     * @return
     */
    public String getReplyMessage() {
        return replyMessage;
    }

    /**
     * リプライ先ヒトコトのURLを得る。
     * @return
     */
    public String getReplyStatusUrl() {
        return replyStatusUrl;
    }

    /**
     * リプライ先ヒトコトの投稿者のログイン名を得る。
     * @return
     */
    public String getReplyUserLoginId() {
        return replyUserLoginId;
    }

    /**
     * リプライ先ヒトコトの投稿者のユーザ名を得る。
     * @return
     */
    public String getReplyUserNick() {
        return replyUserNick;
    }

    /**
     *位置情報の名前? を得る。
     * @return
     */
    public String getAreaname() {
        return areaname;
    }

    /**
     * 位置情報のコード?を得る。
     * @return
     */
    public String getAreacode() {
        return areacode;
    }

    /**
     * エポック（秒）を得る。
     * @return
     */
    public long getEpoch() {
        return epoch;
    }

    /**
     * Second Life URL?を得る。
     * @return
     */
    public String getSlurl() {
        return slurl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof FriendHitokoto))
            return false;
        FriendHitokoto h = (FriendHitokoto)obj;
        if(getRid().equals(h.getRid()))
            return true;
        return false;
    }
}
