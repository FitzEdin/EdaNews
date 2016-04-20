package uk.ac.kent.fe44.edanews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by fitzroy on 28/01/2016.
 *
 * Feeds the data into the list of articles
 */
public class ArticleListAdapter extends ListAdapter {

    //constructor
    public ArticleListAdapter(ArticleListFragment fragment) {
        super(fragment);
    }

    //called when a new item should be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.adapter_article_item,
                        parent,
                        false
                );

        adapterId = ArticlesApp.ARTICLE_CALLER_ID;
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //populates new rows with data
    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Article article = model.getArticleList().get(position);
        //grab article's details as it comes into view; does
        //not need to listen for response because the aim is
        //to populate the article object itself
        if(article.getContents() == null) {
            model.loadArticleDetails(adapterId, article.getRecordID(), position, null);
        }
        holder.setData(article);
    }

    //returns size of data list
    @Override
    public int getItemCount() {
        return model.getArticleList().size();
    }

    /*handles layout for each item in the list*/
    public class ViewHolder extends ListAdapter.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }

        public ArrayList<Article> getList() {
            return model.getArticleList();
        }
    }
}
