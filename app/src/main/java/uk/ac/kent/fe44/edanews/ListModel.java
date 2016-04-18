package uk.ac.kent.fe44.edanews;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fe44 on 17/04/16.
 */
abstract public class ListModel {
    protected ArrayList<Article> list = new ArrayList<>();

    /*members for performing article list request*/
    public static final String RECORD_ID = "record_id", TITLE = "title", DATE = "date",
            SHORT_INFO = "short_info", IMAGE_URL = "image_url";
    /** The base url for constructing an article's web page */
    public static final String WEB_PAGE_URL = "http://www.eda.kent.ac.uk/school/news_article.aspx?aid=";

    @NonNull
    protected Article getArticle(JSONObject object) throws JSONException {
        Article article;
        //check for pre-existence in MasterList
        ArticleModel model = ArticleModel.getInstance();
        int recordId = object.getInt(RECORD_ID);
        if(model.isInMaster(recordId)) {
            //skip parsing and return pre-existing article
            article = model.getArticleFromMaster(recordId);
        }else {
            //build article with the data
            article = new Article(
                    object.getString(IMAGE_URL),
                    object.getInt(RECORD_ID),
                    object.getString(TITLE),
                    object.getString(SHORT_INFO),
                    object.getString(DATE)
            );

            //add web url to article
            String url = WEB_PAGE_URL + article.getRecordID();
            article.setWeb_page(url);
        }

        return article;
    }

    public ArrayList<Article> getList() {
        return list;
    }

    public void setList(ArrayList<Article> articleList) {
        this.list = articleList;
    }
}
