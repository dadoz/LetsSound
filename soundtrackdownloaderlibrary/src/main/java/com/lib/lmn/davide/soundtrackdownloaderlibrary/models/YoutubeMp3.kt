package com.lib.lmn.davide.soundtrackdownloaderlibrary.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by davide on 30/09/2017.
 */

data class YoutubeMp3(val vidID: String, val vidTitle: String, val vidInfo: VidInfo) {
    inner class VidInfo {
        @SerializedName("0")
        @Expose
        val mp332Kb: Mp3Kb32? = null
        @SerializedName("1")
        @Expose
        val mp3Kb128: Mp3Kb128? = null
        @SerializedName("2")
        @Expose
        val mp3Kb192: Mp3Kb192? = null
        @SerializedName("3")
        @Expose
        val mp3Kb256: Mp3Kb256? = null
        @SerializedName("4")
        @Expose
        val mp3Kb320: Mp3Kb320? = null
    }


    inner class Mp3Kb32 {
        @SerializedName("dloadUrl")
        @Expose
        var dloadUrl: String? = null
        @SerializedName("bitrate")
        @Expose
        var bitrate: Int? = null
        @SerializedName("mp3size")
        @Expose
        var mp3size: String? = null

    }

    inner class Mp3Kb128 {

        @SerializedName("dloadUrl")
        @Expose
        var dloadUrl: String? = null
        @SerializedName("bitrate")
        @Expose
        var bitrate: Int? = null
        @SerializedName("mp3size")
        @Expose
        var mp3size: String? = null

    }

    inner class Mp3Kb192 {

        @SerializedName("dloadUrl")
        @Expose
        var dloadUrl: String? = null
        @SerializedName("bitrate")
        @Expose
        var bitrate: Int? = null
        @SerializedName("mp3size")
        @Expose
        var mp3size: String? = null

    }

    inner class Mp3Kb256 {

        @SerializedName("dloadUrl")
        @Expose
        var dloadUrl: String? = null
        @SerializedName("bitrate")
        @Expose
        var bitrate: Int? = null
        @SerializedName("mp3size")
        @Expose
        var mp3size: String? = null

    }

    inner class Mp3Kb320 {

        @SerializedName("dloadUrl")
        @Expose
        var dloadUrl: String? = null
        @SerializedName("bitrate")
        @Expose
        var bitrate: Int? = null
        @SerializedName("mp3size")
        @Expose
        var mp3size: String? = null

    }
}