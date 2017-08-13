package com.application.letssound.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.managers.VolleyMediaArtManager;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.views.CircularNetworkImageView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by davide on 30/11/15.
 */
public class SoundTrackLatestPlayRecyclerViewAdapter extends RecyclerView
            .Adapter<SoundTrackLatestPlayRecyclerViewAdapter
            .DataObjectHolder> implements ItemTouchHelperAdapter {
    private final VolleyMediaArtManager volleyMediaArtManager;
    private List<SoundTrack> list;
    private WeakReference<OnItemClickListenerInterface> listener;
    private WeakReference<LatestPlayAdapterCallbacks> listenerCallbacks;

    /**
     *
     * @param dataset
     * @param itemClickListenerRef
     */
    public SoundTrackLatestPlayRecyclerViewAdapter(List<SoundTrack> dataset,
                                                OnItemClickListenerInterface itemClickListenerRef,
                                                LatestPlayAdapterCallbacks lst2,
                                                Context context) {
        list = dataset;
        listener = new WeakReference<>(itemClickListenerRef);
        listenerCallbacks = new WeakReference<>(lst2);
        volleyMediaArtManager = VolleyMediaArtManager.getInstance(new WeakReference<Context>(context), null);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_play_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        SoundTrack selectedItem = list.get(position);
        holder.title.setText(selectedItem.getSnippet().getTitle());
        holder.url.setText("Author: " + selectedItem.getSnippet().getChannelId());
        holder.durationTime.setText("00:00");

        if (selectedItem.getSnippet().getThumbnails().getHigh() != null) {
            holder.mediaArtImageView.setImageUrl(selectedItem.getSnippet()
                    .getThumbnails().getHigh().getUrl(),
                    volleyMediaArtManager.getImageLoader());
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && listener.get() != null)
                listener.get().onItemClick(list.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public void onItemDismiss(int position) {
        String videoId = list.get(position).getId().getVideoId();
        list.remove(position);
        notifyDataSetChanged();
        listenerCallbacks.get().onItemDismissCallback(videoId);
    }


    /**
     *
     */
    public interface OnItemClickListenerInterface {
        void onItemClick(SoundTrack soundTrack);
    }

    /**
     * viewHolder
     */
    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final TextView title;
        private final TextView durationTime;
        private final TextView url;
        private final CircularNetworkImageView mediaArtImageView;

        /**
         *
         * @param view
         */
        public DataObjectHolder(View view) {
            super(view);
            itemView = view;
            title = (TextView) itemView.findViewById(R.id.titleTextId);
            url = (TextView) itemView.findViewById(R.id.urlTextId);
            durationTime = (TextView) itemView.findViewById(R.id.durationTimeTextId);
            mediaArtImageView = (CircularNetworkImageView) itemView.findViewById(R.id.mediaArtImageViewId);
        }
    }

}

