package com.github.lijingnan.piechartfix;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class PieChartRendererFixCover extends PieChartRenderer {
    private static final String TAG = "PieChartRendererFixCove";
    private boolean auto_adapt_text_size;
    private int measuredHeight;
    private float topAndBottomSpace;
    private List<Integer> colors;
    private boolean line_color_with_pie;

    PieChartRendererFixCover(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    PieChartRendererFixCover setAuto_adapt_text_size(boolean auto_adapt_text_size) {
        this.auto_adapt_text_size = auto_adapt_text_size;
        return this;
    }

    PieChartRendererFixCover setLine_color_with_pie(boolean line_color_with_pie) {
        this.line_color_with_pie = line_color_with_pie;
        return this;
    }

    @Override
    public void drawValues(Canvas c) {
        //获取可用高度
        measuredHeight = mChart.getMeasuredHeight();
        println("measuredHeight = " + measuredHeight);
        topAndBottomSpace = measuredHeight - mChart.getRadius() * 2;
        println("mChart.getRadius() = " + mChart.getRadius());
        println("topAndBottomSpace = " + topAndBottomSpace);
        drawValuesWithAVG(c);
    }

    private void println(String s) {
        Log.i(TAG, s);
    }

    /**
     * 左右两侧的空间根据数据个数进行等分，如果等分之后，还是会重叠，那说明数据太多了。如果开始自适应模式，则会自动调整字体大小，避免重叠
     */
    private void drawValuesWithAVG(Canvas c) {
        MPPointF center = mChart.getCenterCircleBox();
        float radius = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles(); //每一个数据占据的角度 15 15 15
        float[] absoluteAngles = mChart.getAbsoluteAngles(); //每一个数据累计后的总角度 15 30 45
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();
        final float holeRadiusPercent = mChart.getHoleRadius() / 100.f;
        float labelRadiusOffset = radius / 10f * 3.6f;
        if (mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;
        }
        final float labelRadius = radius - labelRadiusOffset;
        PieData data = mChart.getData();
        data.getColors();
        List<IPieDataSet> dataSets = data.getDataSets();
        float yValueSum = data.getYValueSum();
        boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();
        float angle;
        int xIndex = 0;
        c.save();
        float offset = Utils.convertDpToPixel(5.f);
        for (int i = 0, size = dataSets.size(); i < size; i++) {
            IPieDataSet dataSet = dataSets.get(i);
            final boolean drawValues = dataSet.isDrawValuesEnabled();
            if (!drawValues && !drawEntryLabels)
                continue;
            final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
            final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();
            applyValueTextStyle(dataSet);
            float lineHeight = Utils.calcTextHeight(mValuePaint, "Q")
                    + Utils.convertDpToPixel(4f);
            println("lineHeight = " + lineHeight);
            IValueFormatter formatter = dataSet.getValueFormatter();
            int entryCount = dataSet.getEntryCount();
            mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));
            if (line_color_with_pie) {
                colors = dataSet.getColors();
                if (colors == null || colors.size() == 0) {
                    colors = new ArrayList<>();
                    for (int color : ColorTemplate.JOYFUL_COLORS) {
                        colors.add(color);
                    }
                }
            } else {
                mValueLinePaint.setColor(dataSet.getValueLineColor());

            }
            final float sliceSpace = getSliceSpace(dataSet);
            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);
            int rightCount = 0;
            int leftCount = 0;
            //先统计左右两侧的数据数量
            for (int j = 0; j < entryCount; j++) {
                if (xIndex == 0) {
                    angle = 0.f;
                } else {
                    angle = absoluteAngles[xIndex - 1] * phaseX;
                }
                final float sliceAngle = drawAngles[xIndex];
                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);
                // offset needed to center the drawn text in the slice
                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;
                angle = angle + angleOffset;
                final float transformedAngle = rotationAngle + angle * phaseY;
                final boolean drawXOutside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawYOutside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                //左右算法不一样,左边是从下往上排的，即你可以理解为饼图是顺时针方向，从零点排到12点的360度圆形
                if (drawXOutside || drawYOutside) {
                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {
                        leftCount++; //左边部分
                    } else {
                        rightCount++; //右边部分
                    }
                }
                xIndex++;
            }
            xIndex = 0;
            float rightSpace = (rightCount > 1) ? radius * 2 / (rightCount - 1) : radius / 2;
            float leftSpace = (leftCount > 1) ? radius * 2 / (leftCount - 1) : radius / 2;
            int tempRightIndex = 0;
            int tempLeftIndex = 0;
            int lastRightPt2y = 0;
            int lastLeftPt2y = 0;
            //左右分开进行绘制
            for (int j = 0; j < entryCount; j++) {
                PieEntry entry = dataSet.getEntryForIndex(j);
                println("entry getLabel = " + entry.getLabel() + ", getValue = " + entry.getValue());
                if (xIndex == 0) {
                    angle = 0.f;
                } else {
                    angle = absoluteAngles[xIndex - 1] * phaseX;
                }
                final float sliceAngle = drawAngles[xIndex];
                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);
                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;
                angle = angle + angleOffset;
                final float transformedAngle = rotationAngle + angle * phaseY;
                float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
                        / yValueSum * 100f : entry.getY();
                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);
                final boolean drawXOutside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawYOutside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawXInside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                final boolean drawYInside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                if (drawXOutside || drawYOutside) {
                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;
                    float pt2x, pt2y;
                    float labelPtx, labelPty;
                    float line1Radius;
                    if (mChart.isDrawHoleEnabled()) {
                        line1Radius = (radius - (radius * holeRadiusPercent))
                                * valueLinePart1OffsetPercentage
                                + (radius * holeRadiusPercent);
                    } else {
                        line1Radius = radius * valueLinePart1OffsetPercentage;
                    }
                    final float pt0x = line1Radius * sliceXBase + center.x;
                    final float pt0y = line1Radius * sliceYBase + center.y;
                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
                    final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;
                    //左右算法不一样,左边是从下往上排的，即你可以理解为饼图是顺时针方向，从零点排到12点的360度圆形，建议先看else里的，即右边的，方便理解
                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {//左边部分
                        pt2x = center.x - radius - 5;
                        if (leftCount > 1) {
                            pt2y = (measuredHeight - topAndBottomSpace / 2) - leftSpace * tempLeftIndex;
                            if (tempLeftIndex > 0 && lastLeftPt2y - pt2y - lineHeight * 2 < 0) {
                                pt2y = lastLeftPt2y - lineHeight * 2;
                            }
                        } else {
                            pt2y = pt1y;
                        }
                        lastLeftPt2y = (int) pt2y;
                        tempLeftIndex++;
                        println("left pt2y = " + pt2y + ", tempLeftIndex = " + tempLeftIndex);
                        mValuePaint.setTextAlign(Paint.Align.RIGHT);
                        if (drawXOutside)
                            getPaintEntryLabels().setTextAlign(Paint.Align.RIGHT);
                        labelPtx = pt2x - offset;
                        labelPty = pt2y;
                    } else {//右边部分
                        pt2x = center.x + radius + 5;
                        if (rightCount > 1) {
                            pt2y = topAndBottomSpace / 2 + rightSpace * tempRightIndex;
                            //
                            if (lineHeight *2 + lastRightPt2y - pt2y >= 10) {
                                pt2y = lineHeight * 2 + lastRightPt2y;
                            }
                        } else {
                            pt2y = pt1y;
                        }
                        lastRightPt2y = (int) pt2y;
                        tempRightIndex++;
                        mValuePaint.setTextAlign(Paint.Align.LEFT);
                        if (drawXOutside)
                            getPaintEntryLabels().setTextAlign(Paint.Align.LEFT);
                        labelPtx = pt2x + offset;
                        labelPty = pt2y;
                        println("right pt2y = " + pt2y + ", tempRightIndex = " + tempRightIndex);
                    }
                    if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
                        if (line_color_with_pie) {
                            mValueLinePaint.setColor(colors.get(j % colors.size()));
                        }
                        c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
                        c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
                    }
                    if (drawXOutside && drawYOutside) {
                        drawValue(c, formatter, value, entry, 0, labelPtx, labelPty,
                                dataSet.getValueTextColor(j));
                        //3.1.0及之后使用这个方法
//                        drawValue(c, formatter.getFormattedValue(value, entry, 0, mViewPortHandler),
//                                labelPtx, labelPty, dataSet.getValueTextColor(j));
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight);
                        }
                    } else if (drawXOutside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight / 2.f);
                        }
                    } else if (drawYOutside) {
                        float minTextSize = Math.min(rightSpace, leftSpace);
                        if (auto_adapt_text_size && mValuePaint.getTextSize() > minTextSize) {
                            mValuePaint.setTextSize(minTextSize);
                            lineHeight = Utils.calcTextHeight(mValuePaint, "Q");
                        }
                        drawValue(c, formatter, value, entry, 0, labelPtx,
                                labelPty + lineHeight / 2.f, dataSet.getValueTextColor(j));
                        //3.1.0及之后使用这个方法
//                        drawValue(c, formatter.getFormattedValue(value, entry, 0, mViewPortHandler),
//                                labelPtx, labelPty + lineHeight / 2.f, dataSet.getValueTextColor(j));
                    }
                }
                if (drawXInside || drawYInside) {
                    float x = labelRadius * sliceXBase + center.x;
                    float y = labelRadius * sliceYBase + center.y;
                    mValuePaint.setTextAlign(Paint.Align.CENTER);
                    if (drawXInside && drawYInside) {
                        drawValue(c, formatter, value, entry, 0, x, y,
                                dataSet.getValueTextColor(j));
                        //3.1.0及之后使用这个方法
//                        drawValue(c, formatter.getFormattedValue(value, entry, 0, mViewPortHandler),
//                                x, y, dataSet.getValueTextColor(j));
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight);
                        }
                    } else if (drawXInside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight / 2f);
                        }
                    } else {
                        drawValue(c, formatter, value, entry, 0, x, y + lineHeight / 2f,
                                dataSet.getValueTextColor(j));
                        //3.1.0及之后使用这个方法
//                        drawValue(c, formatter.getFormattedValue(value, entry, 0, mViewPortHandler),
//                                x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
                    }
                }
                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                    Drawable icon = entry.getIcon();
                    float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
                    float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
                    y += iconsOffset.x;
                    Utils.drawImage(c, icon, (int) x, (int) y, icon.getIntrinsicWidth(),
                            icon.getIntrinsicHeight());
                }
                xIndex++;
            }
            MPPointF.recycleInstance(iconsOffset);
        }
        println("-------------------------- pt2y = --------------------------");
        MPPointF.recycleInstance(center);
        c.restore();
    }
}