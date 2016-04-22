package uk.ac.kent.fe44.edanews.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.R;
import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.model.ArticleModel;

/**
 * Created by fitzroy on 26/02/2016.
 *
 * Feeds the data into the list of articles
 */
public abstract class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    protected ArticleModel model = ArticleModel.getInstance();
    protected ListFragment fragment;
    private String ELLIPSIS = "...";

    protected int adapterId;

    //constructor
    public ListAdapter(ListFragment fragment) {
        super();
        this.fragment = fragment;
    }

    /*handles layout for each item in the list*/
    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        protected Article article;
        protected Toolbar toolbar;
        protected FloatingActionButton fab;
        protected LinearLayout card;
        protected TextView title;
        protected TextView date;
        protected NetworkImageView photo;
        protected ImageView faveIc;
        protected ImageView savedIc;
        protected ImageView shareIc;

        /*sub classes determine which list they are using*/
        protected abstract ArrayList<Article> getList();

        //common
        protected View.OnClickListener itemTap = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                View statusBar = fragment.getActivity().findViewById(android.R.id.statusBarBackground);
//                View navBar = fragment.getActivity().findViewById(android.R.id.navigationBarBackground);
//
//                if (statusBar != null) {
//                    Pair<View, String> bob = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
//                }
                //create an options object for transition
                ActivityOptionsCompat optionsCompat
                        = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        fragment.getActivity(),
                        //Pair.create((View) card, ArticlesApp.TRANSITION_CARD),
                        Pair.create((View) toolbar, ArticlesApp.TRANSITION_TOOLBAR),
                        Pair.create((View) fab, ArticlesApp.TRANSITION_FAB),
                        Pair.create((View) title, ArticlesApp.TRANSITION_TITLE),
                        Pair.create((View) photo, ArticlesApp.TRANSITION_PHOTO)
                );
                fragment.onItemClicked(getAdapterPosition(), optionsCompat.toBundle());
            }
        };
        /* handle long click on article image */
        protected NetworkImageView.OnLongClickListener imgLongTap
                = new NetworkImageView.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                fragment.onLongTap(getAdapterPosition());
                return true;
            }
        };
        /* handle tap on faves icon */
        protected ImageView.OnClickListener faveICTap
                = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView ic = (ImageView)v;

                //if a fave
                if(article.isFave()) {
                    model.removeFromFaves(article);
                    ic.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }else {
                    model.addToFaves(article);
                    ic.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
        };
        /* handle tap on saved icon */
        protected ImageView.OnClickListener savedICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView ic = (ImageView)v;

                //if saved before
                if(article.isSaved()) {
                    model.removeFromSaved(article);
                    ic.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);
                }else {
                    model.addToSaved(article);
                    ic.setImageResource(R.drawable.ic_watch_later_black_24dp);
                }
            }
        };
        /* handle tap on share icon */
        protected ImageView.OnClickListener shareICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragment.onItemShared(getAdapterPosition());
            }
        };

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(itemTap);

            //get a handle on UI views
            toolbar = (Toolbar)fragment.getActivity().findViewById(R.id.list_toolbar);
            fab = (FloatingActionButton)fragment.getActivity().findViewById(R.id.search_fab);
            card = (LinearLayout)itemView.findViewById(R.id.article_card);
            title = (TextView)itemView.findViewById(R.id.article_title);
            date = (TextView)itemView.findViewById(R.id.article_date);
            photo = (NetworkImageView)itemView.findViewById(R.id.article_photo);

            faveIc = (ImageView)itemView.findViewById(R.id.ic_faves);
            savedIc = (ImageView) itemView.findViewById(R.id.ic_save);
            shareIc = (ImageView) itemView.findViewById(R.id.ic_share);

            photo.setOnLongClickListener(imgLongTap);
            photo.setOnClickListener(itemTap);

            faveIc.setOnClickListener(faveICTap);
            savedIc.setOnClickListener(savedICTap);
            shareIc.setOnClickListener(shareICTap);
        }

        //add values from data model to each row
        public void setData(Article article) {
            this.article = article;
            //protection for super long and super short titles
            int titleLength = article.getTitle().length() - 3;
            int length = (titleLength <= 20) ? titleLength : 20;

            title.setText(article.getTitle().substring(0, length) + ELLIPSIS);
            date.setText(article.getDate());
            photo.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());
            faveIc.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            savedIc.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);
            //no need to change the share icon

            // force the recyclerView to redraw icons correctly for each article
            if(article.isFave()) {  faveIc.setImageResource(R.drawable.ic_favorite_black_24dp); }
            if(article.isSaved()) { savedIc.setImageResource(R.drawable.ic_watch_later_black_24dp); }
        }
    }
}
