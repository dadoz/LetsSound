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
        val firstInfo: FirstInfo? = null
        @SerializedName("1")
        @Expose
        private val secondInfo: SecondInfo? = null
        @SerializedName("2")
        @Expose
        private val thirdInfo: ThirdInfo? = null
        @SerializedName("3")
        @Expose
        private val fourthInfo: FourthInfo? = null
        @SerializedName("4")
        @Expose
        private val fifthInfo: FifthInfo? = null
    }


    inner class FirstInfo {
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

    inner class SecondInfo {

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

    inner class ThirdInfo {

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

    inner class FourthInfo {

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

    inner class FifthInfo {

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