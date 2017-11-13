package io.tanjundang.chat.base.entity.type;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/26
 * @Description:
 */

public enum HandleType {

    ACCEPT(1),
    REJECT(0);

    int type;

    HandleType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
