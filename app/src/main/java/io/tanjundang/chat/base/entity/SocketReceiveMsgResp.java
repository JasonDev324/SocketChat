package io.tanjundang.chat.base.entity;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2017/10/31
 * @Description:
 */

public class SocketReceiveMsgResp {

    private String code;
    private ReceiveMsgInfo data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReceiveMsgInfo getData() {
        return data;
    }

    public void setData(ReceiveMsgInfo data) {
        this.data = data;
    }

    public static class ReceiveMsgInfo {

        private int groupId;
        private int userId;
        private String userName;
        private ContentMsg content;

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

        public ContentMsg getContent() {
            return content;
        }

        public void setContent(ContentMsg content) {
            this.content = content;
        }

    }
    // 为0则为私聊// txt: 文本, audio: 音频, video: 视频

}
