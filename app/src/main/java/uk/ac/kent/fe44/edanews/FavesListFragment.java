package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class FavesListFragment extends ListFragment {

    private View view;
    private CardView empty;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_faves_list, container, false);
        empty = (CardView) view.findViewById(R.id.empty_list);

        //prep for config
        prepForConfig();

        //set up list adapter
        listAdapter = new FavesListAdapter(this);
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
        listView = (RecyclerView)view.findViewById(R.id.faves_list_view);
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
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);
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
        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
    }

    @Override
    public void onFavesUpdate() {
        super.onFavesUpdate();
        showEmptyView();
    }
}
