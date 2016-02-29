package uk.ac.kent.fe44.edanews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by fitzroy on 28/01/2016.
 *
 * Feeds the data into the list of articles
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private ArticleModel model = ArticleModel.getInstance();
    private ArticleListFragment fragment;

    //constructor
    public ArticleListAdapter(ArticleListFragment fragment) {
        super();
        this.fragment = fragment;
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

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //populates new rows with data
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = model.getArticleList().get(position);
        holder.setData(article);
    }

    //returns size of data list
    @Override
    public int getItemCount() {
        return model.getArticleList().size();
    }

    /*handles layout for each item in the list*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private NetworkImageView photo;
        private ImageView faveIc;
        private ImageView savedIc;
        private ImageView shareIc;

        private View.OnClickListener itemTap = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragment.onItemClicked(getAdapterPosition());
            }
        };

        /* handle long click on article image */
        private NetworkImageView.OnLongClickListener imgLongTap
                = new NetworkImageView.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                int position = getAdapterPosition();
                //TODO: place dialog here
                String str = model.getArticleList().get(position).getTitle();
                Toast.makeText(itemView.getContext(), str, Toast.LENGTH_LONG).show();
                return true;
            }
        };

        /* handle tap on faves icon */
        private ImageView.OnClickListener faveICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView ic = (ImageView)v;
                int position = getAdapterPosition();
                Article article = model.getArticleList().get(position);

                //if a fave
                if(article.isFave()) {
                    model.removeFromFaves(position);
                    ic.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Toast.makeText(itemView.getContext(), R.string.faves_removed, Toast.LENGTH_SHORT).show();
                }else{
                    model.addToFaves(position);
                    ic.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Toast.makeText(itemView.getContext(), R.string.faves_added, Toast.LENGTH_SHORT).show();
                }
            }
        };

        /* handle tap on saved icon */
        private ImageView.OnClickListener savedICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), "SAVE ARTICLE", Toast.LENGTH_SHORT).show();
                ImageView imgVw = (ImageView)v;
                imgVw.setImageResource(R.drawable.ic_watch_later_black_24dp);
    /*            ImageView ic = (ImageView)v;
                int position = getAdapterPosition();
                Article article = model.getArticleList().get(position);

                //if a fave
                if(article.isFave()) {
                    model.removeFromFaves(position);
                    ic.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }else{
                    model.addToFaves(position);
                    ic.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
    */
            }
        };

        /* handle tap on share icon */
        private ImageView.OnClickListener shareICTap = new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*TODO: implement share*/
                Toast.makeText(itemView.getContext(), "SHOUT LOUD", Toast.LENGTH_SHORT).show();
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
            photo.setOnClickListener(itemTap);

            faveIc.setOnClickListener(faveICTap);
            savedIc.setOnClickListener(savedICTap);
            shareIc.setOnClickListener(shareICTap);
        }

        //add values from data model to each row
        public void setData(Article article) {
            //display the first 20 characters only
            title.setText(article.getTitle().substring(0, 20) + "...");
            date.setText(article.getDate());
            photo.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());
            faveIc.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            savedIc.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);
            //no need to change the share icon

            // force the recyclerView to redraw icons correctly for each article
            if(article.isFave()) {  faveIc.setImageResource(R.drawable.ic_favorite_black_24dp); }
            if(article.isSaved()) { faveIc.setImageResource(R.drawable.ic_watch_later_black_24dp); }
        }
    }
}
