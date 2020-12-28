package com.financialplugins.cryptocurrencynavigator.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.financialplugins.cryptocurrencynavigator.api.AppItem;
import com.financialplugins.cryptocurrencynavigator.api.AppsApi;
import com.financialplugins.cryptocurrencynavigator.api.AppsResponse;
import com.google.android.gms.ads.MobileAds;

import com.financialplugins.cryptocurrencynavigator.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";


    private AppsApi api;
    public static List<AppItem> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);


        prepareApi();
        api.apps().enqueue(new Callback<AppsResponse>() {
            @Override
            public void onResponse(Call<AppsResponse> call, Response<AppsResponse> response) {
                apps = new ArrayList<>();

                try {
                    for (AppItem item : response.body().items) {
                        PackageManager pm = getPackageManager();
                        PackageInfo pi = null;
                        try {
                            pi = pm.getPackageInfo(item.PackageName, 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (pi == null) {
                            apps.add(item);
                        }
                    }
                }catch (Exception e){
                    return;
                }
                for (AppItem item : apps) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = "https://play.google.com/store/apps/details?id=" + item.PackageName;
                                Document doc = Jsoup.connect(url)
                                        .userAgent("Chrome/4.0.249.0 Safari/532.5")
                                        .referrer("http://www.google.com").get();
                                String iconUrl = doc.body().getElementsByClass("T75of").attr("src").split(" ")[0];

                                item.Image = Picasso.with(getApplicationContext()).load(iconUrl).get();
                            } catch (IOException e) {
                                return;
                            }
                        }
                    });
                    t.start();
                }
            }

            @Override
            public void onFailure(Call<AppsResponse> call, Throwable t) {
                apps = new ArrayList<>();
            }
        });

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void prepareApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://45.61.138.223:8000/v1/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(AppsApi.class);
    }
}
