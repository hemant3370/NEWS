package com.hemant.myfeed.model;

import com.hemant.myfeed.R;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Yalantis
 */
@RealmClass
public class Topic extends RealmObject {
    public int avatar;

   @PrimaryKey private String topic;
    public int background;
    public String mainLink;
    public RealmList<StringObject> interests = new RealmList<>();
    public RealmList<StringObject> getLinks() {
        return links;
    }

    public RealmList<StringObject> getInterests() {
        return interests;
    }

    public RealmList<StringObject> links = new RealmList<>();

    public Topic(){

    }
    public Topic(Realm realm,
                 String topic,
                 Map<String, String> linkTopicPair) {
//        Topic topicData = realm.createObject(Topic.class,topic);
        switch (topic){
            case "Sports" :
                this.avatar = R.drawable.sports;
            case "Health" :
                this.avatar = R.drawable.healthormedical;
            case "World" :
                this.avatar = R.drawable.world;
            case "Politics" :
                this.avatar = R.drawable.politics;
            case "Science" :
                this.avatar = R.drawable.science;
            case "Entertainment" :
                this.avatar = R.drawable.entertainment;
            default:
                this.avatar = R.mipmap.ic_launcher;
        }
        this.topic = topic;
        this.background = R.color.purple;

        if (linkTopicPair != null){
        for (Map.Entry<String, String> entry : linkTopicPair.entrySet()) {
            this.interests.add(StringObject.init(realm, entry.getKey()));
            this.links.add(StringObject.init(realm,entry.getValue()));
        }

        }
        this.mainLink = this.links.get(0).string;
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getAvatar() {
        switch (this.topic){
            case "Sports" :
                return R.drawable.sports;
            case "Health" :
                return R.drawable.healthormedical;
            case "World" :
                return R.drawable.world;
            case "Politics" :
                return R.drawable.politics;
            case "Science" :
                return R.drawable.science;
            case "Entertainment" :
                return R.drawable.entertainment;
            default:
                return R.mipmap.ic_launcher;
        }
    }


    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getMainLink() {
        return mainLink;
    }





}
