package com.example.yuanyuanlai.drivenoworry.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yuanyuanlai.drivenoworry.BackgroundService.PollingService;
import com.example.yuanyuanlai.drivenoworry.Base.BaseActivity;
import com.example.yuanyuanlai.drivenoworry.Bean.Tab;
import com.example.yuanyuanlai.drivenoworry.Fragment.HomeFragment;
import com.example.yuanyuanlai.drivenoworry.Fragment.MyFragment;
import com.example.yuanyuanlai.drivenoworry.Fragment.StaticsFragment;
import com.example.yuanyuanlai.drivenoworry.Lock.LockService;
import com.example.yuanyuanlai.drivenoworry.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity{

    private List<Tab> mTabs;
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(this,LockService.class);
        startService(intent);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void initViews() {

        initStatusBar();
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mLayoutInflater = getLayoutInflater();
        mTabs = new ArrayList<>();
        mTabs.add(new Tab(R.string.tab_home,R.drawable.home_selector, HomeFragment.class));
        mTabs.add(new Tab(R.string.tab_statics,R.drawable.statics_selector,StaticsFragment.class));
        mTabs.add(new Tab(R.string.tab_my,R.drawable.my_selector,MyFragment.class));
        mTabHost.setup(this,getSupportFragmentManager(),R.id.container);
        for (Tab tab:mTabs){
            mTabHost.addTab(mTabHost.newTabSpec(getString(tab.getStringId())).setIndicator(buildIndicator(tab)),tab.getFragment(),null);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setCurrentTab(0);

    }


    private View buildIndicator(Tab tab) {

        View view = mLayoutInflater.inflate(R.layout.indicator, null);
        TextView textViewTitle = (TextView) view.findViewById(R.id.indicator_text);
        ImageView imageViewIcon = (ImageView) view.findViewById(R.id.indicator_image);
        imageViewIcon.setImageResource(tab.getImageId());
        textViewTitle.setText(tab.getStringId());
        return view;

    }

    @Override
    public void onMultiClick(View view) {

    }

    @Override
    protected void setContentView() {

        setContentView(R.layout.activity_main);

    }
}