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

    public String getHtml() {
        return html;
    }

    public String getText() {
        return text;
    }

    public String getRid() {
        return rid;
    }

    public long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public boolean getUserProtected() {
        return userProtected;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhotoThombnailUrl() {
        return photoThombnailUrl;
    }

    public String[] getFavorites() {
        return favorites;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public String getReplyStatusUrl() {
        return replyStatusUrl;
    }

    public String getReplyUserLoginId() {
        return replyUserLoginId;
    }

    public String getReplyUserNick() {
        return replyUserNick;
    }

    public String getAreaname() {
        return areaname;
    }

    public String getAreacode() {
        return areacode;
    }

    public long getEpoch() {
        return epoch;
    }

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
