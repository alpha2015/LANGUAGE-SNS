package com.nhnnext.android.languageexchange.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alpha on 2015. 8. 26..
 */
public class ImageLoadHelper {
    private static ImageLoadHelper mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private ImageLoader.ImageCache imageCache;

    private ImageLoadHelper(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        imageCache = new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url.substring(url.indexOf("http")));
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url.substring(url.indexOf("http")), bitmap);
            }
        };
        mImageLoader = new ImageLoader(mRequestQueue, imageCache);
    }

    public static synchronized ImageLoadHelper getInstance(Context context) {
        if (mCtx != context)
            mInstance = new ImageLoadHelper(context);
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void imageUpdate(String url, Bitmap bitmap) {
        imageCache.putBitmap(url, bitmap);
    }
}