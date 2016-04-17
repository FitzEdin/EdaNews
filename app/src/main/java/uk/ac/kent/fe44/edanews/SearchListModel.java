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
public class SearchListModel {

    /** List of search results. */
    private ArrayList<Article> searchList = new ArrayList<>();
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
            searchList.clear();
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

    /**
     * Return the list of search results.
     * @return ArrayList The list of search results.
     */
    public ArrayList<Article> getSearchList() {
        return searchList;
    }

    public void setSearchList(ArrayList<Article> searchList) {
        this.searchList = searchList;
    }
}
