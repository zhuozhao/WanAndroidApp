package com.zz.wanandroid.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zz.wanandroid.R;

/**
 * @author zhaozhuo
 * @date 2018/7/13 9:36
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration  {

    public static final String TAG = "decoration";
    private Drawable dividerLine;

    public MyItemDecoration(Context context) {
        dividerLine = context.getResources().getDrawable(R.drawable.main_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - left;

        int childViewCount = parent.getChildCount();
        for (int i = 0; i < childViewCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            //top = childView's bottom + childView's end margin
            int top = childView.getBottom() + params.getMarginEnd();
            //bottom = top + 分隔线的真实高度
            int bottom = top + dividerLine.getIntrinsicHeight();

            //画分隔线
            dividerLine.setBounds(left, top, right, bottom);
            dividerLine.draw(c);
        }
    }

    //重写此方法，防止设置的波纹背景把分隔线覆盖掉。
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //除最后一行外，向下位移分隔线
        int currentItemPosition = parent.getChildAdapterPosition(view);
        int endItemPosition = parent.getAdapter().getItemCount() - 1;
        if (currentItemPosition != endItemPosition ) {
            outRect.bottom = dividerLine.getIntrinsicHeight();
        }
    }


}
