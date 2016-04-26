package com.hemant.news.pojo;

/**
 * Created by anuraggupta on 07/02/16.
 */
public class Channel {
    public String getChannelname() {
        return Channelname;
    }

    public void setChannelname(String channelname) {
        Channelname = channelname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String Channelname;
    public String url;
    public Channel(String Channelname, String url){
        this.Channelname = Channelname;
        this.url = url;
    }

}
