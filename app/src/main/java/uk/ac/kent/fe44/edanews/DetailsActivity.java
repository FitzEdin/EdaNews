package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class DetailsActivity extends AppCompatActivity implements ArticleModel.OnDetailsUpdateListener {

    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleContents;
    private NetworkImageView articlePhoto;

    private ArticleModel model = ArticleModel.getInstance();
    private Article article;
    private int articleId;
    private int itemIndex;
    private int callerId;

    private static FloatingActionButton faveFab;

    /* handle taps on fab */
    private View.OnClickListener fabLstnr =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //toggle article's fave status
                    if(article.isFave()) {
                        //remove from faves list
                        model.removeFromFaves(article);

                        //change icon
                        faveFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }else {
                        //add to faves list
                        model.addToFaves(article);

                        //change icon
                        faveFab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //describe what happens when the activity enters
        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementEnterTransition(
                    TransitionInflater.from(this)
                            .inflateTransition(R.transition.shared_image)
            );
        }
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra values to pass on to fragment
        Intent i = getIntent();
        itemIndex = i.getIntExtra(ArticlesApp.ITEM_ID, 0);
        callerId = i.getIntExtra(ArticlesApp.CALLER_ID, ArticlesApp.ARTICLE_CALLER_ID);

        //get a handle on the fab button
        faveFab = (FloatingActionButton) findViewById(R.id.plus_fab);
        //set click listener on fab
        faveFab.setOnClickListener(fabLstnr);

        articleTitle = (TextView)findViewById(R.id.detail_title);
        articleDate = (TextView)findViewById(R.id.detail_date);
        articleContents = (TextView)findViewById(R.id.detail_contents);
        articlePhoto = (NetworkImageView)findViewById(R.id.detail_img);

        //take from the correct list based on the parent activity
        switch (callerId){
            case ArticlesApp.ARTICLE_CALLER_ID:
                article = model.getArticleList().get(itemIndex);
                //set correct icon
                setCorrectIcon();
                break;
            case ArticlesApp.FAVES_CALLER_ID:
                //hide fab button if we got here via FavesList
                faveFab.setVisibility(View.INVISIBLE);
                article = model.getFavesList().get(itemIndex);
                break;
            case ArticlesApp.SAVED_CALLER_ID:
                //hide fab button if we got here via FavesList
                faveFab.setVisibility(View.INVISIBLE);
                article = model.getSavedList().get(itemIndex);
                break;
            case ArticlesApp.SEARCH_CALLER_ID:
                article = model.getSearchList().get(itemIndex);
                //set correct icon
                setCorrectIcon();
                break;
        }

        updateDetails(itemIndex, callerId);
    }

    private void setCorrectIcon() {
        if(article.isFave()){   faveFab.setImageResource(R.drawable.ic_favorite_white_24dp);    }
        else{   faveFab.setImageResource(R.drawable.ic_favorite_border_white_24dp); }
    }

    /*populate the views with article's details*/
    public void updateDetails(int index, int caller) {
        //get the article from the correct list
        Article article = new Article();
        switch(caller){
            case ArticlesApp.ARTICLE_CALLER_ID:
                article = ArticleModel
                        .getInstance()
                        .getArticleList()
                        .get(index);
                break;
            case ArticlesApp.FAVES_CALLER_ID:
            case ArticlesApp.SAVED_CALLER_ID:
                article = ArticleModel
                        .getInstance()
                        .getMasterList()
                        .get(index);
                break;
            case ArticlesApp.SEARCH_CALLER_ID:
                article = ArticleModel
                        .getInstance()
                        .getSearchList()
                        .get(index);
                break;
        }

        //update UI with data
        articleTitle.setText(article.getTitle());
        articleDate.setText(article.getDate());
        articlePhoto.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());

        if(article.getContents() != null) {
            articleContents.setText(article.getContents());
        }else{
            //show network message
            articleContents.setText(R.string.loading_contents);

            //get a handle on article's record id for network request
            articleId = article.getRecordID();
            getArticleDetails();
        }
    }

    /*prompts the ArticleModel class to get article's details from the network*/
    public void getArticleDetails() {
        //get details
        ArticleModel model = ArticleModel.getInstance();
        model.loadArticleDetails(callerId, articleId, itemIndex, this);
    }

    @Override
    public void onDetailsUpdate() {
        Article article = ArticleModel
                .getInstance()
                .getArticleList()
                .get(itemIndex);
        articleContents.setText(article.getContents());
    }
}
