package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;


/**
 * PeekActivity shows a smaller version of the Article
 * above the list view. It receives an Article object
 * as part of its bundle and presents the details of
 * this article object on screen.
 */
public class PeekActivity extends AppCompatActivity {

    NetworkImageView peekPhoto;
    TextView peekTitle;
    TextView peekShortInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peek);

        long x = 100, y = 100;

        MotionEvent event = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                x, y, 0);
        this.dispatchTouchEvent(event);

        //get a handle on the views
        peekPhoto = (NetworkImageView) findViewById(R.id.peek_photo);
        peekTitle = (TextView) findViewById(R.id.peek_title);
        peekShortInfo = (TextView) findViewById(R.id.peek_short_info);

        //load things from intent into into views
        Intent p = getIntent();
        peekPhoto.setImageUrl(
                p.getStringExtra(ArticlesApp.IMAGE_URL),
                ArticlesApp.getInstance().getImageLoader()
        );
        peekTitle.setText(p.getStringExtra(ArticlesApp.TITLE));
        peekShortInfo.setText(p.getStringExtra(ArticlesApp.SHORT_INFO));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_UP):
                Toast.makeText(this, "You raised your finger", Toast.LENGTH_SHORT).show();
                return true;

            case (MotionEvent.ACTION_DOWN):
                Toast.makeText(this, "You are pressing down", Toast.LENGTH_SHORT).show();
                return true;

            default:
//                Toast.makeText(this, "You moved your finger", Toast.LENGTH_SHORT).show();
                return false;
        }
    }
}
