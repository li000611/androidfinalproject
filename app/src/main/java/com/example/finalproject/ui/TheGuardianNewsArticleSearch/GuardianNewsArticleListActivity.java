package com.example.finalproject.ui.TheGuardianNewsArticleSearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.R;
import com.example.finalproject.adapter.TheGuardianNewsArticle.GuardianNewsAdapter;
import com.example.finalproject.model.TheGuardianNewsArticle.GuardianArticle;
import com.example.finalproject.opener.DatabaseOpener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * The list activity for Guardian News
 */
public class GuardianNewsArticleListActivity extends AppCompatActivity {
    ProgressBar progressBar;
    View container;
    Toolbar toolbar;
    FloatingActionButton searchBtn;

    List<GuardianArticle> listNews;
    ListView listView;
    DatabaseOpener dbOpener;
    GuardianNewsAdapter adapter;

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

        this.dbOpener = new DatabaseOpener(this);

        this.listView = findViewById(R.id.template_list_view);
        this.progressBar = findViewById(R.id.loading_bar);
        this.container = findViewById(R.id.list_container);
        this.toolbar = findViewById(R.id.list_template_toolbar);
        this.searchBtn = findViewById(R.id.search_btn);


        this.listNews = (List<GuardianArticle>) getIntent().getSerializableExtra(GuardianNewsArticleSearchFragment.GUARDIAN_NEWS_LIST_INTENT_EXTRA);
        this.adapter = new GuardianNewsAdapter(this.listNews, this);

        this.listView.setAdapter(this.adapter);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.toolbar.setVisibility(View.VISIBLE);
        this.searchBtn.setVisibility(View.INVISIBLE);
        setSupportActionBar(this.toolbar);
        this.toolbar.setNavigationOnClickListener(e -> finish());
        getSupportActionBar().setTitle(getString(R.string.all_related_guardian_news));

        registerForContextMenu(listView);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(this, GuardianArticleDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(GuardianNewsArticleSearchFragment.GUARDIAN_NEWS_INTENT_EXTRA, this.listNews.get(position));
            bundle.putBoolean(GuardianNewsArticleSearchFragment.ALLOW_FAVORITE_INTENT_EXTRA, Boolean.TRUE);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    /**
     * The method that gets called when context menu appears
     *
     * @param menu The context menu
     * @param v The view of the menu
     * @param menuInfo The context menu info
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.template_list_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_item_menu, menu);
        }
    }

    /**
     * The method that gets called when context item gets selected
     *
     * @param item The MenuItem that was selected
     * @return True (Always)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_favorite_context_menu:
                GuardianArticle article = listNews.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                long id = GuardianArticle.insert(dbOpener, article);
                article.setId(id);

                listNews.add(article);
                this.adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
