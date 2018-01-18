package io.tanjundang.chat.base.view.ninegridview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.tanjundang.chat.R;
import io.tanjundang.chat.base.utils.DialogTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.ImageLoaderTool;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/12
 * @Description: 自定义ViewGroup最重要一点是onMeasure必须要考虑完所有情况
 * 否则会出现内容显示不全的问题
 * 1.宽高都为AT_MOST
 * 2.宽为AT_MOST
 * 3.高位AT_MOST
 */

public class NineGridView extends ViewGroup {

    private static final int MAX_LENGTH = 7;
    private Context mContext;
    //    private ArrayList<String> picUrls = new ArrayList<>();
    private onClickListener listener;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        ImageView addPicView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.item_add_image, null);
        LayoutParams lp = new LayoutParams(Functions.dp2px(70), Functions.dp2px(70));
//        lp.leftMargin = Functions.dp2px(15);
//        lp.rightMargin = Functions.dp2px(15);
        addPicView.setLayoutParams(lp);
        addPicView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.addPic();
            }
        });
        addView(addPicView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = null;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        下面俩个参数默认是填满屏幕
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


//        由子view测量自己的高度
        measureChildren(widthMeasureSpec, heightMeasureSpec);

//        child.getMeasuredWidth()指子view的宽度
        int childCount = getChildCount();
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            int heightWeight;
            int widthWeight;
            if (childCount > 3) {
                heightWeight = 2;
                widthWeight = 3;
            } else if (childCount == 3) {
                heightWeight = 1;
                widthWeight = 3;
            } else {
                heightWeight = 1;
                widthWeight = 2;
            }
            View child = getChildAt(childCount - 1);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            setMeasuredDimension(childWidth * widthWeight, childHeight * heightWeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            int widthWeight;
            if (childCount < 3) {
                widthWeight = 2;
            } else {
                widthWeight = 3;
            }
            View child = getChildAt(childCount - 1);
            int childWidth = child.getMeasuredWidth();
            setMeasuredDimension(childWidth * widthWeight, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            int heightWeight;
            if (childCount < 3) {
                heightWeight = 1;
            } else {
                heightWeight = 2;
            }
            View child = getChildAt(childCount - 1);
            int childHeight = child.getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight * heightWeight);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        View child;
        int height = 0;
        int width = 0;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
//            前三张的布局
            if (i < 3) {
                child.layout(width, height, width + child.getMeasuredWidth(), child.getMeasuredHeight());
                width = width + child.getMeasuredWidth();
            } else {
                if (i == 3) {
                    width = 0;
                    height = child.getMeasuredHeight();
                }
                child.layout(width, height, width + child.getMeasuredWidth(), height + child.getMeasuredHeight());
                width = width + child.getMeasuredWidth();
            }
        }
    }


    public void addPic(String url) {
        ImageView child = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.item_add_image, null);
        LayoutParams lp = new LayoutParams(Functions.dp2px(70), Functions.dp2px(70));
//        lp.leftMargin = Functions.dp2px(15);
//        lp.rightMargin = Functions.dp2px(15);
        child.setLayoutParams(lp);
        final ImageInfo info = new ImageInfo();
        info.setIndex(getChildCount() - 1);
        info.setPicurl(url);
        child.setTag(info);
        child.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final ImageInfo cacheInfo = (ImageInfo) v.getTag();
                DialogTool.getInstance().showDialog((AppCompatActivity) mContext, null, "确定删除该图片？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeViewAt(cacheInfo.getIndex());
                        listener.delPic(cacheInfo.getIndex());
                        refreshIndex();
                        invalidate();
                    }
                }, null);
                return true;
            }
        });
        ImageLoaderTool.getInstance().loadImage(child, info.getPicurl());
        addView(child, getChildCount() - 1);
        refreshIndex();
        invalidate();

    }

    private void refreshIndex() {
        for (int i = 0; i < getChildCount() - 1; i++) {
            ImageView child = (ImageView) getChildAt(i);
            ImageInfo info = (ImageInfo) child.getTag();
            info.setIndex(i);
            child.setTag(info);
        }
    }


    public void setOnClickListener(onClickListener listener) {
        this.listener = listener;
    }

    public interface onClickListener {
        void addPic();
        void delPic(int index);
    }

    public class ImageInfo {
        private int index;
        private String picurl;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
    }
}
