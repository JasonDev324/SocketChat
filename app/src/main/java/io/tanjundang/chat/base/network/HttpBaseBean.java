package io.tanjundang.chat.base.network;


/**
 * @Author: TanJunDang
 * @Date: 2017/10/17
 * @Description:
 */

public class HttpBaseBean {
    int status;
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
