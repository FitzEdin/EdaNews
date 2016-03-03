package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by fitzroy on 01/03/2016.
 */
public abstract class ListFragment extends Fragment {

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

    //interface for activities using this fragment
    public interface OnListItemClickedListener {
        void onItemClicked(int position);
        void onItemShared(int position);
    }

    public void onItemClicked(int position) {
        //perform secondary network request
        mListenerActivity.onItemClicked(position);
    }

    public void onItemShared(int position) {
        //perform secondary network request
        mListenerActivity.onItemShared(position);
    }
}
