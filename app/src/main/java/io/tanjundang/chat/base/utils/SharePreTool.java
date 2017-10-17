package io.tanjundang.chat.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Developer: TanJunDang
 * Date: 2016/2/23
 * Email: TanJunDang@126.com
 * SharePreference工具封装
 */
public class SharePreTool {

    public static SharePreTool mPreUtil;
    private static SharedPreferences mSharePreference;
    private static final String FILE_NAME = "tjd_chat";
    private static SharedPreferences.Editor editor;
    private static String fileName = ""; //用于找到自定义文件或者默认文件

    /**
     * 默认保存的文件
     *
     * @param context
     * @return
     */
    public static SharePreTool getSP(Context context) {
        if (mPreUtil == null || mSharePreference == null || editor == null || !fileName.equals(FILE_NAME)) {
            mPreUtil = new SharePreTool();
            mSharePreference = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            editor = mSharePreference.edit();
            fileName = FILE_NAME;
        }
        return mPreUtil;
    }

    /**
     * 获取文件名为name的SP
     *
     * @param context
     * @param name
     * @return
     */
    public static SharePreTool getSP(Context context, String name) {
        if (mPreUtil == null || mSharePreference == null || editor == null || !fileName.equals(name)) {
            mPreUtil = new SharePreTool();
            mSharePreference = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
            editor = mSharePreference.edit();
            fileName = name;
        }
        return mPreUtil;
    }


    /**
     * 保存String的值
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取String的值
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return mSharePreference.getString(key, "");
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        return mSharePreference.getString(key, defaultValue);
    }

    /**
     * 保存Boolean的值
     *
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }


    public boolean getBoolean(String key) {
        return mSharePreference.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharePreference.getBoolean(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return mSharePreference.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return mSharePreference.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return mSharePreference.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return mSharePreference.getLong(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key) {
        return mSharePreference.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        return mSharePreference.getFloat(key, defaultValue);
    }

    /**
     * 判断是否存在此字段
     */
    public boolean contains(String key) {
        return mSharePreference.contains(key);
    }

    /**
     * 删除mSharePreference文件中对应的Key和value
     */
    public boolean remove(String key) {
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.remove(key);
        return editor.commit();
    }

    public boolean clear() {
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.clear();
        return editor.commit();
    }

}
