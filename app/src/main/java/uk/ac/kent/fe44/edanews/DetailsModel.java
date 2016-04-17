package uk.ac.kent.fe44.edanews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fe44 on 17/04/16.
 */
public class DetailsModel {
    private String DETAILS_URL = "http://www.efstratiou.info/projects/newsfeed/getItem.php?id=";
    private String CONTENTS = "contents";
    private ArticleModel.OnDetailsUpdateListener detailsUpdateListener;
    private static int articleIndex;

    public void load(int articleId, int articleIndex, ArticleModel.OnDetailsUpdateListener detailsUpdateListener) {
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
    private void setOnDetailsUpdateListener(ArticleModel.OnDetailsUpdateListener detailsUpdateListener) {
        this.detailsUpdateListener = detailsUpdateListener;
    }

    private Response.Listener<JSONObject> detailsListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            //handle response from server
            try{
                //extract contents from JSON object response
                ArticleModel.getInstance()
                        .getArticleList()
                        .get(articleIndex)
                        .setContents(response.getString(CONTENTS));

                //mark article as detailed /---/ prevents a second attempt at downloading same data
                ArticleModel.getInstance()
                        .getArticleList()
                        .get(articleIndex)
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
}
