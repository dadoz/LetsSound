package com.application.letssound.auth;

/**
 * Created by davide on 20/11/15.
 */
import com.google.android.gms.common.Scopes;
import com.google.api.services.youtube.YouTubeScopes;

public class AuthCustom {
    // Register an API key here: https://console.developers.google.com
//    public static final String API_KEY = "AIzaSyD1AjCuuJ6sigXLFGNeW-u5NUw2sF_HW6E";
    public static final String API_KEY = "AIzaSyAo0O8tMUjd7-tWFd87nph_4iJRhby8B1c";

    public static final String[] SCOPES = {Scopes.PROFILE, YouTubeScopes.YOUTUBE};
}