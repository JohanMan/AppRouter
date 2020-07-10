package com.johan.approuter;

import android.app.Application;

import com.johan.router.AppRouter;

/**
 * Created by johan on 2020/7/9.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppRouter.init(this);
    }

}
