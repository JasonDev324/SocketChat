package io.tanjundang.chat.base.entity;

import java.util.ArrayList;

/**
 * @Author: TanJunDang
 * @Date: 2017/11/22
 * @Description:
 */

public class SocketOfflineMsg extends SocketBaseBean {
    ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
