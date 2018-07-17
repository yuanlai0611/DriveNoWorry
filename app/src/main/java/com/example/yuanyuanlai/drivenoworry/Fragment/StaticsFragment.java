package com.example.yuanyuanlai.drivenoworry.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.yuanyuanlai.drivenoworry.Base.BaseFragment;
import com.example.yuanyuanlai.drivenoworry.R;
import com.example.yuanyuanlai.drivenoworry.Utils.XAxisValueFormatters;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StaticsFragment extends BaseFragment implements View.OnClickListener{

    private LineChart mChart, bloodLineChart;
    private XAxis xAxis, bloodXAxis;
    private YAxis yAxis, bloodYAxis;
    private ViewPager mViewPager;
    private LayoutInflater layoutInflater;
    private TextView staticTitleTextView, highNumberTextView, lowNumberTextView, updateTimeTextView, tooHighPercentTextView, tooLowPercentTextView, tendencyTextView, qualifiedTextView;
    private LinearLayout linearLayoutDecideBySpan;
    private boolean isViewCreated;
    private PopupWindow popupWindow;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_statics,container,false);
    }

    @Override
    protected void initData() {

        mViewPager.setAdapter(new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {

                View view = layoutInflater.inflate(R.layout.statics_result_card,container,false);

                staticTitleTextView = (TextView)view.findViewById(R.id.statics_title);
                highNumberTextView = (TextView)view.findViewById(R.id.number);
                lowNumberTextView = (TextView)view.findViewById(R.id.low_number);
                updateTimeTextView = (TextView)view.findViewById(R.id.update_time);
                tooHighPercentTextView = (TextView)view.findViewById(R.id.high_side);
                tooLowPercentTextView = (TextView)view.findViewById(R.id.low_side);
                tendencyTextView = (TextView)view.findViewById(R.id.tendency);
                qualifiedTextView = (TextView)view.findViewById(R.id.percent_of_pass);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm");
                updateTimeTextView.setText(simpleDateFormat.format(new Date()));

                LogD("调用了instantiateItem()"+position);

                if (position==0){

                  staticTitleTextView.setText("血压统计");
                  highNumberTextView.setText("111.00");
                  lowNumberTextView.setText("71.10");
                  tooHighPercentTextView.setText("21%");
                  tooLowPercentTextView.setText("11");


                }else if (position==1){

                  staticTitleTextView.setText("心率统计");
                  lowNumberTextView.setVisibility(View.GONE);

                }

                container.addView(view);
                return view;

            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

                LogD("调用了destroyItem（）" + "");
                container.removeView((View)object);

            }
        });

        setData(7,60);
        setBloodData(7, 60);

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void setListener() {

       linearLayoutDecideBySpan.setOnClickListener(this);

    }

    @Override
    protected void initView(View view) {

        mChart = (LineChart)view.findViewById(R.id.line_chart);
        bloodLineChart = (LineChart)view.findViewById(R.id.blood_line_chart);
        mViewPager = (ViewPager)view.findViewById(R.id.statics_viewPager);
        linearLayoutDecideBySpan = (LinearLayout)view.findViewById(R.id.linearLayout_decide_by_span);
        popupWindow = new PopupWindow(getActivity());
        initHeartChart();
        initBloodChart();
        isViewCreated = true;

    }

    private void showPop(){

        // 设置布局文件
        popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu, null));
        // 设置pop透明效果
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // 设置pop出入动画
        popupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        popupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        popupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        popupWindow.setOutsideTouchable(true);

        popupWindow.setElevation(5f);

        // 相对于 + 号正下面，同时可以设置偏移量
        popupWindow.showAtLocation(linearLayoutDecideBySpan,0,Gravity.BOTTOM|Gravity.END,0);


    }

    private void initHeartChart(){

        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.animateY(1000);

        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextColor(getResources().getColor(R.color.colorxAxisLineText));
        xAxis.setAxisLineColor(getResources().getColor(R.color.colorxAxisLine));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(11f);
        xAxis.setValueFormatter(new XAxisValueFormatters());

        yAxis = mChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setSpaceTop(30f);

    }

    private void initBloodChart(){

        bloodLineChart.getDescription().setEnabled(false);
        bloodLineChart.setTouchEnabled(false);
        bloodLineChart.setDragEnabled(false);
        bloodLineChart.setScaleEnabled(false);
        bloodLineChart.getLegend().setEnabled(false);
        bloodLineChart.getAxisRight().setEnabled(false);
        bloodLineChart.getAxisRight().setDrawGridLines(false);
        bloodLineChart.animateY(1000);

        bloodXAxis = bloodLineChart.getXAxis();
        bloodXAxis.setPosition(XAxis.XAxisPosition.TOP);
        bloodXAxis.setTextColor(getResources().getColor(R.color.colorxAxisLineText));
        bloodXAxis.setAxisLineColor(getResources().getColor(R.color.colorxAxisLine));
        bloodXAxis.setDrawAxisLine(false);
        bloodXAxis.setDrawGridLines(true);
        bloodXAxis.setTextSize(11f);
        bloodXAxis.setValueFormatter(new XAxisValueFormatters());

        bloodYAxis = bloodLineChart.getAxisLeft();
        bloodYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        bloodYAxis.setDrawGridLines(false);
        bloodYAxis.setDrawLabels(false);
        bloodYAxis.setDrawAxisLine(false);
        bloodYAxis.setSpaceTop(30f);

    }

    private void setData(int count, float range) {

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 20;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type

            set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setCubicIntensity(0.2f);
            set1.setDrawCircles(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setCircleHoleRadius(2f);
            set1.setCircleColor(getResources().getColor(R.color.colorStaticsBackground));
            set1.setColor(getResources().getColor(R.color.colorStaticsBackground));
            set1.setFillColor(getResources().getColor(R.color.colorStaticsBackground));
            set1.setDrawFilled(true);
            set1.setFillAlpha(30);
            set1.setDrawHorizontalHighlightIndicator(false);

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextSize(12f);
            data.setDrawValues(true);
            data.setValueTextColor(getResources().getColor(R.color.colorxAxisLineText));

            // set data
            mChart.setData(data);
        }
    }

    private void setBloodData(int count, float range){

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yValsSecond = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 20;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(i, val));
            yValsSecond.add(new Entry(i,  val-(float) Math.random()*mult));
        }

        LineDataSet set1;
        LineDataSet set2;

        if (bloodLineChart.getData() != null && bloodLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)bloodLineChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet)bloodLineChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals);
            set2.setValues(yValsSecond);
            bloodLineChart.getData().notifyDataChanged();
            bloodLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type

            set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setCubicIntensity(0.2f);
            set1.setDrawCircles(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setCircleHoleRadius(2f);
            set1.setCircleColor(getResources().getColor(R.color.colorStaticsBackground));
            set1.setColor(getResources().getColor(R.color.colorStaticsBackground));
            set1.setFillColor(getResources().getColor(R.color.colorStaticsBackground));
            set1.setDrawFilled(true);
            set1.setFillAlpha(30);
            set1.setDrawHorizontalHighlightIndicator(false);

            set2 = new LineDataSet(yValsSecond, "DataSet 2");
            set2.setCubicIntensity(0.2f);
            set2.setDrawCircles(true);
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setCircleHoleRadius(2f);
            set2.setCircleColor(getResources().getColor(R.color.colorStaticGreen));
            set2.setColor(getResources().getColor(R.color.colorStaticGreen));
            set2.setFillColor(getResources().getColor(R.color.colorStaticGreen));
            set2.setDrawFilled(true);
            set2.setFillAlpha(30);
            set2.setDrawHorizontalHighlightIndicator(false);

            // create a data object with the datasets
            LineData data = new LineData(set1, set2);
            data.setValueTextSize(12f);
            data.setDrawValues(true);
            data.setValueTextColor(getResources().getColor(R.color.colorxAxisLineText));

            // set data
            bloodLineChart.setData(data);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.linearLayout_decide_by_span:
                showPop();
                LogD("调用了点击事件");
                break;
            default:
                break;
        }

    }
}
