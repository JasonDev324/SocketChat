package io.tanjundang.chat.base.utils.cache;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @Author: TanJunDang
 * @Date: 2017/6/19
 * @Description: 将缓存保存到/data/cache/BUFFER目录下
 */

public abstract class CacheModel {

    private static final String FOLDER = "BUFFER";

    public static void saveList(Context context, String fileName, ArrayList data) {
        new DataCache<>().saveList(context, fileName, data);
    }

    public static ArrayList loadList(Context context, String fileName) {
        return new DataCache<>().loadList(context, fileName);
    }

    public static <T> T loadObject(Context context, String fileName) {
        return new DataCache<>().loadObject(context, fileName);
    }

    public static void saveObject(Context context, String fileName, Object data) {
        new DataCache<>().saveObject(context, fileName, data);
    }

    public static void clear(Context context) {
        File fileDir = new File(context.getCacheDir() + "/" + FOLDER);
        if (fileDir.listFiles() == null) return;
        for (int i = 0; i < fileDir.listFiles().length; i++) {
            fileDir.listFiles()[i].delete();
        }
    }

    static class DataCache<T> {

        private void save(Context context, String folder, String fileName, Object data) {
            if (context == null) return;

            if (TextUtils.isEmpty(folder)) {
                folder = FOLDER;
            }

            File fileDir = new File(context.getCacheDir().getPath(), folder);
//          判断该目录是否存在，是否是文件
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            File file = new File(fileDir, fileName);
            if (file.exists()) {
                file.delete();
            }

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(data);
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void saveObject(Context context, String fileName, Object data) {
            save(context, FOLDER, fileName, data);
        }

        private <T> T loadObject(Context context, String fileName) {
            File file = new File(context.getCacheDir() + "/" + FOLDER, fileName);
            T data = null;
            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (T) ois.readObject();
                    ois.close();
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return data;
        }


        private void saveList(Context context, String fileName, ArrayList<T> data) {
            save(context, FOLDER, fileName, data);
        }

        private ArrayList<T> loadList(Context context, String fileName) {
            ArrayList<T> data = new ArrayList<>();
            File file = new File(context.getCacheDir() + "/" + FOLDER, fileName);
            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (ArrayList<T>) ois.readObject();
                    ois.close();
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return data;
        }

    }

}
