package io.tanjundang.chat.base.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.ButterKnife;
import io.tanjundang.chat.R;

/**
 * @Author: TanJunDang
 * @Date: 2017/7/21
 * @Description: 初始化title_value布局，分两种布局
 * 1.只有Title、Value的布局
 * 2.有Title、Value、Arrow的布局
 */

public class ItemTool {

    /**
     * Title、Value
     *
     * @param layout
     * @param text
     * @param value
     */
    public static void titleValue(ViewGroup layout, String text, String value) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tvTitle);
        TextView tvValue = ButterKnife.findById(layout, R.id.tvValue);
        tvTitle.setText(text);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvValue.setText(value);
    }


    /**
     * Title、箭头
     *
     * @param layout
     * @param text
     */
    public static void titleArrow(ViewGroup layout, String text) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tvTitle);
        TextView tvValue = ButterKnife.findById(layout, R.id.tvValue);
        tvTitle.setText(text);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvValue.setVisibility(View.GONE);
    }

    /**
     * Title、Title图标、箭头
     *
     * @param context
     * @param group
     * @param title      标题
     * @param drawableID Title图标
     */
    public static void titleIconArrow(Context context, ViewGroup group, String title, int drawableID) {
        TextView tvTitle = ButterKnife.findById(group, R.id.tvTitle);
        TextView tvValue = ButterKnife.findById(group, R.id.tvValue);
        tvTitle.setText(title);
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, drawableID), null, null, null);
        tvValue.setVisibility(View.GONE);
    }

    /**
     * Title 、value、箭头
     *
     * @param layout
     * @param text
     * @param value
     */
    public static void titleValueArrow(ViewGroup layout, String text, String value) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tvTitle);
        tvTitle.setText(text);
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        TextView tvValue = ButterKnife.findById(layout, R.id.tvValue);
        tvValue.setText(value);
    }

    /**
     * Title 、value、自定义图标
     *
     * @param layout
     * @param text
     * @param value
     */
    public static void titleValueIcon(ViewGroup layout, String text, String value, int rightDrawableId) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tvTitle);
        tvTitle.setText(text);
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        TextView tvValue = ButterKnife.findById(layout, R.id.tvValue);
        tvValue.setText(value);
        ImageView ivIcon = ButterKnife.findById(layout, R.id.ivArrow);
        ivIcon.setBackground(null);
        ivIcon.setImageResource(rightDrawableId);
    }

    /**
     * Title、Icon、Icon
     *
     * @param context
     * @param layout
     * @param text
     * @param titleDrawID
     * @param drawableID
     * @param tintEnable  标题的图标是否染色
     */
    public static void titleIconIcon(Context context, ViewGroup layout, String text, int titleDrawID, int drawableID, boolean tintEnable) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tvTitle);
        tvTitle.setText(text);
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, titleDrawID), null, null, null);
        ImageView ivIcon = ButterKnife.findById(layout, R.id.ivArrow);
        ivIcon.setBackground(null);
        ivIcon.setImageResource(drawableID);
        if (!tintEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvTitle.setCompoundDrawableTintList(null);
            }
        }
    }


    /**
     * Title、value（可改变颜色、字体大小）
     *
     * @param context
     * @param layout
     * @param title
     * @param titleSize
     * @param titleColorResId
     * @param value
     * @param valueColorResId
     * @param valueSize
     */
    public static void titleValueCustom(Context context, ViewGroup layout, String title, int titleColorResId, int titleSize, String value, int valueColorResId, int valueSize) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tvTitle);
        TextView tvValue = ButterKnife.findById(layout, R.id.tvValue);
        tvTitle.setTextColor(ContextCompat.getColor(context, titleColorResId));
        tvValue.setTextColor(ContextCompat.getColor(context, valueColorResId));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, titleSize);
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, valueSize);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvTitle.setText(title);
        tvValue.setText(value);
    }
}
