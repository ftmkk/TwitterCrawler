import javax.persistence.*;
import java.util.Date;

/**
 * Created by ftmkk on 7/3/19.
 */

@Entity
public class Tweet {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column private String tweetId;

    @Column private boolean isReply;
    @Column(columnDefinition = "varchar(6000)") private String mentions;

    @Column private String fullname;
    @Column private String username;
    @Column private String userId;

    @Column private Date timestamp;

    @Column private String lang;
    @Column(columnDefinition = "varchar(2000)") private String text;

    @Column private int replyCount;
    @Column private int retweetCount;
    @Column private int likeCount;

    ////

    @Column private String resultOf;
    @Column private Date retrieveDate;

    public Tweet(String tweetId, boolean isReply, String mentions, String fullname, String username, String userId, Date timestamp, String lang, String text, int replyCount, int retweetCount, int likeCount) {
        this.tweetId = tweetId;
        this.isReply = isReply;
        this.mentions = mentions;
        this.fullname = fullname;
        this.username = username;
        this.userId = userId;
        this.timestamp = timestamp;
        this.text = text;
        this.lang = lang;
        this.replyCount = replyCount;
        this.retweetCount = retweetCount;
        this.likeCount = likeCount;
    }

    public Tweet() {
    }

    public void setResultOf(String resultOf) {
        this.resultOf = resultOf;
    }

    public void setRetrieveDate(Date retrieveDate) {
        this.retrieveDate = retrieveDate;
    }


    public int getId() {
        return id;
    }

    public String getTweetId() {
        return tweetId;
    }

    public boolean isReply() {
        return isReply;
    }

    public String getMentions() {
        return mentions;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getLang() {
        return lang;
    }

    public String getText() {
        return text;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getResultOf() {
        return resultOf;
    }

    public Date getRetrieveDate() {
        return retrieveDate;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", tweetId='" + tweetId + '\'' +
                ", isReply=" + isReply +
                ", mentions='" + mentions + '\'' +
                ", fullname='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                ", lang='" + lang + '\'' +
                ", replyCount=" + replyCount +
                ", retweetCount=" + retweetCount +
                ", likeCount=" + likeCount +
                ", resultOf='" + resultOf + '\'' +
                ", retrieveDate=" + retrieveDate +
                '}';
    }
}





