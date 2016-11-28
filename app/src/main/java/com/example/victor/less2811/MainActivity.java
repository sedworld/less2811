package com.example.victor.less2811;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;

import com.example.victor.less2811.services.PlayBackService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent playBackIntent = PlayBackService.newInstanse(this);
        startService(playBackIntent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               stopService(PlayBackService.newInstanse(MainActivity.this));
            }
        }, 5000);
    }
}
