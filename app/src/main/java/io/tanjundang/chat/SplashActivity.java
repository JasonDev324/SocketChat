package io.tanjundang.chat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.tanjundang.chat.account.LoginActivity;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.entity.User;
import io.tanjundang.chat.base.utils.PermissionTool;
import io.tanjundang.chat.base.utils.SharePreTool;

/**
 * @Author: TanJunDang
 * @Date: 2017/6/21
 * @Description: 启动页面
 */

public class SplashActivity extends BaseActivity {

    private static final int TOTAL_SECOND = 2000;//显示几秒
    private static final int PERIOD_SECOND = 1000;//倒数间隔

    ArrayList<String> permissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionList.add(Manifest.permission.READ_CONTACTS);
        permissionList.add(Manifest.permission.CAMERA);
        permissionList.add(Manifest.permission.CALL_PHONE);
        Observable.interval(TOTAL_SECOND, PERIOD_SECOND, TimeUnit.MILLISECONDS).take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        final String token = SharePreTool.getSP(SplashActivity.this).getString(Constants.TOKEN);
                        final  long userId = SharePreTool.getSP(SplashActivity.this).getLong(Constants.USER_ID);
                        PermissionTool.getInstance(SplashActivity.this).requestPermissions(permissionList, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                if (userId != 0 && !TextUtils.isEmpty(token)) {
                                    intent.setClass(SplashActivity.this, MainActivity.class);
                                    Global.TOKEN = token;
                                    Global.getInstance().setUserId(userId);
                                } else {
                                    intent.setClass(SplashActivity.this, LoginActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionTool.getInstance(this).onRequestPermissionsResult(permissionList, requestCode, permissions, grantResults);
    }
}
