package uk.ac.kent.fe44.edanews;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArticleListActivity extends ListActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static FloatingActionButton searchFab;
    private static LinearLayout searchBar;
    private static EditText searchTextView;
    private View.OnClickListener onFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //get center of searchBar for animation
            int cx = searchBar.getWidth()/2;
            int cy = searchBar.getHeight()/2;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                anim = ViewAnimationUtils.createCircularReveal(searchBar, cx, cy, 0, finalRadius);
                anim.setDuration(600);
            }
            //toggle searchBar and searchFab visibility
            searchFab.setVisibility(View.INVISIBLE);
            searchBar.setVisibility(View.VISIBLE);
            anim.start();

            //prep the text view for capturing input
            searchTextView.setText("");
            searchTextView.setFocusableInTouchMode(true);
            searchTextView.requestFocus();

            showKeyboard();
        }
    };
    private TextView.OnEditorActionListener searchButtonListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchText = v.getText().toString();
                v.setText("");
                closeSearchBar(searchTextView);
                searchFor(searchText);

                handled = true;
            }
            return handled;
        }
    };

    private SearchListFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.list_fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ArticleListFragment firstFragment = new ArticleListFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment, firstFragment).commit();
        }

        //set up caller id for ArticleDetailsActivity
        callerId = 1;

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpSearch() {
        searchBar = (LinearLayout)findViewById(R.id.search_bar);
        searchTextView = (EditText) findViewById(R.id.search_text);
        //listen for searches
        searchTextView.setOnEditorActionListener(searchButtonListener);

        searchFab = (FloatingActionButton)findViewById(R.id.search_fab);
        //add functionality to the FAB
        searchFab.setOnClickListener(onFabClickListener);

        searchBar.setVisibility(View.INVISIBLE);
        searchFab.setVisibility(View.VISIBLE);
    }

    /**
     * Set the list to be provided to the adapter
     * based on which fragment is being shown.
     * @return an ArrayList<Article> to be shown
     *          in the fragment's RecyclerView
     */
    public ArrayList<Article> getList(){
        switch (callerId) {
            case 3:
                return ArticleModel.getInstance().getSearchList();
            case 2:
                return ArticleModel.getInstance().getFavesList();
            default:
                return ArticleModel.getInstance().getArticleList();
        }
    }

    /**
     * Hide the search bar
     * @param v The view button clicked to hide the search bar.
     */
    public void closeSearchBar(View v) {
        hideKeyboard(this);
        searchBar.setVisibility(View.INVISIBLE);
        searchFab.setVisibility(View.VISIBLE);
    }

    /**
     * Perform a search for the key provided; the fragment is swapped in
     * if not existing previously. Each time it is handed a new key
     * @param key
     */
    public void searchFor(String key) {
        if(callerId != 3) {
            //swap in the search fragment
            callerId = 3;
            getSupportActionBar().setTitle(R.string.title_activity_article_search);

            // Create fragment and give it an argument specifying the article it should show
            searchFragment = new SearchListFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment
            transaction.replace(R.id.list_fragment, searchFragment);

            // Commit the transaction
            transaction.commit();
        }

        searchFragment.searchFor(key);
    }

    /**
     * Hide the keyboard
     * @param activity The activity requesting the keyboard be hidden
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm
                = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchTextView.getWindowToken(), 0);
    }
    /**
     * Show the keyboard
     */
    public void showKeyboard() {
        InputMethodManager imm
                = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchTextView, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        //release resources, files and sensors
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpSearch();
    }

    @Override
    public void onStop() {
        super.onStop();
        //save data
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_list_menu, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        String result = searchView == null ? "true" : "false";
        Log.e("nullSearchView", result);
        searchView.setSearchableInfo(searchManager.getSearchableInfo((new SearchListActivity()).getComponentName()));
        //    searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchBar.getVisibility() == View.VISIBLE) {
            closeSearchBar(searchBar);
        } else {
            switch (callerId) {
                case 2:
                case 3:
                    goHome();
                    break;
                default:
                    super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Dialog d;

        switch (id){
            case R.id.nav_home:
                if(callerId != 1) {
                    goHome();
                }
                break;
            case R.id.nav_faves:
                if(callerId != 2) {
                    goFaves();
                }
                break;
            case R.id.nav_saved:
                Toast.makeText(this, "CallerId: "+callerId, Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_EDA:
                d = createDialog(getString(R.string.school_name), getString(R.string.school_about));
                d.show();
                break;
            case R.id.nav_developer:
                d = createDialog(getString(R.string.about_developer), getString(R.string.developer_about));
                d.show();
                break;
            case R.id.nav_app:
                d = createDialog(getString(R.string.about_app), getString(R.string.app_about));
                d.show();
                break;
            case R.id.nav_contact_EDA:
                d = createDialog(getString(R.string.school_name), getString(R.string.school_contact));
                d.show();
                break;
            case R.id.nav_contact_developer:
                d = createDialog(getString(R.string.developer_name), getString(R.string.developer_contact));
                d.show();
                break;
            case R.id.nav_settings:
                Intent s = new Intent(this, ScrollingActivity.class);
                startActivity(s);
                //d = createDialog("Sorry", "That doesn't do anything yet.");
                //d.show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goFaves() {
        callerId = 2;

        getSupportActionBar().setTitle(R.string.title_activity_faves);

        // Create fragment and give it an argument specifying the article it should show
        FavesListFragment favesFragment = new FavesListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.list_fragment, favesFragment);

        // Commit the transaction
        transaction.commit();
    }

    private void goHome() {
        //go home, Roger !
        callerId = 1;
        getSupportActionBar().setTitle(R.string.app_name);
        // Create fragment and give it an argument specifying the article it should show
        ArticleListFragment homeFragment = new ArticleListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.list_fragment, homeFragment);

        // Commit the transaction
        transaction.commit();
    }
}
