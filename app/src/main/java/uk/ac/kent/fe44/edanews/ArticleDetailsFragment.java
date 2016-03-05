package uk.ac.kent.fe44.edanews;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    public void updateDetails(int index, int caller) {
        this.articleIndex = index;
        //get the article from the correct list
        Article article = new Article();
        switch(caller){
            case 1:
                article = ArticleModel
                        .getInstance()
                        .getArticleList()
                        .get(articleIndex);
                break;
            case 2:
                article = ArticleModel
                        .getInstance()
                        .getFavesList()
                        .get(articleIndex);
                break;
        }

        //update UI with data
        articleTitle.setText(article.getTitle());
        articleDate.setText(article.getDate());
        articlePhoto.setImageUrl(article.getImageURL(), ArticlesApp.getInstance().getImageLoader());

        if(article.isDetailed()) {
            articleContents.setText(article.getContents());
        }else{
            //show network message
            articleContents.setText(R.string.loading_contents);

            //get a handle on article's record id for network request
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


    @Override
    public void onDetach(){
        super.onDetach();
        Toast.makeText(getActivity(), "Detail fragment killed", Toast.LENGTH_SHORT).show();
    }
}
