package uk.ac.kent.fe44.edanews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fe44 on 17/04/16.
 */
public class ArticleListModel {
    /** List of articles that will appear in the newsfeed. */
    private ArrayList<Article> articleList = new ArrayList<>();
    /** An object that is listening for changes to the newsfeed list. */
    private ArticleModel.OnListUpdateListener listUpdateListener;

    /**  */
    private int mStart = 0;
    /** The number fo articles to download from the network per request. */
    private int size = 20;

    /** The base url to make requests to for data. */
    private static String CLIENT_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php?start=";
    /** The base url for constructing an article's web page */
    private static String WEB_PAGE_URL = "http://www.eda.kent.ac.uk/school/news_article.aspx?aid=";

    /**
     * Populates this object's list of articles from multiple
     * sources. The first twenty articles are pulled from the
     * MasterListModel, via the ArticleModel. A network call
     * is then made to check for newer articles, which are
     * added to the head of the list. When the list is
     * scrolled, further network calls are made to populate
     * the list of articles with older articles.
     * @param listUpdateListener
     */
    public void load(ArticleModel.OnListUpdateListener listUpdateListener) {
        //set calling class as listener
        setOnListUpdateListener(listUpdateListener);
        //if mstart = 0, load data from the MasterList AND check for newer article
        //else load data from network
        //build request
        String url = CLIENT_URL + mStart;
        JsonArrayRequest request = new JsonArrayRequest(url, netListener, errorListener);

        //make request
        ArticlesApp.getInstance()
                .getRequestQueue()
                .add(request);

        //set up for pulling next set of articles
        mStart += size;
    }
    /*let the listener know about updates*/
    private void notifyListener() {
        if(listUpdateListener != null) {
            listUpdateListener.onListUpdate();
        }
    }
    /*receive subscriptions for notifications from classes*/
    private void setOnListUpdateListener(ArticleModel.OnListUpdateListener listUpdateListener) {
        this.listUpdateListener = listUpdateListener;
    }

    private Response.Listener<JSONArray> netListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            //handle response from server
            if(mStart == 0){
                articleList.clear();
            }
            try{
                for(int i = 0; i < response.length(); i++) {
                    //extract JSON object from response
                    JSONObject object = response.getJSONObject(i);

                    //build article with the data
                    Article article = new Article(
                            object.getString(ArticleModel.IMAGE_URL),
                            object.getInt(ArticleModel.RECORD_ID),
                            object.getString(ArticleModel.TITLE),
                            object.getString(ArticleModel.SHORT_INFO),
                            object.getString(ArticleModel.DATE)
                    );

                    //add web url to article
                    String url = WEB_PAGE_URL + article.getRecordID();
                    article.setWeb_page(url);

                    //and add to list of Articles
                    getArticleList().add(article);
                }
            }catch(JSONException e) {
                //handle exception
            }
            notifyListener();
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //handle error in response
        }
    };

    public ArrayList<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
    }
}
