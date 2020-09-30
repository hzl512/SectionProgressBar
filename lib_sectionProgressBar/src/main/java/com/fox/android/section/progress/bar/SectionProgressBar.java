package com.fox.android.section.progress.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * @author FoxHuang
 * @date 2020/9/23
 * Desc: 分段进度条
 * 部分参考了：https://github.com/CL-window/progressBar
 */
public class SectionProgressBar extends View {

    private static final String TAG = "SectionProgressBar";

    /**
     * 绘制分段轴总时长（秒）
     */
    private long mTotal = 24 * 60 * 60L;

    /**
     * 单位距离（每秒）
     */
    private float mPixelsPer = 0;

    private int mConvertUnit = 1000;

    /**
     * 分段区域画笔
     */
    private Paint mRegionPaint = new Paint();

    /**
     * 组件屏幕上现实的宽度
     */
    private int mWidth;

    /**
     * 组件屏幕上现实的高度
     */
    private int mHeight;

    /**
     * 进度条颜色
     */
    private int mProgressColor = Color.BLUE;

    private int mProgressBackColor = Color.WHITE;

    /**
     * 是否渐变
     */
    private boolean mShaderShow = false;

    /**
     * 渐变开始颜色
     */
    private int mShaderStartColor = Color.parseColor("#EE4D3A");

    /**
     * 渐变结束颜色
     */
    private int mShaderEndColor = Color.parseColor("#E23200");

    /**
     * 绘制区域的起始点
     */
    private long mStartSection;

    private ArrayList<ShaftRegionItem> mShaftItems;

    private ClickSectionListener clickSectionListener;

    /**
     * 分段的点击事件
     * @param clickSectionListener
     */
    public void setClickSectionListener(ClickSectionListener clickSectionListener) {
        this.clickSectionListener = clickSectionListener;
    }

    public interface ClickSectionListener {
        void clickBar(ShaftRegionItem item, int viewY);
    }

    public SectionProgressBar(Context context) {
        this(context, null);
    }

    public SectionProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SectionProgressbar, defStyleAttr, 0);
        mConvertUnit = attributes.getInteger(R.styleable.SectionProgressbar_p_convertUnit, mConvertUnit);
        mProgressColor = attributes.getColor(R.styleable.SectionProgressbar_p_progressColor, mProgressColor);
        mProgressBackColor = attributes.getColor(R.styleable.SectionProgressbar_p_progressBackColor, mProgressBackColor);

        mShaderShow = attributes.getBoolean(R.styleable.SectionProgressbar_p_shaderShow, mShaderShow);
        mShaderStartColor = attributes.getColor(R.styleable.SectionProgressbar_p_shaderStartColor, mShaderStartColor);
        mShaderEndColor = attributes.getColor(R.styleable.SectionProgressbar_p_shaderEndColor, mShaderEndColor);
        setBackgroundColor(mProgressBackColor);
        mRegionPaint.setColor(mProgressColor);
    }

    public void setShaftItems(ArrayList<ShaftRegionItem> timeShaftItems, long startSection) {
        mShaftItems = timeShaftItems;
        if (mShaftItems != null && mShaftItems.size() > 0) {
            mStartSection = startSection;
        }
        //刷新界面
        invalidate();
    }

    /**
     * 设置转换单位
     *
     * @param mConvertUnit
     */
    public void setConvertUnit(int mConvertUnit) {
        this.mConvertUnit = mConvertUnit;
    }

    /**
     * 设置总长度
     *
     * @param mTotal
     */
    public void setTotal(long mTotal) {
        this.mTotal = mTotal;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidth == 0) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
            initSectionBar();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initSectionBar() {
        Log.e(TAG, "initSectionBar-mWidth-" + mWidth);
        Log.e(TAG, "initSectionBar-mHeight-" + mHeight);
        mPixelsPer = mWidth / (float) mTotal;
        Log.e(TAG, "initSectionBar-mPixelsPer-" + mPixelsPer);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRegionRect(canvas);
    }

    /**
     * 绘制要显示的分段
     * 核心算法，参考了TimerShaftBar，From：https://github.com/Hikvision-Ezviz/EZUIKit-Android
     * @param canvas
     */
    private void drawRegionRect(Canvas canvas) {
        if (mShaftItems != null) {
            long b = System.currentTimeMillis();
            for (int i = 0; i < mShaftItems.size(); i++) {
                ShaftRegionItem timeShaftItem = mShaftItems.get(i);
                int startSection = (int) ((timeShaftItem.getStartSection() - mStartSection) / mConvertUnit);
                int endSection = (int) ((timeShaftItem.getEndSection() - mStartSection) / mConvertUnit);
                int startX = (int) (startSection * mPixelsPer);
                int endX = (int) (endSection * mPixelsPer);
                //存下坐标，给点击判断用；
                timeShaftItem.setStartX(startSection * mPixelsPer);
                timeShaftItem.setEndX(endSection * mPixelsPer);
                mShaftItems.set(i, timeShaftItem);

                if (mShaderShow) {
                    //搞不出来渐变，暂不执行，参考这个实现：https://blog.csdn.net/u010126792/article/details/85237085
                    LinearGradient linearGradient = new LinearGradient(startX, endX, startX,
                            endX, new int[]{mShaderStartColor, mShaderEndColor},
                            null, LinearGradient.TileMode.REPEAT);
                    mRegionPaint.setShader(linearGradient);
                }
                canvas.drawRect(startX, 0, endX, getHeight(), mRegionPaint);
            }
            Log.d(TAG, "drawtime   drawRegionRect time = " + (System.currentTimeMillis() - b));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionType = event.getActionMasked();
        switch (actionType) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE  ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ACTION_UP  +  " + event.getX());
                Log.e(TAG, "ACTION_UP  +  " + event.getRawX());
                Log.e(TAG, "ACTION_UP  +  " + getBottom());
                clickBar(event.getX(), getBottom());
                break;
            default:
                break;
        }
        return true;
    }

    private void clickBar(float x, int viewY) {
        if (mShaftItems != null) {
            long c = System.currentTimeMillis();
            for (ShaftRegionItem item : mShaftItems) {
                if (x >= item.getStartX() && x <= item.getEndX()) {
                    Log.e(TAG, "ACTION_UP  +  " + item.toString());
                    if (clickSectionListener != null) {
                        clickSectionListener.clickBar(item, viewY);
                    }
                    break;
                }
            }
            Log.d(TAG, "clickBar time = " + (System.currentTimeMillis() - c));
        }
    }

}
