package com.application.letssound.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.letssound.R;

/**
 * Created by davide-syn on 8/17/17.
 */

public class EmptyDataView extends FrameLayout {
    private ProgressBar progressBar;
    private ImageView icon;
    private TextView message;

    public EmptyDataView(@NonNull Context context) {
        super(context);
        onInit();
    }

    public EmptyDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public EmptyDataView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    public void onInit() {
        inflate(getContext(), R.layout.search_list_no_item_layout, this);
        progressBar = (ProgressBar) findViewById(R.id.emptyResultProgressBarId);
        icon = (ImageView) findViewById(R.id.searchNoItemImageId);
        message = (TextView) findViewById(R.id.emptyTextLayoutId);
    }

    void setProgressBarVisible(boolean isVisible) {
        progressBar.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setIconRes(int iconRes) {
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), iconRes));
    }
}
