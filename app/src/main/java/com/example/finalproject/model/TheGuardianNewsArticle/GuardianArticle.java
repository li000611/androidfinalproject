package com.example.finalproject.model.TheGuardianNewsArticle;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.finalproject.opener.DatabaseOpener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Guardian Article model for database
 */
public class GuardianArticle implements Serializable {
    private Long id;
    private String title;
    private String url;
    private String section;


    /**
     * The primary constructor for this model
     *
     * @param id The id of the guardian article
     * @param title The title of the guardian article
     * @param url The url of the guardian article
     * @param section The section of the article
     */
    public GuardianArticle(Long id, String title, String url, String section) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.section = section;
    }

    /**
     * Fetch all available guardian articles inside the database
     *
     * @param dbOpener The DatabaseOpener that is opened in the activity
     * @return All the Guardian Articles inside the database
     */
    public static List<GuardianArticle> getAll(DatabaseOpener dbOpener) {
        List<GuardianArticle> res = new ArrayList<>();

        Cursor cursor = dbOpener.getReadableDatabase().query(DatabaseOpener.GUARDIAN_NEWS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Long id = cursor.getLong(cursor.getColumnIndex(DatabaseOpener.GUARDIAN_NEWS_TABLE_ID_COL));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseOpener.GUARDIAN_NEWS_TABLE_URL_COL));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseOpener.GUARDIAN_NEWS_TABLE_TITLE_COL));
                String section = cursor.getString(cursor.getColumnIndex(DatabaseOpener.GUARDIAN_NEWS_TABLE_SECTION_COL));

                res.add(new GuardianArticle(id, title, url, section));

                cursor.moveToNext();
            }
        }

        return res;
    }

    /**
     * Insert Guardian Article into the database
     *
     * @param dbOpener The DatabaseOpener object that is opened inside the activity
     * @param article The Guardian Article to be inserted
     * @return The id of the new Guardian Article
     */
    public static Long insert(DatabaseOpener dbOpener, GuardianArticle article) {
        ContentValues newArticle = new ContentValues();
        newArticle.put(DatabaseOpener.GUARDIAN_NEWS_TABLE_URL_COL, article.getUrl());
        newArticle.put(DatabaseOpener.GUARDIAN_NEWS_TABLE_TITLE_COL, article.getTitle());
        newArticle.put(DatabaseOpener.GUARDIAN_NEWS_TABLE_SECTION_COL, article.getSection());
        return dbOpener.getWritableDatabase().insert(DatabaseOpener.GUARDIAN_NEWS_TABLE_NAME, null, newArticle);
    }

    /**
     * Getter for id attribute
     *
     * @return Id of the article
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id attribute
     *
     * @param id The id to be set for the article
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for title attribute
     *
     * @return The title of the article
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title attribute
     *
     * @param title The title to be set for the article
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the url attribute
     *
     * @return The url of the article
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the url attribute
     *
     * @param url The url to be set for the article
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for the section of this article
     *
     * @return The section of this article
     */
    public String getSection() {
        return section;
    }

    /**
     * Delete the guardian article which has the corresponding id in the parameter
     *
     * @param dbOpener DatabaseOpener The database opener for the Guardian Article model
     * @param id The id of the article to be deleted
     * @return The number of rows affected
     */
    public static int delete(DatabaseOpener dbOpener, Long id) {
        return dbOpener.getWritableDatabase().delete(DatabaseOpener.GUARDIAN_NEWS_TABLE_NAME, String.format("%s = ?", DatabaseOpener.GUARDIAN_NEWS_TABLE_ID_COL), new String[]{Long.toString(id)});
    }
}
