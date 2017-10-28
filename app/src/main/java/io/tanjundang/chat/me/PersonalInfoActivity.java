package io.tanjundang.chat.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/28
 * @Description: 个人资料页面
 */

public class PersonalInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
    }

    public static void Start(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        context.startActivity(intent);
    }

}
