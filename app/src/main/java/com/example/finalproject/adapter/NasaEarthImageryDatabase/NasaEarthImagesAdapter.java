package com.example.finalproject.adapter.NasaEarthImageryDatabase;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.model.NasaEarthImage.NasaEarthImage;

import java.io.FileNotFoundException;
import java.util.List;

import util.GetImageAsyncTask;

/**
 * The ListView adapter for Nasa Earth Image module
 */
public class NasaEarthImagesAdapter extends BaseAdapter {
    private Context context;
    private List<NasaEarthImage> images;
    private ProgressBar progressBar;
    private View container;

    /**
     * Primary constructor
     *
     * @param list The list of the Nasa Earth Images
     * @param context The activity context
     * @param progressBar The progress bar of the activity
     * @param container The container view of the activity
     */
    public NasaEarthImagesAdapter(List<NasaEarthImage> list, Context context, ProgressBar progressBar, View container) {
        setImages(list);
        setContext(context);
        this.progressBar = progressBar;
        this.container = container;
    }

    /**
     * Setter for the context attribute
     *
     * @param context The context of the activity
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Setter for the images attribute
     *
     * @param images The list of Nasa Earth Images
     */
    public void setImages(List<NasaEarthImage> images) {
        this.images = images;
    }

    /**
     * Get the total number of Nasa Earth Images
     *
     * @return The total number of Nasa Earth Images
     */
    @Override
    public int getCount() {
        return images.size();
    }

    /**
     * Get the NasaEarthImage at particular position
     *
     * @param position The position of the item inside the list view
     * @return The NasaEarthImage of the item at particular position
     */
    @Override
    public NasaEarthImage getItem(int position) {
        return images.get(position);
    }

    /**
     * Get the Item ID at particular position
     *
     * @param position  The position of the item inside the list view
     * @return The NasaEarthImage of the item at particular position
     */
    @Override
    public long getItemId(int position) {
        return images.get(position).getId();
    }

    /**
     * Get the view of the NasaEarthImage item inside the list view
     *
     * @param position  The position of the NasaEarthImage inside the list
     * @param convertView The old view to be recycled
     * @param parent The parent view of the list view
     * @return The view of a single item inside the list view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.nasa_earth_image_list_item, parent, false);
        }

        ImageView imgView = convertView.findViewById(R.id.list_item_earth_image);
        TextView longlatView = convertView.findViewById(R.id.list_item_long_lat);
        NasaEarthImage img = images.get(position);
        String fileName = img.getId() + ".nasaearthimage.png";
        try {
            if(img.getId() == null) throw new FileNotFoundException();
            imgView.setImageBitmap(BitmapFactory.decodeStream(context.openFileInput(fileName)));
        } catch (FileNotFoundException e) {
            new GetImageAsyncTask(img.getUrl(), imgView, fileName, context, progressBar, container).execute();
        }

        longlatView.setText(String.format("%.4f, %.4f", img.getLatitude(), img.getLongitude()));
        return convertView;
    }
}
