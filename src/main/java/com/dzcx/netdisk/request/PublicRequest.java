package com.dzcx.netdisk.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.entity.Request;
import com.google.gson.Gson;

import javafx.concurrent.Service;
import javafx.concurrent.Task;



public class PublicRequest extends Service<String> {
	
	private String key, value;
	private Socket socket;
	private MyConfig config = Entrance.config;
	private String ip; int port;

	public PublicRequest(String key, String value) {
		this.key = key;
		this.value = value;
		ip = config.getIp().toString();
		port = Integer.valueOf(config.getPortPublic());
	}
	
	protected Task<String> createTask() {
		return new Task<String>() {
			
			private int reTry = 0;
			private String flag = null, result = null;
			
			protected String call() throws Exception {
				return request();
			}
			
			private String request() throws Exception {
				try {
					if (reTry == 3) return null; // 尝试重连
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), 1000);
				} catch (SocketTimeoutException e) {
					reTry++;
					return request();
				}
				if (socket.isConnected()) {
					Request request = new Request();
					request.setKey(key);
					request.setValue(value);
					OutputStream os = socket.getOutputStream();
					os.write((new Gson().toJson(request) + "\r\n").getBytes("UTF-8"));

					InputStream is = socket.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					while (!(result = flag = br.readLine()).equals("finish")) {
						updateMessage(flag);
					}
					br.close();
					os.flush();
					os.close();
				}
				return result;
			}
		};
	}
	
	public String getRequestKey() {
		return key;
	}
	
	public String getRequestValue() {
		return value;
	}
}