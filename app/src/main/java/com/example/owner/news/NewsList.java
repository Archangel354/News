package com.example.owner.news;

/**
 * Created by Owner on 7/23/2017.
 */

public class NewsList {

    // title for a particular news record
    private String mTitle;

    // authors names for a particular news record
    private String mSection;

    // published date for a particular news record
    private String mDate;

    // The website of the news record
    private String mUrl;

    public NewsList(String Title, String Authors, String Date, String Url) {
        mSection = Authors;
        mTitle = Title;
        mDate = Date;
        mUrl = Url;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmUrl() { return mUrl; }
}
