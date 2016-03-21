package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by fitzroy on 01/03/2016.
 */
public abstract class ListFragment extends Fragment implements ArticleModel.OnFavesUpdateListener {

    protected RecyclerView listView;
    protected GridLayoutManager gridLayoutManager;
    protected ListAdapter listAdapter;

    protected OnListItemClickedListener mListenerActivity;

    public ListFragment() {
        // Required empty public constructor
    }

    // .onCreateView is overridden by all children

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

    /*refresh data set with new information*/
    @Override
    public void onFavesUpdate() {
        //change data set
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    //interface for activities using this fragment
    public interface OnListItemClickedListener {
        void onItemClicked(int position, Bundle bundle);
        void onItemShared(int position);
        void onLongTap(int position);
        void onLongTapReleased(int position);
    }

    public void onItemClicked(int position, Bundle bundle) {
        //perform secondary network request
        mListenerActivity.onItemClicked(position, bundle);
    }

    public void onItemShared(int position) {
        //perform share action
        mListenerActivity.onItemShared(position);
    }

    public void onLongTap(int position) {
        //perform peek action
        mListenerActivity.onLongTap(position);
    }

    /* define what happens when the long tap on the image is released */
    public void onLongTapReleased(int position) {
        //perform peek action
        mListenerActivity.onLongTapReleased(position);
    }
}
