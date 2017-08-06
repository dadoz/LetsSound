package com.application.letssound.managers;

import android.content.Context;
import android.util.Log;

import com.application.letssound.models.HistoryResult;
import com.application.letssound.models.SoundTrack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by davide on 25/07/16.
 */
public class HistoryManager {
    private static HistoryManager instance;
    private static Realm realm;
//    private final static long SCHEMA_VERSION = 1;
//    private final static String SCHEMA_NAME = "history_soundtrack_realmio";
    private String TAG = "HistoryManager";

    /**
     *
     * @param context
     * @return
     */
    public static HistoryManager getInstance(WeakReference<Context> context) {
        init();
        return instance == null ?
                instance = new HistoryManager() : instance;
    }

    /**
     *
     */
    private HistoryManager() {
    }

    /**
     *
     */
    private static void init() {
        realm = Realm.getDefaultInstance();
    }
    /**
     *
     * @param soundTrack
     */
    public void saveOnHistory(SoundTrack soundTrack) {
        if (realm != null &&
                isNotInList(soundTrack.getId().getVideoId())) {
            soundTrack.setTimestamp();
            realm.beginTransaction();
            realm.copyToRealm(soundTrack);
            realm.commitTransaction();
        }
    }

    /**
     *
     * @param videoId
     * @return
     */
    private boolean isNotInList(String videoId) {
        if (realm != null) {
            RealmResults<SoundTrack> tmp = realm.where(SoundTrack.class)
                    .equalTo("id.videoId", videoId)
                    .findAll();
            Log.e(TAG, "size list" + tmp.size());
            return tmp.size() == 0;
        }
        return false;
    }

    /**
     *
     * @param title
     */
    public SoundTrack findSoundTrackByTitle(String title) {
        if (realm != null) {
            return realm.where(SoundTrack.class)
                    .equalTo("snippet.title", title)
                    .findFirst();
        }
        return null;
    }

    /**
     * get history with no filter
     */
    public ArrayList<HistoryResult> getHistory() {
        if (realm != null) {
            ArrayList<HistoryResult> list = new ArrayList<>();
            RealmResults<SoundTrack> tmp = realm.where(SoundTrack.class)
                    .findAll();
            for (SoundTrack obj:
                 tmp) {
                list.add(new HistoryResult(obj.getSnippet().getTitle(), obj.getTimestamp()));
            }
            return list;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getHistoryString() {
        if (realm != null) {
            ArrayList<String> list = new ArrayList<>();
            RealmResults<SoundTrack> tmp = realm.where(SoundTrack.class)
                    .findAll();
            for (SoundTrack obj:
                 tmp) {
                list.add(obj.getSnippet().getTitle());
            }
            return list;
        }
        return null;
    }
}
