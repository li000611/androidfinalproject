package com.example.finalproject.ui.BBCNewsReader;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.R;
import com.example.finalproject.model.BBCNewsReader.BBCNews;
import com.example.finalproject.opener.DatabaseOpener;

import util.CommonDateFormat;

/**
 * @Author Min Li
 * Entails all the java code that is compiled when rendering BBCNews module
 */
public class BBCNewsReaderDetail extends AppCompatActivity {
    BBCNews bbcNews;
    TextView titleView;
    TextView descView;
    TextView dateView;
    TextView linkView;
    Toolbar toolBar;
    DatabaseOpener dbOpener;

    /**
     * On create lifecycle hook method of an activity
     * @param savedInstanceState The saved instance state of an activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc_news_detail);
        dbOpener = new DatabaseOpener(this);

        this.titleView = findViewById(R.id.bbc_news_title);
        this.descView = findViewById(R.id.bbc_news_description);
        this.dateView = findViewById(R.id.bbc_news_date);
        this.linkView = findViewById(R.id.bbc_news_link);
        this.toolBar = findViewById(R.id.bbc_news_detail_toolbar);

        if (getIntent().getBooleanExtra(BBCNewsReaderFragment.ALLOW_FAVORITE_INTENT_EXTRA, Boolean.FALSE)) {
            setSupportActionBar(this.toolBar);
        }
        this.toolBar.setNavigationOnClickListener(e -> finish());

        this.bbcNews = (BBCNews) getIntent().getSerializableExtra(BBCNewsReaderFragment.SINGLE_BBC_NEWS_INTENT_EXTRA);
        this.titleView.setText(this.bbcNews.getTitle());
        this.descView.setText(this.bbcNews.getDescription());
        this.dateView.setText(CommonDateFormat.SIMPLE_DATE_FORMAT.format(this.bbcNews.getDate()));
        this.linkView.setText(this.bbcNews.getUrl());

    }

    /**
     * Inflate the menu inside the toolbar options
     *
     * @param menu The menu that is created when it is used to render
     * @return True (always)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Method hook when an option item is selected on the menu
     *
     * @param item The MenuItem that was selected when users clicks on the item
     * @return True (always)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.add_to_favorite_btn:
                BBCNews.insert(dbOpener, this.bbcNews);
                Toast.makeText(this, getString(R.string.item_is_added_to_favorite), Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }
}
