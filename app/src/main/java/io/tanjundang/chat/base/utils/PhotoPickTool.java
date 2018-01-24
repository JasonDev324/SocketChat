package io.tanjundang.chat.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.UncapableCause;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.me.AddMomentsActivity;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/17
 * @Description: 知乎图片选择工具
 * 需自行添加photo_pick_size=120dp
 */

public class PhotoPickTool {
    public static int REQUEST_CODE_CHOOSE = 0xff;

    static class Holder {
        static final PhotoPickTool INSTANCE = new PhotoPickTool();
    }

    public static PhotoPickTool getInstance() {
        return Holder.INSTANCE;
    }

    public void selectPhoto(Context context, int maxNum) {
        Matisse.from((Activity) context)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(maxNum)
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.photo_pick_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public void selectPhoto(BaseFragment fragment, int maxNum) {
        Matisse.from(fragment)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(maxNum)
                .gridExpectedSize(fragment.getResources().getDimensionPixelSize(R.dimen.photo_pick_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    /**
     * 获取得到的图片集合
     * @param data
     * @return
     */
    public List<Uri> getResult(Intent data) {
        List<Uri> selectedList = Matisse.obtainResult(data);
        return selectedList;
    }
}
