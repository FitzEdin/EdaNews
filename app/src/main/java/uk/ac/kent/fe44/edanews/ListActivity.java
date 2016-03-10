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
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnListItemClickedListener {

    protected final static String ITEM_ID = "ITEM_ID";
    protected final static String CALLER_ID = "CALLER_ID";
    protected final static String PLAIN_TEXT = "text/plain";
    protected int callerId;

    private final static String classArticleActivity = "ArticleListActivity";
    private final static String classFavesActivity = "FavesListActivity";

    protected NavigationView navView;
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

    /*define what happens on sharing a list item*/
    @Override
    public void onLongTap(int position) {
        Article article = getList().get(position);

        String title = article.getTitle();
        String message = article.getShortInfo();
        String imgUrl = article.getImageURL();

        Dialog d = createDialog(title, message);
        d.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Dialog d;

        switch (id){
            case R.id.nav_home:
                //Toast.makeText(this, this.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                switch(this.getClass().getSimpleName()) {
                    //add cases for other classes
                    case classArticleActivity:
                        //do nothing
                        Toast.makeText(this, "We're here already", Toast.LENGTH_SHORT).show();
                        break;
                    case classFavesActivity:
                        finish();
                        break;
                }
                break;
            case R.id.nav_faves:
                //Toast.makeText(this, "My favourite place", Toast.LENGTH_SHORT).show();
                switch(this.getClass().getSimpleName()) {
                    //add cases for other classes
                    case classArticleActivity:
                        Intent i = new Intent(this, FavesListActivity.class);
                        startActivity(i);
                        break;
                    case classFavesActivity:
                        //do nothing
                        Toast.makeText(this, "We're here already", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.nav_saved:
                //Toast.makeText(this, "My saved articles", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_EDA:
                d = createDialog(getString(R.string.school_name), getString(R.string.school_about));
                d.show();
                break;
            case R.id.nav_developer:
                //Toast.makeText(this, "Student from Saint Kitts", Toast.LENGTH_SHORT).show();
                d = createDialog(getString(R.string.about_developer), getString(R.string.developer_about));
                d.show();
                break;
            case R.id.nav_app:
                //Toast.makeText(this, "About the app", Toast.LENGTH_SHORT).show();
                d = createDialog(getString(R.string.about_app), getString(R.string.app_about));
                d.show();
                break;
            case R.id.nav_contact_EDA:
                //Toast.makeText(this, "Contact EDA at Kent Uni", Toast.LENGTH_SHORT).show();
                d = createDialog(getString(R.string.school_name), getString(R.string.school_contact));
                d.show();
                break;
            case R.id.nav_contact_developer:
                d = createDialog(getString(R.string.developer_name), getString(R.string.developer_contact));
                d.show();
                break;
            case R.id.nav_settings:
                d = createDialog("Sorry", "That doesn't do anything yet.");
                d.show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
