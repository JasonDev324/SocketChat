package io.tanjundang.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/25
 * @Description:
 */

public class SocketConnector {
    String ipAddress;
    int port;

    Socket socket;

    BufferedReader br;
    BufferedWriter bw;

    public SocketConnector(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        connect();
    }

    public void connect() {
        try {
            socket = new Socket(ipAddress, port);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
