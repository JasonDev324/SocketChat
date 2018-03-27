package io.tanjundang.chat.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.GlideTool;
import io.tanjundang.chat.base.utils.ImageLoaderTool;
import io.tanjundang.chat.base.utils.SharePreTool;


/**
 * @Author: TanJunDang
 * @Date: 2017/10/20
 * @Description: socket通信参考 http://www.voidcn.com/article/p-rbowyrhj-va.html
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
        String qiNiuToken = SharePreTool.getSP(this).getString(Constants.QINIU_TOKEN);
        long userId = SharePreTool.getSP(this).getLong(Constants.USER_ID);
        Global.TOKEN = token;
        Global.getInstance().setNickname(nickname);
        Global.getInstance().setEmail(email);
        Global.getInstance().setUserId(userId);
        Global.getInstance().setQiniuToken(qiNiuToken);
        CrashHandler.getInstance().init(getApplicationContext());
    }
}
