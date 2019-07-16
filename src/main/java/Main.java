import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    private static Configuration readConfig(String path){
        try (InputStream input = new FileInputStream(path)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return new Configuration(
                    Integer.parseInt(prop.getProperty("totalTweetCount")),
                    Integer.parseInt(prop.getProperty("tweetPerTimePeriod")),
                    Integer.parseInt(prop.getProperty("timePeriodInDays")),
                    Boolean.parseBoolean(prop.getProperty("headless")),
                    prop.getProperty("lang"),
                    prop.getProperty("tagFile"),
                    Boolean.parseBoolean(prop.getProperty("doubleSearchTags"))
            );

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        Configuration c = readConfig("config.properties");

        assert c != null;
        int totalTweetCount = c.getTotalTweetCount();
        int tweetPerTimePeriod = c.getTweetPerTimePeriod();
        int timePeriodInDays = c.getTimePeriodInDays();
        boolean headless = c.isHeadless();
        String lang = c.getLang();
        String tagFile = c.getTagFile();
        boolean doubleSearchTags = c.isDoubleSearchTags();


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