package io.tanjundang.chat.talk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liaoinstan.springview.widget.SpringView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.entity.SocketInitJson;
import io.tanjundang.chat.base.entity.SocketKeepConnectJson;
import io.tanjundang.chat.base.entity.SocketMsgResp;
import io.tanjundang.chat.base.entity.type.ChatType;
import io.tanjundang.chat.base.network.SocketConnector;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.GsonTool;
import io.tanjundang.chat.base.utils.LogTool;
import io.tanjundang.chat.friends.ChatMsgActivity;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/19
 * @Description: 聊天界面
 */

public class ChatTestActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.springView)
    SpringView springView;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.ivAudio)
    ImageView ivAudio;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.ivMoon)
    ImageView ivMoon;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.llBottomTab)
    LinearLayout llBottomTab;
    @BindView(R.id.rootview)
    RelativeLayout rootview;
    @BindView(R.id.tvMsg)
    TextView tvMsg;

    String receiveMsg;
    String sendMsg;

    String ipHost = "59.110.136.203";
    //    String ipHost = "lawntiger.free.ngrok.cc";
    int ipPort = 4000;
    long chatId;
    long userId;
    //    p2p私聊、group群聊
    String chatType = "p2p";
    String contentType = "txt";
    String chatTitle;
    ChatType type;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String data = bundle.getString(Constants.MSG);
            LogTool.v(TAG, "收到的消息：" + data);
            try {
                SocketMsgResp resp = GsonTool.getServerBean(data, SocketMsgResp.class);
                if (resp.getCode().equals("msg")) {
                    SocketMsgResp.SocketMsgInfo info = resp.getData();
                    SocketMsgResp.ContentMsg contentMsg = info.getContent();
                    tvMsg.append("from  " + info.getUserName() + " :  " + contentMsg.getBody() + "\n");
                } else if (resp.getCode().equals("response")) {

                }
            } catch (Exception e) {
                tvMsg.append("from server:" + data + "\n");
            }
        }
    };

    /**
     * @param context
     * @param id
     * @param chatType
     * @param chatTitle
     */
    public static void Start(Context context, long id, ChatType chatType, String chatTitle) {
        Intent intent = new Intent(context, ChatTestActivity.class);
        intent.putExtra(Constants.ID, id);
        intent.putExtra(Constants.TYPE, chatType);
        intent.putExtra(Constants.DATA, chatTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userId = Global.getInstance().getUserId();
        chatTitle = getIntent().getStringExtra(Constants.DATA);
        type = (ChatType) getIntent().getSerializableExtra(Constants.TYPE);
        if (type == ChatType.P2P) {
            chatId = getIntent().getLongExtra(Constants.ID, 0);
        } else {
            chatId = getIntent().getLongExtra(Constants.ID, 0);
        }
        chatType = type == ChatType.P2P ? "p2p" : "group";
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        TalkTask task = new TalkTask(this);
//        task.execute();
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        ivRight.setImageResource(R.drawable.ic_contact_enable);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ivRight.setImageTintList(getColorStateList(R.color.tint_color_white));
        }
        ivRight.setVisibility(View.VISIBLE);
        tvTitle.setText(chatTitle);

        connect();
    }


    @OnClick({R.id.btnSend, R.id.ivAudio,
            R.id.ivPhoto, R.id.ivCamera,
            R.id.ivMoon, R.id.ivMore,
            R.id.ivRight})
    public void onClick(View v) {
        if (v.equals(btnSend)) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                    e.onNext(1);
                }
            }).subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            sendMsg();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            etContent.setText("");
                            Functions.toast("发送成功");
                        }
                    });
        } else if (v.equals(ivAudio)) {

        } else if (v.equals(ivPhoto)) {

        } else if (v.equals(ivCamera)) {

        } else if (v.equals(ivMoon)) {
        } else if (v.equals(ivMore)) {
        } else if (v.equals(ivRight)) {
            ChatMsgActivity.Start(this, chatId);
        }
    }

    @Override
    public void onBackPressed() {
        close();
        finish();
    }

    Disposable disposable;

    public void close() {
        connector.close();
        if (disposable != null)
            disposable.dispose();
    }

    SocketConnector connector;

    public void connect() {
        connector = new SocketConnector(ipHost, ipPort, new SocketConnector.ReceiveListener() {
            @Override
            public void receive(String receiveMsg) {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MSG, receiveMsg);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        SocketInitJson initJson = new SocketInitJson();
        initJson.setCode("init");
        SocketInitJson.DataBean data = new SocketInitJson.DataBean();
        data.setId(userId);
        initJson.setData(data);
        String initStr = GsonTool.getObjectToJson(initJson);
        connector.write(initStr);
    }

    public void sendMsg() {
        sendMsg = etContent.getText().toString().trim();
        SocketMsgResp json = new SocketMsgResp();
        SocketMsgResp.SocketMsgInfo info = new SocketMsgResp.SocketMsgInfo();
        SocketMsgResp.ContentMsg msg = new SocketMsgResp.ContentMsg();
        msg.setContentType(contentType);
        msg.setBody(sendMsg);
        info.setChatType(chatType);
        info.setId(chatId);
        info.setContent(msg);
        json.setCode("msg");
        json.setData(info);
        String msgContent = GsonTool.getObjectToJson(json);
        connector.write(msgContent);
        LogTool.v(TAG, "发送的消息" + msgContent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }
}
