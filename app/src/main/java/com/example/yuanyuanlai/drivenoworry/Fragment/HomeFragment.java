package com.example.yuanyuanlai.drivenoworry.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.yuanyuanlai.drivenoworry.Base.BaseFragment;
import com.example.yuanyuanlai.drivenoworry.R;
import com.example.yuanyuanlai.drivenoworry.Utils.ScreenUtil;


public class HomeFragment extends BaseFragment {

    private ViewPager viewPagerHomeFragment;
    private FrameLayout frameLayoutViewPagerContainer;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    protected void initData() {

        viewPagerHomeFragment.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {

                View view = getLayoutInflater().inflate(R.layout.instance_result_card
                        ,container,false);
                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View)object);
            }


        });

        viewPagerHomeFragment.setOffscreenPageLimit(3);
        viewPagerHomeFragment.setPageMargin(ScreenUtil.dp2px(15,getContext()));

        frameLayoutViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPagerHomeFragment.dispatchTouchEvent(event);
            }
        });



    }

    @Override
    protected void setData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView(View view) {

        viewPagerHomeFragment = (ViewPager)view.findViewById(R.id.fragment_home_viewPager);
        frameLayoutViewPagerContainer = (FrameLayout)view.findViewById(R.id.viewPager_container);

    }
}
