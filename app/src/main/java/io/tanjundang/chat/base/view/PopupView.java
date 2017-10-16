package io.tanjundang.chat.base.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import io.tanjundang.chat.R;


/**
 * @Author: TanJunDang
 * @Date: 2017/8/16
 * @Description: 通用弹出View
 */

public class PopupView extends View {
    PopupWindow popupWindow;
    int height;
    ViewCallback callback;
    View rootview;
    Context context;
    int gravity = Gravity.BOTTOM;

    /**
     * @param context
     * @param layoutId 布局
     * @param callback
     */
    public PopupView(@NonNull Context context, int layoutId, ViewCallback callback, PopupWindow.OnDismissListener dismissListener) {
        this(context, null, layoutId, callback, dismissListener);
    }

    public PopupView(@NonNull Context context, @Nullable AttributeSet attrs, int layoutId, ViewCallback callback, PopupWindow.OnDismissListener dismissListener) {
        this(context, attrs, 0, layoutId, callback, dismissListener);
    }

    public PopupView(@NonNull final Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, int layoutId, ViewCallback callback, final PopupWindow.OnDismissListener dismissListener) {
        super(context, attrs, defStyleAttr);
        this.callback = callback;
        this.context = context;
//        height = ScreenUtils.getScreenHeight(context) / 2;
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        rootview = LayoutInflater.from(context).inflate(layoutId, null);
        callback.handleView(rootview);
        popupWindow = new PopupWindow(rootview, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setAnimationStyle(R.style.PopupWindowStyle);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (dismissListener != null) {
                    dismissListener.onDismiss();
                }
                modifyWindowBackground(1);
            }
        });
    }


    public void setWidthHeight(int width, int height) {
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        invalidate();
    }


    /**
     * 设置弹出动画
     *
     * @param animStyle R.style.xxx
     */
    public void setAnimStyle(int animStyle) {
        popupWindow.setAnimationStyle(animStyle);
    }

    /**
     * @param gravity Gravity.TOP
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    private void show() {
        modifyWindowBackground(0.6f);
        popupWindow.showAtLocation(rootview, gravity, 0, 0);
    }

    public void showWithOffset(int xoffset, int yoffset) {
        modifyWindowBackground(0.6f);
        popupWindow.showAtLocation(rootview, gravity, xoffset, yoffset);
    }

    public void showAsDropDown(View anchor) {
        modifyWindowBackground(0.6f);
        popupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int x, int y) {
        modifyWindowBackground(0.6f);
        popupWindow.showAsDropDown(anchor, x, y);
    }

    public void hide() {
        popupWindow.dismiss();
    }

    /**
     * 用于返回时判断popupwindow是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    /**
     * 点击自动判断是显示还是关闭
     */
    public void handleShow() {
        if (popupWindow.isShowing()) {
            hide();
        } else {
            show();
        }
    }

    public void modifyWindowBackground(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = alpha;//设置阴影透明度
        ((Activity) context).getWindow().setAttributes(lp);
    }

    /**
     * 处理布局里的View
     */
    public interface ViewCallback {
        void handleView(View rootview);
    }
}
