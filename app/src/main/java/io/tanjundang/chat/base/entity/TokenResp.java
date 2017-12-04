package io.tanjundang.chat.base.entity;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Date: 2017/11/27
 * @Description:
 */

public class TokenResp extends HttpBaseBean {

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
