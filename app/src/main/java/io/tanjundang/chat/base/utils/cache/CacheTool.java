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
    private static final String GROUP_RECEIVE_MSG_INFO = "GROUP_RECEIVE_MSG_INFO";
    private static final String SEND_MSG_INFO = "SEND_MSG_INFO";
    private static final String GROUP_SEND_MSG_INFO = "GROUP_SEND_MSG_INFO";

    public static void release(Context context) {
        CacheModel.clear(context);
    }


    public static void saveUser(Context context, User data) {
        CacheModel.saveObject(context, ACCOUNT, data);
    }

    public static User loadUser(Context context) {
        return CacheModel.loadObject(context, ACCOUNT);
    }

    //接收消息
    public static void saveReceiveMsg(Context context, ArrayList<SocketMsgResp.SocketMsgInfo> list, long userId) {
        CacheModel.saveList(context, RECEIVE_MSG_INFO + userId, list);
    }

    public static ArrayList<SocketMsgResp.SocketMsgInfo> loadReceiveMsg(Context context, long userId) {
        return CacheModel.loadList(context, RECEIVE_MSG_INFO + userId);
    }

    public static void saveGroupReceiveMsg(Context context, ArrayList<SocketMsgResp.SocketMsgInfo> list, long groupId) {
        CacheModel.saveList(context, GROUP_RECEIVE_MSG_INFO + groupId, list);
    }

    public static ArrayList<SocketMsgResp.SocketMsgInfo> loadGroupReceiveMsg(Context context, long groupId) {
        return CacheModel.loadList(context, GROUP_RECEIVE_MSG_INFO + groupId);
    }

    //发送消息
    public static void saveSendMsg(Context context, ArrayList<SocketMsgResp.SocketMsgInfo> list, long friendId) {
        CacheModel.saveList(context, SEND_MSG_INFO + friendId, list);
    }

    public static ArrayList<SocketMsgResp.SocketMsgInfo> loadSendMsg(Context context, long friendId) {
        return CacheModel.loadList(context, SEND_MSG_INFO + friendId);
    }

    public static void saveGroupSendMsg(Context context, ArrayList<SocketMsgResp.SocketMsgInfo> list, long groupId) {
        CacheModel.saveList(context, GROUP_SEND_MSG_INFO + groupId, list);
    }

    public static ArrayList<SocketMsgResp.SocketMsgInfo> loadGroupSendMsg(Context context, long groupId) {
        return CacheModel.loadList(context, GROUP_SEND_MSG_INFO + groupId);
    }
}
