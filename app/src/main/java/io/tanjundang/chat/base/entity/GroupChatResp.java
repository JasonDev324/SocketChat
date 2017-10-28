package io.tanjundang.chat.base.entity;

import java.util.ArrayList;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/27
 * @Description:
 */

public class GroupChatResp extends HttpBaseBean {

    private ArrayList<GroupBean> data;

    public ArrayList<GroupBean> getData() {
        return data;
    }

    public void setData(ArrayList<GroupBean> data) {
        this.data = data;
    }

    public static class GroupBean {
        private int group_id;
        private GroupInfo group;

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public GroupInfo getGroup() {
            return group;
        }

        public void setGroup(GroupInfo group) {
            this.group = group;
        }


    }

    public static class GroupInfo {

        private int id;
        private String name;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
