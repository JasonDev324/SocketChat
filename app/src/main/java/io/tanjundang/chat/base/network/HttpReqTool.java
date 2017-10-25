package io.tanjundang.chat.base.network;

import android.text.TextUtils;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.tanjundang.chat.BuildConfig;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.utils.LogTool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/22
 * 请求工具
 */


public class HttpReqTool {

    private Retrofit retrofit;
    //    此处修改host
    private static int CONNECT_TIME_OUT = 15;

    private static class Holder {
        private static HttpReqTool INSTANCE = new HttpReqTool();
    }

    public static HttpReqTool getInstance() {
        return Holder.INSTANCE;
    }

    HttpReqTool() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOKHttpClient())
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    /**
     * Log打印拦截器
     */
    class HttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogTool.d(LogTool.TAG, getClass().getName() + "\n" + message);
        }
    }

    private OkHttpClient getOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(logInterceptor);
//        设置超时时间

        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                // 添加新的参数
                if (TextUtils.isEmpty(Global.TOKEN)) {
                    request = request
                            .newBuilder()
                            .build();
                } else {
//                    设置公共请求参数
                    HttpUrl.Builder authorizedUrlBuilder = request.url()
                            .newBuilder()
                            .scheme(request.url().scheme())
                            .host(request.url().host())
                            .addQueryParameter(Constants.TOKEN, Global.TOKEN);
//                            .addQueryParameter(Constants.USER_ID, String.valueOf(Global.getInstance().getUserId()));

                    request = request
                            .newBuilder()
                            .url(authorizedUrlBuilder.build())
//                        添加头部信息(共同请求参数)
//                            .addHeader(Constants.TOKEN, Global.TOKEN)
//                            .addHeader(Constants.USER_ID, String.valueOf(Global.getInstance().getUserId()))
                            .build();
                }

                return chain.proceed(request);
            }
        });
        return builder.build();
    }

    public <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }


    public <T> T createApiWithBaseUrl(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOKHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

    /**
     * 文件下载
     *
     * @param url      待下载url
     * @param savePath 保存路径
     * @param fileName 文件名
     * @param listener 回调
     */
    public void fileDownload(String url, String savePath, String fileName, FileDownloadListener listener) {
        FileDownloader
                .getImpl()
                .create(url)
                .setPath(savePath + fileName)
                .setListener(listener)
                .start();
    }

}
