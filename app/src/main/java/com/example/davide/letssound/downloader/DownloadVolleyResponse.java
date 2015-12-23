package com.example.davide.letssound.downloader;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.davide.letssound.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

import static android.os.Environment.*;

/**
 * Created by davide on 22/12/15.
 */
public class DownloadVolleyResponse implements Response.Listener<byte[]>, Response.ErrorListener {
    private final String filename;

    public DownloadVolleyResponse(String filename) {
        this.filename = filename;
    }

    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response!=null) {

                //Read file name from headers
//                String content = request.responseHeaders.get("Content-Disposition")
//                        .toString();
//                StringTokenizer st = new StringTokenizer(content, "=");
//                String[] arrTag = st.toArray();
//
//                String filename = arrTag[1];
//                filename = filename.replace(":", ".");
//                Log.d("DEBUG::FILE NAME", filename);

                try{
                    //covert reponse to input stream
                    InputStream input = new ByteArrayInputStream(response);
                    File path = getExternalStorageDirectory();
                    File file = new File(path, filename);
                    map.put("resume_path", file.toString());
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                    byte data[] = new byte[1024];

                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("KEY_ERROR", "requested url not found");
        error.printStackTrace();
    }
}
