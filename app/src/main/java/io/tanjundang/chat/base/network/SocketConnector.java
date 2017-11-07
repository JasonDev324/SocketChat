package io.tanjundang.chat.base.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.entity.SocketInitJson;
import io.tanjundang.chat.base.entity.SocketKeepConnectJson;
import io.tanjundang.chat.base.utils.FormatTool;
import io.tanjundang.chat.base.utils.GsonTool;
import io.tanjundang.chat.base.utils.LogTool;


/**
 * @Author: TanJunDang
 * @Date: 2017/10/25
 * @Description: Socket封装
 */

public class SocketConnector {

    public Socket socket;

    private BufferedReader br;
    private BufferedWriter bw;

    String receiveMsg;

    //    设置连接超时
    private static final int CONNECT_TIME_OUT = 3000;
    Callback listener;

    String ipAddress;
    int port;
    Thread connectThread;
    Timer timer;
    Timer beatHeat;
    //    每20s发送心跳
    int BEATHEAT_PERIOD_SECOND = 20;

    boolean isReconnect = false;

    public SocketConnector(String ipAddress, int port, Callback listener) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.listener = listener;
        connect();
        sendBeatHeat();
    }

    private void sendInitMsg() {
        SocketInitJson initJson = new SocketInitJson();
        initJson.setCode("init");
        SocketInitJson.DataBean data = new SocketInitJson.DataBean();
        data.setId(Global.getInstance().getUserId());
        initJson.setData(data);
        String initStr = GsonTool.getObjectToJson(initJson);
        write(initStr);
    }

    public void connect() {
        timer = new Timer();
        beatHeat = new Timer();
        connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ipAddress, port), CONNECT_TIME_OUT);
                    socket.setKeepAlive(true);
                    socket.setTcpNoDelay(true);
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    if (listener == null) {
                        throw new RuntimeException("Callback is null");
                    }

//                    连接成功后发送初始化连接的msg
                    sendInitMsg();

                    if (isReconnect) {
                        listener.reconnect();
                    }

                    try {
                        while ((receiveMsg = br.readLine()) != null) {
                            listener.receive(receiveMsg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        connectThread.start();
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void write(final String msg) {
        if (bw == null) {
            LogTool.e("SocketConnector", "服务器GG");
            return;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    bw.write(msg + "\r");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.sendFailure(e.getMessage());
                }
            }
        }, 500);
    }

    /**
     * 抛出异常给外部，用于发送心跳
     *
     * @param msg
     * @throws IOException
     */
    public void sendBeatHeatMsg(String msg) throws IOException {
        if (bw == null) {
            LogTool.e("SocketConnector", "心跳bw为null");
            return;
        }
        LogTool.i("SocketConnector", "正常发送心跳包-----" + FormatTool.getYyyyMmDdHhMmSs(System.currentTimeMillis()));
        bw.write(msg + "\r");
        bw.flush();
    }

    private void sendBeatHeat() {
        /**
         * 定时向服务器发送消息，让服务器过滤掉该信息。
         */
        SocketKeepConnectJson json = new SocketKeepConnectJson();
        json.setCode("ping");
        final String jsonStr = GsonTool.getObjectToJson(json);
        Observable.interval(BEATHEAT_PERIOD_SECOND, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
//              socket.sendUrgentData(0xFF); // 发送心跳包
                            /**
                             * 定时向服务器发送消息，让服务器过滤掉该信息。
                             */
                            sendBeatHeatMsg(jsonStr);
                        } catch (IOException e) {
                            reconnect();
                        }
                    }
                });
    }

    public void close() {
        beatHeat.cancel();
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
        LogTool.i("SocketConnector", "Socket关闭");
    }

    /**
     * 重连
     */
    public void reconnect() {
        LogTool.i("SocketConnector", "发送心跳失败,请求重连");
        isReconnect = true;
        close();
        connect();
    }


    public interface Callback {

        void receive(String msg);

        void sendFailure(String error);

        void reconnect();
    }
}
