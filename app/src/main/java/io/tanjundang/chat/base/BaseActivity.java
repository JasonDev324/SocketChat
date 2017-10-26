package io.tanjundang.chat.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;


import io.tanjundang.chat.BuildConfig;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.network.ApiObserver;


/**
 * Author: TanJunDang
 * Email: TanJunDang324@gmail.com
 * Date: 2016/4/3
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getName();
    protected final String AUTHOR = "TanJunDang";

    protected ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
//        设置progressDialog的样式需要在初始化的时候进行
        dialog = new ProgressDialog(this, R.style.ProgressDialogStyle);
//        该方法不是用于修改ProgressStyle的样式，而是设置dialog形状
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("加载中....");
    }

}
