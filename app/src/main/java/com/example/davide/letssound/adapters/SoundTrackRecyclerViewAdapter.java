package com.example.davide.letssound.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.example.davide.letssound.R;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.example.davide.letssound.managers.VolleyMediaArtManager;
import com.example.davide.letssound.models.SoundTrack;
import com.example.davide.letssound.views.CircularNetworkImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davide on 30/11/15.
 */
public class SoundTrackRecyclerViewAdapter extends RecyclerView
            .Adapter<SoundTrackRecyclerViewAdapter
            .DataObjectHolder> {
    private final VolleyMediaArtManager volleyMediaArtManager;
    private List<SoundTrack> list;
    private WeakReference<OnItemClickListenerInterface> listener;

    /**
     *
     * @param dataset
     * @param itemClickListenerRef
     */
    public SoundTrackRecyclerViewAdapter(List<SoundTrack> dataset,
                                         WeakReference<OnItemClickListenerInterface> itemClickListenerRef,
                                         WeakReference<Context> ctx) {
        list = dataset;
        listener = itemClickListenerRef;
        volleyMediaArtManager = VolleyMediaArtManager.getInstance(ctx, null);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new DataObjectHolder(view, listener);
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
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    /**
     *
     * @param lst
     */
    public void addAll(ArrayList<SoundTrack> lst) {
        this.list = lst;
    }

    /**
     *
     */
    public void clearAll() {
        list.clear();
    }

    /**
     *
     */
    public interface OnItemClickListenerInterface {
        void onItemClick(int position, View v);
    }

    /**
     * viewHolder
     */
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final WeakReference<OnItemClickListenerInterface> listenerRef;
        private final View itemView;
        private final TextView title;
        private final TextView durationTime;
        private final TextView url;
        private final CircularNetworkImageView mediaArtImageView;

        /**
         *
         * @param view
         * @param itemClickListenerRef
         */
        public DataObjectHolder(View view,
                                WeakReference<OnItemClickListenerInterface> itemClickListenerRef) {
            super(view);
            itemView = view;
            listenerRef = itemClickListenerRef;
            title = (TextView) itemView.findViewById(R.id.titleTextId);
            url = (TextView) itemView.findViewById(R.id.urlTextId);
            durationTime = (TextView) itemView.findViewById(R.id.durationTimeTextId);
            mediaArtImageView = (CircularNetworkImageView) itemView.findViewById(R.id.mediaArtImageViewId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onItemClick(getAdapterPosition(), v);
        }
    }

}
