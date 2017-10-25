package io.tanjundang.chat.base.entity;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/25
 * @Description:
 */

public class AddFriendResp extends HttpBaseBean {
    AddFriendInfo data;

    public AddFriendInfo getData() {
        return data;
    }

    public void setData(AddFriendInfo data) {
        this.data = data;
    }

    public static class AddFriendInfo {
        long add_id;

        public long getAdd_id() {
            return add_id;
        }

        public void setAdd_id(long add_id) {
            this.add_id = add_id;
        }
    }
}
