package com.application.letssound.adapters.observer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.application.letssound.adapters.SoundTrackRecyclerViewAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by davide on 27/07/16.
 */
public class SoundTrackSearchObserver extends RecyclerView.AdapterDataObserver {
    private final WeakReference<SoundTrackRecyclerViewAdapter> adapterRef;
    private final View emptyView;

    public SoundTrackSearchObserver(WeakReference<SoundTrackRecyclerViewAdapter> adapter, View view) {
        adapterRef = adapter;
        emptyView = view;
    }

    @Override
    public void onChanged() {
        if (adapterRef.get() != null) {
            boolean isEmpty = adapterRef.get().getItemCount() == 0;
            emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
    }
}
