package com.example.victor.less2811.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.victor.less2811.models.Song;
import com.example.victor.less2811.views.SongsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 05.12.2016.
 */

public class SongsPresenter {
    private SongsView mView = null;

    public void onAttachView(@NonNull SongsView songsView){
        mView = songsView;
    }

    public void loadAllSongs() {
        List<Song> songs = SongLoader.getAllSongs(mView.getContext());

        new AsyncTask<Void, Void, List<Song>>() {
            @Override
            protected List<Song> doInBackground(Void... voids) {
                try{
                 return SongLoader.getAllSongs(mView.getContext());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return new ArrayList<Song>();
            }

            @Override
            protected void onPostExecute(List<Song> songList) {
                super.onPostExecute(songList);
                if(mView == null) return;
                mView.onAllSongsLoaded(songList);
            }
        }.execute();

    }




    public void onDetach() {
        mView = null;
    }


}
