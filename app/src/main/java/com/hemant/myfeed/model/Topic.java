package com.hemant.myfeed.model;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Yalantis
 */
public class Topic {
    private int avatar;
    private String topic;
    private int background;
    String mainLink;
    private List<String> interests = new ArrayList<>();

    public List<String> getLinks() {
        return links;
    }

    public List<String> getInterests() {
        return interests;
    }

    public List<String> links = new ArrayList<>();

    public Topic(int avatar,
                 String topic,
                 String mainLink,
                 int background,
                 Map<String, String> linkTopicPair) {
        this.avatar = avatar;
        this.topic = topic;
        this.background = background;
        this.mainLink = mainLink;
        if (linkTopicPair != null){
        for (Map.Entry<String, String> entry : linkTopicPair.entrySet()) {
            this.interests.add(entry.getKey());
            this.links.add(entry.getValue());
        }

        }
    }
    public Topic(){

         }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public void setMainLink(String mainLink) {
        this.mainLink = mainLink;
    }



}
