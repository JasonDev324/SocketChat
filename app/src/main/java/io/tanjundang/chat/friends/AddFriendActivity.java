package io.tanjundang.chat.friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.AddFriendResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/26
 * @Description: 新增朋友
 */

public class AddFriendActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.etEmail)
    EditText etEmail;

    public static void StartForResult(BaseFragment fragment, int reqCode) {
        Intent intent = new Intent(fragment.getContext(), AddFriendActivity.class);
        ((Activity) fragment.getContext()).startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("添加好友");
        tvSubTitle.setText("添加");
    }

    @OnClick({R.id.tvSubTitle})
    public void onClick(View v) {
        if (v.equals(tvSubTitle)) {
            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("邮箱不能为空");
                return;
            }
            HttpReqTool.getInstance()
                    .createApi(BusinessApi.class)
                    .addFriend(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<AddFriendResp>() {
                        @Override
                        public void onSuccess(AddFriendResp resp) {
                            if (resp.isSuccess()) {
                                AddFriendResp.AddFriendInfo info = resp.getData();
                                if (info == null) return;
                                Intent intent = new Intent();
                                intent.putExtra(Constants.ID, info.getAdd_id());
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
