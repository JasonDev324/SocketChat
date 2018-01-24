package io.tanjundang.chat.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.MomentsResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpBaseBean;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.DialogTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.GridItemDivider;
import io.tanjundang.chat.base.view.ItemDivider;
import io.tanjundang.chat.base.view.ninegridview.NineGridView;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/11
 * @Description: 朋友圈
 */

public class MomentsActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.ivRight)
    ImageView ivRight;

    MomentsAdapter mAdapter;
    ArrayList<MomentsResp.MomentsItemInfo> list = new ArrayList<>();
    int REQ_ADD_MOMENTS = 0XFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("朋友圈");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.ic_menu_camera);
        mAdapter = new MomentsAdapter(this, R.layout.list_item_moments, list);
        recyclerView.addItemDecoration(new ItemDivider(ContextCompat.getColor(this, R.color.divider_color_gray), ItemDivider.HORIZONTAL, Functions.dp2px(10)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        //设置 Header 为 Material样式
        refreshLayout.setRefreshHeader(new TaurusHeader(this));
//设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshLayout.finishLoadmore();
            }
        });
        refreshLayout.autoRefresh();

    }

    @OnClick({R.id.ivRight})
    public void onClick(View v) {
        if (v.equals(ivRight)) {
            AddMomentsActivity.Start(this, REQ_ADD_MOMENTS);
        }
    }

    private void getData() {
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getMoments("mine")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<MomentsResp>() {
                    @Override
                    public void onSuccess(MomentsResp resp) {
                        dialog.dismiss();
                        if (resp.isSuccess()) {
                            list.clear();
                            MomentsResp.MomentsInfo info = resp.getData();
                            if (info == null) return;

                            if (info.getData() != null & !info.getData().isEmpty()) {
                                list.addAll(info.getData());
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Functions.toast(resp.getMsg());
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        Functions.toast(error);
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ADD_MOMENTS && resultCode == RESULT_OK) {
            refreshLayout.autoRefresh();
        }
    }

    public class MomentsAdapter extends CommonRecyclerViewAdapter<MomentsResp.MomentsItemInfo> {

        public MomentsAdapter(Context context, int layoutId, ArrayList list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final MomentsResp.MomentsItemInfo data, int pos) {
            holder.setText(R.id.tvName, data.getUser_id() + "", null)
                    .setText(R.id.tvContent, data.getContent(), null)
                    .setText(R.id.tvDate, data.getCreated_at(), null)
                    .setTextClick(R.id.tvDel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogTool.getInstance()
                                    .showDialog(MomentsActivity.this, null, "确定要删除?", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HttpReqTool.getInstance()
                                                    .createApi(BusinessApi.class)
                                                    .delMoments(data.getId())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new ApiObserver<HttpBaseBean>() {
                                                        @Override
                                                        public void onSuccess(HttpBaseBean resp) {
                                                            if (resp.isSuccess()) {
                                                                refreshLayout.autoRefresh();
                                                            } else {
                                                                Functions.toast(resp.getMsg());
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(String error) {

                                                        }
                                                    });
                                        }
                                    }, null);
                        }
                    });
            RecyclerView recyclerView;
            if (data.getPictures() != null) {
                recyclerView = holder.getView(R.id.recyclerView);
                PicAdapter picAdapter = new PicAdapter(MomentsActivity.this, R.layout.item_add_image, data.getPictures());
                recyclerView.setLayoutManager(new GridLayoutManager(MomentsActivity.this, 3));
                recyclerView.setAdapter(picAdapter);
            } else {
                recyclerView = null;
            }
        }
    }

    private class PicAdapter extends CommonRecyclerViewAdapter<String> {
        public PicAdapter(Context context, int layoutId, ArrayList list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, String data, int pos) {
            holder.setImageResource(R.id.ivImage, data, null);
        }
    }
}
