/**
 * Created by ftmkk on 7/13/19.
 */
public class Configuration {
    private int totalTweetCount;
    private int tweetPerTimePeriod;
    private int timePeriodInDays;
    private boolean headless;
    private String lang;
    private String tagFile;
    private boolean doubleSearchTags;

    public Configuration(int totalTweetCount, int tweetPerTimePeriod, int timePeriodInDays, boolean headless, String lang, String tagFile, boolean doubleSearchTags) {
        this.totalTweetCount = totalTweetCount;
        this.tweetPerTimePeriod = tweetPerTimePeriod;
        this.timePeriodInDays = timePeriodInDays;
        this.headless = headless;
        this.lang = lang;
        this.tagFile = tagFile;
        this.doubleSearchTags = doubleSearchTags;
    }


    public int getTotalTweetCount() {
        return totalTweetCount;
    }

    public int getTweetPerTimePeriod() {
        return tweetPerTimePeriod;
    }

    public int getTimePeriodInDays() {
        return timePeriodInDays;
    }

    public boolean isHeadless() {
        return headless;
    }

    public String getLang() {
        return lang;
    }

    public String getTagFile() {
        return tagFile;
    }

    public boolean isDoubleSearchTags() {
        return doubleSearchTags;
    }
}
