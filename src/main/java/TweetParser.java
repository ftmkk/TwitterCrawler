import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Date;

/**
 * Created by ftmkk on 7/4/19.
 */
public class TweetParser {

    public static Tweet parse(WebElement element){

        String tweetId = element.getAttribute("data-tweet-id");
        boolean isReply = false;
        if(element.getAttribute("data-is-reply-to")!=null) if(element.getAttribute("data-is-reply-to").equals("true")) isReply = true;
        String mentions = null;
        if(isReply) mentions = element.getAttribute("data-mentions");
        String fullname = element.getAttribute("data-name");
//        System.out.println(element.findElement(By.className("fullname")).getText());
        String username = element.getAttribute("data-screen-name");
//        System.out.println(element.findElement(By.className("username")).getText());
        String userId = element.getAttribute("data-user-id");
        Date timestamp = new Date(Long.parseLong(element.findElement(By.className("_timestamp")).getAttribute("data-time-ms")));
        String lang = element.findElement(By.className("tweet-text")).getAttribute("lang");
        String text = element.findElement(By.className("tweet-text")).getText();
        int replyCount = countParser(element.findElement(By.className("ProfileTweet-action--reply")).getText());
        int retweetCount = countParser(element.findElement(By.className("ProfileTweet-action--retweet")).getText());
        int likeCount = countParser(element.findElement(By.className("ProfileTweet-action--favorite")).getText());

        return new Tweet(tweetId,isReply,mentions,fullname,username,userId,timestamp,lang,text,replyCount,retweetCount,likeCount);
    }


    private static int countParser(String string){
        int count = 0;
        String likeCountStr = string.split(" ")[0].replace(",","");
        if(!likeCountStr.equals("")){
            count = Integer.parseInt(likeCountStr);
        }
        return count;
    }
}
