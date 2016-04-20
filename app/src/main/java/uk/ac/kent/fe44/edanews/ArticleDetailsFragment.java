package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailsFragment extends Fragment implements ArticleModel.OnDetailsUpdateListener {

    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleContents;
    private NetworkImageView articlePhoto;

    private int articleId;
    private int articleIndex;
    private Article article;

    private int callerId;

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
        articlePhoto = (NetworkImageView)view.findViewById(R.id.detail_img);

        return view;
    }

    /*populate the views with article's details*/
    public void updateDetails(int index, int callerId) {
        this.articleIndex = index;
        this.callerId = callerId;
        //get the article from the correct list
        switch(callerId){
            case ArticlesApp.ARTICLE_CALLER_ID:
                article = ArticleModel
                        .getInstance()
                        .getArticleList()
                        .get(articleIndex);
                break;
            case ArticlesApp.FAVES_CALLER_ID:
                article = ArticleModel
                        .getInstance()
                        .getFavesList()
                        .get(articleIndex);
                break;
            case ArticlesApp.SEARCH_CALLER_ID:
                article = ArticleModel
                        .getInstance()
                        .getSearchList()
                        .get(articleIndex);
                break;
        }

        //update UI with data
        articleTitle.setText(article.getTitle());
        articleDate.setText(article.getDate());
        articlePhoto.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());

        if(article.getContents() == null) {
            //show appropriate network message
            if(ArticlesApp.getInstance().networkIsAvailable()) {
                articleContents.setText(R.string.loading_contents);

                //get a handle on article's record id for network request
                articleId = article.getRecordID();
                getArticleDetails();
            }else{
                articleContents.setText(R.string.no_network);
            }
        }else{
            articleContents.setText(article.getContents());
        }
    }

    /*prompts the ArticleModel class to get article's details from the network*/
    private void getArticleDetails() {
        //get details
        ArticleModel model = ArticleModel.getInstance();
        model.loadArticleDetails(callerId, articleId, articleIndex, this);
    }

    @Override
    public void onDetailsUpdate() {
        articleContents.setText(article.getContents());
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }
}
