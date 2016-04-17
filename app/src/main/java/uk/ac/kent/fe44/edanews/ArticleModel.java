package uk.ac.kent.fe44.edanews;

import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fitzroy on 28/01/2016.
 * Interacts with the Article class
 */
public class ArticleModel {

    private static ArticleModel ourInstance = new ArticleModel();

    /*Constructor*/
    private ArticleModel() {
    }
    public static ArticleModel getInstance() {
        return ourInstance;
    }

    /* Managing our list of articles */
    private ArrayList<Article> articleList = new ArrayList<>();
    public ArrayList<Article> getArticleList() {
        return articleList;
    }


    /** Performing Article List requests
     *  The lists are downloaded in batches of size
     *  and any listener is informed via the interface
     *  */
    /*members for performing article list request*/
    private static String CLIENT_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php?start=";
    private static String WEB_PAGE_URL = "http://www.eda.kent.ac.uk/school/news_article.aspx?aid=";
    private String RECORD_ID = "record_id", TITLE = "title", DATE = "date",
            SHORT_INFO = "short_info", IMAGE_URL = "image_url";
    private OnListUpdateListener listUpdateListener;
    private int mStart = 0;
    private int size = 20;

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
                            object.getString(IMAGE_URL),
                            object.getInt(RECORD_ID),
                            object.getString(TITLE),
                            object.getString(SHORT_INFO),
                            object.getString(DATE)
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
    /*loads a set of twenty articles*/
    public void loadData(OnListUpdateListener listUpdateListener) {
        //set calling class as listener
        setOnListUpdateListener(listUpdateListener);
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
    public void setOnListUpdateListener(OnListUpdateListener listUpdateListener) {
        this.listUpdateListener = listUpdateListener;
    }
    /*classes that need this data implement this interface*/
    public interface OnListUpdateListener {
        void onListUpdate();
    }
    /*end definition for members an methods for performing article list request(s)*/



    /** Performing Article Detail requests
     *  The detail for each article, ie, its contents
     *  and web page url is downloaded with a second
     *  network request.
     *  */
    /*members and methods for performing request for article's details*/
    private String DETAILS_URL = "http://www.efstratiou.info/projects/newsfeed/getItem.php?id=";
    private String CONTENTS = "contents";
    private OnDetailsUpdateListener detailsUpdateListener;
    private static int articleIndex;
    private ArticleDetailsFragment me;

    private Response.Listener<JSONObject> detailsListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            //handle response from server
            try{
                //extract contents from JSON object response
                getArticleList().get(articleIndex)
                        .setContents(response.getString(CONTENTS));

                //mark article as detailed /---/ prevents a second attempt at downloading same data
                getArticleList().get(articleIndex)
                        .setIsDetailed(true);

            }catch(JSONException e) {
                //handle exception
            }
            notifyDetailsListener();
        }
    };
    private Response.ErrorListener detailsErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //handle error in response
        }
    };
    /*loads the details of a particular article, by appending its id to DETAILS_URL*/
    public void loadArticleDetails(int articleId, int articleIndex, OnDetailsUpdateListener detailsUpdateListener) {
        //set calling class as listener
        if(detailsUpdateListener != null) {
            setOnDetailsUpdateListener(detailsUpdateListener);
        }
        //localise index for use by the response listener
        this.articleIndex = articleIndex;

        //create url for particular article
        String url = DETAILS_URL + articleId;

        JsonObjectRequest request = new JsonObjectRequest(url, null, detailsListener, detailsErrorListener);
        //make request
        ArticlesApp.getInstance()
                .getRequestQueue()
                .add(request);
    }
    private void notifyDetailsListener() {
        if(detailsUpdateListener != null) {
            detailsUpdateListener.onDetailsUpdate();
        }
    }
    /*receive subscriptions for notifications from classes*/
    private void setOnDetailsUpdateListener(OnDetailsUpdateListener detailsUpdateListener) {
        this.detailsUpdateListener = detailsUpdateListener;
    }
    public interface OnDetailsUpdateListener {
        void onDetailsUpdate();
    }
    /*end definition of members and methods for getting article's details*/




    /** Performing search requests
     *  The lists are downloaded in batches of size
     *  and any listener is informed via the interface
     *  */
    /*members for performing article list request*/
    private String SEARCH_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php?titleHas=";
    private String KEY;
    private OnSearchListUpdateListener searchListUpdateListener;
    /* Managing our list of articles */
    private ArrayList<Article> searchList = new ArrayList<>();
    public ArrayList<Article> getSearchList() {
        return searchList;
    }

    private Response.Listener<JSONArray> searchListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            //handle response from server
            searchList.clear();
            try{
                for(int i = 0; i < response.length(); i++) {
                    //extract JSON object from response
                    JSONObject object = response.getJSONObject(i);

                    //build article with the data
                    Article article = new Article(
                            object.getString(IMAGE_URL),
                            object.getInt(RECORD_ID),
                            object.getString(TITLE),
                            object.getString(SHORT_INFO),
                            object.getString(DATE)
                    );

                    //and add to list of Articles
                    getSearchList().add(article);
                }
            }catch(JSONException e) {
                //handle exception
            }
            notifySearchListener();
        }
    };
    private Response.ErrorListener searchErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //handle error in response
        }
    };
    /*searches for a set of articles with a certain title*/
    public void loadSearchData(String key, OnSearchListUpdateListener searchListUpdateListener) {
        //set calling class as listener
        setOnSearchListUpdateListener(searchListUpdateListener);
        //build request
        String url = SEARCH_URL + key;
        JsonArrayRequest request = new JsonArrayRequest(url, searchListener, searchErrorListener);

        //make request
        ArticlesApp.getInstance()
                .getRequestQueue()
                .add(request);
    }
    /*let the listener know about updates*/
    private void notifySearchListener() {
        if(searchListUpdateListener != null) {
            searchListUpdateListener.onSearchListUpdate();
        }
    }
    /*receive subscriptions for notifications from classes*/
    private void setOnSearchListUpdateListener(OnSearchListUpdateListener searchListUpdateListener) {
        this.searchListUpdateListener = searchListUpdateListener;
    }
    /*classes that need this data implement this interface*/
    public interface OnSearchListUpdateListener {
        void  onSearchListUpdate();
    }
    /*end definition for members an methods for performing article list request(s)*/



    /** Manages the faves list; no network requests are
     * involved, and the list itself is transient. It is
     * built dynamically using getFavesList(). Any additions
     * and deletions are done using addToFaves() and
     * removeFromFaves(), respectively. Any listeners are
     * notified after each addition and deletion.
     *  */
    /*managing my faves list*/
    // dropped this; broke my model private ArrayList<Article> favesList = new ArrayList<>();
    private OnFavesUpdateListener favesUpdateListener;

    public ArrayList<Article> getFavesList() {
        ArrayList<Article> favesList = new ArrayList<>();
        for (Article a : getArticleList()) {
            if(a.isFave()){
                favesList.add(a);
            }
        }
        return favesList;
    }
    public void setFavesList(ArrayList<Article> favesList) {
        //TODO: populate faves list with items from memory
    }
    public void addToFaves(int position){
        //mark article as favourite in ArticleList
        getArticleList().get(position).setIsFave(true);
        //add it to list of Faves
        //favesList is no longer persistent getFavesList().add(getArticleList().get(position));
        //notify whoever is listening
        notifyFavesListener();
    }
    public void removeFromFaves(Article article) {
        //get position of this article in the ArticleList
        int articleListPosition = getArticleList().indexOf(article);
        //unmark article as favourite in ArticleList
        (getArticleList().get(articleListPosition)).setIsFave(false);
        //remove it from list of Faves
        // favesList is no longer persistent getFavesList().remove(article);
        //notify whoever is listening
        notifyFavesListener();
    }
    private void notifyFavesListener() {
        if(favesUpdateListener != null) {
            favesUpdateListener.onFavesUpdate();
        }
    }
    /*receive subscriptions for notifications from classes*/
    public void setOnFavesUpdateListener(OnFavesUpdateListener favesUpdateListener) {
        this.favesUpdateListener = favesUpdateListener;
    }
    public interface OnFavesUpdateListener {
        void onFavesUpdate();
    }
}
