package io.tanjundang.chat.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.entity.type.ChatType;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpBaseBean;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.DialogTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.ItemTool;
import io.tanjundang.chat.talk.ChatTestActivity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/28
 * @Description: 个人资料页面
 */

public class PersonalInfoActivity extends BaseActivity {

    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSign)
    TextView tvSign;
    @BindView(R.id.flHead)
    FrameLayout flHead;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.tvDel)
    TextView tvDel;
    @BindView(R.id.rlRootView)
    RelativeLayout rlRootView;
    @BindView(R.id.rlEmail)
    RelativeLayout rlEmail;
    @BindView(R.id.rlAccount)
    RelativeLayout rlAccount;
    @BindView(R.id.tvBack)
    TextView tvBack;
    FriendsResp.FriendsInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
        Observable
                .interval(1, TimeUnit.SECONDS)
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        dialog.dismiss();
                    }
                });
    }

    private void initView() {
        info = (FriendsResp.FriendsInfo) getIntent().getSerializableExtra(Constants.DATA);
        ButterKnife.bind(this);
        Functions.setImmerseStatusBar(this, rlRootView);
        rlTitle.bringToFront();
        tvName.setText(info.getName());
        ItemTool.titleValue(rlAccount, "帐号", info.getName());
        ItemTool.titleValue(rlEmail, "邮箱", info.getEmail());
        dialog.show();
    }

    public static void Start(Context context, FriendsResp.FriendsInfo info) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        intent.putExtra(Constants.DATA, info);
        context.startActivity(intent);
    }

    @OnClick({R.id.tvBack, R.id.tvDel,
            R.id.tvSend})
    public void onClick(View v) {
        if (v.equals(tvBack)) {
            finish();
        } else if (v.equals(tvSend)) {
            ChatTestActivity.Start(this, info.getFriend_id(), ChatType.P2P, info.getName());
        } else if (v.equals(tvDel)) {
            DialogTool.getInstance()
                    .showDialog(this, "删除好友", "同时会屏蔽对方的临时对话，不再接收此人的消息。", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HttpReqTool.getInstance()
                                    .createApi(BusinessApi.class)
                                    .delFriend(info.getFriend_id())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ApiObserver<HttpBaseBean>() {
                                        @Override
                                        public void onSuccess(HttpBaseBean resp) {
                                            if (resp.isSuccess()) {
                                                finish();
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
    }
}
