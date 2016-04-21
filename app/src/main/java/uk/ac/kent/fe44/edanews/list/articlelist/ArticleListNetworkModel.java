package uk.ac.kent.fe44.edanews.list.articlelist;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.model.ArticleModel;
import uk.ac.kent.fe44.edanews.ArticlesApp;
import uk.ac.kent.fe44.edanews.model.ListNetworkModel;

/**
 * Created by fe44 on 17/04/16.
 */
public class ArticleListNetworkModel extends ListNetworkModel {
    /** List of articles that will appear in the newsfeed. */
    /** An object that is listening for changes to the newsfeed list. */
    private ArticleModel.OnListUpdateListener listUpdateListener;

    /**  */
    private int mStart = 0;
    /** The number fo articles to download from the network per request. */
    private int size = 20;

    /** The base url to make requests to for data. */
    private static String CLIENT_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php?start=";

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
                list.clear();
            }
            try{
                for(int i = 0; i < response.length(); i++) {
                    //extract JSON object from response
                    JSONObject object = response.getJSONObject(i);
                    Article article = getArticle(object);
                    //and add to list of Articles
                    getList().add(article);
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
}
