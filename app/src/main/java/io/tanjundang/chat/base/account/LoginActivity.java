package io.tanjundang.chat.base.account;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etAccount)
    AutoCompleteTextView etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
}
