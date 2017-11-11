package io.tanjundang.chat.friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.AddFriendResp;
import io.tanjundang.chat.base.entity.SocketFriendReqResp;
import io.tanjundang.chat.base.entity.type.SetType;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpBaseBean;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.GsonTool;

import static io.tanjundang.chat.MainActivity.connector;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/26
 * @Description: 新增朋友
 */

public class CommonSetActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.etContent)
    EditText etContent;

    SetType type;

    public static void StartForResult(Context context, int reqCode, SetType type) {
        Intent intent = new Intent(context, CommonSetActivity.class);
        intent.putExtra(Constants.TYPE, type);
        ((Activity) context).startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_set);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        type = (SetType) getIntent().getSerializableExtra(Constants.TYPE);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubTitle.setVisibility(View.VISIBLE);
        String title = "";
        String hint = "";
        if (type == SetType.ADD_FRIEND) {
            title = "添加好友";
            hint = "Email";
        } else if (type == SetType.OPEN_GROUP) {
            title = "新建聊天组";
            hint = "聊天组名称";
        }
        tvTitle.setText(title);
        tvSubTitle.setText("添加");
        etContent.setHint(hint);
    }

    @OnClick({R.id.tvSubTitle})
    public void onClick(View v) {
        if (v.equals(tvSubTitle)) {
            final String content = etContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                String error = "";
                if (type == SetType.ADD_FRIEND) {
                    error = "邮箱不能为空";
                } else if (type == SetType.OPEN_GROUP) {
                    error = "聊天组名不能为空";
                }
                etContent.setError(error);
                return;
            }

            if (type == SetType.ADD_FRIEND) {
                HttpReqTool.getInstance()
                        .createApi(BusinessApi.class)
                        .addFriend(content)
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Consumer<AddFriendResp>() {
                            @Override
                            public void accept(AddFriendResp resp) throws Exception {
                                if (resp.isSuccess()) {
                                    AddFriendResp.AddFriendInfo info = resp.getData();
                                    if (info == null) return;
                                    SocketFriendReqResp bean = new SocketFriendReqResp();
                                    bean.setCode("notice");
                                    SocketFriendReqResp.FriendReqInfo reqInfo = new SocketFriendReqResp.FriendReqInfo();
                                    reqInfo.setContent("我可以加你好友吗？");
                                    reqInfo.setName(content);
                                    reqInfo.setType("addFriend");
                                    reqInfo.setId(info.getAdd_id());
                                    reqInfo.setTime(System.currentTimeMillis());
                                    bean.setData(reqInfo);
                                    String reqStr = GsonTool.getObjectToJson(bean);
                                    connector.write(reqStr);
                                } else {
                                    Functions.toast(resp.getMsg());
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ApiObserver<AddFriendResp>() {
                            @Override
                            public void onSuccess(AddFriendResp resp) {
                                Intent intent = new Intent();
                                intent.putExtra(Constants.ID, resp.getData().getAdd_id());
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });
            } else if (type == SetType.OPEN_GROUP) {
                HttpReqTool.getInstance()
                        .createApi(BusinessApi.class)
                        .openGroupChat(content)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ApiObserver<HttpBaseBean>() {
                            @Override
                            public void onSuccess(HttpBaseBean resp) {
                                if (resp.isSuccess()) {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
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

        }
    }
}
