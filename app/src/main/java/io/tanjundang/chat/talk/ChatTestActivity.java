package io.tanjundang.chat.talk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Constants;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.entity.SocketMsgResp;
import io.tanjundang.chat.base.entity.type.ChatType;
import io.tanjundang.chat.base.event.ReceiveMsgEvent;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.GsonTool;
import io.tanjundang.chat.base.utils.LogTool;
import io.tanjundang.chat.base.utils.RxBus;
import io.tanjundang.chat.friends.ChatMsgActivity;

import static io.tanjundang.chat.MainActivity.connector;

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

    String sendMsg;
    //    String ipHost = "lawntiger.free.ngrok.cc";
    int ipPort = 4000;
    String ipHost = "59.110.136.203";

    long chatId;
    long userId;
    //    p2p私聊、group群聊
    String chatType = "p2p";
    String contentType = "txt";
    String chatTitle;
    ChatType type;

    Disposable disposable;

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
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        ivRight.setImageResource(R.drawable.ic_contact_enable);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ivRight.setImageTintList(getColorStateList(R.color.tint_color_white));
        }
        ivRight.setVisibility(View.VISIBLE);
        tvTitle.setText(chatTitle);

        disposable = RxBus.getDefault()
                .toObservable(ReceiveMsgEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReceiveMsgEvent>() {
                    @Override
                    public void accept(ReceiveMsgEvent receiveMsgEvent) throws Exception {
                        SocketMsgResp.SocketMsgInfo info = receiveMsgEvent.getInfo();
                        tvMsg.append(receiveMsgEvent.getData());
                    }
                });
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
    }


    public void close() {
        if (disposable != null)
            disposable.dispose();
        finish();
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
        LogTool.i(TAG, "发送的消息" + msgContent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }
}
