package uk.ac.kent.fe44.edanews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by fitzroy on 07/03/2016.
 */
public abstract class ListActivity extends AppCompatActivity
        implements ListFragment.OnListItemClickedListener {

    protected final static String ITEM_ID = "ITEM_ID";
    protected final static String CALLER_ID = "CALLER_ID";
    protected final static String PLAIN_TEXT = "text/plain";
    protected int callerId;

    private final static String classArticleActivity = "ArticleListActivity";
    private final static String classFavesActivity = "FavesListActivity";

    protected NavigationView navView;
    protected Dialog d;
    //protected boolean hasTwoPanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onItemClicked(int position) {
        //TODO: remove
        //Toast.makeText(this, "Hehe. That tickles!", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, ArticleDetailsActivity.class);
        i.putExtra(ITEM_ID, position);
        i.putExtra(CALLER_ID, callerId);
        startActivity(i);

        /*TODO: uncomment for two-pane
        if(hasTwoPanes) {
            ArticleDetailsFragment fragment = (ArticleDetailsFragment)getFragmentManager().findFragmentById(R.id.details_fragment);
            fragment.updateDetails(position);
        }else {
            Intent i = new Intent(this, ArticleDetailsActivity.class);
            i.putExtra(ITEM_ID, position);
            startActivity(i);
        }
        /*end of section for two-panes*/
    }

    /*define what happens when a long press is done on an item image */
    @Override
    public void onLongTap(int position) {
        Article article = getList().get(position);

        String title = article.getTitle();
        String message = article.getShortInfo();
        String imgUrl = article.getImageURL();

        d = createDialog(title, message);
        d.show();
    }

    /* define what happens when the long tap on the image is released */
    @Override
    public void onLongTapReleased(int position){
        if(d.isShowing()){
            d.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*create the About dialog*/
    public Dialog createDialog(String title, String message) {
    /*    LayoutInflater inflater = this.getLayoutInflater();
        View aboutView = inflater.inflate(R.layout.about_dialog, null); */
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null);
        // Create the AlertDialog object and return it
        return builder.create();
    }


    /*create the About dialog*/
    public Dialog createPeekDialog(String title, String shortInfo, String imgUrl) {
        //get the overall view for the dialog
        View peekView = findViewById(R.id.peek_view);

        //get handles on the child views
        TextView titleView = (TextView)findViewById(R.id.peek_title);
        TextView shortInfoView = (TextView)findViewById(R.id.peek_short_info);
        //NetworkImageView imgView = (NetworkImageView)findViewById(R.id.peek_photo);

        //add the
        titleView.setText(title);
        shortInfoView.setText(shortInfo);
        //imgView.setImageUrl(imgUrl, ArticlesApp.getInstance().getImageLoader());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setView(peekView)
                .setPositiveButton("Close", null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
