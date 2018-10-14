package com.choosemuse.example.libmuse.Listeners;

import com.choosemuse.example.libmuse.MainActivity;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseConnectionListener;
import com.choosemuse.libmuse.MuseConnectionPacket;

import java.lang.ref.WeakReference;

/**
 * Created by Macbook on 8/18/16.
 */

public class ConnectionListener extends MuseConnectionListener {
    final WeakReference<MainActivity> activityRef;

    public ConnectionListener(final WeakReference<MainActivity> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void receiveMuseConnectionPacket(final MuseConnectionPacket p, final Muse muse) {
        activityRef.get().receiveMuseConnectionPacket(p, muse);
    }
}