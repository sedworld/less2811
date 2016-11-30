package com.example.victor.less2811;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;

import com.example.victor.less2811.services.PlayBackService;

public class MainActivity extends AppCompatActivity {

    private PlayBackService mService;
    private boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent playBackIntent = PlayBackService.newInstanse(this);
        playBackIntent.setAction(PlayBackService.ACTION_PLAY);
        startService(playBackIntent);


//  just for example. stop service w/delay
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               stopService(PlayBackService.newInstanse(MainActivity.this));
//            }
//        }, 20000);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayBackService.PlayBackBinder binder = (PlayBackService.PlayBackBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent playBackService = PlayBackService.newInstanse(this);
        bindService(playBackService, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("main", "onDestroy");
    }
}
