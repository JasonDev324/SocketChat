package io.tanjundang.chat.talk;

import android.content.Context;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.LogTool;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/19
 * @Description: 聊天界面
 */

public class ChatActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
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

    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    String receiveMsg;
    String sendMsg;

    String ipHost = "59.110.136.203";
    //    String ipHost = "lawntiger.free.ngrok.cc";
    int ipPort = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TalkTask task = new TalkTask(this);
        task.execute();
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());

    }


    @OnClick({R.id.btnSend, R.id.ivAudio,
            R.id.ivPhoto, R.id.ivCamera,
            R.id.ivMoon, R.id.ivMore})
    public void onClick(View v) {
        if (v.equals(btnSend)) {
            if (socket == null) return;
            if (socket.isConnected()) {
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
            }
        } else if (v.equals(ivAudio)) {
            if (socket == null) return;
            Functions.toast("服务器连接状态" + socket.isConnected());
        } else if (v.equals(ivPhoto)) {

        } else if (v.equals(ivCamera)) {

        } else if (v.equals(ivMoon)) {

        } else if (v.equals(ivMore)) {

        }
    }

    @Override
    public void onBackPressed() {
        close();
        finish();
    }

    class TalkTask extends AsyncTask<Void, String, Void> {
        WeakReference<Context> weakReference;
        ChatActivity activity;

        public TalkTask(Context context) {
            weakReference = new WeakReference(context);
            activity = (ChatActivity) weakReference.get();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                socket = new Socket(ipHost, ipPort);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                boolean isConnect = socket.isConnected();
                LogTool.v(TAG, "服务器链接状态：" + isConnect);
                while ((receiveMsg = br.readLine()) != null) {
                    publishProgress(receiveMsg);//将数据发送到onProgressUpdate
                }
            } catch (IOException e) {
                LogTool.e(TAG, "server error" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tvMsg.append("from server:" + values[0] + "\n");
        }

    }

    public void close() {
        try {
            if (socket != null)
                socket.close();

            if (bw != null)
                bw.close();

            if (br != null)
                br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        if (bw == null) return;
        sendMsg = etContent.getText().toString().trim();
        try {
            bw.write(sendMsg + "\r");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
