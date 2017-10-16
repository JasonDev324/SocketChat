package io.tanjundang.chat.base;

import io.tanjundang.chat.BuildConfig;


/**
 * Author: TanJunDang
 * Date: 2015/10/29.
 * Email:TanJunDang@126.com
 * 全局变量
 * 使数据在不同的地方调用
 */
public class Global {
    public static boolean DEBUG = BuildConfig.DEBUG_MODE;
    public static String TOKEN = "";


    static class Holder {
        static Global INSTANCE = new Global();
    }

    public static Global getInstance() {
        return Holder.INSTANCE;
    }


}
