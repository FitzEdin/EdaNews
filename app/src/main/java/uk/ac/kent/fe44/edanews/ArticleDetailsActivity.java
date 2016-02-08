package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class ArticleDetailsActivity extends AppCompatActivity {

    private String ITEM_ID = "ITEM_ID";
    private static FloatingActionButton faveFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        final int itemId = i.getIntExtra(ITEM_ID, 0);

        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        faveFab = (FloatingActionButton) findViewById(R.id.fave_fab);
        faveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get handle on article
                ArticleModel model = ArticleModel.getInstance();
                Article article = model.getArticleList().get(itemId);

                //toggle article's fave status
                if(article.isFave()) {
                    //remove from faves list
                    article.setIsFave(false);
                    model.getFavesList().remove(article);   //won't know article's index on faves list

                    //change icon
                    faveFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    toast.setText("Removed from faves list.");
                    toast.show();
                }else {
                    //add to faves list
                    article.setIsFave(true);
                    model.getFavesList().add(article);

                    //change icon
                    faveFab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    toast.setText("Added to faves list.");
                    toast.show();
                }
            }
        });

        ArticleDetailsFragment fragment = (ArticleDetailsFragment)getFragmentManager()
                .findFragmentById(R.id.details_fragment);
        fragment.updateDetails(itemId);
    }
}
