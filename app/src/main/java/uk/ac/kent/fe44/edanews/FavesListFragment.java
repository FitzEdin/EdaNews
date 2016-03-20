package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class FavesListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faves_list, container, false);


        //set up layout manager
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        /*TODO: have multiple spans for different rows*/
    /*    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position){
                switch(position){
                    case 0:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
    */
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);

        //set up list adapter
        listAdapter = new FavesListAdapter(this);

        //set up visual elements
        listView = (RecyclerView)view.findViewById(R.id.faves_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
    }
}
