package com.example.davide.letssound.services;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by davide on 13/07/16.
 */
public class MediaService extends MediaBrowserService {
    @Nullable
    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        Log.e("TAG", "hey get root callback");
        return null;
    }

    @Override
    public void onLoadChildren(String parentId, Result<List<MediaBrowser.MediaItem>> result) {
        Log.e("TAG", "hey on load children");
    }

}
