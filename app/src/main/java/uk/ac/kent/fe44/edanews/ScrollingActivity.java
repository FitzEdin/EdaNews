package uk.ac.kent.fe44.edanews;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

public class ScrollingActivity extends AppCompatActivity {

    private static FloatingActionButton plusFab;
    private static LinearLayout plusBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        setUpPlus();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpPlus() {
        plusBar = (LinearLayout) findViewById(R.id.plus_bar);
        plusFab = (FloatingActionButton) findViewById(R.id.plus_fab);
        plusFab.setOnClickListener(onPlusFabClickListener);

        plusBar.setVisibility(View.INVISIBLE);
        plusFab.setVisibility(View.VISIBLE);
    }

    /**
     *
     */
    private void closePlusBar() {
        //toggle searchBar and searchFab visibility
        Animator anim = createAnimator(plusFab, 150);

        plusBar.setVisibility(View.INVISIBLE);
        plusFab.setVisibility(View.VISIBLE);
        anim.start();
    }

    /**
     *
     * @param showMe
     * @param duration
     * @return
     */
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

    @Override
    public void onBackPressed() {
        if(plusBar.getVisibility() == View.VISIBLE) {
            closePlusBar();
        }else {
            super.onBackPressed();
        }
    }
}
