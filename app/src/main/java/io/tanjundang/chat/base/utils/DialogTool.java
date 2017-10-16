package io.tanjundang.chat.base.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;


/**
 * Developer: TanJunDang
 * Email: TanJunDang324@gmail.com
 * 对话框工具类
 * 需要翻转的地方，需要在onCreate中再次调用方法
 * 遇到的问题？
 * 如何封装一个DialogTool，在其屏幕翻转时，利用旧Fragment重构界面？
 * 为什么不推荐使用构造函数传参。
 */
public class DialogTool {

    private final String DIALOG_TAG = "DialogToolFragment";

    /**
     * 单例模式初始化工具
     *
     * @return
     */
    DialogToolFragment dialog;

    private static class Holder {
        private static DialogTool INSTANCE = new DialogTool();
    }

    DialogTool() {
    }

    public static DialogTool getInstance() {
        return Holder.INSTANCE;
    }


    public void showDialog(AppCompatActivity context, String title, String msg, final DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        /**
         * 方法一 旋转屏幕消失
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", positiveListener);
        builder.setNegativeButton("取消", negativeListener);
        builder.create().show();
        /**
         * 方法二 旋转屏幕不消失，建议使用 跟PermissionTool有冲突
         */

//        dialog = DialogToolFragment.newInstance(title, msg).setPositiveListener(positiveListener).setNegativeListener(negativeListener);
//        dialog.setCancelable(false);
////        dialog.setRetainInstance(false);//Fragment忽略重建，true设置旋转屏幕后消失。
//        dialog.show(context.getSupportFragmentManager(), DIALOG_TAG);
    }

    /**
     * 当需要重新构建屏幕的时候调用该方法
     *
     * @param context
     * @param title
     * @param msg
     * @param positiveListener
     * @param negativeListener
     */
    public void setRetainBundle(FragmentActivity context, String title, String msg, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        dialog = (DialogToolFragment) context.getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);//找到重构前的fragment
        dialog.setTitle(title);
        dialog.setMsg(msg);
        dialog.setNegativeListener(negativeListener);
        dialog.setPositiveListener(positiveListener);
    }


    public static class DialogToolFragment extends DialogFragment {

        private static String TITLE = "TITLE";
        private static String MSG = "MSG";
        String title;
        String msg;
        DialogInterface.OnClickListener positiveListener;
        DialogInterface.OnClickListener negativeListener;
        private String[] items = new String[]{"1", "2", "3"};

        public DialogToolFragment() {
        }


        public static DialogToolFragment newInstance(String title, String msg) {
            DialogToolFragment fragment = new DialogToolFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TITLE, title);
            bundle.putString(MSG, msg);
            fragment.setArguments(bundle);
            return fragment;
        }


        public DialogToolFragment setPositiveListener(DialogInterface.OnClickListener positiveListener) {
            this.positiveListener = positiveListener;
            return this;
        }

        public DialogToolFragment setNegativeListener(DialogInterface.OnClickListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            title = bundle.getString(TITLE);
            msg = bundle.getString(MSG);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(title);
            builder.setMessage(msg);

            builder.setPositiveButton("确定", positiveListener);
            builder.setNegativeButton("取消", negativeListener);
            return builder.create();
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

}
