package io.tanjundang.chat.base.entity;

import io.tanjundang.chat.base.network.HttpBaseBean;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/17
 * @Description:
 */

public class LoginResp extends HttpBaseBean {

    LoginInfo data;

    public LoginInfo getData() {
        return data;
    }

    public void setData(LoginInfo data) {
        this.data = data;
    }

    public static class LoginInfo {
        String name;
        String email;
        String api_token;
        String inner_password;
        long id;

        public String getInner_password() {
            return inner_password;
        }

        public void setInner_password(String inner_password) {
            this.inner_password = inner_password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getApi_token() {
            return api_token;
        }

        public void setApi_token(String api_token) {
            this.api_token = api_token;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}
