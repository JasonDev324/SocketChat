package io.tanjundang.chat.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.ChatMsgResp;
import io.tanjundang.chat.base.entity.User;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.GridItemDivider;
import io.tanjundang.chat.base.view.HeaderAdapter;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/30
 * @Description: 聊天信息页面
 */

public class ChatMsgActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ChatPersonAdapter mAdapter;
    ArrayList<ChatMsgResp.ChatMsgInfo> list = new ArrayList<>();

    long groupId;

    public static void Start(Context context, long groupId) {
        Intent intent = new Intent(context, ChatMsgActivity.class);
        intent.putExtra(Constants.ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);
        initView();
        getData();
    }

    private void initView() {
        groupId = getIntent().getLongExtra(Constants.ID, groupId);
        dialog.show();
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("聊天信息");

        mAdapter = new ChatPersonAdapter(ChatMsgActivity.this, R.layout.list_item_chat_person, list);
        recyclerView.addItemDecoration(new GridItemDivider(4, Functions.dp2px(10), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mAdapter);
    }

    private void getData() {
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getGroupMember(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ChatMsgResp>() {
                    @Override
                    public void onSuccess(ChatMsgResp resp) {
                        dialog.dismiss();
                        if (resp.isSuccess()) {
                            list.clear();
                            ChatMsgResp.ChatMsgInfo info = new ChatMsgResp.ChatMsgInfo();
                            info.setLayoutType(1);
                            list.add(info);
                            if (resp.getData() != null & !resp.getData().isEmpty()) {
                                list.addAll(resp.getData());
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Functions.toast(resp.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        Functions.toast(error);
                    }
                });
    }


    public class ChatPersonAdapter extends CommonRecyclerViewAdapter<ChatMsgResp.ChatMsgInfo> {
        public ChatPersonAdapter(Context context, int layoutId, ArrayList<ChatMsgResp.ChatMsgInfo> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final ChatMsgResp.ChatMsgInfo data, int pos) {
            User user = data.getUser();
            if (user == null) return;
            holder.setImageResource(R.id.ivAvatar, data.getLayoutType() == 0 ? R.drawable.ic_default_avatar : R.drawable.ic_upload_photo, null)
                    .setText(R.id.tvName, data.getUser().getName(), null)
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
        }
    }
}
