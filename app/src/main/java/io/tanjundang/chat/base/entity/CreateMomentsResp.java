package io.tanjundang.chat.base.entity;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/18
 * @Description:
 */

public class CreateMomentsResp extends HttpBaseBean {

    MomentsResp.MomentsItemInfo data;

    public MomentsResp.MomentsItemInfo getData() {
        return data;
    }

    public void setData(MomentsResp.MomentsItemInfo data) {
        this.data = data;
    }
}
