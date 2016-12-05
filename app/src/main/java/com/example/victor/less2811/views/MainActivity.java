package com.example.victor.less2811.views;

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

import com.example.victor.less2811.R;
import com.example.victor.less2811.models.Song;
import com.example.victor.less2811.presenter.SongsPresenter;
import com.example.victor.less2811.services.PlayBackService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongsView{

    private PlayBackService mService;
    private boolean mBound = false;

    public static final String TAG = "Main: ";

    private SongsPresenter mPresenter = new SongsPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter.onAttachView(this);
        mPresenter.loadAllSongs();

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
        mPresenter.onDetach();
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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onAllSongsLoaded(List<Song> songList) {
        Log.d(TAG, "songs loaded");
        Song [] array = new Song[songList.size()];
        System.out.println("Main_resu: " + Arrays.toString(songList.toArray(array)));
    }
}
