package com.example.android.newsappdenisa;

//Contains title, section and date of the post
public class Post {
    //Title of the post
    private String mTitleName;
    //Section in which post belong to
    private String mSectionName;
    //Date when was the post published
    private String mDate;
    //Url of the post
    private String mUrl;
    //Author of the post
    private String mAuthor;

    public Post(String title, String section, String date, String url, String author) {
        mTitleName = title;
        mSectionName = section;
        mDate = date;
        mUrl = url;
        mAuthor = author;
    }
    //Get title of the post
    public String getTitle() {
        return mTitleName;
    }
    //Get section of the post
    public String getSection() {
        return mSectionName;
    }
    //Get date of the post
    public String getDate() {
        return mDate;
    }
    //Get url of the post
    public String getUrl() {
        return mUrl;
    }
    //Get author of the post
    public String getAuthor() {
        return mAuthor;
    }
}


