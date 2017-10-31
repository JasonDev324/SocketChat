package io.tanjundang.chat.base.entity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/31
 * @Description:
 */

public class SocketInitJson {


    private String code;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
