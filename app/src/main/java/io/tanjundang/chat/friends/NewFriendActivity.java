package io.tanjundang.chat.friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.entity.type.HandleType;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpBaseBean;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.ItemTool;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.ItemDivider;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/26
 * @Description: 新朋友列表
 */

public class NewFriendActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rlAdd)
    RelativeLayout rlAdd;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springView)
    SpringView springView;
    NewFriendAdapter mAdapter;

    ArrayList<FriendsResp.FriendsInfo> list = new ArrayList<>();
    int REQ_ADD_FRIEND = 0XFF;

    public static void Start(Context context) {
        Intent intent = new Intent(context, NewFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        tvTitle.setText("新朋友");
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new NewFriendAdapter(this, R.layout.list_item_friends, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new ItemDivider(Color.GRAY, ItemDivider.HORIZONTAL));
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
        ItemTool.titleArrow(rlAdd, "新朋友");
    }

    @OnClick({R.id.rlAdd})
    public void onClick(View v) {
        AddFriendActivity.StartForResult(this, REQ_ADD_FRIEND);
    }

    public void getData() {
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getFriends("waiting")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<FriendsResp>() {
                    @Override
                    public void onSuccess(FriendsResp resp) {
                        if (resp.isSuccess()) {
                            list.clear();
                            if (resp.getData() != null & !resp.getData().isEmpty()) {
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
                        Functions.toast(error);
                    }
                });
    }

    class NewFriendAdapter extends CommonRecyclerViewAdapter<FriendsResp.FriendsInfo> {
        public NewFriendAdapter(Context context, int layoutId, ArrayList<FriendsResp.FriendsInfo> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final FriendsResp.FriendsInfo data, int pos) {
            LinearLayout llHandle = holder.getView(R.id.llHandle);
            llHandle.setVisibility(View.VISIBLE);
            holder.setImageResource(R.id.ivAvatar, null)
                    .setText(R.id.tvName, data.getName(), null)
                    .setText(R.id.tvAccept, "同意", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HttpReqTool
                                    .getInstance()
                                    .createApi(BusinessApi.class)
                                    .handleFriendReq(data.getFriend_id(), HandleType.ACCEPT.getType())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ApiObserver<HttpBaseBean>() {
                                        @Override
                                        public void onSuccess(HttpBaseBean resp) {
                                            if (resp.isSuccess()) {
                                                springView.callFresh();
                                            } else {
                                                Functions.toast(resp.getMsg());
                                            }
                                        }

                                        @Override
                                        public void onFailure(String error) {

                                        }
                                    });
                        }
                    })
                    .setText(R.id.tvReject, "拒绝", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HttpReqTool
                                    .getInstance()
                                    .createApi(BusinessApi.class)
                                    .handleFriendReq(data.getFriend_id(), HandleType.REJECT.getType())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ApiObserver<HttpBaseBean>() {
                                        @Override
                                        public void onSuccess(HttpBaseBean resp) {
                                            springView.callFresh();
                                        }

                                        @Override
                                        public void onFailure(String error) {

                                        }
                                    });
                        }
                    })
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Functions.toast("进入聊天页面");
                        }
                    });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ADD_FRIEND && resultCode == Activity.RESULT_OK) {
            springView.callFresh();
        }
    }
}
