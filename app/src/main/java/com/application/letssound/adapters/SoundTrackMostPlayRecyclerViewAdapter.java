package com.application.letssound.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.helpers.VolleyMediaArtHelper;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.ui.views.CircularNetworkImageView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by davide-syn on 8/18/17.
 */

public class SoundTrackMostPlayRecyclerViewAdapter extends RecyclerView.Adapter<SoundTrackMostPlayRecyclerViewAdapter.ViewHolder> {
    private final List<SoundTrack> items;
    private final VolleyMediaArtHelper volleyMediaArtManager;

    public SoundTrackMostPlayRecyclerViewAdapter(List<SoundTrack> list, Context context) {
        this.items = list;
        volleyMediaArtManager = VolleyMediaArtHelper.getInstance(new WeakReference<>(context), null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_played_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.icon.setImageDrawable( --> from url items.get(position).getSnippet().getThumbnails().getStandard().getUrl()
        SoundTrack bindableItem = items.get(position);
        holder.title.setText(bindableItem.getSnippet().getTitle());
        if (bindableItem.getSnippet().getThumbnails().getHigh() != null) {
            holder.mediaArtImageView.setImageUrl(bindableItem.getSnippet()
                            .getThumbnails().getHigh().getUrl(), volleyMediaArtManager.getImageLoader());
        }

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        private final CircularNetworkImageView mediaArtImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.mostPlayedTitleTextViewId);
            this.mediaArtImageView = (CircularNetworkImageView) itemView.findViewById(R.id.mostPlayedIconImageViewId);
        }
    }
}
