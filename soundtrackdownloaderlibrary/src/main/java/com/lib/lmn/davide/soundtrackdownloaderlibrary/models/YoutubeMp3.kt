package com.lib.lmn.davide.soundtrackdownloaderlibrary.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by davide-syn on 11/10/17.
 */

class YoutubeMp3 {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("timestamp")
    @Expose
    var timestamp: String? = null
    @SerializedName("error")
    @Expose
    var error: String? = null
    @SerializedName("dlMusic")
    @Expose
    var dlMusic: String? = null
    @SerializedName("dlVideo")
    @Expose
    var dlVideo: String? = null
    @SerializedName("sizeMusic")
    @Expose
    var sizeMusic: String? = null
    @SerializedName("sizeVideo")
    @Expose
    var sizeVideo: String? = null
    @SerializedName("currentVideoSize")
    @Expose
    var currentVideoSize: String? = null
    @SerializedName("currentMusicSize")
    @Expose
    var currentMusicSize: String? = null
    @SerializedName("remoteAddress")
    @Expose
    var remoteAddress: String? = null
    @SerializedName("filenameVideo")
    @Expose
    var filenameVideo: String? = null
    @SerializedName("filenameMusic")
    @Expose
    var filenameMusic: String? = null
}