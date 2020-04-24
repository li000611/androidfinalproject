package com.example.finalproject.async.NasaImageOfTheDay;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.example.finalproject.model.NasaImageOfTheDay.NasaImage;
import com.example.finalproject.ui.NasaImageOfTheDay.NasaImageOfTheDayDetailActivity;
import com.example.finalproject.ui.NasaImageOfTheDay.NasaImageOfTheDayFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import util.CommonDateFormat;

/**
 * Async class for fetching Nasa Images from NASA API
 */
public class NasaImageGet extends AsyncTask<Object, Integer, Object> {
    private NasaImage img;
    private Context ctx;
    private ProgressBar loadingBar;
    private Date date;
    private View container;

    /**
     * Primary constructor for NasaImageGet
     *
     * @param ctx The activity context
     * @param loadingBar The loading bar inside the activity (can be null)
     * @param date The date which will be fetched at
     * @param container The container view inside the activity
     */
    public NasaImageGet(Context ctx, ProgressBar loadingBar, Date date, View container) {
        this.ctx = ctx;
        this.loadingBar = loadingBar;
        this.date = date;
        this.container = container;
    }

    /**
     * Async method for fetching the images
     *
     * @param objects Varargs for `execute`
     * @return ""
     */
    @Override
    protected String doInBackground(Object... objects) {
        try {
            publishProgress(0);
            URL url = new URL(String.format("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=%s", CommonDateFormat.NASA_IMAGE_OF_THE_DAY_DATE_FORMAT.format(this.date)));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            JSONObject json = new JSONObject(result);
            Date date = CommonDateFormat.NASA_IMAGE_OF_THE_DAY_DATE_FORMAT.parse(json.getString("date"));

            publishProgress(50);
            img = new NasaImage(null, json.getString("url"), date, json.getString("hdurl"));

            Bundle bundle = new Bundle();
            bundle.putSerializable(NasaImageOfTheDayFragment.NASA_IMAGE_INTENT_EXTRA, this.img);
            bundle.putBoolean(NasaImageOfTheDayFragment.ALLOW_FAVORITE_INTENT_EXTRA, Boolean.TRUE);
            Intent intent = new Intent(this.ctx, NasaImageOfTheDayDetailActivity.class);
            intent.putExtras(bundle);

            this.ctx.startActivity(intent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Post fetch method
     *
     * @param obj The object that is returned from `doInBackground`
     */
    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        loadingBar.setProgress(100);
        loadingBar.setVisibility(View.INVISIBLE);
        container.setAlpha(1);
        container.setClickable(Boolean.TRUE);
    }

    /**
     * Method that will be called when call `publishProgress`
     *
     * @param values The progress percentage
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        loadingBar.setVisibility(View.VISIBLE);
        loadingBar.setProgress(values[0]);
        container.setAlpha(0.2f);
        container.setClickable(Boolean.FALSE);
    }

}
