package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleListActivity extends ListActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static FloatingActionButton searchFab;
    private static LinearLayout searchBar;
    private static EditText searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

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
        //Toast.makeText(this, "ArticleListActivity.onCreate", Toast.LENGTH_SHORT);

        //set up caller id for ArticleDetailsActivity
        callerId = 1;

        searchBar = (LinearLayout)findViewById(R.id.search_bar);
        searchTextView = (EditText) findViewById(R.id.search_text);
        //listen for searches
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = v.getText().toString();
                    v.setText("");
                    closeSearchBar(searchTextView);
                    searchFor(searchText);
                    handled = true;
                }
                return handled;
            }
        });

        searchFab = (FloatingActionButton)findViewById(R.id.search_fab);
        //add functionality to the FAB
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Snackbar.make(view, "Wattu looking for?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            */
                searchFab.setVisibility(View.INVISIBLE);
                searchBar.setVisibility(View.VISIBLE);
                searchBar.hasFocus();
            }
        });

        searchBar.setVisibility(View.INVISIBLE);
        searchFab.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);
    }

    public ArrayList<Article> getList(){
        return ArticleModel.getInstance().getArticleList();
    }

    public void closeSearchBar(View v) {
        hideKeyboard(this);
        searchBar.setVisibility(View.INVISIBLE);
        searchFab.setVisibility(View.VISIBLE);
    }
    public void searchFor(String key) {
        //Toast.makeText(this, "Searching for: " + key, Toast.LENGTH_SHORT).show();
        //make network request
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Dialog d;

        switch (id){
            case R.id.nav_faves:
                Intent i = new Intent(this, FavesListActivity.class);
                startActivity(i);
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
}
