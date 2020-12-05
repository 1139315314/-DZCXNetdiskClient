package com.dzcx.netdisk.core;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.controller.Main;
import com.dzcx.netdisk.entity.IOHistory;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.entity.Request;
import com.dzcx.netdisk.entity.UploadFile;
import com.google.gson.Gson;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Upload extends Service<Double> {
	
	private boolean isShutdown = false;
	private static SimpleListProperty<UploadFile> list = new SimpleListProperty<UploadFile>();
	private SimpleBooleanProperty finish = new SimpleBooleanProperty(false);

	private InputStream is;
	private OutputStream os;
	private static int iUpload = 0; // 正在传输的下标
	private MyConfig config = Entrance.config;
	private String ip; int port;
	private double transpeed = 0;
	private boolean isUploading = false;
	private AudioClip ac;

	public Upload() {
		list.set(FXCollections.observableArrayList());
		ip = config.getIp().toString();
		port = Integer.valueOf(config.getPortUpload());
	}

	protected Task<Double> createTask() {
		return new Task<Double>() {
			protected Double call() throws Exception {
				ac = Applet.newAudioClip(this.getClass().getClassLoader().getResource("photo/finish.wav"));
				Gson gson = new Gson();
				Socket socket = null;
				while (!isShutdown) {
					// 执行传输
					for (int i = 0; i < list.size(); i++) {
						// 音效相关
						isUploading = Boolean.valueOf(config.getSound().toString());
						// 上一个文件上传完成后且慢
						Thread.sleep(500);
						// 如果上一次的下载任务完成了，但是还没释放socket连接，则关闭掉
						if (socket != null && socket.isConnected()) {
							socket.close();
							socket = null;
						}
						// 开始连接
						socket = new Socket();
						socket.connect(new InetSocketAddress(ip, port), 8000);
						// 连接成功
						if (socket.isConnected()) {
							// 获取输出流，等等向服务器发送所要上传的文件等信息
							os = socket.getOutputStream();
							Request request = new Request();
							request.setKey("upload");
							request.setValue(gson.toJson(list.get(i)));
							request(gson.toJson(request).toString());
							// 获取输入流
							is = socket.getInputStream();
							BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
							// 收到服务器的ready信号，准备好上传了。
							if (br.readLine().equals("ready")) {
								// 标记正在下载列表中的第几个
								iUpload = i;
								// 获取本地文件的输入流
								FileInputStream fis = new FileInputStream(new File(list.get(i).getFromPath()));
								// 获取服务器的输出流，等等把本地的文件通过输出流上传到服务器
								DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
								// 缓冲数组
								byte[] bytes = new byte[4096];
								// 每一次读取数据的字节数
								int length = 0;
								// 进度条和文件大小
								double progress = 0, fileSize = list.get(i).getSize();
								while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
									dos.write(bytes, 0, length);
									dos.flush();
									// 当前文件已经上传的字节数
									progress += length;
									// 所有文件的传输总数据，用来计算上传速度
									transpeed += length;
									// 更新一下当前文件已经上传了多少字节
									updateValue(progress);
									// 更新一下当前文件上传的完成百分比
									updateProgress(progress / fileSize, 1);
								}
								if (fis != null) fis.close();
								if (dos != null) dos.close();
								finish.setValue(!finish.getValue());
								Main.getIoHistories().add(new IOHistory(list.get(i).getName(), list.get(i).getToPath(), false));
								updateMessage(String.valueOf(Math.random()));
							}
						}
					}
					if (isUploading && Boolean.valueOf(config.getSound().toString())) {
						ac.play();
						isUploading = false;
					}
					list.get().clear();
					Thread.sleep(3000);
				}
				return null;
			}
		};
	}

	private void request(Object data) throws IOException {
		if (os != null) os.write((data.toString() + "\r\n").getBytes("UTF-8"));
	}

	public void add(List<File> files, String path) {
		UploadFile file;
		for (int i = 0; i < files.size(); i++) {
			if (files.get(i).isFile()) {
				file = new UploadFile(
					files.get(i).getName(),
					files.get(i).getAbsolutePath(),
					path,
					files.get(i).length()
				);
				list.get().add(file);
			}
		}
	}
	
	public void add(UploadFile file) {
		list.get().add(file);
	}
	
	public static int getIndex() {
		return iUpload;
	}
	
	public void shutdown() {
		this.isShutdown = true;
	}
	
	public double getTranspeed() {
		return transpeed;
	}
	
	public static SimpleListProperty<UploadFile> getListProperty() {
		return list;
	}

	public SimpleBooleanProperty getFinish() {
		return finish;
	}
}