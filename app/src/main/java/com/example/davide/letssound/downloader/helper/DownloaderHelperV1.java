package com.example.davide.letssound.downloader.helper;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.davide.letssound.helpers.SoundTrackStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by davide on 01/01/16.
 */
public class DownloaderHelperV1 implements DownloaderInterface {

    private static DownloaderHelperV1 instance;
    private final WeakReference<Activity> activityWeakReference;
    private WeakReference<OnDownloadHelperResultInterface> resultWeakRef;
    private SoundTrackStatus soundTrackStatus;

    /**
     *
     * @param activityWeakReference
     * @param resultWeakRef
     */
    public DownloaderHelperV1(WeakReference<Activity> activityWeakReference,
                              WeakReference<OnDownloadHelperResultInterface> resultWeakRef) {
        this.activityWeakReference = activityWeakReference;
        this.resultWeakRef = resultWeakRef;
        this.soundTrackStatus = SoundTrackStatus.getInstance();
    }

    /**
     *
     * @param activityWeakReference
     * @return
     */
    public static DownloaderHelperV1 getInstance(WeakReference<Activity> activityWeakReference,
                                                 WeakReference<OnDownloadHelperResultInterface> resultWeakRef) {
        return instance == null ?
                instance = new DownloaderHelperV1(activityWeakReference, resultWeakRef) : instance;
    }

    /**
     *
     * @param dataString
     * @return
     */
    private String buildSoundTrackUrlByJSON(String videoId, String dataString) {
        try {
            JSONObject reader = new JSONObject(dataString);
            String tsCreate = reader.getString("ts_create");
            String r = reader.getString("r");
            String h2 = reader.getString("h2");
            return "/" + videoId + "/" + tsCreate + "/" + r + "/" + h2;
        } catch (JSONException e) {
            resultWeakRef.get().handleError(e);
        }
        return null;
    }

    /**
     *
     * @param videoId
     * @param timestamp
     */
    @Override
    public void retrieveSoundTrackAsync(final String videoId, String timestamp) {

        // Instantiate the RequestQueue.
        String END_POINT = "https://shrouded-island-4422.herokuapp.com/api/getVideoInfoUrl/" +
                videoId + "/" + timestamp;
        RequestQueue queue = Volley.newRequestQueue(activityWeakReference.get());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, END_POINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultWeakRef.get().handleSuccess("success " + response);
                        String url = response.replaceAll("\"", "");
                        retrieveVideoInfoAsync(videoId, url);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultWeakRef.get().handleError(error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     *
     * @param videoId
     * @param videoInfoUrl
     */
    private void retrieveVideoInfoAsync(final String videoId, String videoInfoUrl) {
        RequestQueue queue = Volley.newRequestQueue(activityWeakReference.get());
        Log.e("TAG", videoInfoUrl);
        Log.e("TAG", videoId);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, videoInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonResult = response.split("=", 2)[1];
                            resultWeakRef.get().handleSuccess("success " + jsonResult);
                            retrieveSoundTrackUrlAsync(videoId, jsonResult);
                        } catch (Exception e) {
                            resultWeakRef.get().handleError(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        resultWeakRef.get().handleError(error);
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     *
     * @param jsonResult
     */
    private void retrieveSoundTrackUrlAsync(String videoId, String jsonResult) {
        // Instantiate the RequestQueue.
        String END_POINT = "https://shrouded-island-4422.herokuapp.com/api/getSoundTrackUrl" +
                buildSoundTrackUrlByJSON(videoId, jsonResult);
        Log.e("TAG", END_POINT);
        RequestQueue queue = Volley.newRequestQueue(activityWeakReference.get());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, END_POINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO refactorize it
                        resultWeakRef.get().handleSuccess("success " + response);
                        try {
                            if (soundTrackStatus.isPlayStatus()) {
                                resultWeakRef.get().startMediaPlayer(response);
                                return;
                            }
                            if (soundTrackStatus.isDownloadStatus()) {
                                resultWeakRef.get().downloadSoundTrackByUrl(response);
                            }
                        } catch (Exception e) {
                            resultWeakRef.get().handleError(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultWeakRef.get().handleError(error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



}
