package com.example.inappwindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.inappwindow.databinding.LayoutWindowBinding;

public class MainActivity extends AppCompatActivity {
    private CustomWindowWidget customWindowWidget;
    private LayoutWindowBinding binding;
    private Handler handler;
    private static  final  String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customWindowWidget = new CustomWindowWidget(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.layout_window, null, false);
            customWindowWidget.setContentView(binding.getRoot());
        }

        handler = new Handler();
        handler.postDelayed(() -> {
            View parentView = MainActivity.this.getWindow().getDecorView().getRootView();
            customWindowWidget.show(parentView, CustomWindowWidget.LAYOUT_FULL_SCREEN);
        },500);
    }
}