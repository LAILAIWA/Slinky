package com.lai.slinky.RecyclerView;

/**
 * Created by Administrator on 2017/3/3.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    //添加分割线
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private int mOrientation;
    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        mDividerHeight = mDivider.getIntrinsicHeight();
        //mDivider = context.getResources().getDrawable(R.drawable.divider);
        a.recycle();
        setOrientation(orientation);
    }
    public void setDividerHeight(int height){
        mDividerHeight = height;
    }

    public void setDividerDrawable(Drawable drawable){
        this.mDivider = drawable;
    }
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    /**
     * 画分割线
     * @param c 画布
     * @param parent
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        /**
         * 画每个item的分割线
         */
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            //mDivider.getIntrinsicHeight() 单位dp
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);/*规定好左上角和右下角*/
            mDivider.draw(c);
        }
    }
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left +mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 确定divider的位置，设置在outRect中
     * 设置分割线的宽和高
     * 这个函数在计算RecyclerView中每个child大小时会用到
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //outRect当前Item四周的间距
        if (mOrientation == VERTICAL_LIST) {
            int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
            int count = parent.getAdapter().getItemCount();
            if(position < count - 1) {
                outRect.set(0, 0, 0,mDividerHeight);
            }
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }
}