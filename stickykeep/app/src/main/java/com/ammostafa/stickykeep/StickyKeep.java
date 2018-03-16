package com.ammostafa.stickykeep;

/**
 * Created by Aminov on 3/1/2018.
 */

import android.app.Application;

public class StickyKeep extends Application {

    private static StickyKeep mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized StickyKeep getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}