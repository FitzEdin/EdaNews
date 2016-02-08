package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ArticleDetailsActivity extends AppCompatActivity {

    private String ITEM_ID = "ITEM_ID";
    private static FloatingActionButton faveFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        faveFab = (FloatingActionButton) findViewById(R.id.fave_fab);
        faveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add article to fave list

                //change icon on article
                faveFab.setImageResource(R.drawable.ic_favorite_white_24dp);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        int itemId = i.getIntExtra(ITEM_ID, 0);

        ArticleDetailsFragment fragment = (ArticleDetailsFragment)getFragmentManager()
                .findFragmentById(R.id.details_fragment);
        fragment.updateDetails(itemId);
    }
}
