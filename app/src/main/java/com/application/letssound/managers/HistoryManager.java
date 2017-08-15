package com.application.letssound.managers;

import android.content.Context;
import android.util.Log;

import com.application.letssound.models.HistoryResult;
import com.application.letssound.models.SoundTrack;

import java.util.Iterator;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by davide on 25/07/16.
 */
public class HistoryManager {
    private static HistoryManager instance;
    private static Realm realm;
    private String TAG = "HistoryManager";
    private PublishSubject<SoundTrack> searchedItemSubject = PublishSubject.create();

    /**
     *
     * @return
     */
    public PublishSubject<SoundTrack> getSearchedItemSubject() {
        return searchedItemSubject;
    }
    /**
     *
     * @param context
     * @return
     */
    public static HistoryManager getInstance(Context context) {
        init(context);
        return (instance == null) ? instance = new HistoryManager() : instance;
    }

    /**
     *
     */
    private HistoryManager() {
    }

    /**
     *
     * @param context
     */
    private static void init(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    private static void updateTimeStamp() {
        if (realm != null) {
            RealmResults<SoundTrack> list = realm.where(SoundTrack.class).findAll();
            for (SoundTrack item : list) {
                if (item.getTimestamp() == 0)
                    realm.executeTransaction((realm) -> item.setTimestamp());
            }
        }
    }

    /**
     *
     * @param soundTrack
     */
    public void saveOnHistory(SoundTrack soundTrack) {
        //publish on next item added
        searchedItemSubject.onNext(soundTrack);

        //save on db
        if (realm != null) {
            realm.executeTransaction((realm -> {
                soundTrack.setTimestamp();
                realm.copyToRealm(soundTrack);
            }));
        }
    }

    /**
     *
     */
    public void removeFromHistory(String videoId) {
        if (realm != null) {
            realm.executeTransaction(realm1 -> {
                try {
                    realm1.where(SoundTrack.class).equalTo("id.videoId", videoId).findFirst().deleteFromRealm();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
    public Observable<List<HistoryResult>> getHistory() {
        return Observable.from(realm
                .where(SoundTrack.class)
                .findAll())
                .flatMap(results -> Observable.just(results))
                .map(soundTrack -> new HistoryResult(soundTrack.getSnippet().getTitle(), soundTrack.getTimestamp()))
                .toList();
    }
    /**
     * get history with no filter
     */
    public Iterator<SoundTrack> getSoundTrackIterator() {
        return realm
                .where(SoundTrack.class)
                .findAll()
                .iterator();
    }

//    /**
//     *
//     * @return
//     */
//    public ArrayList<String> getHistoryString() {
//        ArrayList<String> list = new ArrayList<>();
//        RealmResults<SoundTrack> tmp = realm.where(SoundTrack.class)
//                .findAll();
//        for (SoundTrack obj: tmp) {
//            list.add(obj.getSnippet().getTitle());
//        }
//        return list;
//    }
}
