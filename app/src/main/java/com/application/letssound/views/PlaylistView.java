package com.application.letssound.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.letssound.R;

/**
 * Created by davide-syn on 8/17/17.
 */
public class PlaylistView extends RelativeLayout {
    private ImageView playListImageView;
    private TextView creatorPlayListText;
    private TextView titlePlayListText;

    public PlaylistView(Context context) {
        super(context);
        onInit();
    }

    public PlaylistView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public PlaylistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    void onInit() {
        inflate(getContext(), R.layout.playlist_item, this);
        playListImageView = (ImageView) findViewById(R.id.playListImageViewId);
        titlePlayListText = (TextView) findViewById(R.id.titlePlayListTextId);
        creatorPlayListText = (TextView) findViewById(R.id.creatorPlayListTextId);
    }

}
