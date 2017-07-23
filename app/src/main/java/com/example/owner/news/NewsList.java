package com.example.owner.news;

/**
 * Created by Owner on 7/23/2017.
 */

public class NewsList {

    // title for a particular book
    private String mTitle;

    // authors names for a particular book
    private String mSection;

    // published date for a particular book
    private String mDate;

    public NewsList(String Title, String Authors, String Date) {
        mSection = Authors;
        mTitle = Title;
        mDate = Date;
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
}
