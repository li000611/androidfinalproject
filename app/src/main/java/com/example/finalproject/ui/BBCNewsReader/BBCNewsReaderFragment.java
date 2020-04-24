package com.example.finalproject.ui.BBCNewsReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.finalproject.adapter.BBCNewsReader.BBCNewsAdapter;
import com.example.finalproject.async.BBCNewsReader.BBCNewsGet;
import com.example.finalproject.model.BBCNewsReader.BBCNews;
import com.example.finalproject.opener.DatabaseOpener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * The fragment for BBC news module
 */
public class BBCNewsReaderFragment extends Fragment {
    public static String BBC_NEWS_INTENT_EXTRA = "news_list";
    public static String SINGLE_BBC_NEWS_INTENT_EXTRA = "news";
    public static String ALLOW_FAVORITE_INTENT_EXTRA = "allow_favorite";

    private FloatingActionButton searchBtn;
    private View root;
    private DatabaseOpener dbOpener;
    private List<BBCNews> newsList;
    private BBCNewsAdapter adapter;
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
        newsList = BBCNews.getAll(dbOpener);

        adapter = new BBCNewsAdapter(getContext(), newsList);

        listView = root.findViewById(R.id.template_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(getContext(), BBCNewsReaderDetail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(SINGLE_BBC_NEWS_INTENT_EXTRA, this.newsList.get(position));
            bundle.putBoolean(ALLOW_FAVORITE_INTENT_EXTRA, Boolean.FALSE);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete_this_bbc_news));
            builder.setPositiveButton(getText(R.string.OK), (dialog, which) -> {
                BBCNews.delete(this.dbOpener, id);
                this.newsList.remove(position);
                this.adapter.notifyDataSetChanged();
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.deleted_success, Snackbar.LENGTH_SHORT).show();
            }).setNegativeButton(getText(R.string.cancel), null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });

        searchBtn = root.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(e -> new BBCNewsGet(getContext(), this.loadingBar, this.container).execute());
        searchBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_black_24dp, getContext().getTheme()));
        return root;
    }

    /**
     * Java lifecycle that is called when application is resumed
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        this.newsList = BBCNews.getAll(this.dbOpener);
        this.adapter.setNewsList(this.newsList);
        this.adapter.notifyDataSetChanged();
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
        menu.findItem(R.id.text_note).setVisible(Boolean.TRUE);
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
                        .setMessage(getString(R.string.help_bbc_content))
                        .setIcon(R.drawable.ic_live_help_black_24dp)
                        .setPositiveButton(R.string.OK, null)
                        .show();
                break;
            case R.id.text_note:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText editText = new EditText(getContext());

                editText.setText(sharedPref.getString(getString(R.string.shared_pref_text_note), null));

                alert.setTitle(getString(R.string.enter_your_note));
                alert.setView(editText);

                alert.setPositiveButton(R.string.OK,
                    (DialogInterface dialog, int whichButton) -> {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.shared_pref_text_note), editText.getText().toString());
                        editor.commit();
                });
                alert.setNegativeButton(R.string.cancel, null);
                AlertDialog dialog = alert.create();
                dialog.setView(editText, 50, 0, 50, 0);
                dialog.show();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
