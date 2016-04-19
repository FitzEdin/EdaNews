package uk.ac.kent.fe44.edanews;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.support.v4.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by fitzroy on 07/03/2016.
 */
public abstract class ListActivity extends AppCompatActivity
        implements ListFragment.OnListItemClickedListener {

    protected final static String PLAIN_TEXT = "text/plain";
    protected int callerId;

    protected NavigationView navView;
    //protected boolean hasTwoPanes;

    private PeekDialog pkDialog = new PeekDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //describe what happens when the activity exits
        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementExitTransition(
                    TransitionInflater.from(this)
                            .inflateTransition(R.transition.shared_image)
            );
        }
    }

    //each subclass has to provide access to its list
    public abstract ArrayList<Article> getList();

    /*define what happens on sharing a list item*/
    @Override
    public void onItemShared(int position) {
        Article article = getList().get(position);
        String str = article.getWeb_page();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType(PLAIN_TEXT);
        startActivity(Intent.createChooser(sendIntent, "Share on..."));
    }

    /*define what happens on choosing a list item*/
    @Override
    public void onItemClicked(int position, Bundle bundle) {
        Intent i = new Intent(this, ArticleDetailsActivity.class);

        i.putExtra(ArticlesApp.ITEM_ID, position);
        i.putExtra(ArticlesApp.CALLER_ID, callerId);

        startActivity(i, bundle);
    }

    /*define what happens when a long press is done on an item image */
    @Override
    public void onLongTap(int position) {
        Article article = getList().get(position);

        //prep bundle for fragment
//        Bundle args = new Bundle();
//        args.putString(ArticlesApp.TITLE, article.getTitle());
//        args.putString(ArticlesApp.SHORT_INFO, article.getShortInfo());
//        args.putString(ArticlesApp.IMAGE_URL, article.getImageURL());
//
//        pkDialog.setArguments(args);    //pass bundle to fragment
//        pkDialog.show(getFragmentManager(), ArticlesApp.TAG_PEEK_ARTICLE);

        //prep intent for peek activity
        Intent p = new Intent(this, PeekActivity.class);

        //send article title, image, short info
        p.putExtra(ArticlesApp.IMAGE_URL, article.getImageURL());
        p.putExtra(ArticlesApp.TITLE, article.getTitle());
        p.putExtra(ArticlesApp.SHORT_INFO, article.getShortInfo());

        //send what's needed to launch the details activity
        p.putExtra(ArticlesApp.ITEM_ID, position);
        p.putExtra(ArticlesApp.CALLER_ID, callerId);

        startActivity(p);
    }

    /* define what happens when the long tap on the image is released */
    @Override
    public void onLongTapReleased(int position){
            //pkDialog.dismiss();
    }


    /*create the About dialog*/
    public Dialog createDialog(String title, String message) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public int getCallerId() {
        return callerId;
    }
}
