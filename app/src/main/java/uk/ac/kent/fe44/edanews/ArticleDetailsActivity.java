package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class ArticleDetailsActivity extends AppCompatActivity {

    private ArticleModel model = ArticleModel.getInstance();
    private Article article;
    private Toast toast;
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
                        model.removeFromFaves(itemId);

                        //change icon
                        faveFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        toast.setText(R.string.faves_removed);
                        toast.show();
                    }else {
                        //add to faves list
                        model.addToFaves(itemId);

                        //change icon
                        faveFab.setImageResource(R.drawable.ic_favorite_black_24dp);
                        toast.setText(R.string.faves_added);
                        toast.show();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        itemId = i.getIntExtra(ITEM_ID, 0);
        callerId = i.getIntExtra(CALLER_ID, 1);

        //take from the correct list based on the parent activity
        switch (callerId){
            case 1:
                article = model.getArticleList().get(itemId);
                break;
            case 2:
                article = model.getFavesList().get(itemId);
                break;
        }

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        faveFab = (FloatingActionButton) findViewById(R.id.fave_fab);
        //set correct icon
        if(article.isFave()){   faveFab.setImageResource(R.drawable.ic_favorite_white_24dp);    }
        else{   faveFab.setImageResource(R.drawable.ic_favorite_border_white_24dp); }
        //set click listener on fab
        faveFab.setOnClickListener(fabLstnr);

        ArticleDetailsFragment fragment = (ArticleDetailsFragment)getFragmentManager()
                .findFragmentById(R.id.details_fragment);
        fragment.updateDetails(itemId, callerId);
    }
}
