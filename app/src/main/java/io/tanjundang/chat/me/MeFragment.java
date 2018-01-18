package io.tanjundang.chat.me;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.QiNiuTokenResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.LogTool;
import io.tanjundang.chat.base.utils.PicUploadTool;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/17
 * @Description: 个人资料
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvAlbum)
    TextView tvAlbum;
    @BindView(R.id.tvWallet)
    TextView tvWallet;
    @BindView(R.id.tvFriends)
    TextView tvFriends;
    @BindView(R.id.tvUpdate)
    TextView tvUpdate;
    @BindView(R.id.ivSetting)
    ImageView ivSetting;

    Unbinder unbinder;

    public static MeFragment getInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvName.setText(Global.getInstance().getNickname());
        tvEmail.setText(Global.getInstance().getEmail());
        return view;
    }

    int REQ_ZIP = 0xff;
    int REQ_NO_ZIP = 0XFA;

    @OnClick({R.id.ivAvatar, R.id.tvAlbum,
            R.id.tvWallet, R.id.tvFriends,
            R.id.tvUpdate, R.id.ivSetting})
    public void onClick(View v) {
        if (v.equals(ivAvatar)) {
//            通过Uri来操作文件，将文件保存到本地
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                uri = FileProvider.getUriForFile(getContext(), "io.tanjundang.chat.provider", new File(Functions.getSDPath(), "uriPhoto.jpg"));
            } else {
                uri = Uri.fromFile(new File(Functions.getSDPath(), "uriPhoto.jpg"));
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQ_NO_ZIP);
        } else if (v.equals(tvAlbum)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQ_ZIP);
//            SocketMsgResp.SocketMsgInfo info = new SocketMsgResp.SocketMsgInfo();
//            info.setChatType("p2p");
//            info.setUserName("TJD");
//            info.setContent(new SocketMsgResp.ContentMsg("txt", "顶你肺"));
//            Intent intent = new Intent(MsgReceiver.MSG_ACTION);
//            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//            intent.putExtra(Constants.DATA, info);
//            getContext().sendBroadcast(intent);
        } else if (v.equals(tvWallet)) {
            Intent intent = new Intent(getContext(), MomentsActivity.class);
            startActivity(intent);
        } else if (v.equals(tvFriends)) {
            Functions.toast(Global.getInstance().getQiniuToken());
            LogTool.d(TAG, "Qi Niu Token :" + Global.getInstance().getQiniuToken());
        } else if (v.equals(tvUpdate)) {
            Functions.toast(Functions.getGitVersion());
        } else if (v.equals(ivSetting)) {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ZIP) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");


//            File file = new File(Functions.getSDPath(), "zipPhoto.jpg");
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(file);
////                bitmap输出到文件
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                fos.flush();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        } else if (requestCode == REQ_NO_ZIP) {
//            通过file来定位文件，通过fileInputStream获取bitmap
            File file = new File(Functions.getSDPath(), "uriPhoto.jpg");
            PicUploadTool.getInstance().upload(file.getPath(), "qiniutjd.jpg", Global.getInstance().getQiniuToken(), new PicUploadTool.Callback() {
                @Override
                public void onSuccess(String url) {
                    Functions.toast("上错成功");
                }

                @Override
                public void onFailure(String error) {
                    Functions.toast(error);
                }
            });
//            Bitmap bitmap = getimage(file.getPath());
//            File outputFile = new File(Functions.getSDPath(), "zipImage.jpg");
//            try {
//                storeImage(bitmap, outputFile.getPath());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            try {
////                加载bitmap
//                FileInputStream is = new FileInputStream(file);
//                Bitmap bitmap = BitmapFactory.decodeStream(is);
//
////                压缩bitmap
//                Bitmap zipBitmap = compressImage(bitmap);
//                File zipFile = new File(Functions.getSDPath(), "zipImage.jpg");
//                FileOutputStream fos = new FileOutputStream(zipFile);
//                zipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.flush();
//                ivAvatar.setImageBitmap(zipBitmap);
//                zipBitmap.recycle();
//                bitmap.recycle();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    }

    public static Bitmap getimage(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
