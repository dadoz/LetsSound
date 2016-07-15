package com.example.davide.letssound.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

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

}
