import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ftmkk on 7/4/19.
 */
public class Crawler {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JBrowserDriver driver;
    private boolean headless;
    private String lang;
    private static Logger logger = Logger.getLogger(Crawler.class);

    public Crawler(boolean headless, String lang) {
        this.headless = headless;
        this.lang = lang;
        logger.setLevel(Level.TRACE);
    }

    public void setDriver(){

        driver = new JBrowserDriver(
                Settings
                        .builder()
                        .timezone(Timezone.AMERICA_NEWYORK)
                        .headless(headless)
//                        .userAgent(UserAgent.CHROME)
                        .socketTimeout(60000)
                        .connectTimeout(30000)
                        .connectionReqTimeout(10000)
                        .ajaxResourceTimeout(15000)
                        .blockAds(true)
                        .ignoreDialogs(true)
                        .loggerLevel(java.util.logging.Level.OFF)
                        .quickRender(true)
//                        .proxy(new ProxyConfig(ProxyConfig.Type.HTTP, "hidden", 4444, "hidden", "hidden"))
//                        .ssl("compatible")
//                        .javaOptions("-Djsse.enableSNIExtension=false")
//                        .hostnameVerification(false)
                        .build()
        );
        System.setProperty("webdriver.http.factory", "apache");
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }


    public String urlOfSearchTag(String tag){
//        return "https://twitter.com/search?q=%23"+tag+"&l=fa";
        return "https://twitter.com/search?q=%23"+tag+"&l="+lang;
    }

    public String urlOfSearchTag(String tag, Date startDate, Date endDate){
//        return "https://twitter.com/search?q=%23"+tag+"&l=fa";
//        return "https://twitter.com/search?q=%23"+tag+"&l=en";

        return "https://twitter.com/search?" +
                "l="+lang+"" +
                "&q=\""+tag+"\"" +
                " since%3A"+dateFormat.format(startDate) +
                " until%3A"+dateFormat.format(endDate)+
                "&src=typd";
    }

    public List<Tweet> fetchTweets(String url,String tag, int desiredTweetCount){

        logger.trace("going to url: "+url);
        driver.get(url);
        logger.trace("Page loaded.");

        int currentCount = driver.findElements(By.cssSelector(".tweet")).size();
        logger.trace(currentCount+" tweets found till now.");
        int lastCount;
        int cntr = 0;
        while (currentCount < desiredTweetCount && cntr <= 5 && currentCount>15) {
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lastCount = currentCount;
            currentCount = driver.findElements(By.cssSelector(".tweet")).size();
            if(lastCount==currentCount){
                cntr++;
            }else{
                cntr = 0;
            }
            logger.trace(currentCount+" tweets found till now.");
        }


        List<Tweet> tweets = new ArrayList<>();
        List<WebElement> tweetTextElements = driver.findElements(By.cssSelector(".tweet"));
        logger.trace("adding "+tweetTextElements.size()+" tweets ...");
        for(WebElement e : tweetTextElements){
            try{
                if(e.findElements(By.cssSelector(".Tombstone")).size()!=0) continue;
                Tweet tweet =TweetParser.parse(e);
                tweet.setResultOf(tag);
                tweet.setRetrieveDate(new Date(System.currentTimeMillis()));
                tweets.add(tweet);
            }catch (Exception ex){
                ex.printStackTrace();
                System.out.println(e.getText());
                System.out.println(e.getAttribute("outerHTML"));
            }
        }

        return tweets;
    }


    public void closeDriver(){
        driver.kill();
    }

}
