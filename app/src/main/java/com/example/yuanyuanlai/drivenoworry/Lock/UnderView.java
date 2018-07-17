package com.example.yuanyuanlai.drivenoworry.Lock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.yuanyuanlai.drivenoworry.Utils.ScreenUtil;

public class UnderView extends FrameLayout {

    private float mStartX;
    private View mMoveView;
    private int mWidth;
    private OnSlideFinishListener mOnSlideFinishListener;

    public UnderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mWidth = ScreenUtil.getScreenWidth(context);

    }

    public void setSlideListener(OnSlideFinishListener onSlideFinishListener){
        mOnSlideFinishListener = onSlideFinishListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float nx = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = nx;
                onAnimationEnd();
            case MotionEvent.ACTION_MOVE:
                handleMoveView(nx);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doTriggerEvent(nx);
                break;
        }
        return true;
    }

    private void handleMoveView(float x) {
        float movex = x - mStartX;
        if (movex < 0)
            movex = 0;
        mMoveView.setTranslationX(movex);

        float mWidthFloat = (float) mWidth;//屏幕显示宽度
        if(getBackground()!=null){
            getBackground().setAlpha((int) ((mWidthFloat - mMoveView.getTranslationX()) / mWidthFloat * 200));//初始透明度的值为200
        }
    }

    private void doTriggerEvent(float x) {
        float movex = x - mStartX;
        if (movex > (mWidth * 0.4)) {
            moveMoveView(mWidth-mMoveView.getLeft(),true);//自动移动到屏幕右边界之外，并finish掉

        } else {
            moveMoveView(-mMoveView.getLeft(),false);//自动移动回初始位置，重新覆盖
        }
    }

    private void moveMoveView(float to,boolean exit){

        ObjectAnimator animator = ObjectAnimator.ofFloat(mMoveView, "translationX", to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(getBackground()!=null){
                    getBackground().setAlpha((int) (((float) mWidth - mMoveView.getTranslationX()) / (float) mWidth * 200));
                }
            }
        });//随移动动画更新背景透明度
        animator.setDuration(250).start();

        if(exit){
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOnSlideFinishListener.onSlideFinish();
                }
            });
        }//监听动画结束，利用Handler通知Activity退出
    }

    public void setMoveView(View moveView){
        mMoveView = moveView;
    }

    public interface OnSlideFinishListener{
        void onSlideFinish();
    }



}
