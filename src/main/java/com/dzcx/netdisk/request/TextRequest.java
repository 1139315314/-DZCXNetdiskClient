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
	private String ip; int port;

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
					// 封装请求
					Request request = new Request();
					request.setKey(key);
					request.setValue(value);
					// 发送请求给服务器
					OutputStream os = socket.getOutputStream();
					os.write((new Gson().toJson(request) + "\r\n").getBytes("UTF-8"));
					// 获取服务器发送来的数据
					InputStream is = socket.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					StringBuffer text = new StringBuffer();
					while (!(result = flag = br.readLine()).equals("finish")) {
						text.append(flag + "\r\n");
					}
					// 更新文本数据，文本减去回车换行两个符号，才是真正的文本
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