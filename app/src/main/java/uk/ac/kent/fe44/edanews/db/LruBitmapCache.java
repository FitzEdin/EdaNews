package uk.ac.kent.fe44.edanews.db;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by fitzroy on 09/02/2016.
 *
 * Stores images loaded from the network.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    /*methods used for storing and retrieving images*/
    @Override
    public Bitmap getBitmap(String url) {
        return get(formatUrl(url));
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(formatUrl(url), bitmap);
    }

    /*calculates size of bitmaps stored in cache*/
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    /**
     * Reformat the key to represent the correct url.
     * @param url String The key to reformat.
     * @return The formatted url
     */
    private String formatUrl(String url) {
        String[] parts = url.split(":");
        String formattedUrl;
        if(parts.length > 1) {
            formattedUrl = "https:" + parts[1];
        }else{
            formattedUrl = "https:" + parts[0];
        }
        return formattedUrl;
    }
}
