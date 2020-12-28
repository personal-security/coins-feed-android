package com.financialplugins.cryptocurrencynavigator.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.financialplugins.cryptocurrencynavigator.R;
import com.financialplugins.cryptocurrencynavigator.adapters.AppsAdapter;

import static com.financialplugins.cryptocurrencynavigator.activities.SplashScreenActivity.apps;


public class MoreAppsActivity extends AppCompatActivity {
    Context context;

    private RecyclerView rvApps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        context = this;

        rvApps = findViewById(R.id.rvApps);


        rvApps.setLayoutManager(new LinearLayoutManager(context));
        rvApps.setAdapter(new AppsAdapter(context, apps));
    }
}
