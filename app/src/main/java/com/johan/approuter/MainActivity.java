package com.johan.approuter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.johan.router.ActivityResultCallback;
import com.johan.router.AppRouter;
import com.johan.router.RouterField;
import com.johan.router.RouterPath;
import com.johan.router.annotation.AutoRouter;

@AutoRouter
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goA(View view) {
        AppRouter.with(this).putInt(RouterField.AActivity_Age, 25)
                .putString(RouterField.AActivity_Name, "Johan")
                .navigationForResult(RouterPath.AActivityPath, new ActivityResultCallback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        System.err.println("onActivityResult ------------------");
                    }
                });
    }

}
