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
    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val dispsable =  youtubeDownloaderService.fetchUrlByVideoId(videoId)
                .debounce (2, TimeUnit.MINUTES)
                .flatMap({ youtubeDownloaderService.fetchUrlByVideoId(videoId) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ item ->
                    fileDownloaderManager.getSoundTrack(videoId, Uri.parse(item.dlMusic))
                }, {error -> run { Log.e(javaClass.name, error.message); fileDownloaderManager.lst2.onErrorResponse(VolleyError(error)) } })
    }
}
