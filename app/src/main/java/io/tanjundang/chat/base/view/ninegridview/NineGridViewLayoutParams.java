package io.tanjundang.chat.base.view.ninegridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/19
 * @Description:
 */

public class NineGridViewLayoutParams extends ViewGroup.LayoutParams {
    public NineGridViewLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public NineGridViewLayoutParams(int width, int height) {
        super(width, height);
    }

    public NineGridViewLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }
}
