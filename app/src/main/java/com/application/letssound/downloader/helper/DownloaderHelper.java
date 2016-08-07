package com.application.letssound.downloader.helper;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.letssound.helpers.SoundTrackStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by davide on 01/01/16.
 */
public class DownloaderHelper implements DownloaderInterface, Response.ErrorListener, Response.Listener<String> {

    private static DownloaderHelper instance;
    private final WeakReference<Activity> activityWeakReference;
    private final WeakReference<OnDownloadHelperResultInterface> resultWeakRef;
    private final SoundTrackStatus soundTrackStatus;

    private String FORMAT_TYPE = "JSON";
    private String YOUTUBE_BASE_PATH = "https://www.youtube.com/watch?v=";
    private String END_POINT =  "http://www.youtubeinmp3.com/fetch/" +
            "?format=" + FORMAT_TYPE +
            "&video=" + YOUTUBE_BASE_PATH;
    /**
     *
     * @param activityWeakReference
     * @param resultWeakRef
     */
    public DownloaderHelper(WeakReference<Activity> activityWeakReference,
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
    public static DownloaderHelper getInstance(WeakReference<Activity> activityWeakReference,
                                                 WeakReference<OnDownloadHelperResultInterface> resultWeakRef) {
        return instance == null ?
                instance = new DownloaderHelper(activityWeakReference, resultWeakRef) : instance;
    }

    /**
     * changing method - now handling request by http://www.youtubeinmp3.com/api
     * @param videoId
     */
    @Override
    public void retrieveSoundTrackAsync(final String videoId, String dataString) {
        Volley.newRequestQueue(activityWeakReference.get())
            .add(new StringRequest(Request.Method.GET, END_POINT + videoId, this, this));
    }

    /**
     *
     * @param dataString
     * @return
     */
    private String getSoundTrackDownloadUrlByJSON(String dataString) {
        try {
            JSONObject obj = new JSONObject(dataString);
//            String title = obj.getString("title");
            return obj.getString("link");
        } catch (JSONException e) {
            resultWeakRef.get().handleError(e);
        }
        return null;
    }
    /**
     * @deprecated
     * @param dataString
     * @return
     */
    private String getSoundTrackLengthByJSON(String dataString) {
        try {
            JSONObject obj = new JSONObject(dataString);
            return obj.getString("length");
        } catch (JSONException e) {
            resultWeakRef.get().handleError(e);
        }
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        resultWeakRef.get().handleError(error);

    }

    @Override
    public void onResponse(String response) {
        try {
            String url = getSoundTrackDownloadUrlByJSON(response);
            handleDownloadStatus(url);
            handlePlayStatus(url);
        } catch (Exception e) {
            resultWeakRef.get().handleError(e);
        }
    }

    public void handlePlayStatus(String url) throws Exception {
        if (soundTrackStatus.isPlayStatus() &&
                url != null) {
//            resultWeakRef.get().handleSuccess("Eureka playing:" + url);
            resultWeakRef.get().startMediaPlayer(url);
        }
    }

    public void handleDownloadStatus(String url) throws Exception {
        if (soundTrackStatus.isDownloadStatus()) {
            resultWeakRef.get().downloadSoundTrackByUrl(url);
        }
    }

}
