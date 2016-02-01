package uk.ac.kent.fe44.edanews;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fitzroy on 31/01/2016.
 *
 * Handles globally needed objects
 */
public class ArticlesApp extends Application {

    private static ArticlesApp instance;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        requestQueue = Volley.newRequestQueue(this);
    }

    public static ArticlesApp getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
