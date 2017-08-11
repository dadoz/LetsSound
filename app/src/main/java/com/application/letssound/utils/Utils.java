package com.application.letssound.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.application.letssound.BuildConfig;
import com.application.letssound.services.MediaService;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by davide on 14/07/16.
 */
public class Utils {
    /**
     *
     * @param message
     * @param color
     */
    public static void createSnackBarByBackgroundColor(View view, String message, int color) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }

    /**
     *
     * @param videoUrl
     * @param thumbnailUrl
     * @param title
     * @return
     */
    public static Bundle buildPlayBundle(String videoUrl, String thumbnailUrl, String title) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MediaService.PARAM_TRACK_URI, videoUrl == null ? null : Uri.parse(videoUrl));
        bundle.putParcelable(MediaService.PARAM_TRACK_THUMBNAIL, Uri.parse(thumbnailUrl));
        bundle.putString(MediaService.PARAM_TRACK_TITLE, title);
        return bundle;
    }
    public static Bundle buildFilePlayBundle(String videoUrl, String thumbnailUrl, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(MediaService.PARAM_TRACK_URI, videoUrl);
        bundle.putParcelable(MediaService.PARAM_TRACK_THUMBNAIL, Uri.parse(thumbnailUrl));
        bundle.putString(MediaService.PARAM_TRACK_TITLE, title);
        return bundle;
    }

    /**
     *
     * @param currentPosition
     * @param duration
     * @return
     */
    public static int getCurrentPosition(int currentPosition, int duration) {
        Double percentage = ((double) currentPosition / duration) * 100;
        return percentage.intValue();
    }

    /**
     * get caligraphy spannable
     * @param title
     * @param assets
     * @return
     */
    public static String getCalligraphySpannable(String title, AssetManager assets) {
        if (assets != null) {
            Typeface typeFace = TypefaceUtils.load(assets, BuildConfig.FONT_PATH);
            if (typeFace != null) {
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(title);
                stringBuilder.setSpan(new CalligraphyTypefaceSpan(typeFace), 0, title.length(), 0);
                return stringBuilder.toString();
            }
        }
        return title;
    }
}
