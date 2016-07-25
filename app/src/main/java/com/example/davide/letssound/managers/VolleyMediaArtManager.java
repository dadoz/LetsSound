package com.example.davide.letssound.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.util.AbstractList;


/**
 * Created by davide on 25/07/16.
 */
public class VolleyMediaArtManager implements Response.Listener<Bitmap>, Response.ErrorListener {

    private static VolleyMediaArtManager instance;
    private static WeakReference<OnVolleyMediaArtCallbackInterface> listenerRef;
    private final WeakReference<Context> ctx;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private VolleyMediaArtManager(WeakReference<Context> context) {
        ctx = context;
        init();
    }

    /**
     * init method
     */
    private void init() {
        getRequestQueue();
        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     *
     * @param context
     * @return
     */
    public static synchronized VolleyMediaArtManager getInstance(WeakReference<Context> context,
                                                                 WeakReference<OnVolleyMediaArtCallbackInterface> listener) {
        listenerRef = listener;
        if (instance == null) {
            instance = new VolleyMediaArtManager(context);
        }
        return instance;
    }

    /**
     *
     * @param mediaArtUri
     */
    public void retrieveMediaArtAsync(Uri mediaArtUri) {
        int maxHeight = 200;
        int maxWidth = 200;
        ImageRequest request = new ImageRequest(mediaArtUri.toString(), this, maxWidth, maxHeight,
            null, null, this);
        addToRequestQueue(request);
    }

    /**
     *
     * @return
     */
    private RequestQueue getRequestQueue() {
        return requestQueue == null ?
                requestQueue = Volley.newRequestQueue(ctx.get().getApplicationContext()) : requestQueue;
    }

    /**
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    @Override
    public void onResponse(Bitmap response) {
        this.listenerRef.get().onVolleyMediaArtSuccess(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        this.listenerRef.get().onVolleyMediaArtError(error);
    }

    public interface OnVolleyMediaArtCallbackInterface {
        void onVolleyMediaArtSuccess(Bitmap response);
        void onVolleyMediaArtError(VolleyError error);
    }
}
