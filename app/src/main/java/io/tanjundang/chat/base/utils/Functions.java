package io.tanjundang.chat.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.tanjundang.chat.BuildConfig;
import io.tanjundang.chat.R;


/**
 * Author: TanJunDang
 * 工具方法集合：包括Toast、屏幕操作、dp转换、键盘OC等方法。
 */
public class Functions {

    public static int MALE = 0;//男
    public static int FEMALE = 1;//女
    public static int REQ_PICTURE = 0XFF;
    private static Context appContext;

    public static void init(Context context) {
        appContext = context;
    }

    private static Toast toast = null;


    /**
     * Toast封装
     *
     * @param text
     */
    public static void toast(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 判断是否合法手机号码
     *
     * @param telNum
     * @return
     */
    public static boolean isMobiPhoneNum(String telNum) {
        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }

    /**
     * 根据值来显示男女
     */

    public static String getStringByGender(int gender) {
        Resources resources = appContext.getResources();
        String sex = "";
        if (gender == MALE) {
            sex = "男";
        } else {
            sex = "女";
        }
        return sex;
    }

    /**
     * 隐藏软键盘
     *
     * @param
     * @param view
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) appContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static void showKeyboard(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }


    /**
     * 根据名字获取对应的图片资源id
     * 不需要R.drawable前缀
     *
     * @param s
     * @return
     */
    public static int getResourceIdByString(String s) {
        try {
            Field field = R.drawable.class.getField(s);
            return Integer.parseInt(field.get(null).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 此方法只是获取手机的当前时间
     * 必须要加上服务器下发的时间跟系统的时间差(offset = serverTime - systemTime)。
     *
     * @return
     */
    public static long getCurrentFixTime() {
        int offset = 0;
        return Calendar.getInstance().getTimeInMillis() + offset;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        WindowManager manager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        WindowManager manager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * dp 转 px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, appContext.getResources().getDisplayMetrics());
    }


    /**
     * px 转  dp
     *
     * @param pxVal
     * @return
     */
    public static float px2dp(float pxVal) {
        final float scale = appContext.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * 将一个String集合进行逗号拼装
     *
     * @param list
     * @return
     */
    public static String getStringListByPoint(ArrayList<String> list) {
        String result = "";
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String text = (String) iterator.next();
            result += text + ",";
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 获取图片的File路径
     *
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    private static String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(appContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(appContext, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(appContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(appContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 需要在onactivityresult里用REQ_PICTURE接收本地图片数据
     *
     * @param context
     */
    public static void SkipToImagePickActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Choose Picture"), REQ_PICTURE);
    }

//    /**
//     * 从Intent获取本地path
//     * 用法：用在onActivityResult里
//     *
//     * @param data
//     * @return
//     */
//    public static void getImageFromIntent(Intent data, PictureCallBack callback) {
//        String path = "";
//        Uri uri = data.getData();
//        path = Functions.getPath(uri);
////        upload(path, callback);
//    }


    /**
     * 获取sd卡路径,如果没有sd卡路径,就在/data/data下创建
     *
     * @return
     */
    public static String getSDPath() {
        String sdPath = "";

        if (isSDCardExist()) {
            sdPath = Environment.getExternalStorageDirectory() + "/ZHKJ_SYQ/";
        } else {
            sdPath = Environment.getDataDirectory() + "/ZHKJ_SYQ/";
        }

        File file = new File(sdPath); //创建路径
        if (!file.exists()) {
            file.mkdirs();
        }

        return sdPath;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 查看APK版本 展示所用 1.2.0
     */
    public static String getAppVersionName() {
        String versionName = "";
        PackageManager manager = appContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(appContext.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取git版本，需要在Manifest里定义，在gradle理赋值
     *
     * @return
     */
    public static String getGitVersion() {
        String chanel = "";
        String gitVersion = "";
        try {
            ApplicationInfo info = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
            gitVersion = info.metaData.getString("GIT_VERSION");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        chanel = String.format("%s-%s-%s", BuildConfig.FLAVOR, gitVersion, BuildConfig.BUILD_TYPE);
        return chanel;
    }

    /**
     * 获取版本ID 用于与服务端版本号对比，从而决定是否需要升级
     * <p/>
     * 可使用BuildType直接获取版本号、版本、渠道号、BuildType等数据
     *
     * @param
     * @return
     */
    public static int getAppVersionCode() {
        int versionCode = 0;
        PackageManager manager = appContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(appContext.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void SkipToAppDetail() {
        //跳转到当前应用详情页面
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", appContext.getPackageName(), null));
        appContext.startActivity(intent);
    }


    // 通过文件头来判断是否gif
    public static boolean isGifByFile(File file) {
        try {
            int length = 10;
            InputStream is = new FileInputStream(file);
            byte[] data = new byte[length];
            is.read(data);
            String type = getType(data);
            is.close();

            if (type.equals("gif")) {
                return true;
            }
        } catch (Exception e) {
            LogTool.v("GIF", e.getMessage());
        }

        return false;
    }

    private static String getType(byte[] data) {
        String type = "";
        if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
            type = "png";
        } else if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            type = "gif";
        } else if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                && data[9] == 'F') {
            type = "jpg";
        }
        return type;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int statusBarHeight1 = -1;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    /**
     * 获取actionbar高度
     *
     * @return
     */
    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (appContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, appContext.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static boolean isKeyboardShow(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 根据url获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        String result = url.substring(url.lastIndexOf('/') + 1);
        return result;
    }

    public static String getTailFromFileName(String url) {
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");//正则判断
        Matcher mc = pat.matcher(url);//条件匹配
        while (mc.find()) {
            String substring = mc.group();//截取文件名后缀名
            return substring;
        }
        return "";
    }


    /**
     * @param targetStr 要处理的字符串
     * @description 切割字符串，将文本和img标签碎片化，如"ab<img>cd"转换为"ab"、"<img>"、"cd"
     */
    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>");
        Matcher matcher = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                splitTextList.add(targetStr.substring(lastIndex, matcher.start()));
            }
            splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        return splitTextList;
    }

    /**
     * 获取img标签中的src值
     *
     * @param content
     * @return
     */
    public static String getImgSrc(String content) {
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    str_src = m_src.group(3);
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return str_src;
    }

    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     * SpannableStringBuilder textString = TextUtilTools.highlight(item.getItemName(), KnowledgeActivity.searchKey);
     * vHolder.tv_itemName_search.setText(textString);
     */
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.parseColor("#EE5C42"));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static String getPrefixUrl(String imgPath) {
        String prefix = "http://p.shuiyq.com";
        if (!imgPath.contains(prefix)) {
            imgPath = prefix + imgPath;
        }
        return imgPath;
    }

    /**
     * 将手机号码中间四位隐藏
     *
     * @param phone
     * @return
     */
    public static String getHidePhoneNum(String phone) {
        String prefix = phone.substring(0, 3);
        String tailfix = phone.substring(7, phone.length());
        return prefix + "****" + tailfix;
    }

    /**
     * 判断是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean hasWeChat(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 沉浸式状态栏
     * 1.须在Activity根布局设置android:fitsSystemWindows="false"
     * =============================================================================================
     */


    /**
     * 判断是否有navigationBar
     *
     * @param context
     * @return
     */
    public static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        return hasNavigationBar;
    }

    /**
     * 设置Activity根布局上下padding(statusBar、navigationBar的高度)
     * 当手机有navigationBar，同时又要设置沉浸式状态栏时，NavigationBar会与布局内容重叠在一起，所以需手动修改页面根布局的底部padding。
     *
     * @param view
     * @param context
     */
    public static void setNavigationBarPadding(View view, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int marginTop = getStatusBarHeight(view.getContext());
            view.setPadding(view.getPaddingLeft(), 0,
                    view.getPaddingRight(), getNavigationBarHeight(context));
            return;
        }
    }

    /**
     * 获取NavigationBar的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 设置沉浸式状态栏以及状态栏透明
     *
     * @param activity
     */
    public static void setImmerseStatusBar(Activity activity) {
        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-------无法获取到状态栏高度");
        }
        return Functions.dp2px(24);
    }

    /**
     * 判断email是否合法
     *
     * @param email
     * @return
     */
    public static boolean isVaildEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    /**
     * 限制只能输入字母和数字以及特殊符号，默认弹出英文输入法
     *
     * @param editText
     */
    public static void setNoChineseInput(EditText editText) {
        final String input_format = "<![CDATA[0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-~!@#$%^?？/{}`[]|\\\\——~·`;:´·\\'+=,.*()~·！“”：，；、;‘’˜¡¿&\\\"<>]]>";
        /**
         *
         */
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = input_format.toCharArray();
                return data;
            }
        });

    }

    /**
     * 限制只能输入字母和数字，默认弹出英文输入法
     *
     * @param editText
     */
    public static void setOnlyEnglishNumInput(EditText editText) {
        final String input_format = "qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM1234567890";
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = input_format.toCharArray();
                return data;
            }
        });
    }
}
