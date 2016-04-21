package uk.ac.kent.fe44.edanews.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.R;


/**
 * PeekActivity shows a smaller version of the Article
 * above the list view. It receives an Article object
 * as part of its bundle and presents the details of
 * this article object on screen.
 */
public class PeekActivity extends AppCompatActivity {

    //visual elements
    private NetworkImageView peekPhoto;
    private TextView peekTitle;
    private TextView peekShortInfo;
    private ImageView moreIc;
    private ImageView closeIc;

    private Intent i;

    //event listeners
    /* handle tap on more icon */
    protected ImageView.OnClickListener moreICTap = new ImageView.OnClickListener(){
        @Override
        public void onClick(View v) {
            //open details activity
            startActivity(i);
            //close this activity
            finish();
        }
    };
    /* handle tap on more icon */
    protected ImageView.OnClickListener closeICTap = new ImageView.OnClickListener(){
        @Override
        public void onClick(View v) {
            //close this activity
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peek);

        //get a handle on the views
        peekPhoto = (NetworkImageView) findViewById(R.id.peek_photo);
        peekTitle = (TextView) findViewById(R.id.peek_title);
        peekShortInfo = (TextView) findViewById(R.id.peek_short_info);

        //get a handle on the buttons on screen
        moreIc = (ImageView) findViewById(R.id.ic_more);
        closeIc = (ImageView) findViewById(R.id.ic_close);

        //load things from intent into into views
        Intent p = getIntent();
        peekPhoto.setImageUrl(
                p.getStringExtra(ArticlesApp.IMAGE_URL),
                ArticlesApp.getInstance().getImageLoader()
        );
        peekTitle.setText(p.getStringExtra(ArticlesApp.TITLE));
        String str = p.getStringExtra(ArticlesApp.SHORT_INFO) + "...";
        peekShortInfo.setText(str);

        //prepare intent for detailsActivity
        //load values from intent
        int position = p.getIntExtra(ArticlesApp.ITEM_ID, 0);
        int callerId = p.getIntExtra(ArticlesApp.CALLER_ID, ArticlesApp.ARTICLE_CALLER_ID);

        //get info for detailsActivity intent
        i = new Intent(this, ArticleDetailsActivity.class);
        i.putExtra(ArticlesApp.ITEM_ID, position);
        i.putExtra(ArticlesApp.CALLER_ID, callerId);

        //add listeners to buttons
        moreIc.setOnClickListener(moreICTap);
        closeIc.setOnClickListener(closeICTap);
    }
}
