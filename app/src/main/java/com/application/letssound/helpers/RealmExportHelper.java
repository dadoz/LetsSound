package com.application.letssound.helpers;

import android.content.Context;
import android.os.Environment;

import com.application.letssound.models.SoundTrack;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by davide-syn on 11/20/17.
 */

public class RealmExportHelper {
    private String strategy;
    private static final String FILENAME = "/soundtrack_list.csv";
    private Context context;

    public void exportAll(Context context) {
        this.context = context;
        switch (strategy) {
            case "CSV":
                exportToCSV();
            case "HTML":
                break;
        }
    }

    private void exportToCSV() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<SoundTrack> soundTrackArray = realm.where(SoundTrack.class).findAll();
            CSVWriter csvWriter = new CSVWriter(new FileWriter(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getPath().concat(FILENAME)));
            for (SoundTrack soundTrack : soundTrackArray) {
                String[] temp = new String[3];
                temp[0] = soundTrack.getId().getVideoId();
                temp[1] = soundTrack.getSnippet().getTitle();
                temp[2] = Long.toString(soundTrack.getTimestamp());
                csvWriter.writeNext(temp);
            }
            csvWriter.close();
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    .getPath().concat(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
            realm.close();
        }
    }

    public RealmExportHelper setStrategy(String strategy) {
        this.strategy = strategy;
        return this;
    }
}
