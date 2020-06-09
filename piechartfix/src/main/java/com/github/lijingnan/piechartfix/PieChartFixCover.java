package com.github.lijingnan.piechartfix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;

/**
 * @description 修复饼图 值在外部显示时，部分值会重叠的问题
 */

public class PieChartFixCover extends PieChart {

    public PieChartFixCover(Context context) {
        this(context, null);
    }

    public PieChartFixCover(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartFixCover(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getAttrs(attrs);
    }

    private void getAttrs(AttributeSet attrs) {
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable") TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PieChartFix);
            boolean auto_adapt_text_size = a.getBoolean(R.styleable.PieChartFix_chart_auto_adapt_text_size, false);
            boolean line_color_with_pie = a.getBoolean(R.styleable.PieChartFix_chart_line_color_with_pie, false);
            a.recycle();
            ((PieChartRendererFixCover) mRenderer)
                    .setAuto_adapt_text_size(auto_adapt_text_size)
                    .setLine_color_with_pie(line_color_with_pie);
        }
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new PieChartRendererFixCover(this, mAnimator, mViewPortHandler);
    }
}