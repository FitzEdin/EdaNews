package uk.ac.kent.fe44.edanews;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import uk.ac.kent.fe44.edanews.db.LruBitmapCache;

/**
 * Created by fitzroy on 31/01/2016.
 *
 * A singleton class, which manages the
 * requestQueue and imageLoader objects
 * used throughout the application.
 */
public class ArticlesApp extends Application {

    private static ArticlesApp instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public static final int ARTICLE_CALLER_ID = 1;
    public static final int FAVES_CALLER_ID = 2;
    public static final int SEARCH_CALLER_ID = 3;
    public static final int SAVED_CALLER_ID = 4;

    public static final int DEPT_EXTRA_ID = 5;
    public static final int DEVELOPER_EXTRA_ID = 6;
    public static final int APP_EXTRA_ID = 7;

    public static final String ITEM_INDEX = "ITEM_INDEX";
    public static final String CALLER_ID = "CALLER_ID";
    public static final String EXTRA_ID = "EXTRA_ID";

    public final static String RECORD_ID = "record_id", TITLE = "title", DATE = "date",
            SHORT_INFO = "short_info", IMAGE_URL = "image_url", CONTENTS = "contents";
    public final static String TRANSITION_TITLE = "article_title", TRANSITION_TOOLBAR = "toolbar",
            TRANSITION_FAB = "fab_button", TRANSITION_PHOTO = "article_photo";

    public final static String PLAIN_TEXT = "text/plain";
    public final static String SAVED_COUNT = "saved_count";

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        requestQueue = Volley.newRequestQueue(this);

        int size = 100 * 1024 * 1024;
        imageLoader = new ImageLoader(requestQueue, new LruBitmapCache(size));
    }

    public static ArticlesApp getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    //Source: previous project kn.muscovado.thadailygeek
    //check that the network is available
    public boolean networkIsAvailable() {
        ConnectivityManager mngr = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mngr.getActiveNetworkInfo();

        boolean isAvailable = false;

        //checks that the network is present && it's connected
        if( ( networkInfo != null ) && ( networkInfo.isConnected() ) ){
            isAvailable = true;		//network is up & running
        }

        return isAvailable;
    }//end networkIsAvailable() method
}
