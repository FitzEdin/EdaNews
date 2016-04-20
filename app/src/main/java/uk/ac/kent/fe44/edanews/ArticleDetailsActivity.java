package uk.ac.kent.fe44.edanews;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ArticleDetailsActivity extends AppCompatActivity {

    private ArticleModel model = ArticleModel.getInstance();
    private Article article;
    private int itemId;
    private int callerId;

    private static FloatingActionButton plusFab;
    private static LinearLayout plusBar;

    private ImageView faveIc;
    private ImageView savedIc;
    private ImageView shareIc;

    /**
     * Respond to clicks on fab button.
     */
    private View.OnClickListener onPlusFabClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Animator anim = createAnimator(plusBar, 600);

            //toggle searchBar and searchFab visibility
            plusFab.setVisibility(View.INVISIBLE);
            plusBar.setVisibility(View.VISIBLE);
            anim.start();
        }
    };

    /* handle taps on fave button */
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
                        model.addToFaves(itemId, callerId);
                        //change icon
                        faveIc.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            };

    private View.OnClickListener savedICLstnr =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //toggle article's saved status
                    if(article.isSaved()) {
                        //remove from saved list
                        article.setIsSaved(false);
            //TODO:            model.removeFromSaved(article);
                        //change icon
                        savedIc.setImageResource(R.drawable.ic_watch_later_outline_black_24dp);
                    }else {
                        //add to saved list
                        article.setIsSaved(true);
            //TODO:            model.addToSaved(itemId, callerId);
                        //change icon
                        savedIc.setImageResource(R.drawable.ic_watch_later_black_24dp);
                    }
                }
            };

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
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra values to pass on to fragment
        Intent i = getIntent();
        itemId = i.getIntExtra(ArticlesApp.ITEM_ID, 0);
        callerId = i.getIntExtra(ArticlesApp.CALLER_ID, ArticlesApp.ARTICLE_CALLER_ID);

        //take from the correct list based on the parent activity
        getArticle();

        //set up plus bar and plus fab
        setUpPlusFab();
        setUpPlusBar();

        ArticleDetailsFragment fragment = (ArticleDetailsFragment)getFragmentManager()
                .findFragmentById(R.id.details_fragment);
        fragment.updateDetails(itemId, callerId);
    }

    private void getArticle() {
        switch (callerId){
            case ArticlesApp.ARTICLE_CALLER_ID:
                article = model.getArticleList().get(itemId);
                break;
            case ArticlesApp.FAVES_CALLER_ID:
                article = model.getFavesList().get(itemId);
                break;
            case ArticlesApp.SEARCH_CALLER_ID:
                article = model.getSearchList().get(itemId);
                break;
        }
    }

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
}
