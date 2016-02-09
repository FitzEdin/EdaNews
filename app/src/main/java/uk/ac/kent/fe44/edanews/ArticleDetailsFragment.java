package uk.ac.kent.fe44.edanews;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailsFragment extends Fragment implements ArticleModel.OnDetailsUpdateListener {

    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleContents;
    private int articleId;
    private int articleIndex;

    //give this fragment an ArticleModel member, and perform network request for article's details

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);

        articleTitle = (TextView)view.findViewById(R.id.detail_title);
        articleDate = (TextView)view.findViewById(R.id.detail_date);
        articleContents = (TextView)view.findViewById(R.id.detail_contents);

        return view;
    }

    /*populate the views with article's details*/
    public void updateDetails(int index) {
        this.articleIndex = index;
        //get the article from the list
        Article article = ArticleModel
                .getInstance()
                .getArticleList()
                .get(articleIndex);

        //update UI with data
        articleTitle.setText(article.getTitle());
        articleDate.setText(article.getDate());

        if(article.isDetailed()) { //details have been downloaded before..
                                    //so no need to do it twice
            articleContents.setText(article.getContents());
        }else {
            // show network message
            articleContents.setText("Loading details...");

            //get a handle on that article's record id for network request
            articleId = article.getRecordID();
            getArticleDetails();
        }
    }

    /*prompts the ArticleModel class to get article's details from the network*/
    public void getArticleDetails() {
        //get details
        ArticleModel model = ArticleModel.getInstance();
        model.setOnDetailsUpdateListener(this);
        model.loadArticleDetails(articleId, articleIndex);
    }


    @Override
    public void onDetailsUpdate() {
        Article article = ArticleModel
                .getInstance()
                .getArticleList()
                .get(articleIndex);
        articleContents.setText(article.getContents());
    }
}
