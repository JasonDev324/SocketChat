package io.tanjundang.chat.base.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Author: TanJunDang on 2015/10/27.
 * 内部数据 读写用 openFileOutput openFileInput
 * 外部数据 读写用 new FileInputStream FileOutputStream
 */
public class FileTool {

    public static final String TAG = "TanJunDang";
    public static final String FILE_NAME = "TanJunDang.txt";
    private static final String ENCODE = "UTF-8";

    public static boolean createFile(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogTool.d(TAG, "文件路径：" + file.getPath());
            return true;
        } else {
            LogTool.d(TAG, "文件路径：" + file.getPath());
            return false;
        }

    }

    public static boolean deleteFile(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            file.delete();
            return true;
        } else {
            Log.d(TAG, "文件不存在");
            return false;
        }
    }

    //向文件写入数据
    public static boolean setData(Context context, String filename, String content) {
        try {
            //调用openFileoutPut
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, ENCODE);
            osw.write(content);
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //获取文件内容
    public static String getData(Context context, String filename) {
        String content = "";
        try {
            FileInputStream fis = context.openFileInput(filename);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            content = EncodingUtils.getString(buffer, ENCODE);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 创建保存在SD卡里的文件
     *
     * @param context
     * @param filename
     * @return
     */
    public static boolean createExtraFile(Context context, String filename) {
        try {
            //当手机插入sd卡且有权限时
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                }
                return true;
            } else {
                LogTool.e(TAG, "手机SD卡不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存数据到SD卡
     *
     * @param context
     * @param filename
     * @param content
     * @return
     */
    public static boolean setExtraData(Context context, String filename, String content) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(content.getBytes());
                fos.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取SD卡里 文件的数据
     *
     * @param context
     * @param filename
     * @return
     */
    public static String getExtraData(Context context, String filename) {
        String result = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
                FileInputStream fis = new FileInputStream(path);
                int length = fis.available();
                byte[] buffer = new byte[length];
                fis.read(buffer);
                result = EncodingUtils.getString(buffer, ENCODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
