package io.tanjundang.chat.base.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Developer: TanJunDang
 * Email: TanJunDang324@gmail.com
 * Date: 2016/10/28
 * 通用RecyclerView的ItemDivider
 */

public class ItemDivider extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int color = Color.BLUE;//分割线颜色
    private int length = 5;//分割线长度(水平方向时，length为divider的宽度，垂直方向时，length为divider的高度)
    private int orientation;//方向

    /**
     * @param color       颜色
     * @param orientation 方向
     */
    public ItemDivider(int color, int orientation) {
        this.color = color;
        this.orientation = orientation;
    }

    /**
     * @param color       颜色
     * @param orientation 方向
     * @param length      分割线长度
     */
    public ItemDivider(int color, int orientation, int length) {
        this.length = length;
        this.color = color;
        this.orientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        Paint paint = new Paint();
        paint.setColor(color);
        if (orientation == HORIZONTAL) {
            top = parent.getPaddingTop();
            bottom = parent.getBottom() - parent.getPaddingBottom();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                left = child.getRight() + params.rightMargin;
                right = left + length;
                RectF rect = new RectF(left, top, right, bottom);
                c.drawRect(rect, paint);
            }
        } else {
            left = parent.getPaddingLeft();
            right = parent.getRight() - parent.getPaddingRight();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + length;
                RectF rect = new RectF(left, top, right, bottom);
                c.drawRect(rect, paint);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == HORIZONTAL) {
            outRect.set(0, 0, length, 0);
        } else {
            outRect.set(0, 0, 0, length);
        }
    }
}
