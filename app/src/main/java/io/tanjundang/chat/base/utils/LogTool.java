package io.tanjundang.chat.base.utils;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.tanjundang.chat.BuildConfig;


/**
 * Author TanJunDang
 * Date 2015-07-21-11:50
 * Description:日志实体类
 * 待完善 文件输出控制
 */
public class LogTool {

    public static boolean ENABLE_LOG = BuildConfig.DEBUG_MODE;
    public static final String LOG_FILE_NAME = "Log.txt";//普通日志的文件路径
    public static final String CRASH_FILE_NAME = "TanJunDang.txt";//奔溃日志的文件路径
    private static final String LOGGER_PREFIX = "TJD_PROJECT_";
    public static String TAG = "TanJunDang.github.io";

    /**
     * 控制输出信息,通过TAG控制
     */
    public static void setTag() {
        ENABLE_LOG = Log.isLoggable(TAG, Log.VERBOSE);
    }

    /**
     * 该方法作用：打印崩溃信息并输出
     *
     * @param t
     */
    public static void logCrash(Throwable t) {
        if (t != null) {
            t.printStackTrace();
            String msg = t.getMessage();
            log2File(msg, CRASH_FILE_NAME);
        }
    }

    public static void v(String tag, Object... msg) {
        v(Log.VERBOSE, tag, msg);
    }

    public static void d(String tag, Object... msg) {
        v(Log.DEBUG, tag, msg);
    }

    public static void i(String tag, Object... msg) {
        v(Log.INFO, tag, msg);
    }

    public static void w(String tag, Object... msg) {
        v(Log.WARN, tag, msg);
    }

    public static void e(String tag, Object... msg) {
        v(Log.ERROR, tag, msg);
    }


    /**
     * 封装v
     *
     * @param priority 优先级
     * @param tag      标识
     * @param msg      打印信息
     */
    private static void v(int priority, String tag, Object... msg) {
        if (isEnableLog() && msg != null) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Object o : msg) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(o == null ? "null" : o.toString());
                i++;
            }

            int logStrLength = sb.length();
            int maxLogSize = Integer.MAX_VALUE;
            String subStr = "";
            subStr = sb.toString();
//            for (i = 0; i <= logStrLength / maxLogSize; i++) {
//                int start = i * maxLogSize;
//                int end = (i + 1) * maxLogSize;
//                end = end > logStrLength ? logStrLength : end;
//                subStr = sb.substring(start, end);
//            }

            /**
             * 封装Log相关方法
             */
            switch (priority) {
                case Log.VERBOSE:
                    Log.v(LOGGER_PREFIX + tag, subStr);
                    break;
                case Log.DEBUG:
                    Log.d(LOGGER_PREFIX + tag, subStr);
                    break;
                case Log.INFO:
                    Log.i(LOGGER_PREFIX + tag, subStr);
                    break;
                case Log.WARN:
                    Log.w(LOGGER_PREFIX + tag, subStr);
                    break;
                case Log.ERROR:
                    Log.e(LOGGER_PREFIX + tag, subStr);
                    break;
            }
        }
    }

    /**
     * 把logStr按照一定的格式输出到文件中
     *
     * @param
     */
    private static void log2File(String logStr, String fileName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
        String dateFormat = sdf.format(new Date());

        FileWriter fw = null;
        try {
            String filePath = Functions.getSDPath() + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            /**
             * 利用FileWriter输出
             */
            fw = new FileWriter(filePath, true);
            fw.write(dateFormat + ":" + logStr + "\r\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                fw = null;
            }
        }
    }

    private static boolean isEnableLog() {
        return ENABLE_LOG;
    }
}
