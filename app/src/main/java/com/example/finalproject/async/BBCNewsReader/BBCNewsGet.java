package com.example.finalproject.async.BBCNewsReader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.finalproject.model.BBCNewsReader.BBCNews;
import com.example.finalproject.ui.BBCNewsReader.BBCNewsReaderFragment;
import com.example.finalproject.ui.BBCNewsReader.BBCNewsReaderList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import util.CommonDateFormat;

/**
 * Async class for fetching BBCNews from the BBCNews API
 */
public class BBCNewsGet extends AsyncTask<Object, Integer, Object> {
    public static final String ITEM_XML_TAG_NAME = "item";
    public static final String TITLE_XML_TAG_NAME = "title";
    public static final String DESCRIPTION_XML_TAG_NAME = "description";
    public static final String LINK_XML_TAG_NAME = "link";
    public static final String PUBLISH_DATE_XML_TAG_NAME = "pubDate";

    private ArrayList<BBCNews> list = new ArrayList<>();
    private Context ctx;
    private ProgressBar progressBar;
    private View container;

    /**
     * PrimaryConstructor for the BBC News
     *
     * @param ctx The context from the activity
     * @param progressBar The Progress Bar view from the activity
     * @param container The container view from the activity
     */
    public BBCNewsGet(Context ctx, ProgressBar progressBar, View container) {
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.container = container;
    }

    /**
     * Async method for fetching the BBCNews
     *
     * @param objects Varargs for `execute`
     * @return null
     */
    @Override
    protected Object doInBackground(Object... objects) {
        try {
            publishProgress(50);
            URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.connect();
            InputStream response = connection.getInputStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new InputStreamReader(response));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG && xpp.getName().equals(ITEM_XML_TAG_NAME)) {
                    int itemEventType = xpp.next();

                    BBCNews bbcNews = new BBCNews();
                    while(true) {
                        if(itemEventType == XmlPullParser.END_TAG && xpp.getName().equals(ITEM_XML_TAG_NAME))
                            break;
                        if(itemEventType == XmlPullParser.START_TAG) {
                            switch(xpp.getName()) {
                                case TITLE_XML_TAG_NAME:
                                    bbcNews.setTitle(getText(xpp));
                                    break;
                                case DESCRIPTION_XML_TAG_NAME:
                                    bbcNews.setDescription(getText(xpp));
                                    break;
                                case LINK_XML_TAG_NAME:
                                    bbcNews.setUrl(getText(xpp));
                                    break;
                                case PUBLISH_DATE_XML_TAG_NAME:
                                    bbcNews.setDate(CommonDateFormat.BBC_NEWS_DATE_FORMAT.parse(getText(xpp)));
                                    break;
                            }
                        }
                        itemEventType = xpp.next();
                    }
                    this.list.add(bbcNews);
                }
                eventType = xpp.next();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the content from the XML Pull Parser
     *
     * @param xpp the XmlPullParser
     * @return The String content of the XML tag
     */
    private String getText(XmlPullParser xpp) {
        try {
            int itemEventType = xpp.next();
            while(itemEventType !=  XmlPullParser.TEXT) {
                itemEventType = xpp.next();
            }
            return xpp.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Post fetch method
     *
     * @param o The object that is returned from `doInBackground`
     */
    @Override
    protected void onPostExecute(Object o) {
        Intent intent = new Intent(ctx, BBCNewsReaderList.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BBCNewsReaderFragment.BBC_NEWS_INTENT_EXTRA, this.list);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (container != null) {
            container.setAlpha(1);
            container.setClickable(Boolean.TRUE);
        }
    }

    /**
     * Method that will be called when call `publishProgress`
     *
     * @param values The progress percentage
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (container != null) {
            container.setAlpha(0.2f);
            container.setClickable(Boolean.FALSE);
        }
    }
}
