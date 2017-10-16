package io.tanjundang.chat.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

/**
 * Developer: TanJunDang
 * Email: TanJunDang324@gmail.com
 * Date: 2016/9/28
 */

public class PermissionTool {
    private static Context appContext;
    public static int REQ_CODE = 0xff;

    private View.OnClickListener dealSuccess;

    //静态内部类内部类里面实例化
    private static class Holder {
        public static PermissionTool INSTANCE = new PermissionTool();
    }

    public static PermissionTool getInstance(Context context) {
        appContext = context;
        return Holder.INSTANCE;
    }


    /**
     * @param permissionList 权限集合
     * @param dealSuccess    成功获取所有权限后的处理
     */
    public void requestPermissions(ArrayList<String> permissionList, View.OnClickListener dealSuccess) {
        this.dealSuccess = dealSuccess;
        if (checkPermissions(permissionList).isEmpty()) {
            dealSuccess.onClick(new View(appContext));
        } else {
            ActivityCompat.requestPermissions((Activity) appContext, (String[]) permissionList.toArray(new String[permissionList.size()]), REQ_CODE);
        }
    }

    /**
     * 检测需要请求的权限
     *
     * @param permissionList
     * @return
     */
    public ArrayList<String> checkPermissions(ArrayList<String> permissionList) {
        ArrayList<String> requestPermissions = new ArrayList<>();
        for (int i = 0; i < permissionList.size(); i++) {
            if (ActivityCompat.checkSelfPermission(appContext, permissionList.get(i)) == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissionList.get(i));
            }
        }
        return requestPermissions;
    }

    /**
     * 返回拒绝的权限
     *
     * @param permissions
     * @param grantResults
     * @return
     */
    private ArrayList permissionDenyList(String[] permissions, int[] grantResults) {
        ArrayList permissionDeny = new ArrayList();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionDeny.add(permissions[i]);
            }
        }
        return permissionDeny;
    }

    /**
     * 处理多个权限
     *
     * @param permissionList
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(final ArrayList<String> permissionList, final int requestCode, final String[] permissions, int[] grantResults) {
        permissionList.clear();
        if (requestCode == PermissionTool.REQ_CODE) {
            if (permissionDenyList(permissions, grantResults).isEmpty()) {
                dealSuccess.onClick(new View(appContext));
            } else {
                permissionList.addAll(permissionDenyList(permissions, grantResults));
                if (isAllPermissionNoLongerDisplay(permissionList)) {
                    DialogTool.getInstance().showDialog((AppCompatActivity) appContext, "", "需要给予相关的权限才能使用该功能", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Functions.SkipToAppDetail();
                        }
                    }, null);

                } else if (permissions.length == 1 && !permissionList.isEmpty()) {
                    DialogTool.getInstance().showDialog((AppCompatActivity) appContext, "必须设置权限才能正常使用该功能", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) appContext, new String[]{permissions[0]}, requestCode);
                        }
                    }, null);
                } else {
                    Functions.toast("必须给予相关的权限才能使用该功能");
                }
            }
        }
    }

    /**
     * 通过判断不再显示权限集合长度跟被拒绝集合长度是否一致来决定是否执行跳去设置页面
     *
     * @param permissions
     * @return
     */
    public boolean isAllPermissionNoLongerDisplay(ArrayList<String> permissions) {
        //获取所有点击了不再显示的权限
        ArrayList<String> noLongerDisplayList = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) appContext, permissions.get(i))) {
                noLongerDisplayList.add(permissions.get(i));
            }
        }
        //判断是否所有权限都不再显示
        if (noLongerDisplayList.size() == permissions.size()) {
            return true;
        } else {
            return false;
        }
    }

}

