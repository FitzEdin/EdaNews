package uk.ac.kent.fe44.edanews.list.searchlist;

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
public class SearchListNetworkModel extends ListNetworkModel {

    private ArticleModel.OnSearchListUpdateListener searchListUpdateListener;

    private String SEARCH_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php?titleHas=";

    public void load(String key, ArticleModel.OnSearchListUpdateListener searchListUpdateListener) {
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
    private void setOnSearchListUpdateListener(ArticleModel.OnSearchListUpdateListener searchListUpdateListener) {
        this.searchListUpdateListener = searchListUpdateListener;
    }

    private Response.Listener<JSONArray> searchListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            //handle response from server
            list.clear();
            try{
                for(int i = 0; i < response.length(); i++) {
                    //extract JSON object from response
                    JSONObject object = response.getJSONObject(i);
                    //check the master list for existence of this article
                    //if this article is there already, then get that old instance
                    //otherwise parse the new data
                    Article article = getArticle(object);

                    //and add to list of Articles
                    getList().add(article);
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
}
