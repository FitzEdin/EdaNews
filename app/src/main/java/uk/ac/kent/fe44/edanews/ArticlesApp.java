package uk.ac.kent.fe44.edanews;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by fitzroy on 31/01/2016.
 *
 * Handles globally needed objects
 */
public class ArticlesApp extends Application {

    private static ArticlesApp instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        requestQueue = Volley.newRequestQueue(this);

        int size = 4 * 1024 * 1024;
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
}
