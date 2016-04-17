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
 *
 * The main model class for the application. Control over
 * specific parts of the model are delegated to subclasses
 * of ListModel. Access to these subclasses is controlled
 * via this class.
 */
public class ArticleModel {

    private static ArticleModel ourInstance = new ArticleModel();

    /*Constructor*/
    private ArticleModel() {
    }
    public static ArticleModel getInstance() {
        return ourInstance;
    }

    /*members for performing article list request*/
    public static final String RECORD_ID = "record_id", TITLE = "title", DATE = "date",
            SHORT_INFO = "short_info", IMAGE_URL = "image_url";





    /**
     * The ArticleListModel class manages the main list of articles that are shown in the
     * newsfeed. It is first populated by copying articles from the MasterListModel, then
     * via a network call as the user scrolls, OR newer articles are found online.
     */
    public ArticleListModel articleList = new ArticleListModel();
    /**
     * Returns the list of articles that will show
     * in the newsfeed; this list is managed by
     * the ArticleListModel class.
     * @return ArrayList List of articles
     */
    public ArrayList<Article> getArticleList() {
        return articleList.getArticleList();
    }
    /**
     * Instructs the ArticleListModel object to
     * load a set of articles into the newsfeed.
     * The object decides where this data should
     * be loaded from.
     * @param listUpdateListener A class that is listening for changes
     *                           to the list of articles being managed by the
     *                           ArticleListModel object. This class should implement
     *                           the OnListUpdateListener interface.
     */
    public void loadData(OnListUpdateListener listUpdateListener) {
        articleList.load(listUpdateListener);
    }
    /**
     * Each class that uses data from the newsfeed should
     * implement this interface to be informed when changes
     * are made to the list of articles.
     */
    public interface OnListUpdateListener {
        void onListUpdate();
    }



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





    /**
     * The SearchListModel class manages the list of articles that appear in the search results.
     */
    public SearchListModel searchList = new SearchListModel();
    /**
     * Returns the list of articles that will show
     * in the search results; this list is managed by
     * the SearchListModel class.
     * @return ArrayList List of articles
     */
    public ArrayList<Article> getSearchList() {
        return searchList.getSearchList();
    }
    /**
     * Passes its parameters to the SearchListModel object
     * for it to perform the search for articles with titles
     * containing the key parameter.
     * @param key String The string for searching titles.
     * @param searchListUpdateListener A class that is listening for changes to the
     *                                 list of articles being managed by the
     *                                 SearchListModel object. This class should
     *                                 implement the OnSearchListUpdateListener interface.
     */
    public void loadSearchData(String key, OnSearchListUpdateListener searchListUpdateListener) {
        searchList.load(key, searchListUpdateListener);
    }
    /**
     * Each class that uses data from the search results should
     * implement this interface to be informed when the list of
     * results are available.
     */
    public interface OnSearchListUpdateListener {
        void  onSearchListUpdate();
    }



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
