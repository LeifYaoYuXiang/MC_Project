package com.example.project;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class HistoryActivity extends AppCompatActivity {
    private HistoryDatabaseOpenHelper historyDatabaseOpenHelper;
    private ArrayList<Record> recordArrayList=new ArrayList<>();

    private LineChart lineChart;
    private TextView overviewText;
    private ArrayList<Entry> rememberPoints=new ArrayList<>();
    private ArrayList<Entry> forgetPoints=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("themePre", MODE_PRIVATE);
        int themeID = sharedPreferences.getInt("themeID", -1);
        setTheme(themeID);

        setContentView(R.layout.activity_history);
        lineChart = findViewById(R.id.line_chart);

        initDataFromDatabase();
        initUI();
        putDataIntoDiagram();

    }


    private void initDataFromDatabase(){
        this.historyDatabaseOpenHelper=new HistoryDatabaseOpenHelper(this,"Records.db",null,1);
        SQLiteDatabase sqLiteDatabase=historyDatabaseOpenHelper.getWritableDatabase();

        Cursor cursor=sqLiteDatabase.query("Record",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                int correct=cursor.getInt(cursor.getColumnIndex("correct"));
                int incorrect=cursor.getInt(cursor.getColumnIndex("incorrect"));
                long date=cursor.getLong(cursor.getColumnIndex("date"));
                Record record=new Record(id,correct,incorrect,date);
                if(SystemClock.currentThreadTimeMillis()-record.getDate()<7*24*60*60*1000){
                    recordArrayList.add(record);
                }else{

                }
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void initUI(){
        overviewText=findViewById(R.id.overview_text);
        if(recordArrayList.size()==0){
            overviewText.setText("You didn't attend self-practice in the latest seven days");
        }else{
            overviewText.setText("You have attend "+recordArrayList.size()+" tests in the latest seven days.");
        }

        Description description = new Description();
        description.setText("Remember P.K. Forget");
        lineChart.setDescription(description);

        //设置是否绘制chart边框的线
        lineChart.setDrawBorders(true);

        //设置chart边框线宽度
        lineChart.setBorderWidth(1f);
        //设置chart是否可以触摸
        lineChart.setTouchEnabled(true);
        //设置是否可以拖拽
        lineChart.setDragEnabled(true);
        //设置是否可以缩放 x和y，默认true
        lineChart.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setDoubleTapToZoomEnabled(false);
        //设置chart动画
        lineChart.animateXY(1000, 1000);

        lineChart.setNoDataText("You haven't test yourself yet");
        float ratio = (float) recordArrayList.size()/(float) 10;
        lineChart.zoom(ratio,1f,0,0);
        Legend legend = lineChart.getLegend();
        //设置图例显示在chart那个位置 setPosition建议放弃使用了
        //设置垂直方向上还是下或中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平方向是左边还是右边或中
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置所有图例位置排序方向
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.CIRCLE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        legend.setWordWrapEnabled(true);



        XAxis xAxis = lineChart.getXAxis();
        //是否启用X轴
        xAxis.setEnabled(true);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置竖线为虚线样式

        //设置左边Y轴
        YAxis axisLeft = lineChart.getAxisLeft();
        //是否启用左边Y轴
        axisLeft.setEnabled(true);
        axisLeft.enableGridDashedLine(10f, 10f, 0f);
    }

    private void putDataIntoDiagram(){
        for(int i=0;i<recordArrayList.size();i++){
            rememberPoints.add(new Entry(i,recordArrayList.get(i).getCorrect()));
            forgetPoints.add(new Entry(i,recordArrayList.get(i).getIncorrect(),i));
        }
        LineDataSet rememberLineData = new LineDataSet(rememberPoints, "Remember");
        LineDataSet forgetLineData=new LineDataSet(forgetPoints,"Forget");
        //设置该线的颜色
        rememberLineData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        forgetLineData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        rememberLineData.setColor(Color.parseColor("#4CAF50"));
        forgetLineData.setColor(Color.parseColor("#E64A19"));

        rememberLineData.setCircleColor(Color.parseColor("#8BC34A"));

        forgetLineData.setCircleColor(Color.parseColor("#FF5252"));
        //设置该线的宽度

        rememberLineData.setLineWidth(2f);
        forgetLineData.setLineWidth(2f);
        rememberLineData.setDrawFilled(true);
        rememberLineData.setFillAlpha(50);
        rememberLineData.setFillColor(Color.parseColor("#8BC34A"));

        forgetLineData.setDrawFilled(true);
        forgetLineData.setFillAlpha(50);
        forgetLineData.setFillColor(Color.parseColor("#FF5252"));

        rememberLineData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        forgetLineData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);


        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(rememberLineData);
        dataSets.add(forgetLineData);
        //把要画的所有线(线的集合)添加到LineData里
        LineData lineData = new LineData(dataSets);
        //把最终的数据setData
        lineChart.setData(lineData);

    }
}
