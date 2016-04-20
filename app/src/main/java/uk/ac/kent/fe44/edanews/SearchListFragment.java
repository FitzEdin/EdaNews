package uk.ac.kent.fe44.edanews;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by fe44 on 22/03/16.
 * The SearchList Fragment receives the search key via the intent bundle and
 * asks the ArticleModel to perform the search. It then listens for responses
 * and handles the UI for loading results or indicating a long network operation.
 */
public class SearchListFragment extends ListFragment
        implements ArticleModel.OnSearchListUpdateListener{

    private ProgressBar mProgressBar;
    private View view;
    private CardView empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the view and a handle on the progress Bar
        view = inflater.inflate(R.layout.fragment_search_list, container, false);
        empty = (CardView) view.findViewById(R.id.empty_list);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        //prep for config
        prepForConfig();

        //set up list adapter
        listAdapter = new SearchListAdapter(this);

        showEmptyView();
        return view;
    }

    private void showEmptyView() {
        if(listAdapter.getItemCount() == 0) {
            //show empty list image
            empty.setVisibility(View.VISIBLE);
        }else {
            //hide empty list image
            empty.setVisibility(View.INVISIBLE);
            //set up the list view
            setUpListView(view);
        }
    }

    /**
     * configure the list view with the newly created
     * listAdapter, and the layoutManager
     * @param view The fragment to place the list view in
     */
    protected void setUpListView(View view) {
        listView = (RecyclerView)view.findViewById(R.id.search_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);
        listView.scrollToPosition(0);
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
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);
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
    public void onSearchListUpdate() {
        //inform the adapter and kill the progress bar
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
            showEmptyView();
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void searchFor(String key) {
        ArticleModel model = ArticleModel.getInstance();

        //perform search and listen for response
        model.loadSearchData(key, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
    }
}
