package io.tanjundang.chat.base.api;

import io.reactivex.Observable;
import io.tanjundang.chat.base.entity.AddFriendResp;
import io.tanjundang.chat.base.entity.ChatMsgResp;
import io.tanjundang.chat.base.entity.CreateMomentsResp;
import io.tanjundang.chat.base.entity.GroupChatResp;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.entity.LoginResp;
import io.tanjundang.chat.base.entity.MomentsResp;
import io.tanjundang.chat.base.entity.QiNiuTokenResp;
import io.tanjundang.chat.base.network.HttpBaseBean;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    Observable<HttpBaseBean> delFriend(@Path("id") long id);

    /**
     * 处理好友请求
     *
     * @param friend_id
     * @param type      {@link io.tanjundang.chat.base.entity.type.HandleType}
     * @return
     */
    @PUT("friends/{id}")
    Observable<HttpBaseBean> handleFriendReq(@Path("id") long friend_id, @Query("type") int type);


    /**
     * 群聊列表
     *
     * @return
     */
    @GET("groups")
    Observable<GroupChatResp> getGroupList();

    /**
     * 新建群聊
     *
     * @param groupName
     * @return
     */
    @FormUrlEncoded
    @POST("groups")
    Observable<HttpBaseBean> openGroupChat(@Field("name") String groupName);

    /**
     * 拉人入群
     *
     * @param userId
     * @param group_id
     * @return
     */
    @PUT("groups/{id}")
    Observable<HttpBaseBean> joinGroup(@Path("id") long userId, @Query("group_id") long group_id);

    /**
     * 查看群员
     *
     * @param group_id
     * @return
     */
    @GET("groups/{id}")
    Observable<ChatMsgResp> getGroupMember(@Path("id") long group_id);

    /**
     * 获取七牛token
     *
     * @return
     */
    @GET("data/qiniu/token")
    Observable<QiNiuTokenResp> getQiNiuToken();

    /**
     * 查看朋友圈
     *
     * @param type
     * @return
     */
    @GET("moments")
    Observable<MomentsResp> getMoments(@Query("type") String type);

    /**
     * 发朋友圈
     * @param contents
     * @param picurls
     * @return
     */
    @FormUrlEncoded
    @POST("moments")
    Observable<CreateMomentsResp> addMoments(@Field("contents") String contents, @Field("pictures") String[] picurls);
}
