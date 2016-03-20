package uk.ac.kent.fe44.edanews;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    //constructor
    public ListAdapter(ListFragment fragment) {
        super();
        this.fragment = fragment;
    }

    /*handles layout for each item in the list*/
    public abstract class ViewHolder extends RecyclerView.ViewHolder {
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
                fragment.onItemClicked(getAdapterPosition());
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
        /* handle end of long click on article image */
        protected NetworkImageView.OnTouchListener imgLongTapReleased
                = new NetworkImageView.OnTouchListener(){
            @Override
            public boolean onTouch(View vw, MotionEvent motionEvent){
                vw.onTouchEvent(motionEvent);
                if((longPressing)
                        && ( (motionEvent.getAction() == MotionEvent.ACTION_UP)
                /*         || (motionEvent.getAction() == MotionEvent.ACTION_MOVE)*/ )
                        ) {
                    fragment.onLongTapReleased(getAdapterPosition());
                    longPressing = false;
                }
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
                if(article.isFave()) {
                    model.removeFromFaves(article);
                    ic.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }else {
                    model.addToFaves(position);
                    ic.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
        };
        /* handle tap on saved icon */
        protected ImageView.OnClickListener savedICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView imgVw = (ImageView)v;
                imgVw.setImageResource(R.drawable.ic_watch_later_black_24dp);
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
            title = (TextView)itemView.findViewById(R.id.article_title);
            date = (TextView)itemView.findViewById(R.id.article_date);
            photo = (NetworkImageView)itemView.findViewById(R.id.article_photo);

            faveIc = (ImageView)itemView.findViewById(R.id.ic_faves);
            savedIc = (ImageView) itemView.findViewById(R.id.ic_save);
            shareIc = (ImageView) itemView.findViewById(R.id.ic_share);

            photo.setOnLongClickListener(imgLongTap);
            photo.setOnTouchListener(imgLongTapReleased);
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
            if(article.isFave()) {  faveIc.setImageResource(R.drawable.ic_favorite_black_24dp); }
            if(article.isSaved()) { savedIc.setImageResource(R.drawable.ic_watch_later_black_24dp); }
        }
    }
}
