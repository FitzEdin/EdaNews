package uk.ac.kent.fe44.edanews;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the view and a handle on the progress Bar
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

//        //get search key from intent
//        String key = getActivity().getIntent().getStringExtra("key");
//        //perform search
//        searchFor(key);


        int spanCount = 1;

        Configuration config = getResources().getConfiguration();

        //set span count based on screen size and orientation
        final boolean isLarge = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
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

        //set up layout manager
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

        //set up list adapter
        listAdapter = new SearchListAdapter(this);

        //set up visual elements
        listView = (RecyclerView)view.findViewById(R.id.search_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onSearchListUpdate() {
        //inform the adapter and kill the progress bar
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
            listView.scrollToPosition(0);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void searchFor(String key) {
        ArticleModel model = ArticleModel.getInstance();

        //perform search and listen for response
        model.loadSearchData(key);
        model.setOnSearchListUpdateListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
    }
}
