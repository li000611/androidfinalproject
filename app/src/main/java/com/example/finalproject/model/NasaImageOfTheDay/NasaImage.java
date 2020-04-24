package com.example.finalproject.model.NasaImageOfTheDay;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.finalproject.opener.DatabaseOpener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Nasa Image model for database
 */
public class NasaImage implements Serializable {
    private Long id;
    private String url;
    private Date date;
    private String hdUrl;

    /**
     * The primary constructor for this model
     *
     * @param id The primary ID of the Nasa Image
     * @param url The url of the Nasa Image
     * @param date The date of the Nasa Image
     * @param hdUrl The HD URL for the Nasa Image
     */
    public NasaImage(Long id, String url, Date date, String hdUrl) {
        this.id = id;
        this.url = url;
        this.date = date;
        this.hdUrl = hdUrl;
    }

    /**
     * Fetch all available NASA image inside the database
     *
     * @param dbOpener The DatabaseOpener that is opened in the activity
     * @return All the Nasa images inside the database
     */
    public static List<NasaImage> getAll(DatabaseOpener dbOpener) {
        List<NasaImage> res = new ArrayList<>();

        Cursor cursor = dbOpener.getReadableDatabase().query(DatabaseOpener.NASA_IMAGE_TABLE_NAME, null, null, null, null, null, DatabaseOpener.NASA_IMAGE_TABLE_DATE_COL + " DESC");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Long id = cursor.getLong(cursor.getColumnIndex(DatabaseOpener.NASA_IMAGE_TABLE_ID_COL));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseOpener.NASA_IMAGE_TABLE_URL_COL));
                String hdUrl = cursor.getString(cursor.getColumnIndex(DatabaseOpener.NASA_IMAGE_TABLE_HDURL_COL));
                Date date = new Date((long) cursor.getInt(cursor.getColumnIndex(DatabaseOpener.NASA_IMAGE_TABLE_DATE_COL)) * 1000);

                res.add(new NasaImage(id, url, date, hdUrl));

                cursor.moveToNext();
            }
        }

        return res;
    }

    /**
     * Insert Nasa Image into the database
     *
     * @param dbOpener The DatabaseOpener object that is opened inside the activity
     * @param image The Nasa Image to be inserted
     * @return The id of the new Nasa Image
     */
    public static Long insert(DatabaseOpener dbOpener, NasaImage image) {
        ContentValues newImg = new ContentValues();
        newImg.put(DatabaseOpener.NASA_IMAGE_TABLE_URL_COL, image.getUrl());
        newImg.put(DatabaseOpener.NASA_IMAGE_TABLE_DATE_COL, (int) (image.getDate().getTime() / 1000));
        newImg.put(DatabaseOpener.NASA_IMAGE_TABLE_HDURL_COL, image.getHdUrl());
        return dbOpener.getWritableDatabase().insert(DatabaseOpener.NASA_IMAGE_TABLE_NAME, null, newImg);
    }

    /**
     * Getter for the url attribute
     *
     * @return The url of this NasaImage
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the url attribute
     *
     * @param url The url to be set for the NASA image
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for the date attribute
     *
     * @return The date of this NASA image
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter for the HD Url of the NASA Image
     *
     * @return HD URL of this NASA Image
     */
    public String getHdUrl() {
        return hdUrl;
    }

    /**
     * Getter for the ID attribute of this NASA Image
     *
     * @return The ID of this NASA Image
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the ID attribute of this NASA Image
     *
     * @param id The ID to be set for the NASA Image
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Delete the NASA Image which has the corresponding id in the parameter
     *
     * @param dbOpener DatabaseOpener The database opener for the Nasa Image model
     * @param id The id of the NASA Image to be deleted
     * @return The number of rows affected
     */
    public static int delete(DatabaseOpener dbOpener, Long id) {
        return dbOpener.getWritableDatabase().delete(DatabaseOpener.NASA_IMAGE_TABLE_NAME, String.format("%s = ?", DatabaseOpener.NASA_IMAGE_TABLE_ID_COL), new String[]{Long.toString(id)});
    }
}
