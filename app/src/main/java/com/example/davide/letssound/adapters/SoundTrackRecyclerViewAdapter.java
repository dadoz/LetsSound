package com.example.davide.letssound.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davide.letssound.R;
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

    public SearchResult getItemByPosition(int position) {
        return mDataset.get(position);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private final OnItemClickListenerInterface itemClickListenerRef;
        TextView title;
        TextView durationTime;
        TextView url;
        View playButton;

        public DataObjectHolder(View itemView, OnItemClickListenerInterface itemClickListenerRef) {
            super(itemView);
            this.itemClickListenerRef = itemClickListenerRef;
            title = (TextView) itemView.findViewById(R.id.titleTextId);
            url = (TextView) itemView.findViewById(R.id.urlTextId);
            durationTime = (TextView) itemView.findViewById(R.id.durationTimeTextId);
            playButton = itemView.findViewById(R.id.playButtonId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListenerRef.onItemClick(getAdapterPosition(), v);
        }
    }

    public SoundTrackRecyclerViewAdapter(List<SearchResult> myDataset,
                                         OnItemClickListenerInterface itemClickListenerRef) {
        mDataset = myDataset;
        this.itemClickListenerRef = itemClickListenerRef;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);

        return new DataObjectHolder(view, itemClickListenerRef);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.title.setText(mDataset.get(position).getSnippet().getTitle());
        long timestamp = mDataset.get(position).getSnippet().getPublishedAt().getValue();
        holder.url.setText(PUBLISHED_AT + new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN)
                .format(new Date(timestamp)));
        holder.durationTime.setText("-");
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnClickListenerInterface) itemClickListenerRef).onClick(position, v);
            }
        });
    }

    public void addItem(SearchResult dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
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
