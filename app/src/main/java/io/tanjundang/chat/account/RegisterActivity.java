package io.tanjundang.chat.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.LoginResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.DialogApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/17
 * @Description: 注册Activity
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.etAccount)
    AutoCompleteTextView etAccount;
    @BindView(R.id.etEmail)
    AutoCompleteTextView etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Functions.setNoChineseInput(etEmail);
    }

    @OnClick({R.id.btnRegister, R.id.btnLogin})
    public void onClick(final View v) {
        if (v.equals(btnRegister)) {
            Functions.hideKeyboard(v);
            String name = etAccount.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!Functions.isVaildEmail(email)) {
                Functions.toast("Email invaild");
                return;
            }

            if (TextUtils.isEmpty(name)) {
                Functions.toast("Name must be not null");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Functions.toast("Password must be not null");
                return;
            }
            HttpReqTool
                    .getInstance()
                    .createApi(BusinessApi.class)
                    .register(name, email, password)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DialogApiObserver<LoginResp>(this) {
                        @Override
                        public void onSuccess(LoginResp resp) {
                            if (resp.isSuccess()) {
                                Functions.toast("Register Success");
                                finish();
                            } else {
                                Functions.toast(resp.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                        }
                    });

        } else if (v.equals(btnLogin)) {
            finish();
        }
    }
}
