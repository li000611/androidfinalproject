package com.example.finalproject.async.NasaEarthImageryDatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.finalproject.model.NasaEarthImage.NasaEarthImage;
import com.example.finalproject.opener.DatabaseOpener;
import com.example.finalproject.ui.NasaEarthImageryDatabase.NasaEarthImageryDatabaseFragment;
import com.example.finalproject.ui.NasaEarthImageryDatabase.NasaEarthImageryDetail;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Async class for fetching Nasa Earth Images from NASA API
 */
public class EarthImageGet extends AsyncTask<Object, Integer, Bitmap> {
    DatabaseOpener dbOpener;
    Context ctx;
    ProgressBar progressBar;
    View container;
    float latitude;
    float longitude;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");

    Date date;
    String imgUrl;
    NasaEarthImage finalImg;

    /**
     * Primary constructor for NasaImageGet
     *
     * @param latitude    The latitude of the image
     * @param longitude   The longitude of the image
     * @param ctx         The context of the activity
     * @param progressBar The progressbar view of the activity
     * @param container   The container view of the activity
     * @param dbOpener    The database opener from the activity
     */
    public EarthImageGet(float latitude, float longitude, Context ctx, ProgressBar progressBar, View container, DatabaseOpener dbOpener) {
        super();
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.container = container;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dbOpener = dbOpener;
    }


    /**
     * Async method for fetching the images
     *
     * @param objects Varargs for `execute`
     * @return null
     */
    @Override
    protected Bitmap doInBackground(Object... objects) {
        try {
            publishProgress(50);
            String urlStr = String.format("http://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/%.6f,%.6f/20?dir=180&ms=500,500&key=AqieTKV8epDYvLm2CvgblzFsqMwfXEqe9OaCinjoYGTNYhVVyq3KiBFPQvJt9YKE", this.latitude, this.longitude);
            ;

            this.date = new Date();
            this.imgUrl = urlStr;

            this.finalImg = new NasaEarthImage(null, longitude, latitude, date, imgUrl);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Post fetch method
     *
     * @param s The image that is returned from `doInBackground`
     */
    @Override
    protected void onPostExecute(Bitmap s) {
        Intent intent = new Intent(ctx, NasaEarthImageryDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(NasaEarthImageryDatabaseFragment.IMAGE_INTENT_EXTRA, this.finalImg);
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
