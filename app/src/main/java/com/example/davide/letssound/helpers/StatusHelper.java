package com.example.davide.letssound.helpers;

import com.example.davide.letssound.adapters.SoundTrackRecyclerViewAdapter;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.google.api.services.youtube.model.SearchResult;

/**
 * Created by davide on 15/07/16.
 */
public class StatusHelper {
    /**
     * @param pos
     */
/*    private void setOnSelectedStatus(int pos) {
        soundTrackStatus.setSelectStatus();
        soundTrackStatus.setCurrentPosition(pos);
        soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
        setOnSelectedActionbar(true);

        SearchResult obj = ((SoundTrackRecyclerViewAdapter) soundTrackRecyclerView.getAdapter())
                .getItemByPosition(pos);
        soundTrackTitleTextView.setText(obj.getSnippet().getTitle());
    }



    /**
     *
     * @param pos
     */
/*    private void setOnPlayingStatus(final int pos) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (soundTrackStatus.isValidPosition(pos)) {
                    soundTrackStatus.setPlayStatus();
                }
                soundTrackStatus.setCurrentPosition(pos);
                soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /**
     *
     */
/*    private void setOnIdleStatus() {
        //TODO crash
        try {
            soundTrackStatus.setCurrentPosition(SoundTrackStatus.INVALID_POSITION);
            soundTrackRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}
