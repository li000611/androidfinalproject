package com.example.finalproject.ui.NasaEarthImageryDatabase;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.adapter.NasaEarthImageryDatabase.NasaEarthImagesAdapter;
import com.example.finalproject.async.NasaEarthImageryDatabase.EarthImageGet;
import com.example.finalproject.model.NasaEarthImage.NasaEarthImage;
import com.example.finalproject.opener.DatabaseOpener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * The fragment for Nasa Earth Imagery module
 */
public class NasaEarthImageryDatabaseFragment extends Fragment {
    public static String IMAGES_INTENT_EXTRA = "images";
    public static String IMAGE_INTENT_EXTRA = "image";
    public static String FROM_LIST_PAGE_INTENT_EXTRA = "from_list_page";

    private FloatingActionButton searchBtn;
    private View root;
    private DatabaseOpener dbOpener;
    private List<NasaEarthImage> images;
    private NasaEarthImagesAdapter adapter;
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
        images = NasaEarthImage.getAll(dbOpener);

        adapter = new NasaEarthImagesAdapter(images, getContext(), loadingBar, this.container);

        listView = root.findViewById(R.id.template_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(getContext(), NasaEarthImageryDetail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NasaEarthImageryDatabaseFragment.IMAGE_INTENT_EXTRA, this.images.get(position));
            bundle.putBoolean(FROM_LIST_PAGE_INTENT_EXTRA, Boolean.TRUE);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete_this_earth_image));
            builder.setPositiveButton(getText(R.string.OK), (dialog, which) -> {
                NasaEarthImage.delete(this.dbOpener, id);
                this.images.remove(position);
                this.adapter.notifyDataSetChanged();
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.deleted_success, Snackbar.LENGTH_SHORT).show();
            }).setNegativeButton(getText(R.string.cancel), null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });

        searchBtn = root.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(e -> {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText latitudeBox = new EditText(getContext());
            latitudeBox.setHint(getString(R.string.latitude));
            latitudeBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            latitudeBox.setText(sharedPref.getString(getString(R.string.shared_pref_nasa_earth_latitude), null));
            layout.addView(latitudeBox); // Notice this is an add method


            final EditText longitudeBox = new EditText(getContext());
            longitudeBox.setHint(getString(R.string.longitude));
            longitudeBox.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            longitudeBox.setText(sharedPref.getString(getString(R.string.shared_pref_nasa_earth_longitude), null));
            layout.addView(longitudeBox); // Another add method
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.enter_longitude_latitude));
            builder.setPositiveButton(getText(R.string.OK), (dialog, which) -> {
                String latitude = latitudeBox.getText().toString();
                String longitude = longitudeBox.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.shared_pref_nasa_earth_latitude), latitude);
                editor.putString(getString(R.string.shared_pref_nasa_earth_longitude), longitude);
                editor.commit();
                new EarthImageGet(Float.valueOf(latitudeBox.getText().toString()),
                        Float.valueOf(longitudeBox.getText().toString()), getContext(), this.loadingBar, this.container, this.dbOpener).execute();
            }).setNegativeButton(getText(R.string.cancel), null);
            AlertDialog dialog = builder.create();
            dialog.setView(layout, 50, 0, 50, 0);
            dialog.show();
        });

        return root;
    }

    /**
     * Java lifecycle that is called when application is resumed
     */
    @Override
    public void onResume() {
        this.images = NasaEarthImage.getAll(this.dbOpener);
        this.adapter.setImages(this.images);
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
                        .setMessage(getString(R.string.help_nasa_earth_image_content))
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
