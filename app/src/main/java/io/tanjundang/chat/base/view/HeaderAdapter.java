package io.tanjundang.chat.base.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @Author: TanJunDang
 * @Date: 2017/6/21
 * @Description: 带头部的RecyclerView
 */

public abstract class HeaderAdapter<T> extends RecyclerView.Adapter<CommonHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private ArrayList<T> list = new ArrayList<>();

    private View mHeaderView;
    public Context mContext;
    int layoutId;

    public abstract void convert(CommonHolder holder, T data, int pos);

    public void setHeaderView(int layoutId) {
        mHeaderView = LayoutInflater.from(mContext).inflate(layoutId, null);
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public HeaderAdapter(Context mContext, @LayoutRes int layoutId, ArrayList<T> list) {
        this.list = list;
        this.layoutId = layoutId;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new CommonHolder(mContext, mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CommonHolder(mContext, layout);
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        if (!list.isEmpty()) {
            T data = list.get(position);
            convert(holder, data, pos);
        }
    }

    public int getRealPosition(CommonHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? list.size() : list.size() + 1;
    }


}