package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This async class handle all the common image fetching and request
 */
public class GetImageAsyncTask extends AsyncTask<Object, Integer, Bitmap> {
    ImageView imgView;
    String url ;
    String fileName;
    Context ctx;
    ProgressBar progressBar;
    View container;

    /**
     * Primary constructor
     *
     * @param url The url of the image to be fetched
     * @param imgView The image view of the view that will be rendered in
     * @param fileName The filename of the image to be saved
     * @param ctx The activity context
     * @param progressBar The progress bar of the context
     * @param container The container view of the context
     */
    public GetImageAsyncTask(String url, ImageView imgView, String fileName, Context ctx, ProgressBar progressBar, View container) {
        super();
        this.imgView = imgView;
        this.url = url;
        this.fileName = fileName;
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.container = container;
    }


    /**
     * Async method for fetching the image
     *
     * @param objects Varargs for `execute`
     * @return null
     */
    @Override
    protected Bitmap doInBackground(Object... objects) {
        if(url == null || url.isEmpty()) return null;
        try {
            publishProgress(50);
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            if(fileName != null && !fileName.isEmpty()) {
                FileOutputStream outputStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
                myBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
            }
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Post fetch method
     *
     * @param s The Bitmap that is returned from `doInBackground`
     */
    @Override
    protected void onPostExecute(Bitmap s) {
        if(s == null) return;
        imgView.setImageBitmap(s);
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        if(container != null) {
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
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if(container != null) {
            container.setAlpha(0.2f);
            container.setClickable(Boolean.FALSE);
        }
    }
}

