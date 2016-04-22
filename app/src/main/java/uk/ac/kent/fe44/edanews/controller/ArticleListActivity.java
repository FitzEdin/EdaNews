package uk.ac.kent.fe44.edanews.controller;

import android.animation.Animator;
import android.app.Activity;
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

import java.util.ArrayList;

import uk.ac.kent.fe44.edanews.ReadLaterService;
import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.view.ListFragment;
import uk.ac.kent.fe44.edanews.R;
import uk.ac.kent.fe44.edanews.list.articlelist.ArticleListFragment;
import uk.ac.kent.fe44.edanews.list.faveslist.FavesListFragment;
import uk.ac.kent.fe44.edanews.model.ArticleModel;
import uk.ac.kent.fe44.edanews.list.savedlist.SavedListFragment;
import uk.ac.kent.fe44.edanews.list.searchlist.SearchListFragment;

public class ArticleListActivity extends ListActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Intent mServiceIntent;

    private static FloatingActionButton searchFab;
    private static LinearLayout searchBar;
    private static EditText searchTextView;
    private View.OnClickListener onFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Animator anim = createAnimator(searchBar, 600);

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

                navView.setCheckedItem(R.id.nav_home);

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

        //load master list from database
        ArticleModel.getInstance().loadMasterList(this);

        //set up caller id for ArticleDetailsActivity
        callerId = ArticlesApp.ARTICLE_CALLER_ID;

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private Animator createAnimator(View showMe, long duration) {
        int cx = showMe.getWidth()/2;
        int cy = showMe.getHeight()/2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(showMe, cx, cy, 0, finalRadius);
            anim.setDuration(duration);
        }
        return anim;
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
            case ArticlesApp.SEARCH_CALLER_ID:
                return ArticleModel.getInstance().getSearchList();
            case ArticlesApp.FAVES_CALLER_ID:
                return ArticleModel.getInstance().getFavesList();
            case ArticlesApp.SAVED_CALLER_ID:
                return ArticleModel.getInstance().getSavedList();
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

        Animator anim = createAnimator(searchFab, 300);

        searchBar.setVisibility(View.INVISIBLE);
        searchFab.setVisibility(View.VISIBLE);
        anim.start();
    }

    /**
     * Perform a search for the key provided; the fragment is swapped in
     * if not existing previously. Each time it is handed a new key.
     * @param key
     */
    public void searchFor(String key) {
        if(callerId != ArticlesApp.SEARCH_CALLER_ID) {
            //swap in the search fragment
            callerId = ArticlesApp.SEARCH_CALLER_ID;
            getSupportActionBar().setTitle(R.string.title_activity_article_search);

            // Create fragment and give it an argument specifying the article it should show
            searchFragment = new SearchListFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment
            transaction.replace(R.id.list_fragment, searchFragment);

            // Commit the transaction
            transaction.commit();
        }

        if(searchFragment != null) {    searchFragment.searchFor(key);  }
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
        launchService = true;
        setUpSearch();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        final int count = ArticleModel.getInstance().getSavedList().size();
        if(launchService && (count > 0)) {
            //start background service

            //intent to pass to background service
            mServiceIntent = new Intent(this, ReadLaterService.class);
            mServiceIntent.putExtra(ArticlesApp.SAVED_COUNT, count);
            this.startService(mServiceIntent);
        }

        //save data
        ArticleModel.getInstance().saveMasterList(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchBar.getVisibility() == View.VISIBLE) {
            closeSearchBar(searchBar);
        } else {
            switch (callerId) {
                case ArticlesApp.FAVES_CALLER_ID:
                case ArticlesApp.SEARCH_CALLER_ID:
                case ArticlesApp.SAVED_CALLER_ID:
                    goNear(ArticlesApp.ARTICLE_CALLER_ID);
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

        switch (id){
            case R.id.nav_home:
                if(callerId != ArticlesApp.ARTICLE_CALLER_ID) {
                    goNear(ArticlesApp.ARTICLE_CALLER_ID);
                }
                break;
            case R.id.nav_faves:
                if(callerId != ArticlesApp.FAVES_CALLER_ID) {
                    goNear(ArticlesApp.FAVES_CALLER_ID);
                }
                break;
            case R.id.nav_saved:
                if(callerId != ArticlesApp.SAVED_CALLER_ID) {
                    goNear(ArticlesApp.SAVED_CALLER_ID);
                }
                break;
            case R.id.nav_EDA:
                goFar(ArticlesApp.DEPT_EXTRA_ID);
                break;
            case R.id.nav_developer:
                goFar(ArticlesApp.DEVELOPER_EXTRA_ID);
                break;
            case R.id.nav_app:
                goFar(ArticlesApp.APP_EXTRA_ID);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goNear(int location) {
        launchService = false;
        ListFragment newFrag;
        callerId = location;    //one of the caller_ids outlined in ArticlesApp

        switch (location) {     //decide where to go
            case ArticlesApp.SAVED_CALLER_ID:
                getSupportActionBar().setTitle(R.string.title_activity_saved);
                navView.setCheckedItem(R.id.nav_saved);
                newFrag = new SavedListFragment();
                break;
            case ArticlesApp.FAVES_CALLER_ID:
                getSupportActionBar().setTitle(R.string.title_activity_faves);
                navView.setCheckedItem(R.id.nav_faves);
                newFrag = new FavesListFragment();
                break;
            default:
                getSupportActionBar().setTitle(R.string.app_name);
                navView.setCheckedItem(R.id.nav_home);
                newFrag = new ArticleListFragment();
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.list_fragment, newFrag);
        // Commit the transaction
        transaction.commit();
    }

    private void goFar(int location) {
        launchService = false;
        Intent s = new Intent(this, ScrollingActivity.class);
        s.putExtra(ArticlesApp.EXTRA_ID, location);
        startActivity(s);
    }
}
