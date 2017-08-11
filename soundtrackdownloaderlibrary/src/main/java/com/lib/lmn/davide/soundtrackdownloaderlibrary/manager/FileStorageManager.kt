package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.content.Context
import com.lib.lmn.davide.soundtrackdownloaderlibrary.models.SoundTrackCache
import com.lib.lmn.davide.soundtrackdownloaderlibrary.models.SoundTrackRealmModule
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.vicpin.krealmextensions.transaction
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by davide-syn on 6/26/17.
 */
class FileStorageManager(context: Context?, lst: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks?) {
    val lst: WeakReference<SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks?> = WeakReference(lst)
    val fileDir: File? = context?.filesDir
    val config: RealmConfiguration by lazy {
        RealmConfiguration.Builder()
                .name("library.realm")
                .modules(SoundTrackRealmModule())
                .build()
    }

    val realm: Realm by lazy {
        Realm.getInstance(config)
    }

    init {
        Realm.init(context)
        //delete previous realm config
//        Realm.deleteRealm(config)
    }
     /**
     * @param key
     * *
     * @return
     */
    fun getCachedFile(videoId: String): String? {
         return realm
                 .where(SoundTrackCache().javaClass)
                 .equalTo("key", videoId)
                 .findFirst()?.key
     }

    /**
     * @param key
     * *
     * @param file
     */
    fun setFileOnCache(videoId: String, file: ByteArray) {
        if (getCachedFile(videoId) == null) {
            //save on db
            saveOnDb(videoId)
            //replace file
            saveFile(videoId, file)
        }
    }

    /**
     *
     */
    private fun saveOnDb(videoId: String) {
        realm.transaction { realm ->
            try {
                realm.createObject(SoundTrackCache().javaClass, videoId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    //new algorithm to handle file name
//    StringBuilder sb = new StringBuilder(len);
//    for (int i = 0; i < len; i++) {
//        char ch = s.charAt(i);
//        if (ch < ' ' || ch >= 0x7F || ch == fileSep || ... // add other illegal chars
//                || (ch == '.' && i == 0) // we don't want to collide with "." or ".."!
//                || ch == escape) {
//            sb.append(escape);
//            if (ch < 0x10) {
//                sb.append('0');
//            }
//            sb.append(Integer.toHexString(ch));
//        } else {
//            sb.append(ch);
//        }
//    }

    /***
     * @param key
     */
    private fun saveFile(key: String?, downloadedFile: ByteArray) {
        Thread(Runnable {
            try {
                val file = File(fileDir, key)
                val stream = FileOutputStream(file)
                stream.write(downloadedFile)
                stream.close()
                lst.get()?.onSoundTrackRetrieveSuccess(file.absolutePath, FileInputStream(file))
            } catch (e: IOException) {
                e.printStackTrace()
                lst.get()?.onSoundTrackRetrieveError(e.message)
            }
        }).start()
    }

    fun retrieveFile(key: String): ByteArray? {
        try {
            val fileInputStream = FileInputStream("$fileDir/$key")
            val byteArray = fileInputStream.readBytes()
            fileInputStream.close()
            return byteArray
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * retrieve as input stream
     */
    fun retrieveFileAsInputStream(key: String): FileInputStream {
        return FileInputStream("$fileDir/$key")
    }

    /**
     * get full path
     */
    fun getFullPath(videoId: String): String? {
        return "$fileDir/$videoId"
    }

    /**

     * @param key
     * *
     * @return
     */
    companion object {
        /**
         * get full path
         */
        fun getFullPath(context: Context, videoId: String): String? {
            return "${context.filesDir}/$videoId"
        }

        /**
         * encoded key
         */
        fun generateEncodedKey(key: String): String {
            var encoded = key
                    .replace("http:\\\\", "")
                    .replace("/", "")
                    .replace("=", "")
                    .toLowerCase()
            if (encoded.length >= 64)
                encoded = encoded.substring(0, 63)
            return encoded
        }
    }
}
