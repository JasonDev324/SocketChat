package io.tanjundang.chat.base.entity;

import java.util.ArrayList;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/11
 * @Description:
 */

public class MomentsResp extends HttpBaseBean {

    private MomentsInfo data;

    public MomentsInfo getData() {
        return data;
    }

    public void setData(MomentsInfo data) {
        this.data = data;
    }

    public static class MomentsInfo {

        private int current_page;
        private int from;
        private int last_page;
        private String next_page_url;
        private String path;
        private int per_page;
        private String prev_page_url;
        private int to;
        private int total;
        private ArrayList<MomentsItemInfo> data;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public String getNext_page_url() {
            return next_page_url;
        }

        public void setNext_page_url(String next_page_url) {
            this.next_page_url = next_page_url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public String getPrev_page_url() {
            return prev_page_url;
        }

        public void setPrev_page_url(String prev_page_url) {
            this.prev_page_url = prev_page_url;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public ArrayList<MomentsItemInfo> getData() {
            return data;
        }

        public void setData(ArrayList<MomentsItemInfo> data) {
            this.data = data;
        }

    }

    public static class MomentsItemInfo {

        private int id;
        private int user_id;
        private String content;
        private String created_at;
        private ArrayList<String> pictures;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public ArrayList<String> getPictures() {
            return pictures;
        }

        public void setPictures(ArrayList<String> pictures) {
            this.pictures = pictures;
        }
    }
}
