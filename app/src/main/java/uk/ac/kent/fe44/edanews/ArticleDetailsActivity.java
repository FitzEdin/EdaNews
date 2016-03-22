package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.View;

public class ArticleDetailsActivity extends AppCompatActivity {

    private ArticleModel model = ArticleModel.getInstance();
    private Article article;
    private int itemId;
    private int callerId;

    private String ITEM_ID = "ITEM_ID";
    private String CALLER_ID = "CALLER_ID";
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
                        model.addToFaves(itemId);

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
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra values to pass on to fragment
        Intent i = getIntent();
        itemId = i.getIntExtra(ITEM_ID, 0);
        callerId = i.getIntExtra(CALLER_ID, 0);

        //get a handle on the fab button
        faveFab = (FloatingActionButton) findViewById(R.id.fave_fab);
        //set click listener on fab
        faveFab.setOnClickListener(fabLstnr);

        //take from the correct list based on the parent activity
        switch (callerId){
            case 1:
                article = model.getArticleList().get(itemId);
                //set correct icon
                setCorrectIcon();
                break;
            case 2:
                //hide fab button if we got here via FavesList
                faveFab.setVisibility(View.INVISIBLE);
                article = model.getFavesList().get(itemId);
                break;
            case 3:
                article = model.getSearchList().get(itemId);
                //set correct icon
                setCorrectIcon();
                break;
        }

        ArticleDetailsFragment fragment = (ArticleDetailsFragment)getFragmentManager()
                .findFragmentById(R.id.details_fragment);
        fragment.updateDetails(itemId, callerId);
    }

    private void setCorrectIcon() {
        if(article.isFave()){   faveFab.setImageResource(R.drawable.ic_favorite_white_24dp);    }
        else{   faveFab.setImageResource(R.drawable.ic_favorite_border_white_24dp); }
    }
}
