package com.hemant.news.Util;



import android.os.Build;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.Node;
import com.hemant.news.R;
import com.hemant.news.model.Topic;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final List<Map<String ,String>>  links = new ArrayList<>();
    public static final List<Topic> TOPICs = new ArrayList<>();
   public static boolean isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static  int marvel_1 = isLollipop ? R.drawable.marvel_1_lollipop : R.drawable.marvel_1;
    public static   int marvel_2 = isLollipop ? R.drawable.marvel_2_lollipop : R.drawable.marvel_2;
    public static   int marvel_3 = isLollipop ? R.drawable.marvel_3_lollipop : R.drawable.marvel_3;
    public static  int marvel_4 = isLollipop ? R.drawable.marvel_4_lollipop : R.drawable.marvel_4;


    static {


//    worldlinks.add("http://feeds.reuters.com/reuters/INworldNews");
//    worldlinks.add("http://www.theguardian.com/world/rss");
//    worldlinks.add("http://feeds.bbci.co.uk/news/world/rss.xml");
//    worldlinks.add("http://rss.cnn.com/rss/edition_world.rss");
//    worldlinks.add("http://feeds.washingtonpost.com/rss/world");
//    worldlinks.add("http://www.thehindu.com/news/international/world/?service=rss");

//    sportslinks.put("Reuters","http://feeds.reuters.com/reuters/sportsNews");
//    sportslinks.put("Guardian","http://www.theguardian.com/uk/sport/rss");
//    sportslinks.add("http://news.bbc.co.uk/sport1/hi/help/rss/default.stm");
//    sportslinks.add("http://rss.cnn.com/rss/edition_sport.rss");
//    sportslinks.add("http://feeds.washingtonpost.com/rss/sports");
//    sportslinks.add("http://www.thehindu.com/sport/?service=rss");
//
//
//    politicslinks.add("http://feeds.reuters.com/Reuters/PoliticsNews");
//    politicslinks.add("http://feeds.bbci.co.uk/news/politics/rss.xml");
//    politicslinks.add("http://feeds.washingtonpost.com/rss/politics");
//    politicslinks.add("http://www.faroo.com/api?q=politics&start=1&length=10&l=en&src=news&f=rss");
//    politicslinks.add("http://www.npr.org/rss/rss.php?id=1012");
//    politicslinks.add("http://feeds.feedburner.com/realclearpolitics/qlMj");
//
//    sciencelinks.add("http://feeds.reuters.com/reuters/scienceNews");
//    sciencelinks.add("http://www.theguardian.com/uk/technology/rss");
//    sciencelinks.add("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml");
//    sciencelinks.add("http://rss.cnn.com/rss/edition_space.rss");
//    sciencelinks.add("http://feeds.washingtonpost.com/rss/rss_speaking-of-science");
//    sciencelinks.add("http://www.thehindu.com/sci-tech/?service=rss");
//
//    healthlinks.add("http://feeds.reuters.com/reuters/healthNews");
//    healthlinks.add("http://www.theguardian.com/lifeandstyle/health-and-wellbeing/rss");
//    healthlinks.add("http://feeds.bbci.co.uk/news/health/rss.xml");
//    healthlinks.add("http://feeds.washingtonpost.com/rss/rss_to-your-health");
//    healthlinks.add("http://www.thehindu.com/sci-tech/health/?service=rss");
//    healthlinks.add("http://www.health.com/health/diet-fitness/feed");
//
//   Business wpost http://feeds.washingtonpost.com/rss/business
//    entertainmentlinks.add("http://feeds.reuters.com/reuters/entertainment");
//    entertainmentlinks.add("http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
//    entertainmentlinks.add("http://rss.cnn.com/rss/edition_entertainment.rss");
//    entertainmentlinks.add("http://feeds.washingtonpost.com/rss/entertainment");
//    entertainmentlinks.add("http://www.thehindu.com/entertainment/?service=rss");
//    entertainmentlinks.add("http://www.tmz.com/rss.xml");

}
//    static {
//
//        links.add(worldlinks);
//        links.add(sportslinks);
//        links.add(sciencelinks);
//        links.add(politicslinks);
//        links.add(entertainmentlinks);
//        links.add(healthlinks);
//    }
//    static {
//
//    }

    public static List<Topic> getTOPICs() {
        return TOPICs;
    }

}
