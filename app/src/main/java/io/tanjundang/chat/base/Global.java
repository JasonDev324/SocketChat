package io.tanjundang.chat.base;

import android.content.Context;

import io.tanjundang.chat.BuildConfig;
import io.tanjundang.chat.base.entity.User;
import io.tanjundang.chat.base.utils.SharePreTool;
import io.tanjundang.chat.base.utils.cache.CacheTool;


/**
 * Author: TanJunDang
 * Date: 2015/10/29.
 * Email:TanJunDang@126.com
 * 全局变量
 */
public class Global {
    public static boolean DEBUG = BuildConfig.DEBUG_MODE;
    public static String TOKEN = "";

    String nickname;
    long userId;
    String email;

    static class Holder {
        static Global INSTANCE = new Global();
    }

    public static Global getInstance() {
        return Holder.INSTANCE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void release(Context mContext) {
        SharePreTool.getSP(mContext.getApplicationContext()).clear();
        CacheTool.release(mContext.getApplicationContext());
    }
}
