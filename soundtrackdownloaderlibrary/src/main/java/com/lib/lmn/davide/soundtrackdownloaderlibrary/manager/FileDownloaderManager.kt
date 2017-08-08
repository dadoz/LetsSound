package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.content.Context
import android.net.Uri
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import java.util.*

/**
 * Created by davide-syn on 6/26/17.
 */

class FileDownloaderManager(context: Context?, private val fileStorageManager: FileStorageManager,
                            val lst: Response.Listener<Any>, val lst2: Response.ErrorListener) {
    val volleyReqQueue: RequestQueue = Volley.newRequestQueue(context)

    /**
     *
     * TODO refactor
     * @param url
     */
    fun getSoundTrack(url: Uri?) {
        //if cachedUrl is not null return and handle file
        getCachedFilePath(url)?.let { filePath -> lst.onResponse(filePath); return }

        //else online request
        url?.let {
            volleyReqQueue.add(InputStreamVolleyRequest(Request.Method.GET, url,
                    Response.Listener<ByteArray> { response -> fileStorageManager.setFileOnCache(url.toString(), response) },
                    Response.ErrorListener { error -> lst2.onErrorResponse(error) },
                    HashMap()))
        }
    }

    /**
     *  cached url
     */
    fun getCachedFilePath(url: Uri?) = url?.toString()?.let { stringUrl -> fileStorageManager.getCachedFile(stringUrl) }

    /**
     *
     */
    internal inner class InputStreamVolleyRequest(method: Int, mUrl: Uri, private val mListener: Response.Listener<ByteArray>,
                                                  errorListener: Response.ErrorListener, params: HashMap<String, String>) :
            Request<ByteArray>(method, mUrl.toString(), errorListener) {
        private val mParams: Map<String, String>

        init {
            setShouldCache(false)
            mParams = params
        }

        @Throws(com.android.volley.AuthFailureError::class)
        override fun getParams(): Map<String, String> {
            return mParams
        }

        override fun deliverResponse(response: ByteArray) {
            mListener.onResponse(response)
        }

        override fun parseNetworkResponse(response: NetworkResponse): Response<ByteArray> {
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
        }
    }
}
