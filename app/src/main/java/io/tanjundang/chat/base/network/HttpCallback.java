package io.tanjundang.chat.base.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/22
 */

public abstract class HttpCallback<T> implements Callback {
    protected abstract void onSuccess(T resp);

    protected abstract void onFailure(String error);

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                T resp = (T) response.body();
                onSuccess(resp);
            } else {
                throw new RuntimeException("response is must be not null");
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        onFailure(t.getMessage() + t.getCause());
    }
}
