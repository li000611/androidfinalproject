package com.example.finalproject.model.BBCNewsReader;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.finalproject.opener.DatabaseOpener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The BBC News model for database
 */
public class BBCNews implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Date date;
    private String url;

    /**
     * The primary constructor for this model
     *
     * @param id  The ID of this BBC News
     * @param title The title of this BBC News
     * @param description The description of this BBC News
     * @param date The date of this BBC News
     * @param url The URL for this BBC News
     */
    public BBCNews(Long id, String title, String description, Date date, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.url = url;
    }

    /**
     * Default constructor
     */
    public BBCNews() {}

    /**
     * Fetch all available BBCNews inside the database
     *
     * @param dbOpener The DatabaseOpener that is opened in the activity
     * @return All the BBCNews inside the database
     */
    public static List<BBCNews> getAll(DatabaseOpener dbOpener) {
        List<BBCNews> res = new ArrayList<>();

        Cursor cursor = dbOpener.getReadableDatabase().query(DatabaseOpener.BBC_NEWS_TABLES_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Long id = cursor.getLong(cursor.getColumnIndex(DatabaseOpener.BBC_NEWS_TABLE_ID_COL));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseOpener.BBC_NEWS_TABLE_TITLE_COL));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseOpener.BBC_NEWS_TABLE_DESCRIPTION_COL));
                Date date = new Date((long) cursor.getInt(cursor.getColumnIndex(DatabaseOpener.BBC_NEWS_TABLE_DATE_COL)) * 1000);
                String url = cursor.getString(cursor.getColumnIndex(DatabaseOpener.BBC_NEWS_TABLE_URL_COL));
                res.add(new BBCNews(id, title, description, date, url));
                cursor.moveToNext();
            }
        }

        return res;
    }

    /**
     * Delete the BBCNews which has the corresponding id in the parameter
     *
     * @param dbOpener DatabaseOpener The database opener for the BBCNews model
     * @param id The id of the BBCNews to be deleted
     * @return The number of rows affected
     */
    public static int delete(DatabaseOpener dbOpener, Long id) {
        return dbOpener.getWritableDatabase().delete(DatabaseOpener.BBC_NEWS_TABLES_NAME, String.format("%s = ?", DatabaseOpener.BBC_NEWS_TABLE_ID_COL), new String[]{Long.toString(id)});
    }

    /**
     * Insert NasaEarthImage into the database
     *
     * @param dbOpener The DatabaseOpener object that is opened inside the activity
     * @param news The BBCNews to be inserted
     * @return The id of the new BBCNews
     */
    public static Long insert(DatabaseOpener dbOpener, BBCNews news) {
        ContentValues newNews = new ContentValues();
        newNews.put(DatabaseOpener.BBC_NEWS_TABLE_TITLE_COL, news.getTitle());
        newNews.put(DatabaseOpener.BBC_NEWS_TABLE_DESCRIPTION_COL, news.getDescription());
        newNews.put(DatabaseOpener.BBC_NEWS_TABLE_DATE_COL, (int) (news.getDate().getTime() / 1000));
        newNews.put(DatabaseOpener.BBC_NEWS_TABLE_URL_COL, news.getUrl());
        return dbOpener.getWritableDatabase().insert(DatabaseOpener.BBC_NEWS_TABLES_NAME, null, newNews);
    }

    /**
     * Getter for the ID attribute
     *
     * @return The id of this BBCNews
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the ID attribute
     *
     * @param id The id of this BBCNews
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the title attribute
     *
     * @return The title of this BBCNews
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title attribute
     *
     * @param title The title of this BBCNews
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the description attribute
     *
     * @return The description of this BBCNews
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description attribute
     *
     * @param description The description of this BBCNews
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the date attribute of the BBCNews
     *
     * @return The date of this BBCNews
     */
    public Date getDate() {
        return date;
    }


    /**
     * Setter for the date attribute of this BBCNews
     *
     * @param date The date for this BBCNews
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the url attribute of this BBCNews
     *
     * @return The url of this BBCNews
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the url attribute of this BBCNews
     *
     * @param url The url of this BBCNews
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
