package io.tanjundang.chat.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
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
import io.tanjundang.chat.base.entity.GroupChatResp;
import io.tanjundang.chat.base.entity.type.ChatType;
import io.tanjundang.chat.base.entity.type.SetType;
import io.tanjundang.chat.base.network.DialogApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.ItemDivider;
import io.tanjundang.chat.talk.ChatTestActivity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/28
 * @Description: 群聊列表
 */

public class GroupChatActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    GroupAdapter mAdapter;

    ArrayList<GroupChatResp.GroupBean> list = new ArrayList<>();
    int REQ_OPEN_GROUP = 0xFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initView();
        getData();
    }

    public static void Start(Context context) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tvSubTitle})
    public void onClick(View v) {
        if (v.equals(tvSubTitle)) {
            CommonSetActivity.StartForResult(this, REQ_OPEN_GROUP, SetType.OPEN_GROUP);
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        tvSubTitle.setText("新建");
        tvSubTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("群聊");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new GroupAdapter(this, R.layout.list_item_group_chat, list);

        recyclerView.addItemDecoration(new ItemDivider(ContextCompat.getColor(this, R.color.divider_color_gray), ItemDivider.HORIZONTAL, Functions.dp2px(1)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
        refreshLayout.setRefreshHeader(new FalsifyHeader(this));
        refreshLayout.autoRefresh();
    }

    public void getData() {
        HttpReqTool.getInstance()
                .createApi(BusinessApi.class)
                .getGroupList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogApiObserver<GroupChatResp>(this) {
                    @Override
                    public void onSuccess(GroupChatResp resp) {
                        if (resp.isSuccess()) {
                            list.clear();
                            if (resp.getData() != null && !resp.getData().isEmpty()) {
                                list.addAll(resp.getData());
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Functions.toast(resp.getMsg());
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(String error) {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    class GroupAdapter extends CommonRecyclerViewAdapter<GroupChatResp.GroupBean> {
        public GroupAdapter(Context context, int layoutId, ArrayList<GroupChatResp.GroupBean> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final GroupChatResp.GroupBean data, int pos) {
            if (data.getGroup() == null) return;
            holder.setText(R.id.tvName, data.getGroup().getName(), null)
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ChatTestActivity.Start(GroupChatActivity.this, data.getGroup_id(), ChatType.GROUP, data.getGroup().getName());
//                            ChatActivity.Start(GroupChatActivity.this, data.getGroup_id(), ChatType.GROUP, data.getGroup().getName());
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_OPEN_GROUP && resultCode == RESULT_OK) {
            refreshLayout.autoRefresh();
        }
    }
}
