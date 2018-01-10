package io.tanjundang.chat.base.entity;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/10
 * @Description:
 */

public class QiNiuTokenResp extends HttpBaseBean {

    private TokenInfo data;

    public TokenInfo getData() {
        return data;
    }

    public void setData(TokenInfo data) {
        this.data = data;
    }

    public static class TokenInfo {

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
