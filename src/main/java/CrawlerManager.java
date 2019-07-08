import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ftmkk on 7/7/19.
 */
public class CrawlerManager {

    private static Logger logger = Logger.getLogger(CrawlerManager.class);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int totalTweetCount;
    private int tweetPerTimePeriod;
    private int timePeriodInDays;
    private Crawler crawler;

    public CrawlerManager(int totalTweetCount, int tweetPerTimePeriod, int timePeriodInDays, Crawler crawler) {
        this.totalTweetCount = totalTweetCount;
        this.tweetPerTimePeriod = tweetPerTimePeriod;
        this.timePeriodInDays = timePeriodInDays;
        this.crawler = crawler;
    }

    public int run(String tag){

        Date endDate;
        Date startDate = new Date();
        int tweetCounter = 0;
        Date minDate = null;
        try {
            minDate = dateFormat.parse("2006-3-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int lessThan20Counter = 0;
        while(tweetCounter < totalTweetCount && startDate.compareTo(minDate)==1){

            List<Tweet> tweets = new ArrayList<>();

            endDate = dateMinusDays(startDate,timePeriodInDays*lessThan20Counter+1);

            startDate = dateMinusDays(endDate,timePeriodInDays);

            logger.info("Searching for "+
                    tweetPerTimePeriod+" tweets " +
                    "using tag: "+tag +
                    "["+dateFormat.format(startDate)+" to "+dateFormat.format(endDate)+"]");

            String url = crawler.urlOfSearchTag(tag,startDate,endDate);

            tweets.addAll(crawler.fetchTweets(url,tag, tweetPerTimePeriod));
            logger.info(tweets.size()+" tweets found.");

            if(tweets.size()<10){
                lessThan20Counter++;
            }else{
                lessThan20Counter = 0;
            }

            int sevedTweetCounter = 0;
            for (Tweet t : tweets) {
                if(Hibernate.add(t)) sevedTweetCounter++;
            }
            logger.info(sevedTweetCounter+" tweets added to database.");

        }
        return tweetCounter;
    }

    private Date dateMinusDays(Date date,int daysCount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -1*daysCount*24);
        return cal.getTime();
    }
}
