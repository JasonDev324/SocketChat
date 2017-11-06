package io.tanjundang.chat.base.entity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/31
 * @Description:
 */

public class SocketMsgResp {

    private String code;
    private SocketMsgInfo data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SocketMsgInfo getData() {
        return data;
    }

    public void setData(SocketMsgInfo data) {
        this.data = data;
    }

    public static class SocketMsgInfo {
        //接收消息用到
        private int groupId;// 为0则为私聊
        private int userId;// 发送者id
        private String userName;// 发送者姓名
        private String groupName;

        //        发送消息用到
        private String chatType;// p2p: 私聊, group: 群聊
        private long id;//好友id或者群id
        private ContentMsg content;


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
    }

    public static class ContentMsg {
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
