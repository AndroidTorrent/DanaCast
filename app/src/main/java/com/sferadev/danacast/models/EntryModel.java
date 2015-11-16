package com.sferadev.danacast.models;

public class EntryModel {

    private int mType, mId;
    private String mTitle, mURL;

    public EntryModel(int type, String title, String url) {
        mType = type;
        mTitle = title;
        mURL = url;
    }

    public EntryModel(int type, int id, String title, String url) {
        mType = type;
        mId = id;
        mTitle = title;
        mURL = url;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mURL;
    }

    public void setLink(String link) {
        mURL = link;
    }
}