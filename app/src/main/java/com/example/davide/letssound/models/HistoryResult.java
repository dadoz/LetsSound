package com.example.davide.letssound.models;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by davide on 27/07/16.
 */
public class HistoryResult {
    private final long timestamp;
    private final String title;

    public HistoryResult(String title, long timestamp) {
        this.title = title;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     *
     * @return
     */
    public String getTimestamp() {
        Calendar cal = Calendar.getInstance(Locale.ITALIAN);
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd-MM-yy", cal).toString();
    }


}
