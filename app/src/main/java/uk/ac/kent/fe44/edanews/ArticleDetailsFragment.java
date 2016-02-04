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

    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleContents;

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);

        articleTitle = (TextView)view.findViewById(R.id.detailTitle);
        articleDate = (TextView)view.findViewById(R.id.detailDate);
        articleContents = (TextView)view.findViewById(R.id.detailContents);

        return view;
    }

    public void updateDetails(int id) {
        Article article = ArticleModel
                .getInstance()
                .getArticleList()
                .get(id);

        //TODO: Update UI with data
        articleTitle.setText(article.getTitle());
        articleDate.setText(article.getDate());
        articleContents.setText(article.getTitle() + " "
                + article.getTitle() + " "
                + article.getTitle() + " "
                + article.getTitle() + " "
                + article.getTitle() + " "
                + article.getTitle() + " "
                + article.getTitle() + " "
                + article.getTitle());
    }


}
