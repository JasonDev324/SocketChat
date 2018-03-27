package io.tanjundang.chat.base.network;

import android.app.ProgressDialog;
import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.utils.LogTool;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/16
 * @Description:
 */

public abstract class DialogApiObserver<T> implements Observer<T> {
    Disposable disposable;

    public abstract void onSuccess(T resp);

    public abstract void onFailure(String error);

    protected ProgressDialog dialog;

    public DialogApiObserver(Context mContext) {
        dialog = new ProgressDialog(mContext, R.style.ProgressDialogStyle);
//        该方法不是用于修改ProgressStyle的样式，而是设置dialog形状
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("加载中....");
        dialog.show();
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T resp) {
        onSuccess(resp);
    }

    @Override
    public void onError(Throwable e) {
        dialog.dismiss();
        LogTool.e(getClass().getName(), "错误信息:" + e.getMessage() + " cause：" + e.getCause());
        disposable.dispose();
    }

    @Override
    public void onComplete() {
        dialog.dismiss();
        disposable.dispose();
    }
}
