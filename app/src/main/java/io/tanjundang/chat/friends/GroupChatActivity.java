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

import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.GroupChatResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpBaseBean;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.ItemDivider;

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
    @BindView(R.id.springView)
    SpringView springView;

    GroupAdapter mAdapter;

    ArrayList<GroupChatResp.GroupBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initView();
    }

    public static void Start(Context context) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        context.startActivity(intent);
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
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new RotationHeader(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    public void getData() {
        HttpReqTool.getInstance()
                .createApi(BusinessApi.class)
                .getGroupList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GroupChatResp>() {
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
                        springView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onFailure(String error) {
                        springView.onFinishFreshAndLoad();
                    }
                });
    }

    class GroupAdapter extends CommonRecyclerViewAdapter<GroupChatResp.GroupBean> {
        public GroupAdapter(Context context, int layoutId, ArrayList<GroupChatResp.GroupBean> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final GroupChatResp.GroupBean data, int pos) {
            holder.setText(R.id.tvName, data.getGroup().getName(), null)
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            HttpReqTool
//                                    .getInstance()
//                                    .createApi(BusinessApi.class)
//                                    .joinGroup(data.getGroup().getId(), data.getGroup_id())
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new ApiObserver<HttpBaseBean>() {
//                                        @Override
//                                        public void onSuccess(HttpBaseBean resp) {
//                                            if (resp.isSuccess()) {
//                                            } else {
//                                                Functions.toast(resp.getMsg());
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(String error) {
//
//                                        }
//                                    });
                        }
                    });
        }
    }
}
