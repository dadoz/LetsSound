package com.example.davide.letssound.fragments;

import android.content.Context;
import android.util.Log;

import com.example.davide.letssound.models.SoundTrack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by davide on 25/07/16.
 */
public class HistoryManager {
    private static HistoryManager instance;
    private static WeakReference<Context> ctxRef;
    private static Realm realm;
    private final static long SCHEMA_VERSION = 1;
    private final static String SCHEMA_NAME = "history_soundtrack_realmio";
    private String TAG = "HistoryManager";

    /**
     *
     * @param context
     * @return
     */
    public static HistoryManager getInstance(WeakReference<Context> context) {
        ctxRef = context;
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
        realm = Realm.getInstance(new RealmConfiguration.Builder(ctxRef.get())
                .name(SCHEMA_NAME)
                .schemaVersion(SCHEMA_VERSION)
//                .encryptionKey(getKey())
//                .modules(new MySchemaModule())
//                .migration(new MyMigration())
                .build());
    }
    /**
     *
     * @param soundTrack
     */
    public void saveOnHistory(SoundTrack soundTrack) {
        if (realm != null &&
                isNotInList(soundTrack.getId().getVideoId())) {
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
     * get history with no filter
     */
    public ArrayList<SoundTrack> getHistory() {
        if (realm != null) {
            ArrayList<SoundTrack> list = new ArrayList<>();
            RealmResults<SoundTrack> tmp = realm.where(SoundTrack.class)
                    .findAll();
            for (SoundTrack obj:
                 tmp) {
                list.add(obj);
            }
            return list;
        }
        return null;
    }
}
