package com.example.davide.letssound.adapters;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davide.letssound.R;
import com.example.davide.letssound.singleton.SoundTrackStatus;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;

import java.text.SimpleDateFormat;
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
    private List<SearchResult> mDataset;
    private OnItemClickListenerInterface itemClickListenerRef;
    private SoundTrackStatus soundTrackStatus;
    public SearchResult getItemByPosition(int position) {
        return mDataset.get(position);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        //TODO weakRef please
        private final OnItemClickListenerInterface itemClickListenerRef;
        private final View itemView;
        private final TextView title;
        private final TextView durationTime;
        private final TextView url;
        private final View playButton;
        private final View pauseButton;
        private final View mainSelectedView;
        private final View shareTextView;
        private final View downloadTextView;

        public DataObjectHolder(View itemView, OnItemClickListenerInterface itemClickListenerRef) {
            super(itemView);
            this.itemClickListenerRef = itemClickListenerRef;
            title = (TextView) itemView.findViewById(R.id.titleTextId);
            url = (TextView) itemView.findViewById(R.id.urlTextId);
            durationTime = (TextView) itemView.findViewById(R.id.durationTimeTextId);
            playButton = itemView.findViewById(R.id.playButtonId);
            pauseButton = itemView.findViewById(R.id.pauseButtonId);
            mainSelectedView = itemView.findViewById(R.id.resultItemSelectLayoutId);
            shareTextView = itemView.findViewById(R.id.shareTextId);
            downloadTextView = itemView.findViewById(R.id.downloadTextId);
            this.itemView = itemView;

            itemView.setOnClickListener(this);
            shareTextView.setOnClickListener(this);
            downloadTextView.setOnClickListener(this);
            playButton.setOnClickListener(this);
            pauseButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playButtonId:
                case R.id.downloadTextId:
                case R.id.shareTextId:
                case R.id.pauseButtonId:
                    ((OnClickListenerInterface) itemClickListenerRef).onClick(getAdapterPosition(), v);
                    break;
                default:
                    itemClickListenerRef.onItemClick(getAdapterPosition(), v);

            }
        }
    }

    public SoundTrackRecyclerViewAdapter(List<SearchResult> dataset,
                                         OnItemClickListenerInterface itemClickListenerRef) {
        mDataset = dataset;
        this.itemClickListenerRef = itemClickListenerRef;
        this.soundTrackStatus = SoundTrackStatus.getInstance();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);

        return new DataObjectHolder(view, itemClickListenerRef);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        //thumbnail Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault().getUrl();
        holder.title.setText(mDataset.get(position).getSnippet().getTitle());
        DateTime publishedAt = mDataset.get(position).getSnippet().getPublishedAt();
        holder.url.setText(publishedAt != null ?
                PUBLISHED_AT + new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
                        .format(new Date(publishedAt.getValue())) :
                " - ");
        holder.durationTime.setText("00:00");
        //TODO responsibility pattern - REMEBER this is viewHolder pos :()
        setSelectedItem(holder, position);
        if (!soundTrackStatus.isSelectStatus()) {
            setPlayingItem(holder, position);
        }
    }

    /**
     *
     * @param holder
     * @param position
     */
    private void setPlayingItem(DataObjectHolder holder, final int position) {
        boolean isPlaying = soundTrackStatus.isPlayStatus() &&
                soundTrackStatus.getCurrentPosition() == position;
        holder.itemView.setBackgroundColor(isPlaying ?
                ((Fragment) itemClickListenerRef)
                        .getActivity().getResources().getColor(R.color.md_blue_grey_800) :
                Color.TRANSPARENT);

        holder.playButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        holder.pauseButton.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
    }

    /**
     *
     * @param holder
     * @param position
     */
    private void setSelectedItem(DataObjectHolder holder, int position) {
        boolean isSelected = soundTrackStatus.isSelectStatus() &&
                soundTrackStatus.getCurrentPosition() == position;
        holder.mainSelectedView.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.title.setTextColor(((Fragment) itemClickListenerRef)
                .getActivity().getResources().getColor(isSelected ?
                        R.color.md_amber_400 :
                        R.color.md_blue_grey_400));
        holder.itemView.setBackgroundColor(isSelected ?
                ((Fragment) itemClickListenerRef)
                        .getActivity().getResources().getColor(R.color.md_blue_grey_800) :
                Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListenerInterface {
        void onItemClick(int position, View v);
    }
    public interface OnClickListenerInterface {
        void onClick(int position, View v);
    }

}
