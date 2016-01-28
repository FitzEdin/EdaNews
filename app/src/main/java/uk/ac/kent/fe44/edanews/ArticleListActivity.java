package uk.ac.kent.fe44.edanews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView noNetworkRetry;
    static private FloatingActionButton fab;
    static private LinearLayout searchBar;
    protected ProgressBar mProgressBar;

    private RecyclerView articleListView;
    private LinearLayoutManager layoutManager;
    private ArticleListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        searchBar = (LinearLayout)findViewById(R.id.search_bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        noNetworkRetry = (TextView)findViewById(R.id.no_network_retry);

        tryForNetwork();

        //set up layout manager
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //set up list adapter
        listAdapter = new ArticleListAdapter();

        //set up visual elements
        articleListView = (RecyclerView)findViewById(R.id.article_list_view);
        articleListView.setLayoutManager(layoutManager);
        articleListView.setAdapter(listAdapter);

        //kill progressBar
        mProgressBar.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Snackbar.make(view, "Wattu looking for?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            */
                if(searchBar.getVisibility() == View.VISIBLE) {
                    searchBar.setVisibility(View.INVISIBLE);
                }else{
                    searchBar.setVisibility(View.VISIBLE);
                    searchBar.hasFocus();
                }
            }
        });

        noNetworkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryForNetwork();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void tryForNetwork(){
        //check that there is a connection before starting async thread

        //hide everything
        mProgressBar.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
        noNetworkRetry.setVisibility(View.INVISIBLE);

        if(isNetworkAvailable()){   //network is there? grab things
            //hide no network message if present, show fab+progress bar
            mProgressBar.setVisibility(View.VISIBLE);
            /*create object to get blog posts
            GetBlogPostsTask getBlogPosts = new GetBlogPostsTask();
            //run that object
            getBlogPosts.execute(); */
        }else {        //no network? hide progress bar and tell the user
            noNetworkRetry.setVisibility(View.VISIBLE);
        }
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

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Welcome HOME ! ! !", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_faves) {
            Toast.makeText(this, "My favourite place", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_EDA) {
            Toast.makeText(this, "School at Kent Uni", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_developer) {
            Toast toast = Toast.makeText(this, "Student from Saint Kitts", Toast.LENGTH_SHORT);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Source: previous project kn.muscovado.thadailygeek
    //check that the network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager mngr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mngr.getActiveNetworkInfo();

        boolean isAvailable = false;

        //checks that the network is present && it's connected
        if( ( networkInfo != null ) && ( networkInfo.isConnected() ) ){
            isAvailable = true;		//network is up & running
        }

        return isAvailable;
    }//end isNetworkAvailable() method
}
