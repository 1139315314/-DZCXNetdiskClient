package com.dzcx.netdisk.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.entity.Request;
import com.google.gson.Gson;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TextRequest extends Service<String> {
	
	private String key, value;
	private Socket socket;
	private MyConfig config = Entrance.config;
	private String ip, token; int port;

	public TextRequest(String key, String value) {
		this.key = key;
		this.value = value;
		ip = config.getIp();
		port = Integer.valueOf(config.getPortPublic());
	}
	
	protected Task<String> createTask() {
		return new Task<String>() {
			protected String call() throws Exception {
				String flag = null, result = null;
				socket = new Socket();
				socket.connect(new InetSocketAddress(ip, port), 8000);
				if (socket.isConnected()) {
					Request request = new Request();
					request.setKey(key);
					request.setValue(value);
					OutputStream os = socket.getOutputStream();
					os.write((new Gson().toJson(request) + "\r\n").getBytes("UTF-8"));
					InputStream is = socket.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					StringBuffer text = new StringBuffer();
					while (!(result = flag = br.readLine()).equals("finish")) {
						text.append(flag + "\r\n");
					}
					updateMessage(text.substring(0, text.length() - 2));
					br.close();
					os.flush();
					os.close();
				}
				return result;
			}
		};
	}
}