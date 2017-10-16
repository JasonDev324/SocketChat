package io.tanjundang.chat.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

import io.tanjundang.chat.R;

/**
 * @Author: TanJunDang
 * @Date: 2017/7/27
 * @Description:
 */

public class GlideTool {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        if (mContext == null) {
            throw new RuntimeException("GlideTool need to be init");
        }
    }

    static class Holder {
        static final GlideTool INSTANCE = new GlideTool();
    }

    public static GlideTool getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 默认图为app Logo
     *
     * @param imageView
     * @param url
     */
    public void loadImage(ImageView imageView, String url) {
        Glide.with(mContext).load(url).centerCrop().error(R.mipmap.google_app).into(imageView);
    }

    public void loadImage(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(mContext)
                    .load(stream.toByteArray())
                    .placeholder(R.mipmap.google_app)
                    .into(imageView);
        }
    }

    public void loadImage(ImageView imageView, String url, int width, int height) {
        Glide.with(mContext).load(url).centerCrop().error(R.mipmap.google_app).crossFade().override(width, height).into(imageView);
    }

    /**
     * 加载大图
     *
     * @param imageView
     * @param url
     */
    public void loadBigImage(ImageView imageView, String url) {
        Glide.with(mContext).load(url).centerCrop().placeholder(R.mipmap.google_app).error(R.mipmap.google_app).crossFade().into(imageView);
    }

    /**
     * @param imageView
     * @param errorDrawId
     * @param url
     */
    public void loadBigImage(ImageView imageView, int errorDrawId, String url) {
        Glide.with(mContext).load(url).centerCrop().placeholder(R.mipmap.google_app).error(errorDrawId).crossFade().into(imageView);
    }

    /**
     * 加载本地drawable
     *
     * @param imageView
     * @param drawableId
     */
    public void loadLocalImage(ImageView imageView, int drawableId) {
        Glide.with(mContext).load(drawableId).centerCrop().crossFade().error(R.mipmap.google_app).into(imageView);
    }

}
