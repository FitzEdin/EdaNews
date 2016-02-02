package uk.ac.kent.fe44.edanews;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailsFragment extends Fragment {

    private TextView txVw;

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);

        txVw = (TextView)view.findViewById(R.id.detailTxVw);

        return view;
    }

    public void updateDetails(int id) {
        Article article = ArticleModel
                .getInstance()
                .getArticleList()
                .get(id);

        //TODO: Update UI with data
        txVw.setText(article.getTitle());
    }


}
