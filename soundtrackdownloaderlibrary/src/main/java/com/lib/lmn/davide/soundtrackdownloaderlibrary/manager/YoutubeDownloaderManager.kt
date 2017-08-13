package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.net.Uri
import com.android.volley.VolleyError
import com.lib.lmn.davide.soundtrackdownloaderlibrary.BuildConfig
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by davide-syn on 6/30/17.
 */
class YoutubeDownloaderManager(val youtubeDownloaderService: YoutubeDownloaderModule.YoutubeDownloaderService,
                               val fileDownloaderManager: FileDownloaderManager) {

    companion object {
        val FORMAT_TYPE = "JSON"
    }

    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val dispsable =  youtubeDownloaderService.fetchUrlByVideoId(FORMAT_TYPE,
                BuildConfig.YOUTUBE_BASE_PATH + videoId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ (title, link) ->
                    fileDownloaderManager.getSoundTrack(videoId, Uri.parse(link))},
                    {error -> fileDownloaderManager.lst2.onErrorResponse(VolleyError(error))})
    }


}
