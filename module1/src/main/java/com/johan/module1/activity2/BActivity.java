package com.johan.module1.activity2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.johan.router.annotation.AutoField;
import com.johan.router.annotation.AutoRouter;

/**
 * Created by johan on 2020/7/7.
 */

@AutoRouter
public class BActivity extends AppCompatActivity {

    @AutoField
    private float money;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
