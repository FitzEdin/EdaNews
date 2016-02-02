package uk.ac.kent.fe44.edanews;

import android.content.res.Resources;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fitzroy on 28/01/2016.
 * Interacts with the Article class
 */
public class ArticleModel {

    private String CLIENT_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php";
    private String RECORD_ID = "record_id", TITLE = "title", DATE = "date", IMAGE_URL = "image_url";

    private OnListUpdateListener listUpdateListener;

    private static ArticleModel ourInstance = new ArticleModel();

    private Response.Listener<JSONArray> netListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            //handle response from server
            //TODO: remove clear from article list
            getArticleList().clear();

            try{
                for(int i = 0; i < response.length(); i++) {
                    //extract JSON object from response
                    JSONObject object = response.getJSONObject(i);

                    //build article with the data
                    Article article = new Article(
                            object.getString(IMAGE_URL),
                            object.getInt(RECORD_ID),
                            object.getString(TITLE),
                            object.getString(DATE)
                    );

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

    //list of Articles
    private ArrayList<Article> articleList = new ArrayList<>();

    /*Constructor*/
    private ArticleModel() {
    }

    /*grab data from the network*/
    public void loadData() {
        //TODO: modify URL for grabbing sets of data
        //create a request object
        JsonArrayRequest request = new JsonArrayRequest(CLIENT_URL, netListener, errorListener);
        //make request
        ArticlesApp.getInstance()
                .getRequestQueue()
                .add(request);
    }

    /*let the listener know about updates*/
    private void notifyListener() {
        if(listUpdateListener != null) {
            listUpdateListener.onListUpdate(getArticleList());
        }
    }

    public static ArticleModel getInstance() {
        return ourInstance;
    }

    public ArrayList<Article> getArticleList() {
        return articleList;
    }

    /*receive subscriptions for notifications from classes*/
    public void setOnListUpdateListener(OnListUpdateListener listUpdateListener) {
        this.listUpdateListener = listUpdateListener;
    }

    /*informs appropriate classes when the data is updated*/
    public interface OnListUpdateListener {
        void  onListUpdate(ArrayList<Article> articleList);
    }
}
