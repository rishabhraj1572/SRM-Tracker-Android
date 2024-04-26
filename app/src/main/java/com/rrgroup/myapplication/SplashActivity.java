package com.rrgroup.myapplication;

import static com.rrgroup.myapplication.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_splash);
        FullScreencall();
        Splash();
    }

    private void Splash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start();

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    public void start(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    public void FullScreencall() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // Use LIGHT_NAVIGATION_BAR to make icons dark
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Use LIGHT_STATUS_BAR to set status bar icons dark
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // Use IMMERSIVE_STICKY for immersive mode with bars

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }
}


