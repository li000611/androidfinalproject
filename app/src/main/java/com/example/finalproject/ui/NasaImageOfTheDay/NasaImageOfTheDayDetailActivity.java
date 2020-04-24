package com.example.finalproject.ui.NasaImageOfTheDay;

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
import com.example.finalproject.model.NasaImageOfTheDay.NasaImage;
import com.example.finalproject.opener.DatabaseOpener;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import util.GetImageAsyncTask;

/**
 * The detail activity for Nasa Image module
 */
public class NasaImageOfTheDayDetailActivity extends AppCompatActivity {
    NasaImage image;
    ImageView imgView;
    TextView dateView;
    TextView urlView;
    TextView hdUrlView;
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
        setContentView(R.layout.activity_nasa_image_of_the_day_detail);

        this.image = (NasaImage) getIntent().getSerializableExtra(NasaImageOfTheDayFragment.NASA_IMAGE_INTENT_EXTRA);

        this.dbOpener = new DatabaseOpener(this);

        this.imgView = findViewById(R.id.nasa_image_of_the_day_image);
        this.dateView = findViewById(R.id.nasa_image_of_the_day_date);
        this.urlView = findViewById(R.id.nasa_image_of_the_day_url);
        this.hdUrlView = findViewById(R.id.nasa_image_of_the_day_hd_url);
        this.progressBar = findViewById(R.id.nasa_image_of_the_day_detail_loading_bar);
        this.container = findViewById(R.id.nasa_image_of_the_day_detail_container);
        this.toolBar = findViewById(R.id.nasa_image_of_the_day_detail_toolbar);

        if (getIntent().getBooleanExtra(NasaImageOfTheDayFragment.ALLOW_FAVORITE_INTENT_EXTRA, Boolean.FALSE)) {
            setSupportActionBar(toolBar);
        }

        this.progressBar.setVisibility(View.INVISIBLE);
        this.toolBar.setNavigationOnClickListener(e -> finish());


        String fileName = this.image.getId() + ".nasaimageoftheday.png";
        try {
            if(this.image.getId() == null) throw new FileNotFoundException();
            imgView.setImageBitmap(BitmapFactory.decodeStream(openFileInput(fileName)));
        } catch (FileNotFoundException e) {
            new GetImageAsyncTask(this.image.getUrl(), this.imgView, this.image.getId() == null ? null : fileName,
                    this, this.progressBar, this.container).execute();
        }

        this.dateView.setText(simpleDateFormat.format(this.image.getDate()));
        this.urlView.setText(getString(R.string.image_url_colon) + this.image.getUrl());
        this.hdUrlView.setText(getString(R.string.hd_image_url_colon) + this.image.getHdUrl());
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
                NasaImage.insert(dbOpener, this.image);
                Toast.makeText(this, getString(R.string.item_is_added_to_favorite), Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }
}
