package uk.ac.kent.fe44.edanews;


import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private ProgressBar myProgressBar;
    private OnListUpdateListener listUpdateListener;
    private static ArticleModel ourInstance = new ArticleModel();

    /*members for performing article list request*/
    private String CLIENT_URL = "http://www.efstratiou.info/projects/newsfeed/getList.php";
    private String RECORD_ID = "record_id", TITLE = "title", DATE = "date", IMAGE_URL = "image_url";
    private ArrayList<Article> articleList = new ArrayList<>();

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
    /*grab initial data from the network*/
    public void loadData(ProgressBar progressBar) {
        myProgressBar = progressBar;
        //TODO: modify URL for grabbing sets of data
        JsonArrayRequest request = new JsonArrayRequest(CLIENT_URL, netListener, errorListener);
        //make request
        ArticlesApp.getInstance()
                .getRequestQueue()
                .add(request);
    }
    /*let the listener know about updates*/
    private void notifyListener() {
        if(listUpdateListener != null) {
            myProgressBar.setVisibility(View.INVISIBLE);
            listUpdateListener.onListUpdate(getArticleList());
        }
    }
    /*receive subscriptions for notifications from classes*/
    public void setOnListUpdateListener(OnListUpdateListener listUpdateListener) {
        this.listUpdateListener = listUpdateListener;
    }
    /*informs appropriate classes when the data is updated*/
    public interface OnListUpdateListener {
        void  onListUpdate(ArrayList<Article> articleList);
    }
    /*end definition for members an methods for performing article list request(s)*/




    /*members and methods for performing request for article's details*/
    private String DETAILS_URL = "http://www.efstratiou.info/projects/newsfeed/getItem.php?id=";
    private String CONTENTS = "contents", WEB_PAGE = "web_page";
    private OnDetailsUpdateListener detailsUpdateListener;
    private static int articleId, articleIndex;
    private ArticleDetailsFragment me;

    private Response.Listener<JSONObject> detailsListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            //handle response from server
            try{
                //extract web_page and contents from JSON object response
                getArticleList().get(articleIndex)
                        .setWeb_page(response.getString(WEB_PAGE));
                getArticleList().get(articleIndex)
                        .setContents(response.getString(CONTENTS));
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
    public void loadArticleDetails(int articleId, int articleIndex) {
        //localise index for further use
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
    public void setOnDetailsUpdateListener(OnDetailsUpdateListener detailsUpdateListener) {
        this.detailsUpdateListener = detailsUpdateListener;
    }
    public interface OnDetailsUpdateListener {
        void onDetailsUpdate();
    }
    /*end definition of members and methods for getting article's details*/



    /*Constructor*/
    private ArticleModel() {
    }
    public static ArticleModel getInstance() {
        return ourInstance;
    }

    public ArrayList<Article> getArticleList() {
        return articleList;
    }
}
