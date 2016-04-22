package uk.ac.kent.fe44.edanews.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import uk.ac.kent.fe44.edanews.model.ArticleModel;

/**
 * Created by fitzroy on 01/03/2016.
 */
public abstract class ListFragment extends Fragment
        implements ArticleModel.OnFavesUpdateListener, ArticleModel.OnSavedUpdateListener {

    protected RecyclerView listView;
    protected GridLayoutManager gridLayoutManager;
    protected ListAdapter listAdapter;

    protected OnListItemClickedListener mListenerActivity;

    public ListFragment() {
        // Required empty public constructor
    }

    private void prepForConfig(Configuration newConfig) {
        //set span count based on screen size and orientation
        final boolean isLarge = newConfig.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
        int spanCount = getSpanCount(newConfig, isLarge);

        //set up layout manager
        setUpLayoutManager(isLarge, spanCount);
    }

    protected void prepForConfig() {
        Configuration config = getResources().getConfiguration();
        //set span count based on screen size and orientation
        prepForConfig(config);
    }

    /**
     * configure the list view with the newly created
     * listAdapter, and the layoutManager
     * @param view The fragment to place the list view in
     */
    abstract protected void setUpListView(View view);/**
     * Determine how many columns each article in the layout
     * occupies based on its position in the list.
     * @param isLarge Boolean describing whether or not the
     *                device's screen is large.
     * @param spanCount The number of columns available in
     *                  the layout; this is determined by
     *                  getSpanCount()
     */
    abstract protected void setUpLayoutManager(final boolean isLarge, int spanCount);
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
    abstract protected int getSpanCount(Configuration config,  boolean isLarge);


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListenerActivity = (OnListItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerActivity = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        prepForConfig(newConfig);
        setUpListView(this.getView());
    }

    /*refresh data set with new information*/
    @Override
    public void onFavesUpdate() {
        //change data set
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSavedUpdate() {
        //change data set
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    //interface for activities using this fragment
    public interface OnListItemClickedListener {
        void onItemClicked(int position, Bundle bundle);
        void onItemShared(int position);
        void onLongTap(int position, Bundle bundle);
    }

    public void onItemClicked(int position, Bundle bundle) {
        //perform secondary network request
        mListenerActivity.onItemClicked(position, bundle);
    }

    public void onItemShared(int position) {
        //perform share action
        mListenerActivity.onItemShared(position);
    }

    public void onLongTap(int position, Bundle bundle) {
        //perform peek action
        mListenerActivity.onLongTap(position, bundle);
    }
}
