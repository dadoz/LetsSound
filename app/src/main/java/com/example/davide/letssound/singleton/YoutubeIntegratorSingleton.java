/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.davide.letssound.singleton;

import android.os.StrictMode;
import android.util.Log;

import com.example.davide.letssound.auth.AuthCustom;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import com.google.api.client.http.HttpTransport;
/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class YoutubeIntegratorSingleton {

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
//    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 30;


    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param queryTerm command line args.
     */
    public static List<SearchResult> searchByQueryString(String queryTerm) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Read the developer key from the properties file.
//        Properties properties = new Properties();
//        try {
//            InputStream in = YoutubeIntegratorSingleton
//                    .class.getResourceAsStream("/" + PROPERTIES_FILENAME);
//            if (in != null) {
//                properties.load(in);
//            }
//        } catch (Exception e) {
//           Log.e("TAG", "There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
//                   + " : " + e.getMessage());
//        }

        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY,
                    new HttpRequestInitializer() {
                        public void initialize(HttpRequest request) throws IOException {
                        }
                    })
                    .setApplicationName("letsSound")
                    .build();

            // Prompt the user to enter a query term.
//            String queryTerm = getInputQuery();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the Google Developers Console for
            // non-authenticated requests. See:
            // https://console.developers.google.com/
//            String apiKey = properties.getProperty("youtube.apikey");

            search.setKey(AuthCustom.API_KEY);
            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            return searchResponse.getItems();
        } catch (GoogleJsonResponseException e) {
            Log.e("TAG", "There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            Log.e("TAG" ,"There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * auth clas
     */
    public static class Auth {
        /**
         * Define a global instance of the HTTP transport.
         */
        public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

        /**
         * Define a global instance of the JSON factory.
         */
        public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    }
}
