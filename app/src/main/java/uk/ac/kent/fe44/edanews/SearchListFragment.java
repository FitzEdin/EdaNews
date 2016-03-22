package uk.ac.kent.fe44.edanews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

        //get search key from intent
        String key = getActivity().getIntent().getStringExtra("key");
        //perform search
        searchFor(key);

        //set up layout manager
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);

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
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void searchFor(String key) {
        ArticleModel model = ArticleModel.getInstance();

        //perform search and listen for response
        model.loadSearchData(key);
        model.setOnSearchListUpdateListener(this);
    }
}
