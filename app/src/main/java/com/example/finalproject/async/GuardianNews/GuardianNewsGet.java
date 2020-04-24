package com.example.finalproject.async.GuardianNews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.finalproject.model.TheGuardianNewsArticle.GuardianArticle;
import com.example.finalproject.ui.TheGuardianNewsArticleSearch.GuardianNewsArticleListActivity;
import com.example.finalproject.ui.TheGuardianNewsArticleSearch.GuardianNewsArticleSearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Async class for fetching Guardian News from TheGuardian API
 */
public class GuardianNewsGet extends AsyncTask<Object, Integer, Object> {
    private Context ctx;
    private ProgressBar loadingBar;
    private View container;
    private String keywords;
    private ArrayList<GuardianArticle> listArticle = new ArrayList<>();

    /**
     * PrimaryConstructor for the Guardian News
     *
     * @param ctx The context of the activity
     * @param loadingBar The loading bar view of the activity
     * @param container The container view of the activity
     * @param keywords The keywords that is used to query the articles
     */
    public GuardianNewsGet(Context ctx, ProgressBar loadingBar, View container, String keywords) {
        this.ctx = ctx;
        this.loadingBar = loadingBar;
        this.container = container;
        this.keywords = keywords;
    }

    /**
     * Async method for fetching the images
     *
     * @param objs Varargs for `execute`
     * @return null
     */
    @Override
    protected String doInBackground(Object... objs) {
        try {
            publishProgress(0);
            URL url = new URL(String.format("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=%s", keywords));
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
            JSONArray arr = json.getJSONObject("response").getJSONArray("results");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                GuardianArticle newArti = new GuardianArticle(null, obj.getString("webTitle"), obj.getString("webUrl"), obj.getString("sectionName"));
                listArticle.add(newArti);
            }

            publishProgress(50);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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

        Intent intent = new Intent(this.ctx, GuardianNewsArticleListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GuardianNewsArticleSearchFragment.GUARDIAN_NEWS_LIST_INTENT_EXTRA, this.listArticle);
        intent.putExtras(bundle);
        this.ctx.startActivity(intent);
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
