package com.example.davide.letssound.adapters;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davide.letssound.R;
import com.example.davide.letssound.helpers.SoundTrackStatus;
import com.example.davide.letssound.models.SoundTrack;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by davide on 30/11/15.
 */
public class SoundTrackRecyclerViewAdapter extends RecyclerView
            .Adapter<SoundTrackRecyclerViewAdapter
            .DataObjectHolder> {
    private static final String PUBLISHED_AT = "Published at - ";
    private List<SoundTrack> list;
    private WeakReference<OnItemClickListenerInterface> listener;
    private SoundTrackStatus soundTrackStatus;

    /**
     *
     * @param dataset
     * @param itemClickListenerRef
     */
    public SoundTrackRecyclerViewAdapter(List<SoundTrack> dataset,
                                         WeakReference<OnItemClickListenerInterface> itemClickListenerRef) {
        list = dataset;
        listener = itemClickListenerRef;
        soundTrackStatus = SoundTrackStatus.getInstance();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new DataObjectHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.title.setText(list.get(position).getSnippet().getTitle());
        holder.url.setText("url");
//        DateTime publishedAt = list.get(position).getSnippet().getPublishedAt();
//        holder.url.setText(publishedAt != null ?
//                new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
//                    .format(new Date(publishedAt.getValue())) :
//                " - ");
        holder.durationTime.setText("00:00");
//        setSelectedItem(holder, position);
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
        //TODO weakRef please
        private final WeakReference<OnItemClickListenerInterface> listenerRef;
        private final View itemView;
        private final TextView title;
        private final TextView durationTime;
        private final TextView url;
        //        private final View playButton;
//        private final View pauseButton;
        private final View mainSelectedView;
        private final View shareTextView;
        private final View downloadTextView;
        private final View mainView;
        private final View songIcon;

        public DataObjectHolder(View itemView, WeakReference<OnItemClickListenerInterface> itemClickListenerRef) {
            super(itemView);
            this.listenerRef = itemClickListenerRef;
            title = (TextView) itemView.findViewById(R.id.titleTextId);
            url = (TextView) itemView.findViewById(R.id.urlTextId);
            durationTime = (TextView) itemView.findViewById(R.id.durationTimeTextId);
//            playButton = itemView.findViewById(R.id.playButtonId);
//            pauseButton = itemView.findViewById(R.id.pauseButtonId);
            mainSelectedView = itemView.findViewById(R.id.resultItemSelectLayoutId);
            mainView = itemView.findViewById(R.id.resultItemLayoutId);
            shareTextView = itemView.findViewById(R.id.shareTextId);
            downloadTextView = itemView.findViewById(R.id.downloadTextId);
            songIcon = itemView.findViewById(R.id.songIconId);
            this.itemView = itemView;

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
