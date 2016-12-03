package com.choosemuse.example.libmuse.Listeners;

import com.choosemuse.example.libmuse.MainActivity;
import com.choosemuse.libmuse.MuseListener;

import java.lang.ref.WeakReference;

/**
 * Created by Macbook on 8/18/16.
 */
public class MuseL extends MuseListener {
    final WeakReference<MainActivity> activityRef;

    public MuseL(final WeakReference<MainActivity> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void museListChanged() {
        activityRef.get().museListChanged();
    }
}

