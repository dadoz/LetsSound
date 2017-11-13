package com.lib.lmn.davide.soundtrackdownloaderlibrary.modules

import android.app.Activity
import com.android.volley.Response
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileDownloaderManager
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager
import java.io.FileInputStream

/**
 * Created by davide-syn on 6/26/17.
 */
open class SoundTrackDownloaderModule(val activity: Activity, val lst: OnSoundTrackRetrievesCallbacks?) {
    val fileStorageManager: FileStorageManager = FileStorageManager(activity, lst)

    fun getFileDownloaderManager(): FileDownloaderManager {
        val lst1 = Response.Listener<Any> { videoId -> lst?.onSoundTrackRetrieveSuccess(fileStorageManager.getFullPath(videoId as String) as String,
                fileStorageManager.retrieveFileAsInputStream(videoId)) }
        val lst2 = Response.ErrorListener { error -> lst?.onSoundTrackRetrieveError(error.message) }
        return FileDownloaderManager(activity, fileStorageManager, lst1, lst2)
    }

    interface OnSoundTrackRetrievesCallbacks {
        fun onSoundTrackRetrieveSuccess(filePath: String, fileInputStream: FileInputStream)
        fun onSoundTrackRetrieveError(message: String?)
    }
}
