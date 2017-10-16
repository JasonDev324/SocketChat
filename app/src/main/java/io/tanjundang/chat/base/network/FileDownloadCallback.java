package io.tanjundang.chat.base.network;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/24
 * 文件下载接口
 */

public interface FileDownloadCallback {
    /**
     * @param current 当前进度
     * @param total   总进度
     */
    void onProgress(int current, int total);

    /**
     * @param path 文件路径
     * @param name 文件名
     */
    void onSuccess(String path, String name);

    /**
     * @param errorMsg 错误信息
     */
    void onFailure(String errorMsg);
}
