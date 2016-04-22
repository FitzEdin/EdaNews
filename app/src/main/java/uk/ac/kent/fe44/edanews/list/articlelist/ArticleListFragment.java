package uk.ac.kent.fe44.edanews.list.articlelist;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import uk.ac.kent.fe44.edanews.model.ArticleModel;
import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.view.ListFragment;
import uk.ac.kent.fe44.edanews.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class ArticleListFragment extends ListFragment
        implements ArticleModel.OnListUpdateListener {

    private LinearLayout noNetworkRetry;
    private ProgressBar mProgressBar;
    private boolean loading = false;
    private SwipeRefreshLayout refreshArticleList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        noNetworkRetry = (LinearLayout)view.findViewById(R.id.no_network_retry);

        noNetworkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryForNetwork();
            }
        });

        tryForNetwork();

        //prep for config
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        prepForConfig();

        //set up list adapter
        listAdapter = new ArticleListAdapter(this);

        //set up the list view
        setUpListView(view);

        return view;
    }

    /**
     * configure the list view with the newly created
     * listAdapter, and the layoutManager
     * @param view The fragment to place the list view in
     */
    protected void setUpListView(View view) {
        refreshArticleList =
                (SwipeRefreshLayout)view.findViewById(R.id.refresh_article_list);
        listView = (RecyclerView)view.findViewById(R.id.article_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = gridLayoutManager.getChildCount();
                int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
                int totalItemCount = gridLayoutManager.getItemCount();

                if ((dy > 0)
                        && (!loading)
                        && (totalItemCount - (visibleItemCount + pastVisibleItems)) <= 3
                        ) {
                    //Toast.makeText(getActivity(), "They see me scrolling", Toast.LENGTH_SHORT).show();
                    tryForNetwork();
                    loading = true;
                }
            }
        });
        refreshArticleList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });
    }

    /**
     * Determine how many columns each article in the layout
     * occupies based on its position in the list.
     * @param isLarge Boolean describing whether or not the
     *                device's screen is large.
     * @param spanCount The number of columns available in
     *                  the layout; this is determined by
     *                  getSpanCount()
     */
    protected void setUpLayoutManager(final boolean isLarge, int spanCount) {
        gridLayoutManager.setSpanCount(spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(gridLayoutManager.findFirstVisibleItemPosition());
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isLarge) {
                    //for large screens in all orientations
                    if (((position % 4) == 0) || ((position % 4) == 3)) {
                        return 2;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * Set the number of columns available in
     * the recycler view's layout based on the
     * size of the screen and orientation.
     * @param config System configurations
     * @param isLarge Boolean; whether or not
     *                the device screen is large.
     * @return spanCount The number of columns to
     * set in the layout for this particular device.
     */
    protected int getSpanCount(Configuration config, boolean isLarge) {
        int spanCount = 1;
        if(isLarge) {
            //handle large screens, @least 600x1024
            switch (config.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    spanCount = 3;      //
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    spanCount = 2;      //
                    break;
            }
        }else{
            //handle everything smaller than 600x1024
            switch (config.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    spanCount = 2;      //
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    spanCount = 1;      //
                    break;
            }
        }
        return spanCount;
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
        ArticleModel.getInstance().setOnSavedUpdateListener(this);
    }

    /*refresh data set with new information*/
    @Override
    public void onListUpdate() {
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.INVISIBLE);
            refreshArticleList.setRefreshing(false);
        }

        loading = false;
    }

    private void tryForNetwork(){
        //hide everything
        mProgressBar.setVisibility(View.INVISIBLE);
        noNetworkRetry.setVisibility(View.INVISIBLE);

        if(ArticlesApp.getInstance().networkIsAvailable()){   //network is there? grab things
            //hide no network message if present, show fab+progress bar
            mProgressBar.setVisibility(View.VISIBLE);
            getData(false);
        }else {        //no network? hide progress bar and tell the user
            noNetworkRetry.setVisibility(View.VISIBLE);
        }
    }

    private void getData(boolean refresh) {
        ArticleModel model = ArticleModel.getInstance();

        //load data from network and listen for response
        model.loadData(this, refresh);
    }
}
