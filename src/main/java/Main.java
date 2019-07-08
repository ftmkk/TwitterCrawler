import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        int totalTweetCount = 500000;
        int tweetPerTimePeriod = 2000;
        int timePeriodInDays = 30;
        boolean headless = true;
        String lang = "en";
        String tagFile = "keyTags/searching_items.txt";
        boolean doubleSearchTags = true;


        logger.setLevel(Level.INFO);
        BasicConfigurator.configure();

        List<String> tagList = readTagList(tagFile,doubleSearchTags);
        logger.info(tagList.size()+" tags found.");


        Crawler crawler = new Crawler(headless,lang);
        crawler.setDriver();
        logger.info("Driver set.");

        CrawlerManager manager = new CrawlerManager(
                totalTweetCount,
                tweetPerTimePeriod,
                timePeriodInDays,
                crawler);

        for (String tag : tagList) {
            try{
                logger.info("Searching for "+totalTweetCount+" tweets using tag: "+tag);
                int tweetCounter = manager.run(tag);
                logger.info("Totally, "+tweetCounter+" tweets found.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        crawler.closeDriver();
        logger.info("Driver closed.");
    }


    public static List<String> readTagList(String path,boolean doubleSearchTags){
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
            if(!savedTags.contains(tag) || doubleSearchTags){
                tagList.add(tag);
            }
        }

        return tagList;
    }
}