package io.tanjundang.chat.base.entity;

import java.util.ArrayList;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/30
 * @Description:
 */

public class ChatMsgResp extends HttpBaseBean {

    private ArrayList<ChatMsgInfo> data;

    public ArrayList<ChatMsgInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ChatMsgInfo> data) {
        this.data = data;
    }

    public static class ChatMsgInfo {

        private int id;
        private int group_id;
        private int user_id;
        private User user;

        /**
         * 0-普通item、1-添加item
         */
        int layoutType;

        public int getLayoutType() {
            return layoutType;
        }

        public void setLayoutType(int layoutType) {
            this.layoutType = layoutType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }


    }
}
