package com.application.letssound.network.downloader;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import static android.os.Environment.getExternalStorageDirectory;


/**
 * Created by davide on 22/12/15.
 */
public class DownloadVolleyResponse implements Response.Listener<byte[]>, Response.ErrorListener {
    private final String filename;
    private final WeakReference<OnDownloadCallbackInterface> fragmentWeakReference;
    private final static String LOCAL_PATH = "/letsSound";
    private final static String EXTENSION = ".mp3";
    public enum DownloadStatusEnum { OK, FAILED }

    public DownloadVolleyResponse(String filename,
                                  WeakReference<OnDownloadCallbackInterface> fragmentWeakReference) {
        this.filename = filename;
        this.fragmentWeakReference = fragmentWeakReference;
    }

    @Override
    public void onResponse(byte[] response) {
        try {
            if (response == null) {
                fragmentWeakReference.get()
                        .onDownloadCallback(DownloadStatusEnum.FAILED,
                                new Exception("Failed to get download file info"));
                return;
            }

        //Read file name from headers
    //                String content = request.responseHeaders.get("Content-Disposition")
    //                        .toString();
    //                StringTokenizer st = new StringTokenizer(content, "=");
    //                String[] arrTag = st.toArray();
    //
    //                String filename = arrTag[1];
    //                filename = filename.replace(":", ".");
    //                Log.d("DEBUG::FILE NAME", filename);

            //covert reponse to input stream
            //TODO - mv getExternalStorageDir to music dir
            InputStream input = new ByteArrayInputStream(response);
            File path = new File(getExternalStorageDirectory().getAbsolutePath() + LOCAL_PATH);
            path.mkdirs();
            File file = new File(path, filename + EXTENSION);
    //                    map.put("resume_path", file.toString());
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[1024];

            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            fragmentWeakReference.get()
                    .onDownloadCallback(DownloadStatusEnum.OK, null);
        } catch (Exception e) {
            e.printStackTrace();
            fragmentWeakReference.get()
                    .onDownloadCallback(DownloadStatusEnum.FAILED, e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("KEY_ERROR", "requested url not found");
        error.printStackTrace();
        fragmentWeakReference.get()
                .onDownloadCallback(DownloadStatusEnum.FAILED, error);

    }

    /**
     * interface to handle download callback
     */
    public interface OnDownloadCallbackInterface {
        void onDownloadCallback(DownloadStatusEnum statusEnum, Exception e);
    }
}
