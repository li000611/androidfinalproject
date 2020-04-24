package util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * All the common SimpleDateFormat that can be used for this project
 */
public class CommonDateFormat {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat BBC_NEWS_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss zzz", Locale.ENGLISH);
    public static final SimpleDateFormat NASA_IMAGE_OF_THE_DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
}
