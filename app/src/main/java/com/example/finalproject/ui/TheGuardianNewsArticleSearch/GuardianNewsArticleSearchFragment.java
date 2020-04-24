package com.example.finalproject.ui.TheGuardianNewsArticleSearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.adapter.TheGuardianNewsArticle.GuardianNewsAdapter;
import com.example.finalproject.async.GuardianNews.GuardianNewsGet;
import com.example.finalproject.model.TheGuardianNewsArticle.GuardianArticle;
import com.example.finalproject.opener.DatabaseOpener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * The fragment for Guardian News module
 */
public class GuardianNewsArticleSearchFragment extends Fragment {
    public static String GUARDIAN_NEWS_LIST_INTENT_EXTRA = "news_list";
    public static String GUARDIAN_NEWS_INTENT_EXTRA = "news";
    public static String ALLOW_FAVORITE_INTENT_EXTRA = "allow_favorite";

    private FloatingActionButton searchBtn;
    private View root;
    private DatabaseOpener dbOpener;
    private List<GuardianArticle> articles;
    private GuardianNewsAdapter adapter;
    private ListView listView;
    private ProgressBar loadingBar;
    private View container;
    private SharedPreferences sharedPref;

    /**
     * Fragment lifecycle method that is created and called first
     *
     * @param inflater The inflater for this fragment
     * @param container The container for this fragment
     * @param savedInstanceState The saved instance state of the activity
     * @return The view that renders this fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(Boolean.TRUE);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        dbOpener = new DatabaseOpener(getContext());
        root = inflater.inflate(R.layout.list_template
                , container, false);

        loadingBar = root.findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.INVISIBLE);

        this.container = root.findViewById(R.id.list_container);
        articles = GuardianArticle.getAll(dbOpener);

        adapter = new GuardianNewsAdapter(articles, getContext());

        listView = root.findViewById(R.id.template_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            GuardianArticle idxArticle = articles.get(position);
            Intent intent = new Intent(getContext(), GuardianArticleDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(GUARDIAN_NEWS_INTENT_EXTRA, idxArticle);
            bundle.putBoolean(ALLOW_FAVORITE_INTENT_EXTRA, Boolean.FALSE);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete_this_guardian_news));
            builder.setPositiveButton(getText(R.string.OK), (dialog, which) -> {
                GuardianArticle.delete(this.dbOpener, id);
                this.articles.remove(position);
                this.adapter.notifyDataSetChanged();
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.deleted_success, Snackbar.LENGTH_SHORT).show();
            }).setNegativeButton(getText(R.string.cancel), null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });

        searchBtn = root.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.guardian_news_enter_your_article_search));
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(sharedPref.getString(getString(R.string.shared_pref_guardian_news_keyword), null));

            builder.setPositiveButton(getText(R.string.OK), (dialog, which) -> {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.shared_pref_guardian_news_keyword), input.getText().toString());
                editor.commit();
                new GuardianNewsGet(getContext(), this.loadingBar, this.container, input.getText().toString()).execute();
            }).setNegativeButton(getText(R.string.cancel), null);
            AlertDialog dialog = builder.create();
            dialog.setView(input, 50, 0, 50, 0);
            dialog.show();
        });

        return root;
    }

    /**
     * Java lifecycle that is called when application is resumed
     */
    @Override
    public void onResume() {
        this.articles = GuardianArticle.getAll(this.dbOpener);
        this.adapter.setArticles(this.articles);
        this.adapter.notifyDataSetChanged();
        super.onResume();
    }

    /**
     * Inflate the menu inside the toolbar options
     *
     * @param menu The menu that is created when it is used to render
     * @return True (always)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.help_item).setVisible(Boolean.TRUE);
        menu.findItem(R.id.add_to_favorite_btn).setVisible(Boolean.FALSE);
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
            case R.id.help_item:
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.help))
                        .setMessage(getString(R.string.help_guardian_news_content))
                        .setIcon(R.drawable.ic_live_help_black_24dp)
                        .setPositiveButton(R.string.OK, null)
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
