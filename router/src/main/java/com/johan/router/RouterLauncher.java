package com.johan.router;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by johan on 2020/7/9.
 */

public class RouterLauncher {

    private static final String ROUTER_FRAGMENT_TAG = "RouterFragment";

    private Context context;

    public RouterLauncher(Context context) {
        this.context = context;
    }

    public void startActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
        if (context instanceof FragmentActivity) {
            RouterFragment routerFragment = getRouterFragment((FragmentActivity) context);
            routerFragment.startActivityForResult(intent, callback);
        } else {
            throw new RuntimeException("Only FragmentActivity can execute RouterLauncher.startActivityForResult() !!!");
        }
    }

    private RouterFragment getRouterFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(ROUTER_FRAGMENT_TAG);
        RouterFragment routerFragment = null;
        if (fragment != null && fragment instanceof RouterFragment) {
            routerFragment = (RouterFragment) fragment;
        }
        if (routerFragment == null) {
            routerFragment = RouterFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(routerFragment, ROUTER_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

}
