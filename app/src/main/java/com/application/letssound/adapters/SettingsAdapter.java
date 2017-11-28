package com.application.letssound.adapters;

/**
 * Created by davide on 11/02/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.application.letssound.R;
import com.application.letssound.models.Setting;

import java.util.ArrayList;

/**
 * array adapter
 */
public class SettingsAdapter extends ArrayAdapter<Setting> {
    private final ArrayList<Setting> settingList;

    /**
     *
     * @param context
     * @param resource
     * @param data
     */
    public SettingsAdapter(Context context, int resource, ArrayList<Setting> data) {
        super(context, resource, data);
        this.settingList = data;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        Setting settingObj = settingList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent, false);
        }
        TextView label = convertView.findViewById(R.id.settingLabelTextId);
        TextView description = (convertView.findViewById(R.id.settingDescriptionTextId));

        label.setText(settingObj.getLabel());
        description.setVisibility(settingObj.getDescription() == null ? View.GONE : View.VISIBLE);
        description.setText(settingObj.getDescription());

        return convertView;
    }
}