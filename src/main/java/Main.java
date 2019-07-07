import com.machinepublishers.jbrowserdriver.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class);
//    static Logger logger = LoggerFactory.getLogger(Main.class);
//    public static MySQLAccess mysqlAccess;

    public static void main(String[] args) {

        logger.setLevel(Level.INFO);
        BasicConfigurator.configure();

        Crawler crawler = new Crawler(true);
        crawler.setDriver();
        logger.info("Driver set.");

        List<String> tagList = readTagList("keyTags/frequentResponses.csv");
//        List<String> tagList = readTagList("stimuliList.csv");
        logger.info(tagList.size()+" tags found.");

        int tweetCount = 1000;
        for (String tag : tagList) {
            try{
                logger.info("Searching for "+tweetCount+" tweets using tag: "+tag);
                List<Tweet> tweets = crawler.fetchTweets(tag, tweetCount);
                logger.info(tweets.size()+" tweets found.");

                int cntr = 0;
                for (Tweet t : tweets) {
                    if(Hibernate.add(t)) cntr++;
                }
                logger.info(cntr+" tweets added to database.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        crawler.closeDriver();
        logger.info("Driver closed.");
    }


    public static List<String> readTagList(String path){
        File file =
                new File(path);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<String> tagList = new ArrayList<>();
        List<String> savedTags = Hibernate.getSavedTags();
        while (sc != null && sc.hasNextLine()){
            String tag = sc.nextLine().trim().replace(" ","_").replace("\u200C","_");
            if(!savedTags.contains(tag)){
                tagList.add(tag);
            }
        }

        return tagList;
    }
}