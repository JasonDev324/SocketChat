package io.tanjundang.chat.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.tanjundang.chat.R;
import io.tanjundang.chat.account.LoginActivity;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.event.FinishEvent;
import io.tanjundang.chat.base.utils.RxBus;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.btnExit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        tvTitle.setText("设置");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.btnExit})
    public void onClick(View v) {
        if (v.equals(btnExit)) {
            Global.getInstance().release(this);
            RxBus.getDefault().post(new FinishEvent());
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
