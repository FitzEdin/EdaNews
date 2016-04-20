package uk.ac.kent.fe44.edanews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by fe44 on 20/04/16.
 */
public class SavedListAdapter extends ListAdapter {

    //constructor
    public SavedListAdapter(SavedListFragment fragment) {
        super(fragment);
    }

    //called when a new item should be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.adapter_saved_item,
                        parent,
                        false
                );

        adapterId = ArticlesApp.SAVED_CALLER_ID;
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //populates new rows with data
    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Article article = model.getSavedList().get(position);
        //grab article's details as it comes into view
        if(article.getContents() == null) {
            model.loadArticleDetails(adapterId, article.getRecordID(), position, null);
        }
        holder.setData(article);
    }

    //returns size of data list
    @Override
    public int getItemCount() {
        return model.getSavedList().size();
    }

    /*handles layout for each item in the list*/
    public class ViewHolder extends ListAdapter.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }

        public ArrayList<Article> getList() {
            return model.getSavedList();
        }
    }
}
