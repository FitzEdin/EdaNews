package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class FavesListFragment extends ListFragment implements ArticleModel.OnFavesUpdateListener {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faves_list, container, false);

        //TODO: remove
        //Toast.makeText(getActivity(), "FavesListFragment.onCreateView", Toast.LENGTH_SHORT).show();

        //set up layout manager
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);

        //set up list adapter
        listAdapter = new FavesListAdapter(this);

        //set up visual elements
        listView = (RecyclerView)view.findViewById(R.id.faves_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);

        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);

        return view;
    }

    /*refresh data set with new information*/
    @Override
    public void onFavesUpdate() {
        //change data set
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}
