package com.example.finalproject.model.NasaEarthImage;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.finalproject.opener.DatabaseOpener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Nasa Earth Image model for database
 */
public class NasaEarthImage implements Serializable {
    private Long id;
    private double longitude;
    private double latitude;
    private Date date;
    private String url;

    /**
     * The primary constructor for this model
     *
     * @param id  The ID of this NASA Earth Image
     * @param longitude The longitude of this NASA Earth Image
     * @param latitude The latitude of this NASA Earth Image
     * @param date The date of this NASA Earth Image
     * @param url The URL for this NASA Earth Image
     */
    public NasaEarthImage(Long id, double longitude, double latitude, Date date, String url) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.url = url;
    }

    /**
     * Fetch all available NASA earth images inside the database
     *
     * @param dbOpener The DatabaseOpener that is opened in the activity
     * @return All the Nasa earth images inside the database
     */
    public static List<NasaEarthImage> getAll(DatabaseOpener dbOpener) {
        List<NasaEarthImage> res = new ArrayList<>();

        Cursor cursor = dbOpener.getReadableDatabase().query(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Long id = cursor.getLong(cursor.getColumnIndex(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_ID_COL));
                float latitude = cursor.getFloat(cursor.getColumnIndex(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_LATITUDE_COL));
                float longitude = cursor.getFloat(cursor.getColumnIndex(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_LONGITUDE_COL));
                Date date = new Date((long) cursor.getInt(cursor.getColumnIndex(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_DATE_COL)) * 1000);
                String url = cursor.getString(cursor.getColumnIndex(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_URL_COL));
                res.add(new NasaEarthImage(id, longitude, latitude, date, url));
                cursor.moveToNext();
            }
        }

        return res;
    }

    /**
     * Delete the NASA earth Image which has the corresponding id in the parameter
     *
     * @param dbOpener DatabaseOpener The database opener for the NasaEarthImage model
     * @param id The id of the NASAEarthImage to be deleted
     * @return The number of rows affected
     */
    public static int delete(DatabaseOpener dbOpener, Long id) {
        return dbOpener.getWritableDatabase().delete(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_NAME, String.format("%s = ?", DatabaseOpener.NASA_EARTH_IMAGE_TABLE_ID_COL), new String[]{Long.toString(id)});
    }

    /**
     * Insert NasaEarthImage into the database
     *
     * @param dbOpener The DatabaseOpener object that is opened inside the activity
     * @param image The NasaEarthImage to be inserted
     * @return The id of the new NasaEarthImage
     */
    public static Long insert(DatabaseOpener dbOpener, NasaEarthImage image) {
        ContentValues newImg = new ContentValues();
        newImg.put(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_LATITUDE_COL, image.getLatitude());
        newImg.put(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_LONGITUDE_COL, image.getLongitude());
        newImg.put(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_DATE_COL, (int) (image.getDate().getTime() / 1000));
        newImg.put(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_URL_COL, image.getUrl());
        return dbOpener.getWritableDatabase().insert(DatabaseOpener.NASA_EARTH_IMAGE_TABLE_NAME, null, newImg);
    }

    /**
     * Getter for the ID attribute of this NasaEarthImage
     *
     * @return The ID of the NasaEarthImage
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the ID attribute of this NasaEarthImage
     *
     * @param id The id of the NasaEarthImage
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the longitude attribute of the NasaEarthImage
     *
     * @return The longitude of the NasaEarthImage
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for the latitude attribute of the NasaEarthImage
     *
     * @return The latitude of the NasaEarthImage
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for the date attribute of the NasaEarthImage
     *
     * @return The date of this Nasa Earth Image
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter for the url attribute of the NasaEarthImage
     *
     * @return The url of the Nasa Earth Image
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the url attribute of the NasaEarthImage
     *
     * @param url The url of the NasaEarthImage
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
