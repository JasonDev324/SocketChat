package io.tanjundang.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.base.entity.SocketMsgResp;
import io.tanjundang.chat.base.event.FinishEvent;
import io.tanjundang.chat.base.event.ReceiveMsgEvent;
import io.tanjundang.chat.base.network.SocketConnector;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.GsonTool;
import io.tanjundang.chat.base.utils.LogTool;
import io.tanjundang.chat.base.utils.RxBus;
import io.tanjundang.chat.base.utils.cache.CacheTool;
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

    BaseFragment currentFragment;
    String[] tags = new String[]{"TalkFragment", "FriendsFragment", "MeFragment"};

    TalkFragment talkFragment;
    FriendsFragment friendsFragment;
    MeFragment meFragment;

    public static SocketConnector connector;
    int ipPort = 4000;
    String ipHost = "59.110.136.203";

    ArrayList<SocketMsgResp.SocketMsgInfo> loadList = new ArrayList<>();

    Disposable closeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Functions.setImmerseStatusBar(this, container);
        navigation.setOnNavigationItemSelectedListener(this);

        initFragment();
        switchContent(currentFragment, talkFragment, 0);
        connector = new SocketConnector(ipHost, ipPort, new SocketConnector.Callback() {
            @Override
            public void receive(String data) {
                LogTool.i(TAG, "收到的消息：" + data);
                if (data.startsWith("\\")) {
                    data = Functions.unicode2String(data);
                }

                try {
                    SocketMsgResp resp = GsonTool.getServerBean(data, SocketMsgResp.class);
                    if (resp.getCode().equals("msg")) {
                        SocketMsgResp.SocketMsgInfo info = resp.getData();
                        loadList.clear();
                        loadList.addAll(CacheTool.loadReceiveMsg(MainActivity.this));
                        loadList.add(info);
                        CacheTool.saveReceiveMsg(MainActivity.this, loadList);
                        RxBus.getDefault().post(new ReceiveMsgEvent(info));
                        SocketMsgResp.ContentMsg contentMsg = info.getContent();
                        LogTool.i("SocketMsgInfo：", "from  " + info.getUserName() + " :  " + contentMsg.getBody() + "\n");
                    } else if (resp.getCode().equals("response")) {

                    }
                } catch (Exception e) {
//                tvMsg.append("from server:" + data + "\n");
                }
            }

            @Override
            public void sendFailure(String error) {

            }

            @Override
            public void reconnect() {

            }
        });

        closeDisposable = RxBus.getDefault()
                .toObservable(FinishEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FinishEvent>() {
                    @Override
                    public void accept(FinishEvent finishEvent) throws Exception {
                        finish();
                    }
                });
    }

    private void initFragment() {
        if (talkFragment == null) {
            talkFragment = new TalkFragment();
        }
        if (friendsFragment == null) {
            friendsFragment = new FriendsFragment();
        }
        if (meFragment == null) {
            meFragment = new MeFragment();
        }
    }

    public void switchContent(BaseFragment from, BaseFragment to, int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            currentFragment = to;
            if (!to.isAdded()) {
                transaction
                        .hide(from)
                        .add(R.id.content, to, tags[pos])
                        .commit();
            } else {
                transaction
                        .hide(from)
                        .show(to)
                        .commit();
            }
        } else {
            currentFragment = new TalkFragment();
            transaction
                    .add(R.id.content, currentFragment, tags[0])
                    .commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_talk:
                switchContent(currentFragment, talkFragment, 0);
                return true;
            case R.id.navigation_friends:
                switchContent(currentFragment, friendsFragment, 1);
                return true;
            case R.id.navigation_me:
                switchContent(currentFragment, meFragment, 2);
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
