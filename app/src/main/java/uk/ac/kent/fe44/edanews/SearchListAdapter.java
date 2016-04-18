package uk.ac.kent.fe44.edanews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by fe44 on 22/03/16.
 */
public class SearchListAdapter extends ListAdapter {

    public SearchListAdapter(SearchListFragment fragment) {
        super(fragment);
    }

    //called when a new item should be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.adapter_search_item,
                        parent,
                        false
                );

        adapterId = 2;
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //populates new rows with data
    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Article article = model.getSearchList().get(position);
        //grab article's details as it comes into view
        model.loadArticleDetails(article.getRecordID(), position, null);
        holder.setData(article);
    }

    //returns size of data list
    @Override
    public int getItemCount() {
        return model.getSearchList().size();
    }

    /*handles layout for each item in the list*/
    public class ViewHolder extends ListAdapter.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }

        public ArrayList<Article> getList() {
            return model.getSearchList();
        }
    }
}
