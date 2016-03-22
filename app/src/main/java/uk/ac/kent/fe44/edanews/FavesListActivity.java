package uk.ac.kent.fe44.edanews;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class FavesListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faves_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set up caller id for FavesDetailsActivity
        callerId = 2;
    }

    public ArrayList<Article> getList(){
        return ArticleModel.getInstance().getFavesList();
    }

    @Override
    public void onPause() {
        super.onPause();
        //release resources, files and sensors
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        //save data
    }
}
