package com.application.letssound.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.adapters.interfaces.LatestPlayAdapterCallbacks;
import com.application.letssound.helpers.VolleyMediaArtHelper;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.ui.views.CircularNetworkImageView;
import com.application.letssound.utils.Utils;
import com.google.common.collect.Lists;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.application.letssound.adapters.SoundTrackLatestPlayRecyclerViewAdapter.ViewTypeEnum.LATEST_PLAY_ITEM;
import static com.application.letssound.adapters.SoundTrackLatestPlayRecyclerViewAdapter.ViewTypeEnum.TIMESTAMP_ITEM;

/**
 * Created by davide on 30/11/15.
 */
public class SoundTrackLatestPlayRecyclerViewAdapter extends RecyclerView
            .Adapter<RecyclerView.ViewHolder> {
    private final VolleyMediaArtHelper volleyMediaArtManager;
    private List<SoundTrack> list;
    private WeakReference<OnItemClickListenerInterface> listener;
    private WeakReference<LatestPlayAdapterCallbacks> listenerCallbacks;

    enum ViewTypeEnum {LATEST_PLAY_ITEM, TIMESTAMP_ITEM}
    /**
     *
     * @param dataset
     * @param itemClickListenerRef
     */
    public SoundTrackLatestPlayRecyclerViewAdapter(List<SoundTrack> dataset,
                                                OnItemClickListenerInterface itemClickListenerRef,
                                                LatestPlayAdapterCallbacks lst2,
                                                Context context) {
        list = Lists.reverse(dataset); //revers on realm ???
        listener = new WeakReference<>(itemClickListenerRef);
        listenerCallbacks = new WeakReference<>(lst2);
        volleyMediaArtManager = VolleyMediaArtHelper.getInstance(new WeakReference<Context>(context), null);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ||
                !Utils.isSameDay(list.get(position -1).getTimestamp(), list.get(position).getTimestamp()) ?
                TIMESTAMP_ITEM.ordinal() : LATEST_PLAY_ITEM.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TIMESTAMP_ITEM.ordinal()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.latest_play_timestamp_item, parent, false);
            return new TimestampViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_play_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        SoundTrack item = list.get(position);
        //history item
        if (viewHolder instanceof DataObjectHolder) {
            DataObjectHolder holder = ((DataObjectHolder) viewHolder);
            holder.title.setText(item.getSnippet().getTitle());
            holder.url.setText("Author: " + item.getSnippet().getChannelId());
            holder.durationTime.setText("00:00");
            if (item.getSnippet().getThumbnails().getHigh().getUrl() != null) {
                holder.mediaArtImageView.setImageUrl(item.getSnippet()
                                .getThumbnails().getHigh().getUrl(),
                        volleyMediaArtManager.getImageLoader());
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null && listener.get() != null)
                    listener.get().onItemClick(list.get(position));
            });

            holder.clearSoundTrackButton.setOnClickListener(v -> deleteItem(position));
        }
        //timestamp
        if (viewHolder instanceof TimestampViewHolder) {
            TimestampViewHolder holder = ((TimestampViewHolder) viewHolder);

            holder.timestamp.setText(new SimpleDateFormat("dd MMMM", Locale.ITALY).format(new Date(item.getTimestamp())));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void deleteItem(int position) {
        String videoId = list.get(position).getId().getVideoId();
        list.remove(position);
        notifyDataSetChanged();
        listenerCallbacks.get().onItemDismissCallback(videoId);
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
        private final ImageView clearSoundTrackButton;

        /**
         *
         * @param view
         */
        public DataObjectHolder(View view) {
            super(view);
            itemView = view;
            title = itemView.findViewById(R.id.titleTextId);
            url = itemView.findViewById(R.id.urlTextId);
            durationTime = itemView.findViewById(R.id.durationTimeTextId);
            mediaArtImageView = itemView.findViewById(R.id.mediaArtImageViewId);
            clearSoundTrackButton = itemView.findViewById(R.id.clearSoundTrackButtonId);
        }
    }

    /**
     *
     */
    public interface OnItemClickListenerInterface {
        void onItemClick(SoundTrack soundTrack);
    }

    /**
     *
     */
    class TimestampViewHolder extends DataObjectHolder {
        private final TextView timestamp;
        private final View itemView;

        public TimestampViewHolder(View view) {
            super(view);
            itemView = view;
            timestamp = (TextView) itemView.findViewById(R.id.timestampTextId);

        }
    }

}

