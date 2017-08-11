package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.net.Uri
import com.lib.lmn.davide.soundtrackdownloaderlibrary.BuildConfig
import com.lib.lmn.davide.soundtrackdownloaderlibrary.models.YoutubeDownloaderFile
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by davide-syn on 6/30/17.
 */
class YoutubeDownloaderManager(val youtubeDownloaderService: YoutubeDownloaderModule.YoutubeDownloaderService,
                               val fileDownloaderManager: FileDownloaderManager) {

    /**
     * handle sound track
     */
    fun getSoundTrack(videoId: String, soundTrackObservable: Observable<YoutubeDownloaderFile>) {
                soundTrackObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ (title, link) ->
                    fileDownloaderManager.getSoundTrack(videoId, Uri.parse(link))},
                        Throwable::printStackTrace)
    }

    companion object {
        val FORMAT_TYPE = "JSON"
    }

    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val observable =  youtubeDownloaderService.fetchUrlByVideoId(FORMAT_TYPE,
                BuildConfig.YOUTUBE_BASE_PATH + videoId)
        getSoundTrack(videoId, observable)
    }


}
