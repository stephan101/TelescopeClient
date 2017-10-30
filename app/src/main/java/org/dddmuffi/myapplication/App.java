package org.dddmuffi.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 14.01.2016.
 */
public class App extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
