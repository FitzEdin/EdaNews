package uk.ac.kent.fe44.edanews.list.faveslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.view.ListAdapter;
import uk.ac.kent.fe44.edanews.R;

/**
 * Created by fitzroy on 26/02/2016.
 *
 * Feeds the data into the list of articles
 */
public class FavesListAdapter extends ListAdapter {

    //constructor
    public FavesListAdapter(FavesListFragment fragment) {
        super(fragment);
    }

    //called when a new item should be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.adapter_faves_item,
                        parent,
                        false
                );

        adapterId = ArticlesApp.FAVES_CALLER_ID;
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //populates new rows with data
    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Article article = model.getFavesList().get(position);
        //grab article's details as it comes into view
        if(article.getContents() == null) {
            model.loadArticleDetails(adapterId, article.getRecordID(), position, null);
        }
        holder.setData(article);
    }

    //returns size of data list
    @Override
    public int getItemCount() {
        return model.getFavesList().size();
    }

    /*handles layout for each item in the list*/
    public class ViewHolder extends ListAdapter.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }

        public ArrayList<Article> getList() {
            return model.getFavesList();
        }
    }
}
