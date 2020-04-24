package com.example.finalproject.ui.BBCNewsReader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.R;
import com.example.finalproject.adapter.BBCNewsReader.BBCNewsAdapter;
import com.example.finalproject.model.BBCNewsReader.BBCNews;
import com.example.finalproject.opener.DatabaseOpener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * The list activity for BBC News
 */
public class BBCNewsReaderList extends AppCompatActivity {
    ProgressBar progressBar;
    View container;

    List<BBCNews> bbcNews;
    ListView listView;
    DatabaseOpener dbOpener;
    BBCNewsAdapter adapter;
    FloatingActionButton searchBtn;
    Toolbar toolbar;

    /**
     * The onCreate method lifecycle hook of this activity
     *
     * @param savedInstanceState The saved instance state of this activity
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_template);

        dbOpener = new DatabaseOpener(this);

        this.listView = findViewById(R.id.template_list_view);
        this.progressBar = findViewById(R.id.loading_bar);
        this.searchBtn = findViewById(R.id.search_btn);
        this.container = findViewById(R.id.list_container);
        this.toolbar = findViewById(R.id.list_template_toolbar);

        this.bbcNews = (List<BBCNews>) getIntent().getSerializableExtra(BBCNewsReaderFragment.BBC_NEWS_INTENT_EXTRA);
        this.adapter = new BBCNewsAdapter(this, this.bbcNews);
        this.listView.setAdapter(this.adapter);
        setSupportActionBar(this.toolbar);
        this.toolbar.setNavigationOnClickListener(e -> finish());
        getSupportActionBar().setTitle(getString(R.string.all_today_bbc_news));

        this.toolbar.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.searchBtn.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(this, BBCNewsReaderDetail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BBCNewsReaderFragment.SINGLE_BBC_NEWS_INTENT_EXTRA, this.bbcNews.get(position));
            bundle.putBoolean(BBCNewsReaderFragment.ALLOW_FAVORITE_INTENT_EXTRA, Boolean.TRUE);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
