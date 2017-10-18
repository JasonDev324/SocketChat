package io.tanjundang.chat.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.tanjundang.chat.account.LoginActivity;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.GlideTool;
import io.tanjundang.chat.base.utils.ImageLoaderTool;
import io.tanjundang.chat.base.utils.SharePreTool;


/**
 * Author: Orcish on 2015/10/28.
 * 初始化全局配置
 * 导入so文件，在src/main 下面新建一个jniLibs文件夹 放入文件即可
 */
public class BaseApplication extends Application {
    public static String AUTHOR = "TanJunDang";

    @Override
    public void onCreate() {
        super.onCreate();
        Functions.init(getApplicationContext());
        ImageLoaderTool.initImageLoader(getApplicationContext());
        Stetho.initializeWithDefaults(this);
        GlideTool.init(getApplicationContext());

        String token = SharePreTool.getSP(this).getString(Constants.TOKEN);
        String nickname = SharePreTool.getSP(this).getString(Constants.NICKNAME);
        String email = SharePreTool.getSP(this).getString(Constants.EMAIL);
        long userId = SharePreTool.getSP(this).getLong(Constants.USER_ID);
        Global.TOKEN = token;
        Global.getInstance().setNickname(nickname);
        Global.getInstance().setEmail(email);
        Global.getInstance().setUserId(userId);
    }
}
