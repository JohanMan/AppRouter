package com.johan.module1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.johan.module1.R;
import com.johan.router.AppRouter;
import com.johan.router.RouterField;
import com.johan.router.RouterPath;
import com.johan.router.annotation.AutoField;
import com.johan.router.annotation.AutoRouter;
import com.johan.router.BindField;

/**
 * Created by johan on 2020/7/6.
 */

@AutoRouter
public class AActivity extends AppCompatActivity {

    @AutoField
    @BindField
    private String name;

    @AutoField
    @BindField
    private int age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        AppRouter.bind(this, savedInstanceState);
        ((TextView) findViewById(R.id.parameters_view)).setText("name : " + name + ", age : " + age);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        AppRouter.save(this, outState);
        super.onSaveInstanceState(outState);
    }

    public void goD(View view) {
        AppRouter.with(this).putString(RouterField.DActivity_Value, "this is value!!!").navigation(RouterPath.DActivityPath);
    }

}
