package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class FavesListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnListItemClickedListener {

    private String ITEM_ID = "ITEM_ID";
    private String CALLER_ID = "CALLER_ID";
    private int callerId = 2;

    private NavigationView navView;
    //private boolean hasTwoPanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faves_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*uncomment for two-pane
        if(findViewById(R.id.details_fragment) == null) {
            hasTwoPanes = false;
        }else {
            hasTwoPanes = true;
        }
        /*end of pc to remove*/


        //TODO: remove this
        //Toast.makeText(this, "FavesListActivity.onCreate", Toast.LENGTH_SHORT);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_faves);
    }

    private ArrayList<Article> getList(){
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

    /*define what happens on choosing a list item*/
    @Override
    public void onItemShared(int position) {
        Article article = getList().get(position);
        String str = article.getWeb_page();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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

        switch (id){
            case R.id.nav_home:
                //Toast.makeText(this, "Welcome HOME ! ! !", Toast.LENGTH_SHORT).show();

                navView = (NavigationView) findViewById(R.id.nav_view);
                navView.setNavigationItemSelectedListener(this);
                navView.setCheckedItem(R.id.nav_home);

                finish();
                break;
            case R.id.nav_faves:
                //Toast.makeText(this, "My favourite place", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_saved:
                //Toast.makeText(this, "My saved articles", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_EDA:
                //Toast.makeText(this, "School at Kent Uni", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_developer:
                //Toast.makeText(this, "Student from Saint Kitts", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_app:
                //Toast.makeText(this, "About the app", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact_EDA:
                //Toast.makeText(this, "Contact EDA at Kent Uni", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact_developer:
                //Toast.makeText(this, "Contact the developer", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
