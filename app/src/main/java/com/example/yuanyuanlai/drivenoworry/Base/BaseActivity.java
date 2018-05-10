package com.example.yuanyuanlai.drivenoworry.Base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    public final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
        setListener();
        initData();
        setData();

    }

    public void setOnMultiClickListener(View view){

      view.setOnClickListener(this);

    }


    abstract protected void onMultiClick(View view);

    @Override
    public void onClick(View v) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onMultiClick(v);
        }

    }

    protected void LogD(String words){
        Log.d(TAG,"------>"+words);
    }

    abstract protected void setListener();

    abstract protected void initData();

    abstract protected void setData();

    abstract protected void initViews();

    abstract protected void setContentView();

    public void initStatusBar(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            View view = getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            view.setSystemUiVisibility(options);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

    }

}
