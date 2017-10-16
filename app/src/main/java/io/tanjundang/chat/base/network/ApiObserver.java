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

public class ApiObserver implements Observer {
    Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(Object value) {

    }

    @Override
    public void onError(Throwable e) {
        LogTool.e(getClass().getName(), "Cause:" + e.getMessage());
        Functions.toast("网络不通畅,请检查您的网络是否正常连接");
        disposable.dispose();
    }

    @Override
    public void onComplete() {
        disposable.dispose();
    }
}
