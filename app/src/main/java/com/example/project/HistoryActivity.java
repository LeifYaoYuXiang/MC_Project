package com.example.project;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


public class HistoryActivity extends AppCompatActivity {
    private BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        PieChart pieChart=findViewById(R.id.pieChart);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(true);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setHighlightPerTapEnabled(true);
        Legend l=pieChart.getLegend();
        l.setEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        //设置图例的形状
        l.setForm(Legend.LegendForm.DEFAULT);
        //设置图例的大小
        l.setFormSize(10);
        //设置每个图例实体中标签和形状之间的间距
        l.setFormToTextSpace(10f);
        l.setDrawInside(false);
        //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        l.setWordWrapEnabled(true);
        //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setXEntrySpace(10f);
        //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYEntrySpace(8f);
        //设置比例块Y轴偏移量
        l.setYOffset(0f);
        //设置图例标签文本的大小
        l.setTextSize(18f);
        l.setTextColor(Color.parseColor("#333333"));

        //数据列表
        ArrayList<PieEntry> pieEntryList = new ArrayList();
        pieEntryList.add(new PieEntry(200,"专注学习"));
        pieEntryList.add(new PieEntry(300,"专注运动"));
        pieEntryList.add(new PieEntry(100,"专注会议"));

        PieDataSet pieDataSet=new PieDataSet(pieEntryList,"");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);

        pieChart.setData(pieData);
        pieChart.invalidate();


    }


    private void showBarChart(){

    }
}
