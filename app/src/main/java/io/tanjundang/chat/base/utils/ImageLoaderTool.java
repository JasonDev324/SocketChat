package io.tanjundang.chat.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import io.tanjundang.chat.R;


/**
 * Author: TanJunDang on 2015/10/28.
 * ImageLoader的封装。单例模式
 * 1. "http://site.com/image.png" // from Web
 * 2. "file:///mnt/sdcard/image.png" // from SD card
 * 3. "file:///mnt/sdcard/video.mp4" // from SD card  (video thumbnail)
 * 4. "content://media/external/images/media/13" // from content provider
 * 5. "content://media/external/video/media/13" // from content provider (video thumbnail)
 * 6. "assets://image.png" // from assets
 * 7. "drawable://" + R.drawable.img // from drawables (non-9patch images)[用imageView.setImageResource(drawable)来代替]
 */
public class ImageLoaderTool {

    public static ImageLoader imageLoader; //用于全局初始化
    private static ImageLoaderConfiguration configuration;//默认的ImageLoader配置
    private final String DRAWABLE_PREFIX = "drawable://";//用于加载drawable图像

    private static class Holder {
        private static final ImageLoaderTool loaderTool = new ImageLoaderTool();
    }

    public static ImageLoaderTool getInstance() {
        if (imageLoader == null || configuration == null) {
            throw new RuntimeException("Must be call the method InitImageLoader(Context context) in Application");
        }
        return Holder.loaderTool;
    }

    /**
     * 初始化ImageLoader
     *
     * @param context 在APP中
     */
    public static void initImageLoader(Context context) {
        File diskCache = StorageUtils.getOwnCacheDirectory(context, "Movie/ImageCache/");
        if (configuration == null) {
            configuration = new ImageLoaderConfiguration.Builder(context)
                    .diskCache(new UnlimitedDiscCache(diskCache))
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //md5加密
                    .memoryCacheSize(1024 * 2)//缓存最大字节
                    .memoryCache(new LruMemoryCache(5 * 1024 * 1024))//解决缓存listview滑动后显示默认图
                    .memoryCacheExtraOptions(800, 760)// 保存每个缓存图片的最大长和宽
                    .diskCacheFileCount(100) //缓存的File数量
                    .build();
        }
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(configuration);
        }
    }

    /**
     * 默认图片加载参数
     */
    private static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .showImageOnFail(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .delayBeforeLoading(1000)
            .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
            .build();

    //大图
    public static DisplayImageOptions imageBigOptions = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    /**
     * 加载大图的配置
     */
    private static DisplayImageOptions bigImageOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .delayBeforeLoading(1000)
            .build();


    /**
     * 加载头像类的配置
     */
    private static DisplayImageOptions smallImageOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .showImageOnFail(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .delayBeforeLoading(1000)
            .build();


    /**
     * 加载普通图片
     *
     * @param url
     * @param imageView
     */
    public void loadImage(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, defaultOptions);
    }

    /**
     * 加载drawable图片,不能加载系统的drawable 即android:drawable
     *
     * @param resid
     * @param imageView
     */
    public void loadImageFromDrawable(int resid, ImageView imageView) {
        imageLoader.displayImage(DRAWABLE_PREFIX + resid, imageView);
    }

    /**
     * 加载一张图片
     *
     * @param imageView
     * @param url
     * @param displayImageOptions
     * @param animate
     */
    public void loadImage(ImageView imageView, String url, DisplayImageOptions displayImageOptions, SimpleImageLoadingListener animate) {
        imageLoader.displayImage(url, imageView, displayImageOptions, animate);
    }

    public File getDiskFile(String imageUri) {
        return imageLoader.getDiskCache().get(imageUri);
    }

    public void loadBigImage(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, bigImageOptions);
    }

    public void loadSmallImage(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, smallImageOptions);
    }
}
