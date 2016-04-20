package uk.ac.kent.fe44.edanews;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    private int extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.extra_toolbar);
        setSupportActionBar(toolbar);

        Intent i = this.getIntent();
        extra = i.getIntExtra(ArticlesApp.EXTRA_ID, ArticlesApp.APP_EXTRA_ID);

        setContents();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setContents() {
        //get a handle on screen items
        TextView contents = (TextView) findViewById(R.id.extra_contents);

        //fill contents based on who called
        switch (extra) {
            case ArticlesApp.APP_EXTRA_ID:
                contents.setText(R.string.app_about);
                getSupportActionBar().setTitle(R.string.about_app);
                break;
            case ArticlesApp.DEPT_EXTRA_ID:
                contents.setText(R.string.school_about);
                getSupportActionBar().setTitle(R.string.school_name);
                break;
            case ArticlesApp.DEVELOPER_EXTRA_ID:
                contents.setText(R.string.developer_about);
                getSupportActionBar().setTitle(R.string.about_developer);
                break;
        }
    }
}
