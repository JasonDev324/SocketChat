package io.tanjundang.chat.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.ChatMsgResp;
import io.tanjundang.chat.base.entity.User;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpBaseBean;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.GridItemDivider;

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
    int REQ_FRIEND_SELECT = 0XFF;

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
        dialog.show();
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
                            if (resp.getData() != null & !resp.getData().isEmpty()) {
                                list.addAll(resp.getData());
                            }
                            ChatMsgResp.ChatMsgInfo info = new ChatMsgResp.ChatMsgInfo();
                            info.setUser(new User());
                            info.setLayoutType(1);
                            list.add(info);
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
        public void convert(CommonHolder holder, final ChatMsgResp.ChatMsgInfo data, final int pos) {
            User user = data.getUser();
            if (user == null) return;
            ImageView ivAvatar = holder.getView(R.id.ivAvatar);
            TextView tvName = holder.getView(R.id.tvName);
            tvName.setText(user.getName());
            ivAvatar.setImageResource(data.getLayoutType() == 0 ? R.drawable.ic_default_avatar : R.drawable.ic_upload_photo);
            tvName.setVisibility(data.getLayoutType() == 0 ? View.VISIBLE : View.GONE);
            holder.itemClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos == getItemCount() - 1) {
                        FriendSelectActivity.StartForResult(ChatMsgActivity.this, groupId, REQ_FRIEND_SELECT);
                    } else {
                        Functions.toast("普通");
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == REQ_FRIEND_SELECT && resultCode == RESULT_OK) {
            long userId = data.getLongExtra(Constants.USER_ID, 0);
            dialog.show();
            HttpReqTool.getInstance().createApi(BusinessApi.class)
                    .joinGroup(userId, groupId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HttpBaseBean>() {
                        @Override
                        public void accept(HttpBaseBean resp) throws Exception {
                            if (resp.isSuccess()) {
                                getData();
                            } else {
                                dialog.dismiss();
                                Functions.toast(resp.getMsg());
                            }
                        }
                    });

        }
    }
}
