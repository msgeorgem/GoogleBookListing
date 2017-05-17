package com.example.android.googlebooklisting;

/**
 * Created by Marcin on 2017-05-14.
 */

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;
    private String mDescription;
    private String mUrl;
    private double mRating;
    private String mLanguage;
    private String mThumbnail;

    /**
     * Create a new SingleNews object.
     * @param title     is the title ot the book
     * @param author it the author of the book
     * @param url       is the url of the website about books
     */

    public Book(String title, String author, String publishedDate, String description, String url,
                Double rating, String language, String thumbnail) {

        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
        mDescription = description;
        mUrl = url;
        mRating = rating;
        mLanguage = language;
        mThumbnail = thumbnail;
    }

    /**
     * Get the title of the book.
     */
    public String getTitle() {
        return mTitle;
    }
    /**
     * Get the shorttext of the book.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Get the date of the book.
     */
    public String getPublishedDate(){
        return mPublishedDate;
    }

    /**
     * Get the date of the book.
     */
    public String getDescription(){
        return mDescription;
    }

    /**
     * Get the url of the book.
     */
    public String getUrl() {
        return mUrl;
    }
    /**
     * Get the rating of the book.
     */
    public double getRating() {
        return mRating;
    }
    /**
     * Get the language of the book.
     */
    public String getLanguage() {
        return mLanguage;
    }

    /**
     * Get the thumbnail of the book.
     */
    public String getThumbnail() {
        return mThumbnail;
    }

}

