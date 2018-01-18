package io.tanjundang.chat.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.UncapableCause;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.Set;

import io.tanjundang.chat.R;
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

    public void selectPhoto(Context context) {
        Matisse.from((Activity) context)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.photo_pick_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
}
