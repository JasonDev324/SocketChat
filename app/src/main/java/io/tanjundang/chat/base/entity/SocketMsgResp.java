package io.tanjundang.chat.base.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/31
 * @Description:
 */

public class SocketMsgResp extends SocketBaseBean {
    private SocketMsgInfo data;

    public SocketMsgInfo getData() {
        return data;
    }

    public void setData(SocketMsgInfo data) {
        this.data = data;
    }

    public static class SocketMsgInfo implements Serializable, Comparable<SocketMsgInfo> {
        //接收消息用到
        private int groupId;// 为0则为私聊
        private int userId;// 发送者id
        private String userName;// 发送者姓名
        private String groupName;

        //        发送消息用到
        private String chatType;// p2p: 私聊, group: 群聊
        private long id;//好友id或者群id

        private ContentMsg content;
        long time;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getChatType() {
            return chatType;
        }

        public void setChatType(String chatType) {
            this.chatType = chatType;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public ContentMsg getContent() {
            return content;
        }

        public void setContent(ContentMsg content) {
            this.content = content;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SocketMsgInfo that = (SocketMsgInfo) o;

            if (groupId != that.groupId) return false;
            if (userId != that.userId) return false;
            if (id != that.id) return false;
            if (time != that.time) return false;
            if (userName != null ? !userName.equals(that.userName) : that.userName != null)
                return false;
            if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null)
                return false;
            if (chatType != null ? !chatType.equals(that.chatType) : that.chatType != null)
                return false;
            return content != null ? content.equals(that.content) : that.content == null;
        }

        @Override
        public int hashCode() {
            int result = groupId;
            result = 31 * result + userId;
            result = 31 * result + (userName != null ? userName.hashCode() : 0);
            result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
            result = 31 * result + (chatType != null ? chatType.hashCode() : 0);
            result = 31 * result + (int) (id ^ (id >>> 32));
            result = 31 * result + (content != null ? content.hashCode() : 0);
            result = 31 * result + (int) (time ^ (time >>> 32));
            return result;
        }

        @Override
        public int compareTo(@NonNull SocketMsgInfo o) {
            if (this.getTime() > o.getTime()) {
                return 1;
            } else if (this.getTime() < o.getTime()) {
                return -1;
            }
            return 0;
        }
    }

    public static class ContentMsg implements Serializable {
        private String contentType;// txt: 文本, audio: 音频, video: 视频
        private String body;

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

}
