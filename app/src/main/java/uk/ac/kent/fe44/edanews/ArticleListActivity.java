package uk.ac.kent.fe44.edanews;

import android.content.Context;
import android.content.Intent;
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
        implements NavigationView.OnNavigationItemSelectedListener, ArticleListFragment.OnListItemClickedListener {

    static private FloatingActionButton fab;
    static private LinearLayout searchBar;
    private String ITEM_ID = "ITEM_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Toast.makeText(this, "ArticleListActivity.onCreate", Toast.LENGTH_SHORT);

        searchBar = (LinearLayout)findViewById(R.id.search_bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        searchBar.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    /*define what happens on choosing a list item*/
    @Override
    public void onItemClicked(int position) {
        Intent i = new Intent(this, ArticleDetailsActivity.class);
        i.putExtra(ITEM_ID, position);
        startActivity(i);

        //TODO: remove
        Toast.makeText(this, "Hehe. That tickles!", Toast.LENGTH_SHORT).show();
    }
}
