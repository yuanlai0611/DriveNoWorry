package com.example.yuanyuanlai.drivenoworry.Utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XAxisValueFormatters implements IAxisValueFormatter{

    protected String[] weeks = {"Mon","Tue","Wen","Thu","Fri","Sat","Sun"};

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return weeks[(int) value];
    }


}
