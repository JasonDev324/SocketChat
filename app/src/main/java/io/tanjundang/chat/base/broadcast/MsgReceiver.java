package io.tanjundang.chat.base.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import io.tanjundang.chat.R;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.entity.SocketMsgResp;
import io.tanjundang.chat.base.entity.type.ChatType;
import io.tanjundang.chat.talk.ChatTestActivity;

/**
 * @Author: TanJunDang
 * @Date: 2017/11/22
 * @Description:
 */

public class MsgReceiver extends BroadcastReceiver {
    public static final String MSG_ACTION = "io.tanjundang.github.msg.receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        SocketMsgResp.SocketMsgInfo info = (SocketMsgResp.SocketMsgInfo) intent.getSerializableExtra(Constants.DATA);
        if (info == null) return;

        boolean isGroup = info.getChatType().equals("p2p") ? false : true;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker("收到消息")
                .setSmallIcon(R.drawable.hecha)//图标
                .setContentTitle(isGroup == true ? info.getGroupName() : info.getUserName())
                .setContentText(info.getContent().getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        Intent myIntent = new Intent(context, ChatTestActivity.class);
        myIntent.putExtra(Constants.DATA, isGroup ? info.getGroupName() : info.getUserName());
        myIntent.putExtra(Constants.TYPE, isGroup ? ChatType.GROUP : ChatType.P2P);
        myIntent.putExtra(Constants.ID, isGroup ? info.getGroupId() : info.getUserId());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);//装载Intent
        notification.contentIntent = pendingIntent;//初始化PedingIntent
//        builder.mNotification不能显示标题内容
        manager.notify(0, notification);
    }
}
