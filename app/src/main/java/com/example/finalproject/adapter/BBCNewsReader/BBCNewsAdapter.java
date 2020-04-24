package com.example.finalproject.adapter.BBCNewsReader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.model.BBCNewsReader.BBCNews;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * The ListView adapter for BBC News module
 */
public class BBCNewsAdapter extends BaseAdapter {
    private List<BBCNews> newsList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Context ctx;

    /**
     * Primary constructor
     *
     * @param ctx The activity context
     * @param list The list of the BBC News
     */
    public BBCNewsAdapter(Context ctx, List<BBCNews> list) {
        setNewsList(list);
        this.ctx = ctx;
    }

    /**
     * The setter for the news list adapter
     *
     * @param newsList The list of BBC News
     */
    public void setNewsList(List<BBCNews> newsList) {
        this.newsList = newsList;
    }

    /**
     * Get the size of all the BBC News
     *
     * @return The size of all the BBC Newd
     */
    @Override
    public int getCount() {
        return newsList.size();
    }

    /**
     * Get the BBC News item at particular position inside the list view
     *
     * @param position The position of the BBC News item to be retrieved
     * @return The BBC News item at that particular position
     */
    @Override
    public BBCNews getItem(int position) {
        return newsList.get(position);
    }

    /**
     * Get the Item ID at particular position
     *
     * @param position  The position of the item inside the list view
     * @return The ID of BBCNews of the item at particular position
     */
    @Override
    public long getItemId(int position) {
        Long res = newsList.get(position).getId();
        return res == null ? 0 : res;
    }

    /**
     * Get the view of the BBCNews item inside the list view
     *
     * @param position  The position of the BBCnews inside the list
     * @param convertView The old view to be recycled
     * @param parent The parent view of the list view
     * @return The view of a single item inside the list view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.ctx);
            convertView = inflater.inflate(R.layout.bbc_news_list_item, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.bbc_news_list_item_title);
        TextView descView = convertView.findViewById(R.id.bbc_list_item_description);
        TextView dateView = convertView.findViewById(R.id.bbc_list_item_date);

        BBCNews news = newsList.get(position);
        titleView.setText(news.getTitle());
        descView.setText(news.getDescription());
        dateView.setText(dateFormat.format(news.getDate()));

        return convertView;
    }
}
