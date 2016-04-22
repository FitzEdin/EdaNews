package uk.ac.kent.fe44.edanews.controller;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.R;
import uk.ac.kent.fe44.edanews.model.ArticleModel;

public class ArticleDetailsActivity extends AppCompatActivity
        implements ArticleModel.OnDetailsUpdateListener {

    private ArticleModel model = ArticleModel.getInstance();
    private Article article;    //the actual article
    private int itemIndex;      //the index of the item in the list
    private int callerId;       //identifies the list this article is being opened from

    //visual elements
    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleContents;
    private NetworkImageView articlePhoto;

    private static FloatingActionButton plusFab;
    private static LinearLayout plusBar;

    //action buttons in the plusBar
    private ImageView faveIc;
    private ImageView savedIc;
    private ImageView shareIc;

    /**
     * Respond to clicks on fab button.
     */
    private View.OnClickListener onPlusFabClickListener =
            new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animator anim = createAnimator(plusBar, 600);

            //toggle searchBar and searchFab visibility
            plusFab.setVisibility(View.INVISIBLE);
            plusBar.setVisibility(View.VISIBLE);
            anim.start();
        }
    };

    /**
     * handle taps on fave button
     */
    private View.OnClickListener faveICLstnr =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //toggle article's fave status
                    if(article.isFave()) {
                        //remove from faves list
                        model.removeFromFaves(article);
                        //change icon
                        faveIc.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }else {
                        //add to faves list
                        model.addToFaves(article);
                        //change icon
                        faveIc.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            };

    /**
     * Handle clicks on the save button
     */
    private View.OnClickListener savedICLstnr =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //toggle article's saved status
                    if(article.isSaved()) {
                        //remove from saved list
                        model.removeFromSaved(article);
                        //change icon
                        savedIc.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);
                    }else {
                        //add to saved list
                        model.addToSaved(article);
                        //change icon
                        savedIc.setImageResource(R.drawable.ic_watch_later_black_24dp);
                    }
                }
            };

    /**
     * Share the article online.
     */
    private View.OnClickListener shareICLstnr =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = article.getWeb_page();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, str);
                    sendIntent.setType(ArticlesApp.PLAIN_TEXT);
                    startActivity(Intent.createChooser(sendIntent, "Share on..."));
                }
            };

    /**
     * Get the article from the appropriate list, and
     * populate the views.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //describe what happens when the activity enters
        if(Build.VERSION.SDK_INT >= 21) {
            Transition shared
                    = TransitionInflater.from(this)
                            .inflateTransition(R.transition.shared_image);
            getWindow().setSharedElementEnterTransition(shared);
        }
        setContentView(R.layout.activity_article_details);

        articleTitle = (TextView)findViewById(R.id.detail_title);
        articleDate = (TextView)findViewById(R.id.detail_date);
        articleContents = (TextView)findViewById(R.id.detail_contents);
        articlePhoto = (NetworkImageView)findViewById(R.id.detail_img);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_article_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra values to pass on to fragment
        Intent i = getIntent();
        itemIndex = i.getIntExtra(ArticlesApp.ITEM_INDEX, 0);
        callerId = i.getIntExtra(ArticlesApp.CALLER_ID, ArticlesApp.ARTICLE_CALLER_ID);

        //take from the correct list based on the parent activity
        getArticle();
        updateUI();

        //set up plus bar and plus fab if coming from search or article fragments
        //hide the plusBar if coming from the saved or faves list
        if( (callerId == ArticlesApp.ARTICLE_CALLER_ID) || (callerId == ArticlesApp.SEARCH_CALLER_ID) ){
            setUpPlusFab();
            setUpPlusBar();
        }else {
            hidePlus();
        }
    }

    /**
     * Retrieve the article from the correct list; the
     * callerId identifies which list to pull the article
     * from.
     */
    private void getArticle() {
        switch (callerId){
            case ArticlesApp.ARTICLE_CALLER_ID:
                article = model.getArticleList().get(itemIndex);
                break;
            case ArticlesApp.FAVES_CALLER_ID:
                article = model.getFavesList().get(itemIndex);
                break;
            case ArticlesApp.SAVED_CALLER_ID:
                article = model.getSavedList().get(itemIndex);
                break;
            case ArticlesApp.SEARCH_CALLER_ID:
                article = model.getSearchList().get(itemIndex);
                break;
        }
    }

    /**
     * Update the visual elements with data from the article.
     */
    private void updateUI() {
        //update UI with data
        articleTitle.setText(article.getTitle());
        articleDate.setText(article.getDate());
        articlePhoto.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());

        if(article.getContents() == null) {
            //show appropriate network message
            if(ArticlesApp.getInstance().networkIsAvailable()) {
                articleContents.setText(R.string.loading_contents);

                //load the article's details from the model
                model.loadArticleDetails(callerId, article.getRecordID(), itemIndex, this);
            }else{
                articleContents.setText(R.string.no_network);
            }
        }else{
            articleContents.setText(article.getContents());
        }
    }

    /**
     * Handle response from
     */
    @Override
    public void onDetailsUpdate() {
        articleContents.setText(article.getContents());
    }

    /**
     * Save the master list to the database.
     */
    @Override
    public void onStop() {
        super.onStop();
        ArticleModel.getInstance().saveMasterList(this);
    }

    @Override
    public void onBackPressed() {
        if(plusBar.getVisibility() == View.VISIBLE) {
            closePlusBar();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * Set the correct icons based on the properties of the article.
     */
    private void setCorrectIcons() {
        if(model.isInMaster(article.getRecordID())) {
            if(article.isFave()) {   faveIc.setImageResource(R.drawable.ic_favorite_black_24dp); }
            if(article.isSaved()) {   savedIc.setImageResource(R.drawable.ic_watch_later_black_24dp); }
        }
    }
    private Animator createAnimator(View showMe, long duration) {
        int cx = showMe.getWidth()/2;
        int cy = showMe.getHeight()/2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(showMe, cx, cy, 0, finalRadius);
            anim.setDuration(duration);
        }
        return anim;
    }
    private void closePlusBar() {
        //toggle searchBar and searchFab visibility
        Animator anim = createAnimator(plusFab, 150);

        plusBar.setVisibility(View.INVISIBLE);
        plusFab.setVisibility(View.VISIBLE);
        anim.start();
    }
    private void setUpPlusFab() {
        plusBar = (LinearLayout) findViewById(R.id.plus_bar);
        plusFab = (FloatingActionButton) findViewById(R.id.plus_fab);
        plusFab.setOnClickListener(onPlusFabClickListener);

        plusBar.setVisibility(View.INVISIBLE);
        plusFab.setVisibility(View.VISIBLE);
    }
    private void setUpPlusBar() {
        //get handle on bar icons
        faveIc = (ImageView) findViewById(R.id.ic_details_faves);
        savedIc = (ImageView) findViewById(R.id.ic_details_save);
        shareIc = (ImageView) findViewById(R.id.ic_details_share);

        //shade bar icons appropriately
        setCorrectIcons();

        //set listeners on bar icons
        faveIc.setOnClickListener(faveICLstnr);
        savedIc.setOnClickListener(savedICLstnr);
        shareIc.setOnClickListener(shareICLstnr);
    }
    private void hidePlus() {
        plusBar = (LinearLayout) findViewById(R.id.plus_bar);
        plusFab = (FloatingActionButton) findViewById(R.id.plus_fab);

        plusBar.setVisibility(View.INVISIBLE);
        plusFab.setVisibility(View.INVISIBLE);
    }
}
