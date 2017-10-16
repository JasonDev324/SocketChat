package io.tanjundang.chat.base.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import io.tanjundang.chat.base.utils.GlideTool;


public class CommonHolder extends RecyclerView.ViewHolder {
    View rootview;
    SparseArray<View> viewList;
    Context context;

    public CommonHolder(Context context, View itemview) {
        super(itemview);
        this.context = context;
        rootview = itemview;
        viewList = new SparseArray<>();
    }

    /**
     * 缓存一个view，findId
     *
     * @param viewId
     * @param <E>
     * @return
     */
    public <E extends View> E getView(int viewId) {
        View view = viewList.get(viewId);
        if (view == null) {
            view = rootview.findViewById(viewId);
            viewList.put(viewId, view);
        }
        return (E) view;
    }

    public View getRootview() {
        return rootview;
    }

    public CommonHolder setText(int viewId, String text, View.OnClickListener listener) {
        TextView tvText = getView(viewId);
        tvText.setText(text);
        if (listener != null) tvText.setOnClickListener(listener);
        return this;
    }

    /**
     * 富文本
     *
     * @param viewId
     * @param text
     * @param listener
     * @return
     */
    public CommonHolder setText(int viewId, Spanned text, View.OnClickListener listener) {
        TextView tvText = getView(viewId);
        tvText.setText(text);
        if (listener != null) tvText.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setText(int viewId, String text, int color, View.OnClickListener listener) {
        TextView tvText = getView(viewId);
        tvText.setText(text);
        tvText.setTextColor(ContextCompat.getColor(context, color));
        if (listener != null) tvText.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setCheckListener(int viewId, String text, View.OnClickListener listener) {
        CheckedTextView tvText = getView(viewId);
        tvText.setText(text);
        if (listener != null) tvText.setOnClickListener(listener);
        return this;
    }


    public CommonHolder setImageResource(int viewId, String url, View.OnClickListener listener) {
        ImageView ivImage = getView(viewId);
        if (!TextUtils.isEmpty(url)) {
            GlideTool.getInstance().loadImage(ivImage, url);
        }
        if (listener != null) ivImage.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setImageResource(int viewId, String url, int errorDrawId, View.OnClickListener listener) {
        ImageView ivImage = getView(viewId);
        if (!TextUtils.isEmpty(url)) {
            GlideTool.getInstance().loadImage(ivImage, url);
        } else {
            ivImage.setImageResource(errorDrawId);
        }
        if (listener != null) ivImage.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setImageResource(int viewId, int drawableId, View.OnClickListener listener) {
        ImageView ivImage = getView(viewId);
        GlideTool.getInstance().loadLocalImage(ivImage,drawableId);
        if (listener != null) ivImage.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setImageResource(int viewId, View.OnClickListener listener) {
        ImageView ivImage = getView(viewId);
        if (listener != null) ivImage.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setLayoutClick(int layoutId, View.OnClickListener listener) {
        ViewGroup layout = getView(layoutId);
        if (listener != null)
            layout.setOnClickListener(listener);
        return this;
    }

    public CommonHolder itemClick(View.OnClickListener listener) {
        if (listener != null) rootview.setOnClickListener(listener);
        return this;
    }

}