package com.johan.router;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

/**
 * Created by johan on 2020/7/8.
 */

public class RouterIntent {

    private Context context;
    private AppRouter appRouter;
    private Intent intent;
    private RouterLauncher launcher;

    public RouterIntent(Context context, AppRouter appRouter) {
        this.context = context;
        this.appRouter = appRouter;
        this.intent = new Intent();
        this.launcher = new RouterLauncher(context);
    }

    public RouterIntent putInt(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouterIntent putLong(String key, long value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouterIntent putFloat(String key, float value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouterIntent putDouble(String key, double value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouterIntent putBoolean(String key, boolean value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouterIntent putString(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouterIntent putParcelable(String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }

    public void navigation(String path) {
        Class target = appRouter.getRoute(path);
        intent.setClass(context, target);
        launcher.startActivity(intent);
    }

    public void navigationForResult(String path, ActivityResultCallback callback) {
        Class target = appRouter.getRoute(path);
        intent.setClass(context, target);
        launcher.startActivityForResult(intent, callback);
    }

}
