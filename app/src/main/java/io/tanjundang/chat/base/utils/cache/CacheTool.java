package io.tanjundang.chat.base.utils.cache;

import android.content.Context;

import io.tanjundang.chat.base.entity.SocketMsgResp;
import io.tanjundang.chat.base.entity.User;

/**
 * @Author: TanJunDang
 * @Date: 2017/6/19
 * @Description: 缓存工具 CacheModel调用
 */

public class CacheTool {
    private static final String ACCOUNT = "ACCOUNT";
    private static final String CACHE = "CACHE";
    private static final String MSG_INFO = "MSG_INFO";

    public static void release(Context context) {
        CacheModel.clear(context);
    }


    public static void saveUser(Context context, User data) {
        CacheModel.saveObject(context, ACCOUNT, data);
    }

    public static User loadUser(Context context) {
        return CacheModel.loadObject(context, ACCOUNT);
    }


    public static void saveMsgInfo(Context context, SocketMsgResp.SocketMsgInfo info){
    }

}
