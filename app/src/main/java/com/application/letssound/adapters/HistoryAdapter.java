package com.application.letssound.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.models.HistoryResult;

import java.util.List;

/**
 * Created by davide on 27/07/16.
 */
public class HistoryAdapter extends ArrayAdapter<HistoryResult> implements View.OnClickListener {
    private final List<HistoryResult> itemList;

    public HistoryAdapter(Context context, int resource, List<HistoryResult> list) {
        super(context, resource, list);
        itemList = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        }

        HistoryResult item = itemList.get(position);

        convertView.findViewById(R.id.historyClearButtonId).setOnClickListener(this);
        ((TextView) convertView.findViewById(R.id.historyTimestampTextId)).setText(item.getTimestamp());
        ((TextView) convertView.findViewById(R.id.historyTitleTextId))
                .setText(item.getTitle());
        return convertView;
    }

    @Override
    public void onClick(View view) {
        Log.e("hey", "clear history tag");
    }


}
