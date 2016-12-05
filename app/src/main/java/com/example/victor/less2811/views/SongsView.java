package com.example.victor.less2811.views;

import android.content.Context;

import com.example.victor.less2811.models.Song;

import java.util.List;

/**
 * Created by Victor on 05.12.2016.
 */

public interface SongsView {
    public Context getContext();

    public void onAllSongsLoaded(List<Song> songList);

}
