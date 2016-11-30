package com.example.victor.less2811.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.compat.BuildConfig;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.victor.less2811.MainActivity;
import com.example.victor.less2811.R;

import java.io.IOException;

public class PlayBackService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String ACTION_PLAY = BuildConfig.APPLICATION_ID + "action.PLAY";
    public static final String TAG = PlayBackService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 101;
    private final IBinder mBinder = new PlayBackBinder();
    private MediaPlayer mMediaPlayer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getAction().equals(ACTION_PLAY)){

            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(this, getMedia());
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return Service.START_NOT_STICKY;
     //   return  Service.START_STICKY;

    }

    public static Intent newInstanse(Context context){
        return new Intent(context, PlayBackService.class);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "onUnbind()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate PlayBackService");

        //startForeground();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy PlayBackService");
    }

    public PlayBackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();

        PendingIntent pi = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("TEST Notif")
                        .setContentText("HaiMaxai")
                        .setContentIntent(pi);
        startForeground(NOTIFICATION_ID, builder.build());
    }

    public class PlayBackBinder extends Binder {
        public PlayBackService getService() {
            return PlayBackService.this;
        }

    }

    private Uri getMedia(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
                return contentUri;
            } while (cursor.moveToNext());

        }
        return null;
    };



}
