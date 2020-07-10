package com.johan.module2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.johan.router.AppRouter;
import com.johan.router.annotation.AutoField;
import com.johan.router.annotation.AutoRouter;
import com.johan.router.BindField;

/**
 * Created by johan on 2020/7/9.
 */

@AutoRouter
public class DActivity extends AppCompatActivity {

    @AutoField
    @BindField
    private String value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        AppRouter.bind(this, savedInstanceState);
        ((TextView) findViewById(R.id.parameters_view)).setText("value : " + value);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        AppRouter.save(this, outState);
        super.onSaveInstanceState(outState);
    }

}
