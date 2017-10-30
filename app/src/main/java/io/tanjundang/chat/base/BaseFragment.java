package io.tanjundang.chat.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import io.tanjundang.chat.BuildConfig;
import io.tanjundang.chat.R;


/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date: 2016/4/3
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getName();
    protected final String AUTHOR = "TanJunDang";
    protected ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        设置progressDialog的样式需要在初始化的时候进行
        dialog = new ProgressDialog(getContext(), R.style.ProgressDialogStyle);
//        该方法不是用于修改ProgressStyle的样式，而是设置dialog形状
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("加载中....");
    }
}
