package uk.ac.kent.fe44.edanews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.text.style.TtsSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by fitzroy on 26/02/2016.
 *
 * Feeds the data into the list of articles
 */
public abstract class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    protected ArticleModel model = ArticleModel.getInstance();
    protected ListFragment fragment;
    private String ELLIPSIS = "...";

    int adapterId;

    //constructor
    public ListAdapter(ListFragment fragment) {
        super();
        this.fragment = fragment;
    }

    /*handles layout for each item in the list*/
    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        protected Toolbar toolbar;
        protected FloatingActionButton fab;
        protected CardView card;
        protected TextView title;
        protected TextView date;
        protected NetworkImageView photo;
        protected ImageView faveIc;
        protected ImageView savedIc;
        protected ImageView shareIc;

        boolean longPressing = false;

        /*sub classes determine which list they are using*/
        protected abstract ArrayList<Article> getList();

        //common
        protected View.OnClickListener itemTap = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //create an options object for transition
                ActivityOptionsCompat optionsCompat
                        = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        fragment.getActivity(),
                        Pair.create((View) card, ArticlesApp.TRANSITION_CARD),
                        Pair.create((View) toolbar, ArticlesApp.TRANSITION_TOOLBAR),
                        Pair.create((View) fab, ArticlesApp.TRANSITION_FAB)
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
                longPressing = true;
                return true;
            }
        };
        /* handle tap on faves icon */
        protected ImageView.OnClickListener faveICTap
                = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView ic = (ImageView)v;
                int position = getAdapterPosition();
                Article article = getList().get(position);

                //if a fave
                if(model.isInMaster(article.getRecordID())) {
                    model.removeFromFaves(article);
                    ic.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }else {
                    model.addToFaves(position, adapterId);
                    ic.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
        };
        /* handle tap on saved icon */
        protected ImageView.OnClickListener savedICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView ic = (ImageView)v;
                int position = getAdapterPosition();
                Article article = getList().get(position);

                //if saved before
                if(article.isSaved()) {
                    //remove from saved list
                    model.removeFromSaved(article);
                    //change icon
                    ic.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);
                }else {
                    //add to saved list
                    model.addToSaved(position, adapterId);
                    //change icon
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
            card = (CardView)itemView.findViewById(R.id.article_card);
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
            if(model.isInMaster(article.getRecordID())) {  faveIc.setImageResource(R.drawable.ic_favorite_black_24dp); }
            if(article.isSaved()) { savedIc.setImageResource(R.drawable.ic_watch_later_black_24dp); }
        }
    }
}
