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
    private ArticleModel.OnDetailsUpdateListener detailsUpdateListener;
    private static int articleIndex;
    private int callerId;

    public void load(int callerId, int articleId, int articleIndex, ArticleModel.OnDetailsUpdateListener detailsUpdateListener) {
        this.callerId = callerId;
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
                String contents = response.getString(ArticlesApp.CONTENTS);
                //populate the right list
                switch (callerId) {
                    case ArticlesApp.ARTICLE_CALLER_ID:
                        ArticleModel.getInstance()
                                .getArticleList()
                                .get(articleIndex)
                                .setContents(contents);
                        break;
                    case ArticlesApp.FAVES_CALLER_ID:   // Individual lists cannot be used in this
                    case ArticlesApp.SAVED_CALLER_ID:   // case since they are transient, and
                        ArticleModel.getInstance()      // article indices will change.
                                .getMasterList()
                                .get(articleIndex)
                                .setContents(contents);
                        break;
                    case ArticlesApp.SEARCH_CALLER_ID:
                        ArticleModel.getInstance()
                                .getSearchList()
                                .get(articleIndex)
                                .setContents(contents);
                        break;
                }

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
