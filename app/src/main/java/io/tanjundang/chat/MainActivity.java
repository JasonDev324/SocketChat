package io.tanjundang.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.me.MeFragment;
import io.tanjundang.chat.talk.TalkFragment;
import io.tanjundang.chat.friends.FriendsFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        boolean hasNavigationBar = Functions.hasNavigationBar(this);
        if (hasNavigationBar) {
            Functions.setNavigationBarPadding(this, container);
        }
        Functions.setImmerseStatusBar(this);
        navigation.setOnNavigationItemSelectedListener(this);
        SkipToFragment(TalkFragment.getInstance());
    }

    public void SkipToFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_talk:
                SkipToFragment(TalkFragment.getInstance());
                return true;
            case R.id.navigation_contact:
                SkipToFragment(FriendsFragment.getInstance());
                return true;
            case R.id.navigation_me:
                SkipToFragment(MeFragment.getInstance());
                return true;
        }
        return false;
    }

    long clickTime;

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Functions.toast("再按一次后退键退出程序");
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }
}
