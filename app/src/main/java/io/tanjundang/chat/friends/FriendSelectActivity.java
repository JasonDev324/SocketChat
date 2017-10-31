package io.tanjundang.chat.friends;

import android.app.Activity;
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
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.ItemDivider;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/31
 * @Description: 加入群聊
 */

public class FriendSelectActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springView)
    SpringView springView;

    SelectAdapter mAdapter;
    ArrayList<FriendsResp.FriendsInfo> list = new ArrayList<>();
    long groupId;

    public static void StartForResult(Context context, long groupId, int reqCode) {
        Intent intent = new Intent(context, FriendSelectActivity.class);
        intent.putExtra(Constants.ID, groupId);
        ((Activity) context).startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_select);
        initView();
        getData();
    }

    private void initView() {
        groupId = getIntent().getLongExtra(Constants.ID, 0);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("邀请加入群聊");
        mAdapter = new SelectAdapter(this, R.layout.list_item_friend_select, list);
        ItemDivider divider = new ItemDivider(ContextCompat.getColor(this, R.color.divider_color_gray), ItemDivider.HORIZONTAL, Functions.dp2px(1));
        recyclerView.addItemDecoration(divider);
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

    private void getData() {
        dialog.show();
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getFriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<FriendsResp>() {
                    @Override
                    public void onSuccess(FriendsResp resp) {
                        dialog.dismiss();
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
                        dialog.dismiss();
                        Functions.toast(error);
                        springView.onFinishFreshAndLoad();
                    }
                });
    }

    public class SelectAdapter extends CommonRecyclerViewAdapter<FriendsResp.FriendsInfo> {
        public SelectAdapter(Context context, int layoutId, ArrayList<FriendsResp.FriendsInfo> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final FriendsResp.FriendsInfo data, int pos) {
            holder.setText(R.id.tvName, data.getName(), null)
                    .setImageResource(R.id.ivAvatar, R.drawable.ic_default_avatar, null)
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.USER_ID, data.getFriend_id());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
        }
    }
}
