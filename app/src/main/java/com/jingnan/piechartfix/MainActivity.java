package com.jingnan.piechartfix;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lijingnan.piechartfix.PieChartFixCover;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    protected final String[] parties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieChart pieChart = findViewById(R.id.pie_chart);
        PieChartFixCover pieChartFix = findViewById(R.id.pie_chart_fix);
        setPieChartData(pieChart, null, false);
        setPieChartData(pieChartFix, null, false);
    }

    //  设置饼图数据
    public void setPieChartData(PieChart mChart, Map<String, Double> pieValues, boolean showLegend) {
        mChart.setUsePercentValues(true);
        mChart.setRotationAngle(0);//设置初始旋转角为0度
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(0, 20, 0, 20);
//        mChart.setDrawSliceText(false);//设置隐藏饼图上文字，只显示百分比
        mChart.setDrawHoleEnabled(true);
        mChart.setDrawEntryLabels(true);
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(45f); //半径
        //mChart.setHoleRadius(0)  //实心圆
        mChart.setTransparentCircleRadius(48f);// 半透明圈
        mChart.setDrawCenterText(true);//饼状图中间可以添加文字
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        mChart.setNoDataText("暂无数据");
        mChart.setUsePercentValues(true);//设置显示成比例
        mChart.setCenterText("");
        mChart.setHighlightPerTapEnabled(true);
        Legend mLegend = mChart.getLegend();  //设置比例图
        if (showLegend) {
            mLegend.setEnabled(true);
            mLegend.setFormSize(11f);//比例块字体大小
            mLegend.setXEntrySpace(2f);//设置距离饼图的距离，防止与饼图重合
            mLegend.setYEntrySpace(2f);
            //设置比例块换行...
            mLegend.setWordWrapEnabled(true);
            mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
            mLegend.setTextColor(Color.parseColor("#333333"));//设置图例标签文本的颜色
            mLegend.setForm(Legend.LegendForm.CIRCLE);//设置比例块形状，默认为方块
        } else {
            mLegend.setEnabled(false);
        }
        List<PieEntry> entries = new ArrayList<>();
//        Set set = pieValues.entrySet();
//        Iterator it = set.iterator();
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            PieEntry pieEntry = null;
////            if(Float.valueOf(entry.getValue().toString())<5){
////                pieEntry=new PieEntry(Float.valueOf(entry.getValue().toString()),entry.getKey().toString(),false);
////            }else {
////                pieEntry=new PieEntry(Float.valueOf(entry.getValue().toString()),entry.getKey().toString(),true);
////            }
//            if (!showLegend) {
//                //key值过长的话截取
//                if (entry.getKey().toString().length() > 5) {
//                    pieEntry = new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString().substring(0, 5)+"...");
//                } else {
//                    pieEntry = new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString());
//                }
//            } else {
//                pieEntry = new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString());
//            }
//            entries.add(pieEntry);
//        }

//        entries.add(new PieEntry(1F, parties[0]));
//        entries.add(new PieEntry(1F, parties[6]));
//        entries.add(new PieEntry(1F, parties[7]));
//        entries.add(new PieEntry(80F, parties[8]));
//        entries.add(new PieEntry(1F, parties[9]));
//        entries.add(new PieEntry(1F, parties[1]));
//        entries.add(new PieEntry(1F, parties[2]));
//        entries.add(new PieEntry(1F, parties[3]));
//        entries.add(new PieEntry(1F, parties[4]));
//        entries.add(new PieEntry(80F, parties[5]));

//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
//        entries.add(new PieEntry(1708.220f, "08铝类"));
        entries.add(new PieEntry(1708.220f, "08铝类"));
        entries.add(new PieEntry(1708.220f, "08铝类"));
        entries.add(new PieEntry(1708.220f, "08铝类"));
        entries.add(new PieEntry(1912.7050f, "破碎废钢"));
        entries.add(new PieEntry(4844.440f, "工业料类"));
        entries.add(new PieEntry(5639.9790f, "其他"));
        entries.add(new PieEntry(6431.230f, "中型废钢类"));
        entries.add(new PieEntry(9180.1350f, "冲豆、冲料..."));
        entries.add(new PieEntry(28491.40f, "破碎料类"));
        entries.add(new PieEntry(60058.5230f, "废钢"));
        entries.add(new PieEntry(116619.750f, "钢筋类"));
        entries.add(new PieEntry(230772.69f, "压块类"));
        entries.add(new PieEntry(266247.78f, "重型废钢类"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(5f);//设置饼块选中时偏离饼图中心的距离
        ArrayList<Integer> colors = new ArrayList<>();
        //自定义color
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        //设置数据显示方式有见图
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//lable外部展示
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//value外部展示
        dataSet.setValueLinePart1Length(0.3f);//当值位置为外边线时，表示线的前半段长度。
        dataSet.setValueLinePart2Length(0.5f);//当值位置为外边线时，表示线的后半段长度。
        dataSet.setValueLineColor(Color.BLACK);//设置连接线的颜色
        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PieValueFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);
        mChart.setData(pieData);
        mChart.highlightValues(null);
        mChart.invalidate();
    }

}
