package io.tanjundang.chat;

import android.content.Intent;
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
import io.tanjundang.chat.me.MeFragment;
import io.tanjundang.chat.talk.TalkFragment;
import io.tanjundang.chat.contacts.ContactFragment;

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
                SkipToFragment(ContactFragment.getInstance());
                return true;
            case R.id.navigation_me:
                SkipToFragment(MeFragment.getInstance());
                return true;
        }
        return false;
    }
}
