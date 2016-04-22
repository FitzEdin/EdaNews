package uk.ac.kent.fe44.edanews.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.R;

public class ScrollingActivity extends AppCompatActivity {

    private int extra;
    private String location;

    private View.OnClickListener mapLstnr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri gmmIntentUri = Uri.parse(location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.extra_toolbar);
        setSupportActionBar(toolbar);

        Intent i = this.getIntent();
        extra = i.getIntExtra(ArticlesApp.EXTRA_ID, ArticlesApp.APP_EXTRA_ID);

        location = null;
        setContents();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setContents() {
        //get a handle on screen items
        TextView contents = (TextView) findViewById(R.id.extra_contents);
        ImageView imgExtra = (ImageView) findViewById(R.id.extra_img);
        FloatingActionButton mapFab = (FloatingActionButton) findViewById(R.id.map_fab);
        mapFab.setOnClickListener(mapLstnr);

        //fill contents based on who called
        switch (extra) {
            case ArticlesApp.APP_EXTRA_ID:
                contents.setText(R.string.app_about);
                getSupportActionBar().setTitle(R.string.about_app);

                mapFab.setVisibility(View.INVISIBLE);
                imgExtra.setImageResource(R.drawable.papaya);
                break;
            case ArticlesApp.DEPT_EXTRA_ID:
                contents.setText(R.string.school_about);
                getSupportActionBar().setTitle(R.string.school_name);

                mapFab.setVisibility(View.VISIBLE);
                location = "google.streetview:cbll=51.2980532,1.0646347&cbp=0,280,0,0,0";
                break;
            case ArticlesApp.DEVELOPER_EXTRA_ID:
                contents.setText(R.string.developer_about);
                getSupportActionBar().setTitle(R.string.about_developer);

                mapFab.setVisibility(View.VISIBLE);
                location = "geo:17.2639909,-62.7220057,10.01z";

                Random rand = new Random();
                int m = rand.nextInt(9);

                switch (m) {
                    case 0:
                        imgExtra.setImageResource(R.drawable.a);
                        break;
                    case 1:
                        imgExtra.setImageResource(R.drawable.b);
                        break;
                    case 2:
                        imgExtra.setImageResource(R.drawable.c);
                        break;
                    case 3:
                        imgExtra.setImageResource(R.drawable.d);
                        break;
                    case 4:
                        imgExtra.setImageResource(R.drawable.e);
                        break;
                    case 5:
                        imgExtra.setImageResource(R.drawable.f);
                        break;
                    case 6:
                        imgExtra.setImageResource(R.drawable.g);
                        break;
                    case 7:
                        imgExtra.setImageResource(R.drawable.h);
                        break;
                    case 8:
                        imgExtra.setImageResource(R.drawable.i);
                        break;
                }
                break;
        }
    }
}
