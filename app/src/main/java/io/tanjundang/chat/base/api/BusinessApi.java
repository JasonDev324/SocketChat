package io.tanjundang.chat.base.api;

import io.reactivex.Observable;
import io.tanjundang.chat.base.entity.AddFriendResp;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.entity.LoginResp;
import io.tanjundang.chat.base.network.HttpBaseBean;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * 获取好友列表
     *
     * @return
     */
    @GET("friends")
    Observable<FriendsResp> getFriends();

    /**
     * 获取好友请求列表
     *
     * @return
     */
    @GET("friends")
    Observable<FriendsResp> getFriends(@Query("status") String status);

    /**
     * 添加好友
     *
     * @param email
     * @return
     */
    @FormUrlEncoded
    @POST("friends")
    Observable<AddFriendResp> addFriend(@Field("email") String email);

    /**
     * 删除好友
     *
     * @param id
     * @return
     */
    @DELETE("friends/{id}")
    Observable<AddFriendResp> delFriend(@Path("id") long id);

    /**
     * 处理好友请求
     *
     * @param id
     * @param type
     * @return
     */
    @PUT("friends/{id}")
    Observable<HttpBaseBean> handleFriendReq(@Path("id") long id, @Query("type") int type);


}
