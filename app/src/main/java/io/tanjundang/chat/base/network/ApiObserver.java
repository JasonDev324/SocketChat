package io.tanjundang.chat.base.network;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.LogTool;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/16
 * @Description:
 */

public abstract class ApiObserver<T extends HttpBaseBean> implements Observer<T> {
    Disposable disposable;

    public abstract void onSuccess(T resp);

    public abstract void onFailure(String error);

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T resp) {
        if (resp.isSuccess()) {
            onSuccess(resp);
        } else {
            onFailure(resp.getMsg());
            Functions.toast(resp.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        LogTool.e(getClass().getName(), "返回错误信息:" + e);
        disposable.dispose();
        if (e instanceof ConnectException ||
                e instanceof SocketTimeoutException ||
                e instanceof TimeoutException) {
            //网络错误
            onFailure("请确保网络是否通畅");
        }else{
            onFailure(e.toString());
        }
    }

    @Override
    public void onComplete() {
        disposable.dispose();
    }
}
