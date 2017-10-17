package io.tanjundang.chat.base.api;

import io.reactivex.Observable;
import io.tanjundang.chat.base.entity.LoginResp;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/21
 */

public interface BusinessApi {

    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<LoginResp> login(@Field("email") String email, @Field("password") String password);

    /**
     * 注册
     *
     * @param name
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("register")
    Observable<LoginResp> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);
}
