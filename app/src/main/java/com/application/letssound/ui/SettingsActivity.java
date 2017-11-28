package com.application.letssound.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.application.letssound.R;
import com.application.letssound.adapters.SettingsAdapter;
import com.application.letssound.helpers.RealmExportHelper;
import com.application.letssound.models.Setting;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by davide on 08/02/16.
 */
public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolbarSettingId)
    Toolbar toolbar;
    @BindView(R.id.settingsListViewId)
    ListView settingsListView;
    private ArrayList<Setting> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initActionbar();
        initView();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initActionbar() {
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        buildData();
        ArrayAdapter<Setting> settingsAdapter = new SettingsAdapter(this, -1, data);
        settingsListView.setAdapter(settingsAdapter);
        settingsListView.setOnItemClickListener((parent, view, position, id) -> {
            new RealmExportHelper().setStrategy("CSV").exportAll(getApplicationContext());
        });
    }

    private void buildData() {
        data.add(new Setting(getString(R.string.export_sound_tracks_list), "description", true));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
