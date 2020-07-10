package com.johan.router;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by johan on 2020/7/9.
 */

public class RouterFragment extends Fragment {

    public static RouterFragment newInstance() {
        return new RouterFragment();
    }

    private SparseArray<ActivityResultCallback> callbackArray = new SparseArray<>();
    private AtomicInteger nextInt = new AtomicInteger(1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
        int requestCode = makeRequestCode();
        callbackArray.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    private int makeRequestCode() {
        return nextInt.getAndIncrement();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityResultCallback callback = callbackArray.get(requestCode);
        if (callback == null) return;
        callbackArray.remove(requestCode);
        callback.onActivityResult(resultCode, data);
    }

}
