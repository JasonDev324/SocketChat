package io.tanjundang.chat.base.network;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.LogTool;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/16
 * @Description:
 */

public abstract class ApiObserver<T> implements Observer<T> {
    Disposable disposable;

    public abstract void onSuccess(T resp);

    public abstract void onFailure(String error);

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
        onFailure(e.getMessage());
        LogTool.e(getClass().getName(), "Cause:" + e.getMessage());
        Functions.toast("网络不通畅,请检查您的网络是否正常连接");
        disposable.dispose();
    }

    @Override
    public void onComplete() {
        disposable.dispose();
    }
}
