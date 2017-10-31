package io.tanjundang.chat.base.entity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/31
 * @Description:
 */

public class SocketMsgJson {

    private String code;
    private SocketSendInfo data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SocketSendInfo getData() {
        return data;
    }

    public void setData(SocketSendInfo data) {
        this.data = data;
    }

    public static class SocketSendInfo {
        private String chatType;
        private long id;
        private ContentMsg content;

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


    // p2p: 私聊, group: 群聊
    // 接收的好友或群id
    // txt: 文本, audio: 音频, video: 视频

}
