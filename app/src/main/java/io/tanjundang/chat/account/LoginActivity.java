package io.tanjundang.chat.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.MainActivity;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.LoginResp;
import io.tanjundang.chat.base.entity.QiNiuTokenResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.SharePreTool;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    AutoCompleteTextView etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.rlRootView)
    RelativeLayout rlRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Functions.setImmerseStatusBar(this, rlRootView);
        Functions.setNoChineseInput(etEmail);
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister})
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            Functions.hideKeyboard(v);
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!Functions.isVaildEmail(email)) {
                Functions.toast("Email invaild");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Functions.toast("Password must be not null");
                return;
            }
            dialog.show();
            dialog.setMessage("Authenticating..");
            HttpReqTool
                    .getInstance()
                    .createApi(BusinessApi.class)
                    .login(email, password)
                    .flatMap(new Function<LoginResp, ObservableSource<QiNiuTokenResp>>() {
                        @Override
                        public ObservableSource<QiNiuTokenResp> apply(LoginResp resp) {
                            if (resp.isSuccess()) {
                                LoginResp.LoginInfo info = resp.getData();
                                SharePreTool.getSP(LoginActivity.this).putString(Constants.TOKEN, info.getApi_token());
                                SharePreTool.getSP(LoginActivity.this).putLong(Constants.USER_ID, info.getId());
                                SharePreTool.getSP(LoginActivity.this).putString(Constants.NICKNAME, info.getName());
                                SharePreTool.getSP(LoginActivity.this).putString(Constants.EMAIL, info.getEmail());

                                Global.TOKEN = info.getApi_token();
                                Global.getInstance().setNickname(info.getName());
                                Global.getInstance().setEmail(info.getEmail());
                                Global.getInstance().setUserId(info.getId());

                                return HttpReqTool.getInstance().createApi(BusinessApi.class).getQiNiuToken();
                            } else {
                                Functions.toast(resp.getMsg());
                                return null;
                            }

                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<QiNiuTokenResp>() {
                        @Override
                        public void onSuccess(QiNiuTokenResp resp) {
                            dialog.dismiss();
                            if (resp.isSuccess()) {
                                QiNiuTokenResp.TokenInfo info = resp.getData();
                                if (info == null) return;
                                SharePreTool.getSP(LoginActivity.this).putString(Constants.QINIU_TOKEN, info.getToken());
                                Global.getInstance().setQiniuToken(info.getToken());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Functions.toast(resp.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            dialog.dismiss();
                        }
                    });
        } else if (v.equals(btnRegister)) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }


}
