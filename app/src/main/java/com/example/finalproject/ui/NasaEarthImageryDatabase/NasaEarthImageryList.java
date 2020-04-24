package com.example.finalproject.ui.NasaEarthImageryDatabase;

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

import com.example.finalproject.R;
import com.example.finalproject.adapter.NasaEarthImageryDatabase.NasaEarthImagesAdapter;
import com.example.finalproject.model.NasaEarthImage.NasaEarthImage;
import com.example.finalproject.opener.DatabaseOpener;

import java.util.List;

/**
 * The list activity for Nasa Earth Imagery
 */
public class NasaEarthImageryList extends AppCompatActivity {
    ProgressBar progressBar;
    View container;

    List<NasaEarthImage> images;
    ListView listView;
    DatabaseOpener dbOpener;
    NasaEarthImagesAdapter adapter;

    /**
     * The onCreate method lifecycle hook of this activity
     *
     * @param savedInstanceState The saved instance state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_template);

        dbOpener = new DatabaseOpener(this);

        listView = findViewById(R.id.template_list_view);
        progressBar = progressBar.findViewById(R.id.loading_bar);
        this.container = findViewById(R.id.list_container);

        images = (List<NasaEarthImage>) getIntent().getSerializableExtra(NasaEarthImageryDatabaseFragment.IMAGES_INTENT_EXTRA);
        this.adapter = new NasaEarthImagesAdapter(images, this, progressBar, this.container);
        listView.setAdapter(this.adapter);

        registerForContextMenu(listView);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(this, NasaEarthImageryDetail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NasaEarthImageryDatabaseFragment.IMAGES_INTENT_EXTRA, this.images.get(position));
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.add_favorite_context_menu:
                NasaEarthImage image = images.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                long id = NasaEarthImage.insert(dbOpener, image);
                image.setId(id);

                images.add(image);
                this.adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
