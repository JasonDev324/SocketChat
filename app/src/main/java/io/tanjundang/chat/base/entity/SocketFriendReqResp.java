package io.tanjundang.chat.base.entity;

import java.io.Serializable;

import io.tanjundang.chat.base.entity.type.HandleType;

/**
 * @Author: TanJunDang
 * @Date: 2017/11/11
 * @Description:
 */

public class SocketFriendReqResp extends SocketBaseBean {

    private FriendReqInfo data;

    public FriendReqInfo getData() {
        return data;
    }

    public void setData(FriendReqInfo data) {
        this.data = data;
    }

    public static class FriendReqInfo implements Serializable {

        private String type;
        private long id;
        private String name;
        private String content;
        private long time;
//        0拒绝、1同意
        /**
         * {@link io.tanjundang.chat.base.entity.type.HandleType}
         */
        private int isAccept;

        public boolean isAddFriendSuccess() {
            return HandleType.ACCEPT.getType() == isAccept ? true : false;
        }

        public int getIsAccept() {
            return isAccept;
        }

        public void setIsAccept(int isAccept) {
            this.isAccept = isAccept;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
