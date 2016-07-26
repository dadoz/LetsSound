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
    private static final String PUBLISHED_AT = "Published at - ";
    private final VolleyMediaArtManager volleyMediaArtManager;
    private List<SoundTrack> list;
    private WeakReference<OnItemClickListenerInterface> listener;
    private SoundTrackStatus soundTrackStatus;

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
        soundTrackStatus = SoundTrackStatus.getInstance();
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
        holder.url.setText("url");
        if (selectedItem.getSnippet().getThumbnails().getHigh() != null) {
            holder.mediaArtImageView.setImageUrl(selectedItem.getSnippet().getThumbnails().getHigh().getUrl(),
                    volleyMediaArtManager.getImageLoader());
        }
//        holder.songIcon.setImageBitmap();

//        DateTime publishedAt = list.get(position).getSnippet().getPublishedAt();
//        holder.url.setText(publishedAt != null ?
//                new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
//                    .format(new Date(publishedAt.getValue())) :
//                " - ");
        holder.durationTime.setText("00:00");
//        setSelectedItem(holder, position);
    }

    /**
     *
     * @param songIcon
     * @param url
     */
    private void setTrackMediaArtByUrl(ImageView songIcon, String url) {
        volleyMediaArtManager.retrieveMediaArtAsync(Uri.parse(url));
    }

    @Override
    public int getItemCount() {
        return list.size();
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
    public void removeAll() {
        list.clear();
    }

    /**
     *
     */
    public void clearAll() {
        list.clear();
    }

//    public SoundTrack getItemByPosition(int position) {
//        return list.get(position);
//    }

    /**
     *
     * @param holder
     * @param position
     */
//    private void setPlayingItem(DataObjectHolder holder, final int position) {
//        boolean isPlaying = soundTrackStatus.isPlayStatus() &&
//                soundTrackStatus.getCurrentPosition() == position;
//        holder.itemView.setBackgroundColor(isPlaying ?
//                ((Fragment) listener.get())
//                        .getActivity().getResources().getColor(R.color.md_violet_custom_2) :
//                Color.TRANSPARENT);
//    }

    /**
     *
     * @param holder
     * @param position
     */
//    private void setSelectedItem(DataObjectHolder holder, int position) {
//        boolean isSelected = (soundTrackStatus.isSelectStatus() || soundTrackStatus.isPlayStatus()) &&
//                soundTrackStatus.getCurrentPosition() == position;
//        holder.itemView.setBackgroundColor(isSelected ?
//                ((Fragment) listener.get())
//                        .getActivity().getResources().getColor(R.color.md_violet_custom_2) :
//                Color.TRANSPARENT);
//    }

    /**
     *
     */
    public interface OnItemClickListenerInterface {
        void onItemClick(int position, View v);
    }

    /**
     *
     */
    public interface OnClickListenerInterface {
        void onClick(int position, View v);
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
        private final View mainSelectedView;
        private final View shareTextView;
        private final View downloadTextView;
        private final CircularNetworkImageView mediaArtImageView;

        public DataObjectHolder(View view, WeakReference<OnItemClickListenerInterface> itemClickListenerRef) {
            super(view);
            itemView = view;
            listenerRef = itemClickListenerRef;
            title = (TextView) itemView.findViewById(R.id.titleTextId);
            url = (TextView) itemView.findViewById(R.id.urlTextId);
            durationTime = (TextView) itemView.findViewById(R.id.durationTimeTextId);
            mainSelectedView = itemView.findViewById(R.id.resultItemSelectLayoutId);
//            mainView = itemView.findViewById(R.id.resultItemLayoutId);
            shareTextView = itemView.findViewById(R.id.shareTextId);
            downloadTextView = itemView.findViewById(R.id.downloadTextId);
            mediaArtImageView = (CircularNetworkImageView) itemView.findViewById(R.id.mediaArtImageViewId);

            itemView.setOnClickListener(this);
//            shareTextView.setOnClickListener(this);
//            downloadTextView.setOnClickListener(this);
//            playButton.setOnClickListener(this);
//            pauseButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onItemClick(getAdapterPosition(), v);
        }
    }

}
