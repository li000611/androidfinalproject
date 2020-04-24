package com.example.finalproject.opener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The SQLite database opener for this project
 */
public class DatabaseOpener extends SQLiteOpenHelper {
    public final static String NASA_IMAGE_TABLE_NAME = "nasa_images";
    public final static String NASA_IMAGE_TABLE_ID_COL = "_id";
    public final static String NASA_IMAGE_TABLE_URL_COL = "url";
    public final static String NASA_IMAGE_TABLE_DATE_COL = "date";
    public final static String NASA_IMAGE_TABLE_HDURL_COL = "hdurl";

    public final static String GUARDIAN_NEWS_TABLE_NAME = "guardian_news";
    public final static String GUARDIAN_NEWS_TABLE_ID_COL = "_id";
    public final static String GUARDIAN_NEWS_TABLE_TITLE_COL = "title";
    public final static String GUARDIAN_NEWS_TABLE_URL_COL = "url";
    public final static String GUARDIAN_NEWS_TABLE_SECTION_COL = "section";

    public static final String NASA_EARTH_IMAGE_TABLE_NAME = "nasa_earth_images";
    public static final String NASA_EARTH_IMAGE_TABLE_ID_COL = "_id";
    public static final String NASA_EARTH_IMAGE_TABLE_LONGITUDE_COL = "longitude";
    public static final String NASA_EARTH_IMAGE_TABLE_LATITUDE_COL = "latitiude";
    public static final String NASA_EARTH_IMAGE_TABLE_DATE_COL = "date";
    public static final String NASA_EARTH_IMAGE_TABLE_URL_COL = "url";

    public static final String BBC_NEWS_TABLES_NAME = "bbc_news";
    public static final String BBC_NEWS_TABLE_ID_COL = "_id";
    public static final String BBC_NEWS_TABLE_TITLE_COL = "title";
    public static final String BBC_NEWS_TABLE_DESCRIPTION_COL = "description";
    public static final String BBC_NEWS_TABLE_DATE_COL = "date";
    public static final String BBC_NEWS_TABLE_URL_COL = "url";

    public final static Integer VERSION_NUM = 1;
    public final static String DATABASE_NAME = "final_project_db";

    /**
     * The constructor for database opener
     *
     * @param context The context of the activity
     */
    public DatabaseOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * The onCreate lifecycle of a database opener
     *
     * @param db The SQLiteDatabase object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT UNIQUE, " +
                        "%s INTEGER , " +
                        "%s TEXT)", NASA_IMAGE_TABLE_NAME, NASA_IMAGE_TABLE_ID_COL, NASA_IMAGE_TABLE_URL_COL,
                NASA_IMAGE_TABLE_DATE_COL, NASA_IMAGE_TABLE_HDURL_COL));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT UNIQUE, " +
                        "%s TEXT , " +
                        "%s TEXT)", GUARDIAN_NEWS_TABLE_NAME, GUARDIAN_NEWS_TABLE_ID_COL, GUARDIAN_NEWS_TABLE_TITLE_COL,
                GUARDIAN_NEWS_TABLE_URL_COL, GUARDIAN_NEWS_TABLE_SECTION_COL));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s REAL , " +
                        "%s REAL, " +
                        "%s INTEGER, " +
                        "%s TEXT UNIQUE )", NASA_EARTH_IMAGE_TABLE_NAME, NASA_EARTH_IMAGE_TABLE_ID_COL, NASA_EARTH_IMAGE_TABLE_LONGITUDE_COL,
                NASA_EARTH_IMAGE_TABLE_LATITUDE_COL, NASA_EARTH_IMAGE_TABLE_DATE_COL, NASA_EARTH_IMAGE_TABLE_URL_COL));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s TEXT UNIQUE )", BBC_NEWS_TABLES_NAME, BBC_NEWS_TABLE_ID_COL, BBC_NEWS_TABLE_TITLE_COL,
                BBC_NEWS_TABLE_DESCRIPTION_COL, BBC_NEWS_TABLE_DATE_COL, BBC_NEWS_TABLE_URL_COL));
    }

    /**
     * The onUpgrade lifecycle hook of a database opener
     *
     * @param db The SQLiteDatabase object
     * @param oldVersion The older version of the database
     * @param newVersion The newer version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
