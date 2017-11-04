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

import io.tanjundang.chat.base.utils.LogTool;


/**
 * @Author: TanJunDang
 * @Date: 2017/10/25
 * @Description:
 */

public class SocketConnector {

    Socket socket;

    private BufferedReader br;
    private BufferedWriter bw;

    String receiveMsg;

    //    设置连接超时
    private static final int CONNECT_TIME_OUT = 3000;
    ReceiveListener listener;

    String ipAddress;
    int port;
    Thread connectThread;
    Timer timer = new Timer();

    public SocketConnector(String ipAddress, int port, ReceiveListener listener) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.listener = listener;
        connect();
    }

    public void connect() {
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
                        throw new RuntimeException("need to setReceiveListener ");
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
                }

            }
        }, 0, 500);
    }

    /**
     * 抛出异常给外部，用于发送心跳
     *
     * @param msg
     * @throws IOException
     */
    public void sendMsg(String msg) throws IOException {
        bw.write(msg + "\r");
        bw.flush();
    }

    int HEARBEAT_PERIOD_SECOND = 20;

    public void sendBeatHeat(final String beatHeadMsg) throws IOException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
//                                    socket.sendUrgentData(0xFF); // 发送心跳包
                    /**
                     * 定时向服务器发送消息，让服务器过滤掉该信息。
                     */
                    sendMsg(beatHeadMsg);
                    System.out.println("正常发送心跳包");
                } catch (IOException e) {
                    System.out.println("发送心跳失败,请求重连");
                    reconnect();
                }
            }
        }, 0, HEARBEAT_PERIOD_SECOND * 1000);
//        Observable.interval(HEARBEAT_PERIOD_SECOND, TimeUnit.SECONDS)
//                .subscribe(new Observer<Long>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(Long value) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        disposable.dispose();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        disposable.dispose();
//                    }
//                });
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

    /**
     * 可能还有另外的操作
     */
    public void reconnect() {
        connect();
    }


    public interface ReceiveListener {
        void receive(String msg);
    }
}
