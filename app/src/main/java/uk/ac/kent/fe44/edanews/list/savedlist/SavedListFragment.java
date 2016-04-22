package uk.ac.kent.fe44.edanews.list.savedlist;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import uk.ac.kent.fe44.edanews.model.ArticleModel;
import uk.ac.kent.fe44.edanews.view.ListFragment;
import uk.ac.kent.fe44.edanews.R;

/**
 * Created by fe44 on 20/04/16.
 */
public class SavedListFragment extends ListFragment {

    private View view;
    private LinearLayout empty;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saved_list, container, false);
        empty = (LinearLayout) view.findViewById(R.id.empty_saved_list);

        //prep for config
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        prepForConfig();

        //set up list adapter
        listAdapter = new SavedListAdapter(this);
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
        listView = (RecyclerView)view.findViewById(R.id.saved_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);
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
                    if (((position % 10) == 5) || ((position % 10) == 1)) {
                        return 2;
                    } else {
                        return 1;
                    }
                } else {
                    //for small screens in all orientations
                    if ((position % 5) == 0) {
                        return 2;
                    } else {
                        return 1;
                    }
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
        int spanCount = 2;
        if(isLarge) {
            //handle large screens, @least 600x1024
            switch (config.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    spanCount = 4;      //
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    spanCount = 3;      //
                    break;
            }
        }else{
            //handle everything smaller than 600x1024
            switch (config.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    spanCount = 3;      //
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    spanCount = 2;      //
                    break;
            }
        }
        return spanCount;
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for changes to the Faves and Saved List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
        ArticleModel.getInstance().setOnSavedUpdateListener(this);
    }

    @Override
    public void onSavedUpdate() {
        super.onSavedUpdate();
        showEmptyView();
    }
}
