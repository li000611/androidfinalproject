package com.example.finalproject.adapter.TheGuardianNewsArticle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.model.TheGuardianNewsArticle.GuardianArticle;

import java.util.List;

/**
 * The ListView adapter for Guardian News module
 */
public class GuardianNewsAdapter extends BaseAdapter {
    private Context context;
    private List<GuardianArticle> articles;

    /**
     * Primary constructor
     *
     * @param list The list of the Guardian Articles
     * @param context The activity context
     */
    public GuardianNewsAdapter(List<GuardianArticle> list, Context context) {
        setArticles(list);
        setContext(context);
    }

    /**
     * The setter for the context attribute
     *
     * @param context The activity context
     */
    public void setContext(Context context) {
        this.context = context;
    }


    /**
     * The setter for the articles attribute
     *
     * @param articles The articles to be set
     */
    public void setArticles(List<GuardianArticle> articles) {
        this.articles = articles;
    }

    /**
     * Get the total number of articles
     *
     * @return Total number of articles
     */
    @Override
    public int getCount() {
        return articles.size();
    }

    /**
     * Get item at particular position
     *
     * @param position The position inside a list
     * @return The article item at that position
     */
    @Override
    public GuardianArticle getItem(int position) {
        return articles.get(position);
    }

    /**
     * Get item id at particular position
     *
     * @param position The position inside a list
     * @return The id of article item at that position
     */
    @Override
    public long getItemId(int position) {
        Long id = articles.get(position).getId();
        return id == null ? 0 : id;
    }

    /**
     * Get the View of the single item inside the list view
     *
     * @param position The position of the guardian item inside the list
     * @param convertView The old view to be recycled
     * @param parent The parent view of the list view
     * @return The view of a single item inside the list view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.guardian_article_list_item, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.guardian_article_list_item_title);
        TextView sectionView = convertView.findViewById(R.id.guardian_article_list_item_section);

        titleView.setText(articles.get(position).getTitle());
        sectionView.setText(articles.get(position).getSection());

        return convertView;
    }
}
