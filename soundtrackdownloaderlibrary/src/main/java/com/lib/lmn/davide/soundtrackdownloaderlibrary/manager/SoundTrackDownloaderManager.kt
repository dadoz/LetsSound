package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.app.Activity
import com.android.volley.VolleyError
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule

/**
 * Created by davide-syn on 7/7/17.
 */

class SoundTrackDownloaderManager private constructor(activity: Activity, listener: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks) {
    val fileDownloaderManager: FileDownloaderManager = SoundTrackDownloaderModule(activity, listener).getFileDownloaderManager()
    var youtubeDownloaderManager: YoutubeDownloaderManager = YoutubeDownloaderModule(fileDownloaderManager).getYoutubeDownloadManager()

    init {
        instance = this
    }

    fun downloadAndPlaySoundTrack(videoId: String?) {
        videoId ?: return fileDownloaderManager.lst2.onErrorResponse(VolleyError("Oh Snap: empty videoId - downloadAndPlaySoundTrack"))
        youtubeDownloaderManager.fetchSoundTrackUrlByVideoId(videoId)
    }

    companion object {
        lateinit var instance: SoundTrackDownloaderManager

        fun getInstance(activity: Activity, listener: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks): SoundTrackDownloaderManager {
            instance = SoundTrackDownloaderManager(activity, listener)
            return instance
        }
    }
}
