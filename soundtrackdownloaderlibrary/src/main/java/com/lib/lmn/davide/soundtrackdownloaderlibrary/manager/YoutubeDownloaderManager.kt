package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.net.Uri
import android.util.Log
import com.android.volley.VolleyError
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by davide-syn on 6/30/17.
 */
class YoutubeDownloaderManager(private val youtubeDownloaderService: YoutubeDownloaderModule.YoutubeDownloaderService,
                               private val fileDownloaderManager: FileDownloaderManager) {
    val headers: HashMap<String, String> = HashMap()

    companion object {
        val FORMAT_TYPE = "mp3"
        val HTTP_PROTOCOL = "http:"
    }


    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val dispsable =  youtubeDownloaderService.fetchUrlByVideoId(headers, videoId)
                .debounce (2, TimeUnit.MINUTES)
                .flatMap({ youtubeDownloaderService.fetchUrlByVideoId(headers, videoId) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ item ->
                    fileDownloaderManager.getSoundTrack(videoId, Uri.parse(item.dlMusic))
                }, {error -> run { Log.e("TEST", error.message); fileDownloaderManager.lst2.onErrorResponse(VolleyError(error)) } })
    }
}
