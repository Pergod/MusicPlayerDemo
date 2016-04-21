package com.geekband.mywork9;

/**
 * Created by Hyper on 2016/4/1.
 */
public class SongInfo {
    private String Title;
    private String Singer;


    public SongInfo(String title, String singer) {
        Title = title;
        Singer = singer;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }
}
