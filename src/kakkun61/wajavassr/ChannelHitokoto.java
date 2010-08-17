package kakkun61.wajavassr;

public class ChannelHitokoto {
    /** 本文（HTML版） */
    private String html;

    /** 本文（text版） */
    private String body;

    /** このヒトコトのRID */
    private String rid;

    /** ユーザID */
    private String userLoginId;

    /** ユーザ名 */
    private String userNick;

    /** プロフィール画像最終更新日時 */
    private long userProfimgLastUpdatedAt;

    /** プロフィール画像のURL */
    private String userProfileImageUrl;

    /** チャンネルID */
    private String channelNameEn;

    /** チャンネル名 */
    private String channelTitle;

    /** 隠密モード */
    private boolean channelOnmitsuFg;

    /** 添付画像のURL */
    private String photoUrl;

    /** イイネした人のユーザID */
    private String[] favorites;

    /** リプライ先ヒトコトの本文（HTML版） */
    private String replyHtml;

    /**リプライ先ヒトコトの本文（text版） */
    private String replyBody;

    /** リプライ先ヒトコトの投稿者のユーザID */
    private String replyUserLoginId;

    /** リプライ先ヒトコトの投稿者のユーザ名 */
    private String replyUserNick;

    /** リプライ先ヒトコトの投稿者のプロフィール画像最終更新日時（エポック）（秒）? */
    private long replyUserProfimgLastUpdatedAt;

    /** リプライ先ヒトコトの投稿者のプロフィール画像のURL */
    private String replyUserProfileImageUrl;

    /** リプライ先ヒトコトのURL */
    private String replyUrl;

    /** 投稿日時（例："Tue, 01 Sep 2009 14:50:33 +0900"） */
    private String createdOn;

    public ChannelHitokoto(
            String html,
            String body,
            String rid,
            String userLoginId,
            String userNick,
            long userProfimgLastUpdatedAt,
            String userProfileImageUrl,
            String channelNameEn,
            String channelTitle,
            boolean channelOnmitsuFg,
            String photoUrl,
            String[] favorites,
            String replyHtml,
            String replyBody,
            String replyUserLoginId,
            String replyUserNick,
            long replyUserProfimgLastUpdatedAt,
            String replyUserProfileImageUrl,
            String replyUrl,
            String createdOn) {
        super();
        this.html = html;
        this.body = body;
        this.rid = rid;
        this.userLoginId = userLoginId;
        this.userNick = userNick;
        this.userProfimgLastUpdatedAt = userProfimgLastUpdatedAt;
        this.userProfileImageUrl = userProfileImageUrl;
        this.channelNameEn = channelNameEn;
        this.channelTitle = channelTitle;
        this.channelOnmitsuFg = channelOnmitsuFg;
        this.photoUrl = photoUrl;
        this.favorites = favorites;
        this.replyHtml = replyHtml;
        this.replyBody = replyBody;
        this.replyUserLoginId = replyUserLoginId;
        this.replyUserNick = replyUserNick;
        this.replyUserProfimgLastUpdatedAt = replyUserProfimgLastUpdatedAt;
        this.replyUserProfileImageUrl = replyUserProfileImageUrl;
        this.replyUrl = replyUrl;
        this.createdOn = createdOn;
    }

    public String getHtml() {
        return html;
    }

    public String getBody() {
        return body;
    }

    public String getRid() {
        return rid;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public String getUserNick() {
        return userNick;
    }

    public long getUserProfimgLastUpdatedAt() {
        return userProfimgLastUpdatedAt;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public String getChannelNameEn() {
        return channelNameEn;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public boolean isChannelOnmitsuFg() {
        return channelOnmitsuFg;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String[] getFavorites() {
        return favorites;
    }

    public String getReplyHtml() {
        return replyHtml;
    }

    public String getReplyBody() {
        return replyBody;
    }

    public String getReplyUserLoginId() {
        return replyUserLoginId;
    }

    public String getReplyUserNick() {
        return replyUserNick;
    }

    public long getReplyUserProfimgLastUpdatedAt() {
        return replyUserProfimgLastUpdatedAt;
    }

    public String getReplyUserProfileImageUrl() {
        return replyUserProfileImageUrl;
    }

    public String getReplyUrl() {
        return replyUrl;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof ChannelHitokoto))
            return false;
        ChannelHitokoto h = (ChannelHitokoto)obj;
        if(getRid().equals(h.getRid()))
            return true;
        return false;
    }
}
