package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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


        int spanCount = 2;

        Configuration config = getResources().getConfiguration();

        //set span count based on screen size and orientation
        final boolean isLarge = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
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

        //set up layout manager
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);
        /*TODO: have multiple spans for different rows*/
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position){
                if(isLarge) {
                    //for large screens
                    if (((position % 10) == 5) || ((position % 10) == 1)) {
                        return 2;
                    } else {
                        return 1;
                    }
                }else{
                    //for small screens
                    if ((position % 5) == 0) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            }
        });


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
