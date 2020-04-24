package com.example.finalproject.ui.TheGuardianNewsArticleSearch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.R;
import com.example.finalproject.model.TheGuardianNewsArticle.GuardianArticle;
import com.example.finalproject.opener.DatabaseOpener;

/**
 * The Acvitity for Guardian Article module
 */
public class GuardianArticleDetailActivity extends AppCompatActivity {
    TextView titleView;
    TextView sectionView;
    TextView linkView;
    Toolbar toolbar;
    GuardianArticle article;
    DatabaseOpener dbOpener;

    /**
     * The onCreate method hook of the activity lifecycle
     *
     * @param savedInstanceState The saved instance state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        this.dbOpener = new DatabaseOpener(this);
        this.article = (GuardianArticle) getIntent().getSerializableExtra(GuardianNewsArticleSearchFragment.GUARDIAN_NEWS_INTENT_EXTRA);

        this.titleView = findViewById(R.id.guardian_article_title);
        this.sectionView = findViewById(R.id.guardian_article_section);
        this.linkView = findViewById(R.id.guardian_article_link);
        this.toolbar = findViewById(R.id.article_detail_toolbar);

        this.titleView.setText(this.article.getTitle());
        this.sectionView.setText(this.article.getSection());
        this.linkView.setText(this.article.getUrl());

        if (getIntent().getBooleanExtra(GuardianNewsArticleSearchFragment.ALLOW_FAVORITE_INTENT_EXTRA, Boolean.FALSE)) {
            setSupportActionBar(this.toolbar);
        }
        this.toolbar.setNavigationOnClickListener(e -> finish());
    }

    /**
     * The method that get called when option menu shows up
     *
     * @param menu The menu that is passed
     * @return True (always)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * The method that gets called when an option item is selected
     *
     * @param item The menu item that is selected
     * @return True (Always)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_favorite_btn:
                GuardianArticle.insert(dbOpener, this.article);
                Toast.makeText(this, getString(R.string.item_is_added_to_favorite), Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }
}
