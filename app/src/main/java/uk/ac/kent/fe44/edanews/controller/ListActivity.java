package uk.ac.kent.fe44.edanews.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;

import java.util.ArrayList;

import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.view.ListFragment;
import uk.ac.kent.fe44.edanews.R;

/**
 * Created by fitzroy on 07/03/2016.
 */
public abstract class ListActivity extends AppCompatActivity
        implements ListFragment.OnListItemClickedListener {

    protected int callerId;

    protected NavigationView navView;

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
        sendIntent.setType(ArticlesApp.PLAIN_TEXT);
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
