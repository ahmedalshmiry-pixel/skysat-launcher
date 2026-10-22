package com.skysat.launcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private GridView gridView;
    private AppAdapter adapter;
    private List<AppItem> appList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnFiles = findViewById(R.id.btnFiles);
        Button btnHdmi = findViewById(R.id.btnHdmi);

        btnSettings.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_SETTINGS)));

        btnFiles.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            try {
                startActivity(intent);
            } catch (Exception ignored) {}
        });

        btnHdmi.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setClassName("com.android.tv.settings", "com.android.tv.settings.inputs.InputsActivity");
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });

        gridView = findViewById(R.id.appsGridView);
        adapter = new AppAdapter();
        gridView.setAdapter(adapter);

        loadApps();
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        appList.clear();

        for (ResolveInfo ri : activities) {
            if (ri.activityInfo.packageName.equals(getPackageName())) continue;
            appList.add(new AppItem(
                    ri.loadLabel(pm).toString(),
                    ri.activityInfo.packageName,
                    ri.loadIcon(pm)
            ));
        }

        Collections.sort(appList, (a, b) -> a.name.compareToIgnoreCase(b.name));
        adapter.notifyDataSetChanged();

          
