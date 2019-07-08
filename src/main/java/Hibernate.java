
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class Hibernate {

    private static SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public static List<String> getSavedTags(){
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Tweet.class);
            List results = criteria.list();
            tx.commit();
            List<String> savedTags = new ArrayList<>();
            for(Object t : results){
                Tweet tweet = (Tweet) t;
                String tag = tweet.getResultOf();
                if(!savedTags.contains(tag)){
                    savedTags.add(tag);
                }
            }
            return savedTags;
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            throw e;
        }
        finally {
            session.close();
        }
    }

    public static boolean add(Object obj){
        boolean rslt = false;
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(obj);
//            session.flush();
            tx.commit();
            rslt = true;
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
        finally {
            session.close();
            return rslt;
        }
    }
}