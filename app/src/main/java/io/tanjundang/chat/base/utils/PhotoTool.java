package io.tanjundang.chat.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;


/**
 * @Author: TanJunDang
 * @Date: 2017/12/6
 * @Description: 处理拍照后的图片
 * 拍照后的图片由于文件过大，所以在上传前一般会进行压缩。因此会进行一系列的操作。
 * 压缩后的图片，exif信息会丢失
 * 1.加载图片到bitmap
 * 2.压缩图片
 * 3.翻转图片
 */

public class PhotoTool {
    String sourceImagePath;

    public static PhotoTool getInstance() {
        return Holder.INSTANCE;
    }

    static class Holder {
        static PhotoTool INSTANCE = new PhotoTool();
    }


    /**
     * 压缩图片
     *
     * @param sourceImgPath 原图片路径
     * @param outputPath    压缩后图片的输出路径
     * @param fileName      输出文件名
     * @return 压缩后的bitmap，无exif
     */
    public Bitmap getZipImage(String sourceImgPath, String outputPath, String fileName) {
        sourceImagePath = sourceImgPath;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(outputPath, fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bmp = null;
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        float standardW = 480f;
        float standardH = 800f;

        int zoomRatio = 1;
        if (w > h && w > standardW) {
            zoomRatio = (int) (w / standardW);
        } else if (w < h && h > standardH) {
            zoomRatio = (int) (h / standardH);
        }
        if (zoomRatio <= 0)
            zoomRatio = 1;
        opts.inSampleSize = zoomRatio;
        bmp = BitmapFactory.decodeFile(sourceImgPath, opts);
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, fos);

        return bmp;
    }

    /**
     * 旋转压缩后的bitmap
     *
     * @param bitmap       压缩后的图片
     * @param srcImagePath 原图片的路径(获取原图片的exif信息,加载到bitmap)
     */
    public Bitmap rotateZipImage(Bitmap bitmap) {
        int degree = getPicRotate(sourceImagePath);
        if (degree != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 保存bitmap到本地
     *
     * @param zipBitmap 压缩后的bitmap
     * @param savePath  路径名
     * @param fileName  文件名
     * @return
     */
    public boolean saveZipBitmap(Bitmap zipBitmap, String savePath, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(savePath, fileName));
            zipBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取图片文件旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    private int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 由于压缩后的图片会翻转，所以需要获取原图片的exif信息
     * 复制原图片的exif信息，将原图片的exif信息复制到压缩后的图片上
     *
     * @param oldFilePath 原图片的path
     * @param newFilePath 压缩后图片的path
     * @throws Exception
     */
    public static void saveExif(String oldFilePath, String newFilePath) throws Exception {
        ExifInterface oldExif = new ExifInterface(oldFilePath);
        ExifInterface newExif = new ExifInterface(newFilePath);
        Class<ExifInterface> cls = ExifInterface.class;
        Field[] fields = cls.getFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if (!TextUtils.isEmpty(fieldName) && fieldName.startsWith("TAG")) {
                String fieldValue = fields[i].get(cls).toString();
                String attribute = oldExif.getAttribute(fieldValue);
                if (attribute != null) {
                    newExif.setAttribute(fieldValue, attribute);
                }
            }
        }
        newExif.saveAttributes();
    }

}
