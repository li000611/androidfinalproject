package com.example.finalproject.ui.NasaImageOfTheDay;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.adapter.NasaImageOfTheDay.ImageAdapter;
import com.example.finalproject.async.NasaImageOfTheDay.NasaImageGet;
import com.example.finalproject.model.NasaImageOfTheDay.NasaImage;
import com.example.finalproject.opener.DatabaseOpener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * The fragment for Nasa Image module
 */
public class NasaImageOfTheDayFragment extends Fragment {
    public static String NASA_IMAGE_INTENT_EXTRA = "nasa_image";
    public static String ALLOW_FAVORITE_INTENT_EXTRA = "allow_favorite";

    private FloatingActionButton searchBtn;
    private View root;
    private DatabaseOpener dbOpener;
    private List<NasaImage> images;
    private ImageAdapter adapter;
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
        dbOpener = new DatabaseOpener(getContext());
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        root = inflater.inflate(R.layout.list_template
                , container, false);

        loadingBar = root.findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.INVISIBLE);

        this.container = root.findViewById(R.id.list_container);
        images = NasaImage.getAll(dbOpener);

        adapter = new ImageAdapter(images, getContext(), loadingBar, this.container);

        listView = root.findViewById(R.id.template_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            NasaImage selectedImg = this.images.get(position);
            Intent intent = new Intent(getContext(), NasaImageOfTheDayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NASA_IMAGE_INTENT_EXTRA, selectedImg);
            bundle.putBoolean(ALLOW_FAVORITE_INTENT_EXTRA, Boolean.FALSE);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.delete_this_nasa_image));
            builder.setPositiveButton(getText(R.string.OK), (dialog, which) -> {
                NasaImage selectedImg = this.images.get(position);
                NasaImage.delete(this.dbOpener, selectedImg.getId());
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.nasa_image_of_the_day_pls_enter_your_date));

            Calendar cal = Calendar.getInstance();
            int savedYear = sharedPref.getInt(getString(R.string.shared_pref_year_key), -1);
            int savedMonth = sharedPref.getInt(getString(R.string.shared_pref_month_key), -1);
            int savedDayOfMonth = sharedPref.getInt(getString(R.string.shared_pref_day_of_month_key), -1);

            if(savedYear != -1 && savedMonth != -1 && savedDayOfMonth != -1) {
                cal.set(Calendar.YEAR, savedYear);
                cal.set(Calendar.MONTH, savedMonth);
                cal.set(Calendar.DAY_OF_MONTH, savedDayOfMonth);
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
                Calendar formatCal = Calendar.getInstance();
                formatCal.set(year, month, dayOfMonth);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.shared_pref_year_key), year);
                editor.putInt(getString(R.string.shared_pref_month_key), month);
                editor.putInt(getString(R.string.shared_pref_day_of_month_key), dayOfMonth);
                editor.commit();
                new NasaImageGet(getContext(), this.loadingBar, formatCal.getTime(), this.container).execute();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            DatePicker datePicker = datePickerDialog.getDatePicker();

            datePicker.setMaxDate(new Date().getTime());
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle(null);

            datePickerDialog.show();
        });

        return root;
    }

    /**
     * Java lifecycle that is called when application is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        this.images = NasaImage.getAll(dbOpener);
        this.adapter.setImages(this.images);
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
        menu.findItem(R.id.add_to_favorite_btn).setVisible(Boolean.FALSE);
        menu.findItem(R.id.text_note).setVisible(Boolean.TRUE);
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
                        .setMessage(getString(R.string.help_nasa_image_of_the_day))
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
