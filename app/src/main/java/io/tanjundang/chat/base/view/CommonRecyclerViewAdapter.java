package io.tanjundang.chat.base.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @Author: TanJunDang
 * @Date: 2017/6/16
 * @Description: 通用Adapter
 */

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonHolder> {

    private Context context;
    private int layoutId = 0;
    private ArrayList<T> list = new ArrayList<>();

    public CommonRecyclerViewAdapter(Context context, int layoutId, ArrayList<T> list) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    /**
     * 提供给外部的接口
     *
     * @param holder
     * @param data
     */
    public abstract void convert(CommonHolder holder, T data, int pos);

    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new CommonHolder(context, view);
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        if (!list.isEmpty()) {
            T data = list.get(position);
            convert(holder, data, position);
        }

    }

    /**
     * 改变数据源
     *
     * @param list
     */
    public void onDataChange(ArrayList<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
