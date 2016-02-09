package uk.ac.kent.fe44.edanews;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by fitzroy on 28/01/2016.
 *
 * Feeds the data into the listview
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    private ArticleModel model = ArticleModel.getInstance();
    private ArticleListFragment fragment;


    //constructor
    public ArticleListAdapter(ArticleListFragment fragment) {
        super();

        this.fragment = fragment;
    }

    //called when a new row should be created
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

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onItemClicked(getPosition());
                }
            });

            //get a handle on UI views
            title = (TextView)itemView.findViewById(R.id.article_title);
            date = (TextView)itemView.findViewById(R.id.article_date);
            photo = (NetworkImageView)itemView.findViewById(R.id.article_photo);
        }

        //add values from data model to each row
        public void setData(Article article) {
            title.setText(article.getTitle().substring(0, 20) + "...");
            //title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);
            date.setText(article.getDate());
            photo.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());
            //photo.setImageResource(article.getImgResource());
        }
    }
}
