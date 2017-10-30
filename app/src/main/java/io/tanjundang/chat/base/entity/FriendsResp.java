package io.tanjundang.chat.base.entity;

import java.io.Serializable;
import java.util.ArrayList;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/25
 * @Description:
 */

public class FriendsResp extends HttpBaseBean {

    ArrayList<FriendsInfo> data = new ArrayList<>();

    public ArrayList<FriendsInfo> getData() {
        return data;
    }

    public void setData(ArrayList<FriendsInfo> data) {
        this.data = data;
    }

    public static class FriendsInfo implements Serializable{
        long id;
        long friend_id;
        String email;
        String name;
        String chat_key;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(long friend_id) {
            this.friend_id = friend_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getChat_key() {
            return chat_key;
        }

        public void setChat_key(String chat_key) {
            this.chat_key = chat_key;
        }
    }

}
