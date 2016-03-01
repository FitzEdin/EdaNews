package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class FavesListFragment extends Fragment implements ArticleModel.OnListUpdateListener {

    private RecyclerView favesListView;
    private GridLayoutManager gridLayoutManager;
    private FavesListAdapter listAdapter;

    private OnListItemClickedListener mListenerActivity;

    private int start = 0;

    public FavesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faves_list, container, false);

        //TODO: remove
        Toast.makeText(getActivity(), "FavesListFragment.onCreateView", Toast.LENGTH_SHORT).show();

        //set up layout manager
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);

        //set up list adapter
        listAdapter = new FavesListAdapter(this);

        //set up visual elements
        favesListView = (RecyclerView)view.findViewById(R.id.faves_list_view);
        favesListView.setLayoutManager(gridLayoutManager);
        favesListView.setAdapter(listAdapter);

        return view;
    }

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
    public void onListUpdate(ArrayList<Article> favesList) {
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    //interface for activities using this fragment
    public interface OnListItemClickedListener {
        void onItemClicked(int position);
    }

    public void onItemClicked(int position) {
        //perform secondary network request
        mListenerActivity.onItemClicked(position);
    }
}
