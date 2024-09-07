package cn.changjiahong.bamboo.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.changjiahong.bamboo.R;


/**
 * @author ChangJiahong
 * @version v1.2
 * @Date 20221/6/2
 * @see FlowLayout
 * 修复自定义FlowLayout流式布局 MarginEnd不生效问题
 */
public class FixFlowLayout extends ViewGroup {

    private LayoutParams mLayoutParams;
    /**
     * 行高度集合
     */
    private List<Integer> mHeight = new ArrayList<>();

    /**
     * 每一行View的结合
     */
    private List<List<View>> mViews = new ArrayList<>();

    /**
     * 列间隔
     */
    private int columnInterval = 0;

    /**
     * 行间隔
     */
    private int lineInterval = 0;

    public FixFlowLayout(Context context) {
        this(context, null);
    }

    public FixFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FixFlowLayout, defStyleAttr, 0);
            columnInterval = typedArray.getDimensionPixelSize(R.styleable.FixFlowLayout_column_interval, 0);
            lineInterval = typedArray.getDimensionPixelSize(R.styleable.FixFlowLayout_line_interval, 0);
            typedArray.recycle();
        }


    }

    public int getColumnInterval() {
        return columnInterval;
    }

    public void setColumnInterval(int columnInterval) {
        this.columnInterval = columnInterval;
    }

    public int getLineInterval() {
        return lineInterval;
    }

    public void setLineInterval(int lineInterval) {
        this.lineInterval = lineInterval;
    }

    /**
     * 1.计算上下左右四个值
     * 2.调用layout(l, t, r, b);
     * 会用到每一行的高度
     * 需要每一行的控件宽度值
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //1.初始化队列
        //2.计算上下左右
        //3.调用layout...进行布局

        int lineWidth = 0;
        int lineHeight = 0;
        int width = getWidth();

        //一行View
        List<View> views = new ArrayList<View>();

        mHeight.clear();
        mViews.clear();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;

            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            //Log.i("liuyan", "childWidth = " + childWidth + " childHeight = " + childWidth);
            //换行
            if (width < childWidth + lineWidth) {
                //将上一行的行高加进
                mHeight.add(lineHeight);
                mViews.add(views);
                //重置行宽行高以及Views
                lineWidth = 0;
                lineHeight = childHeight;
                views = new ArrayList<View>();
            }
            //不换行
            lineWidth += childWidth + columnInterval;
            lineHeight = Math.max(lineHeight, childHeight);
            //Log.i("liuyan", "lineWidth = " + lineWidth + " lineHeight = " + lineHeight);
            views.add(child);
        }

        //最后一行没有换行，所以执行以下代码
        mHeight.add(lineHeight);
        mViews.add(views);

        //计算上下左右
        int left = 0;
        int top = 0;
        int totalLine = mViews.size();

        for (int i = 0; i < totalLine; i++) {
            views = mViews.get(i);
            lineHeight = mHeight.get(i);
            for (int j = 0; j < views.size(); j++) {
                View child = views.get(j);
                if (GONE == child.getVisibility()) {
                    continue;
                }

                LayoutParams params = (LayoutParams) child.getLayoutParams();


                //如果是行首View则不计算leftMargin
                //if (0 == j) {
                //    params.leftMargin = 0;
                //    child.setLayoutParams(params);
                //}

                // 偏移
                int xoffest = j == 0 ? 0 : columnInterval;
                int yoffest = i == 0 ? 0 : lineInterval;

                //计算上下左右
                int mL = left + params.leftMargin;
                int mT = top + params.topMargin;
                int mR = mL + child.getMeasuredWidth();
                int mB = mT + child.getMeasuredHeight();

                //Log.i("liuyan", "mL = " + mL + ",mT = " + mT + ",mR = " + mR + ",mB = " + mB);
                child.layout(mL, mT, mR, mB);

                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin + columnInterval;
            }
            left = 0;
            top += lineHeight + lineInterval;
        }
    }

    /**
     * MeasureSpec
     * 测量模式
     * AT_MOST wrap_content
     * EXACTLY match_parent 300dp
     * UNSPECIFIED ScrollView中使用到
     * 测量值 宽度 高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (MeasureSpec.EXACTLY == modeWidth && MeasureSpec.EXACTLY == modeHeight) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //设置本容器宽高
            setMeasuredDimension(sizeWidth, sizeHeight);
            return;
        }

        //记录行的宽度和高度，自定义控件的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;
        int width = sizeWidth;
        int height = 0;

        //如果不是EXACTLY的，就是AT_MOST（ScrollView使用较少）
        if (true) {
            //MeasureSpec.UNSPECIFIED != modeWidth && MeasureSpec.UNSPECIFIED != modeHeight
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);

                //测量子控件宽度和高度
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

                int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

                int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                //换行时
                if (width < lineWidth + childWidth) {
                    width = Math.max(width, lineWidth);

                    //重置行宽
                    lineWidth = childWidth;

                    height += lineHeight + lineInterval;
                    lineHeight = childHeight;
                } else {
                    //不换行时

                    lineWidth += childWidth + columnInterval;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
                //Log.i("liuyan", "flowLayout lineWidth = " + lineWidth + "childWidth = " + childWidth);
                //处理最后一行
                if (count - 1 == i) {
                    height += lineHeight;
                    width = Math.max(lineWidth, width);
                }
            }
            setMeasuredDimension(MeasureSpec.EXACTLY == modeWidth ? sizeWidth : width,
                    MeasureSpec.EXACTLY == modeHeight ? sizeHeight : height);
            int dd = MeasureSpec.EXACTLY == modeHeight ? sizeHeight : height;
        }
    }

    /**
     * 替换LayoutParams
     *
     * @param attrs
     * @return
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        mLayoutParams = new LayoutParams(getContext(), attrs);
        return mLayoutParams;
    }

    /**
     * 添加View
     *
     * @param view
     * @param params
     */
    public void putView(View view, LayoutParams params) {
        LayoutParams layoutParams = null;
        if (null == params) {
            layoutParams = (LayoutParams) view.getLayoutParams();
            if (null == layoutParams) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } else {
            layoutParams = params;
        }
        addView(view, layoutParams);

    }

    public static class LayoutParams extends MarginLayoutParams {


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}
