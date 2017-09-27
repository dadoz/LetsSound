package com.application.letssound.managers;

import com.application.letssound.network.downloader.DownloadVolleyResponse;

/**
 * Created by davide on 13/07/16.
 */
public class DownloadManager {

    /**
     * download action
     */
/*    private void downloadAction() {
        try {
            int pos = soundTrackStatus.getCurrentPosition();
            if (!soundTrackStatus.isValidPosition(pos)) {
                handleError(new Exception("Song position not valid - please select it again"));
                return;
            }
            sendSnackbarMessage("hey download position " + pos);
            swipeContainerLayout.setRefreshing(true);
            downloadSoundTrack(pos);
        } catch (Exception e) {
            swipeContainerLayout.setRefreshing(false);
            e.printStackTrace();
        }
    }

    /**
     *
     * @param position
     */
/*    private void downloadSoundTrack(int position) throws Exception {
        SearchResult obj = getTrackByPosition(position);
        String videoId = obj.getId().getVideoId();
        downloadFilename = obj.getSnippet().getTitle();

        setOnDownloadStatus();
        downloaderHelper.retrieveSoundTrackAsync(videoId, null);
//        retrieveVideoUrlAsync(videoId, Long.toString(getTimestamp()));
    }

    /**
     * @param url
     */
/*    public void downloadSoundTrackByUrl(String url) {
        DownloadVolleyResponse volleyResponse = new DownloadVolleyResponse(downloadFilename,
                new WeakReference<DownloadVolleyResponse.OnDownloadCallbackInterface>(this));
//        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext(),
//                new HurlStack());
//        mRequestQueue.add(new InputStreamVolleyRequest(Request.Method.GET, url,
//                volleyResponse, volleyResponse, null));
    }
    /**
     *
     */
/*    private void setOnDownloadStatus() {
        soundTrackStatus.setDownloadStatus();
    }

    /**
     * TODO presenter
     * @param statusEnum
     * @param e
     */
/*    public void onDownloadCallback(DownloadVolleyResponse.DownloadStatusEnum statusEnum, Exception e) {
        if (statusEnum == DownloadVolleyResponse.DownloadStatusEnum.FAILED) {
            handleError(e);
        } else if (statusEnum == DownloadVolleyResponse.DownloadStatusEnum.OK) {
            swipeContainerLayout.setRefreshing(false);
            soundTrackStatus.setIdleStatus();
            soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
            handleSuccess("Hey you got your song!");
        }
    }

*/
}
