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
import io.tanjundang.chat.board.BoardFragment;
import io.tanjundang.chat.home.HomeFragment;
import io.tanjundang.chat.me.MeFragment;

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
        navigation.setOnNavigationItemSelectedListener(this);
        SkipToFragment(HomeFragment.getInstance());
        boolean hasNavigationBar = Functions.hasNavigationBar(this);
        Functions.setImmerseStatusBar(this);
        if (hasNavigationBar) {
            Functions.setNavigationBarPadding(container, this);
        }
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
            case R.id.navigation_home:
                SkipToFragment(HomeFragment.getInstance());
                return true;
            case R.id.navigation_dashboard:
                SkipToFragment(BoardFragment.getInstance());
                return true;
            case R.id.navigation_notifications:
                SkipToFragment(MeFragment.getInstance());
                return true;
        }
        return false;
    }
}
