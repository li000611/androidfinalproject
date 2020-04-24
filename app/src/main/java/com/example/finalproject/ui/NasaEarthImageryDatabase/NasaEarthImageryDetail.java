package com.example.finalproject.ui.NasaEarthImageryDatabase;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.R;
import com.example.finalproject.model.NasaEarthImage.NasaEarthImage;
import com.example.finalproject.opener.DatabaseOpener;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import util.GetImageAsyncTask;

/**
 * The detail activity for Nasa Image module
 */
public class NasaEarthImageryDetail extends AppCompatActivity {
    NasaEarthImage image;
    TextView longLatView;
    ImageView imgView;
    TextView dateView;
    TextView urlView;
    ProgressBar progressBar;
    View container;
    Toolbar toolBar;
    DatabaseOpener dbOpener;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * The onCreate method lifecycle hook of this activity
     *
     * @param savedInstanceState The saved instance state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_image_detail);
        dbOpener = new DatabaseOpener(this);

        longLatView = findViewById(R.id.long_lat_detail);
        imgView = findViewById(R.id.nasa_earth_image_detail);
        dateView = findViewById(R.id.nasa_earth_image_date);
        urlView = findViewById(R.id.nasa_earth_image_url);
        progressBar = findViewById(R.id.nasa_earth_image_detail_loading_bar);
        container = findViewById(R.id.nasa_earth_image_detail_container);
        toolBar = findViewById(R.id.nasa_earth_image_detail_toolbar);

        if(!getIntent().getBooleanExtra(NasaEarthImageryDatabaseFragment.FROM_LIST_PAGE_INTENT_EXTRA, Boolean.FALSE)) {
            setSupportActionBar(toolBar);
        }

        this.progressBar.setVisibility(View.INVISIBLE);
        this.toolBar.setNavigationOnClickListener(e -> finish());

        this.image = (NasaEarthImage) getIntent().getSerializableExtra(NasaEarthImageryDatabaseFragment.IMAGE_INTENT_EXTRA);
        this.longLatView.setText(String.format("%.4f, %.4f", this.image.getLatitude(), this.image.getLongitude()));
        this.dateView.setText(simpleDateFormat.format(this.image.getDate()));
        this.urlView.setText("Image URL: " + this.image.getUrl());

        String fileName = this.image.getId() + ".nasaearthimage.png";
        try {
            if(this.image.getId() == null) throw new FileNotFoundException();
            imgView.setImageBitmap(BitmapFactory.decodeStream(openFileInput(fileName)));
        } catch (FileNotFoundException e) {
            new GetImageAsyncTask(this.image.getUrl(), imgView, fileName, this, progressBar, this.container).execute();
        }


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
            // action with ID action_refresh was selected
            case R.id.add_to_favorite_btn:
                NasaEarthImage.insert(dbOpener, this.image);
                Toast.makeText(this, getString(R.string.item_is_added_to_favorite), Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }
}
