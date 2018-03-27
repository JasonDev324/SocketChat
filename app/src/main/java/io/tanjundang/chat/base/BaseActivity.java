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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
    }

}
