package com.example.yuanyuanlai.drivenoworry.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtil {

    public static int dp2px(float dpVal, Context mContext) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, mContext.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService( Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }



}
