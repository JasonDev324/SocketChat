package io.tanjundang.chat.base.model;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Developer: TanJunDang
 * Date: 2016/2/15
 * Email: TanJunDang324@126.com
 * 用于缓存数据，实体要实现序列化
 */
public abstract class BaseModel {

    protected static final String FOLDER = "BUFFER"; //存放数据的目录/data/data/<packagename>/files

    public static class DataCache<T> {

        /**
         * @param context  上下文 用于获取根目录
         * @param filename 保存数据的文件名
         * @param folder   文件存放的目录
         * @param data     待保存的数据
         */
        private void save(Context context, String filename, String folder, Object data) {

            //1.检查目录是否存在，不存在则创建
            File fileDir = new File(context.getFilesDir(), folder);
            if (!fileDir.exists() || !fileDir.isDirectory()) { //当文件存在、且文件是一个目录的时候
                fileDir.mkdirs();
            }

            File file = new File(fileDir, filename);
            if (file.exists()) {
                file.delete();
            }
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(data);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //保存list
        public void saveList(Context context, String name, ArrayList<T> list) {
            save(context, name, FOLDER, list);
        }

        //保存对象
        public void saveObject(Context context, String name, Object data) {
            save(context, name, FOLDER, data);
        }

        /**
         * @param context 上下文
         * @param name    文件名
         * @return 获取object数据
         */
        public T loadObject(Context context, String name) {
            T data = null;
            //判断目录是否存在，存在则找到该文件，判断是否是文件，是则读取，不是则创建
            File fileDir = new File(context.getFilesDir(), FOLDER);
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            File file = new File(fileDir, name);
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                data = (T) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        /**
         * @param context 上下文
         * @param name    文件名
         * @return 读取list数据
         */
        public ArrayList<T> loadList(Context context, String name) {

            ArrayList<T> data = null;

            File fileDir = new File(context.getFilesDir(), FOLDER);
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }
            File file = new File(fileDir, name);

            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                data = (ArrayList<T>) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;

        }

    }

}
