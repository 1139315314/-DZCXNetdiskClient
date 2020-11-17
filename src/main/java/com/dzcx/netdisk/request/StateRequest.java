package com.dzcx.netdisk.request;

import com.drew.lang.StringUtil;
import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.entity.Request;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class StateRequest extends Service<String> {

    private Socket socket;
    private MyConfig config = Entrance.config;
    private String ip;
    private int port;
    private boolean isShutdown = false;

    public StateRequest() {
        ip = config.getIp();
        port = config.getPortState();
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 5000);
                if (socket.isConnected()) {
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    while (!isShutdown) {
                        String result = br.readLine();
                        if (result != null && result != "") {
                            updateValue(result);
                        } else {
                            Thread.sleep(200);
                        }
                    }

                    is.close();
                    os.close();
                    socket.close();

                }
                return null;
            }
        };
    }
}
