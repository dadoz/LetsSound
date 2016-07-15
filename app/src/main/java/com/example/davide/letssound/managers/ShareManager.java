package com.example.davide.letssound.managers;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.example.davide.letssound.R;
import com.example.davide.letssound.utils.Utils;
import com.google.api.services.youtube.model.SearchResult;

/**
 * Created by davide on 15/07/16.
 */
public class ShareManager {

    /**
     * share action
     */
/*    private void shareAction() {
        int pos = soundTrackStatus.getCurrentPosition();
        if (!soundTrackStatus.isValidPosition(pos)) {
            handleError(new Exception("Song position not valid - please select it again"));
            return;
        }

        Utils.createSnackBarByBackgroundColor(mainView, "hey share position " + pos,
                ContextCompat.getColor(getContext(), R.color.md_teal_400));
        shareSoundTrack(pos);
    }

    /**
     *
     * @param position
     */
/*    private void shareSoundTrack(int position) {
        SearchResult obj = getTrackByPosition(position);
        String videoId = obj.getId().getVideoId();
        String title = obj.getSnippet().getTitle();
        String content = "[Video URL]: \n http://youtube.com/?v=" + videoId;
        shareSoundTrackByIntent(title, content);
    }

    /**
     *
     * @param title
     * @param content
     */
/*    private void shareSoundTrackByIntent(String title, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(shareIntent, "Share sound track:"));
    }
*/
}
