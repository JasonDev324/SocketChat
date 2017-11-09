package io.tanjundang.chat.base.utils.cache;

import android.content.Context;

import java.util.ArrayList;

import io.tanjundang.chat.base.entity.SocketMsgResp;
import io.tanjundang.chat.base.entity.User;

/**
 * @Author: TanJunDang
 * @Date: 2017/6/19
 * @Description: 缓存工具 CacheModel
 * 必须所有类实现序列化接口
 */

public class CacheTool {
    private static final String ACCOUNT = "ACCOUNT";
    private static final String CACHE = "CACHE";
    private static final String RECEIVE_MSG_INFO = "RECEIVE_MSG_INFO";
    private static final String SEND_MSG_INFO = "SEND_MSG_INFO";

    public static void release(Context context) {
        CacheModel.clear(context);
    }


    public static void saveUser(Context context, User data) {
        CacheModel.saveObject(context, ACCOUNT, data);
    }

    public static User loadUser(Context context) {
        return CacheModel.loadObject(context, ACCOUNT);
    }


    public static void saveReceiveMsg(Context context, ArrayList<SocketMsgResp.SocketMsgInfo> list) {
        CacheModel.saveList(context, RECEIVE_MSG_INFO, list);
    }

    public static ArrayList<SocketMsgResp.SocketMsgInfo> loadReceiveMsg(Context context) {
        return CacheModel.loadList(context, RECEIVE_MSG_INFO);
    }

    public static void saveSendMsg(Context context, ArrayList<SocketMsgResp.SocketMsgInfo> list) {
        CacheModel.saveList(context, SEND_MSG_INFO, list);
    }

    public static ArrayList<SocketMsgResp.SocketMsgInfo> loadSendMsg(Context context) {
        return CacheModel.loadList(context, SEND_MSG_INFO);
    }
}
