package io.tanjundang.chat.base;

import android.support.v7.app.AppCompatActivity;


/**
 * Author: TanJunDang
 * Email: TanJunDang324@gmail.com
 * Date: 2016/4/3
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getName();
    protected final String AUTHOR = "TanJunDang";

//    protected abstract int getLayoutId();
//
//    protected abstract void initView();
//
//    protected abstract void intData();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(getLayoutId());
//        initView();
//        intData();
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
