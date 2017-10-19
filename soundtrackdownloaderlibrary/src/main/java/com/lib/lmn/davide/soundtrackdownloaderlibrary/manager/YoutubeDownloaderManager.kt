package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.net.Uri
import android.util.Log
import com.android.volley.VolleyError
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by davide-syn on 6/30/17.
 */
class YoutubeDownloaderManager(private val youtubeDownloaderService: YoutubeDownloaderModule.YoutubeDownloaderService,
                               private val fileDownloaderManager: FileDownloaderManager) {

    companion object {
        val FORMAT_TYPE = "mp3"
        val HTTP_PROTOCOL = "http:"
    }


    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val dispsable =  youtubeDownloaderService.fetchUrlByVideoId(FORMAT_TYPE, videoId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ item -> fileDownloaderManager.getSoundTrack(videoId, Uri.parse(HTTP_PROTOCOL + item.vidInfo.mp3Kb320?.dloadUrl))},
                    {error -> run { Log.e("TEST", error.message); fileDownloaderManager.lst2.onErrorResponse(VolleyError(error)) } })
    }


}
