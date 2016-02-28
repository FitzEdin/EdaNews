package uk.ac.kent.fe44.edanews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by fitzroy on 26/02/2016.
 */
public class FavesListAdapter extends RecyclerView.Adapter<FavesListAdapter.ViewHolder> {

    private ArticleModel model = ArticleModel.getInstance();
    //private FavesListFragment fragment;

    //constructor
    public FavesListAdapter(/*FavesListFragment fragment*/) {
        super();

        //this.fragment = fragment;
    }

    //called when a new item should be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.fave_article_item,
                        parent,
                        false
                );

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //populates new rows with data
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = model.getFavesList().get(position);
        holder.setData(article);
    }

    //returns size of data list
    @Override
    public int getItemCount() {
        return model.getFavesList().size();
    }

    /*handles layout for each item in the list*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private NetworkImageView photo;
        private ImageView faveIc;
        private ImageView savedIc;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fragment.onItemClicked(getPosition());
                }
            });

            //get a handle on UI views
            title = (TextView)itemView.findViewById(R.id.article_title);
            date = (TextView)itemView.findViewById(R.id.article_date);
            photo = (NetworkImageView)itemView.findViewById(R.id.article_photo);

            faveIc = (ImageView)itemView.findViewById(R.id.ic_faves);
            savedIc = (ImageView) itemView.findViewById(R.id.ic_save);
        }

        //add values from data model to each row
        public void setData(Article article) {
            //display the first 20 characters only
            title.setText(article.getTitle().substring(0, 20) + "...");
            date.setText(article.getDate());
            photo.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());
            faveIc.setImageResource(R.drawable.ic_favorite_black_24dp);
            savedIc.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);

            // force the recyclerView to redraw icons correctly for each article
            if(article.isSaved()) { faveIc.setImageResource(R.drawable.ic_watch_later_black_24dp); }
        }
    }
}
