package com.dzcx.netdisk.core;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.controller.Main;
import com.dzcx.netdisk.entity.*;
import com.google.gson.Gson;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Download extends Service<Double> {
	
	private boolean isShutdown = false;
	private static SimpleListProperty<DownloadFile> list = new SimpleListProperty<DownloadFile>();

	private InputStream is;
	private OutputStream os;

	private static int iDownload = 0; // 正在传输的下标

	private MyConfig config = Entrance.config;

	private File dlFolder;
	private String ip; int port;
	private double transpeed = 0; // 传输总数据，用于测速

	private boolean isDownloading = false; // 用于完成队列时播放音效
	private AudioClip ac;
	
	public Download() {
		list.set(FXCollections.observableArrayList());
		ip = config.getIp();
		port = Integer.valueOf(config.getPortDownload());
	}

	protected Task<Double> createTask() {
		return new Task<Double>() {
			protected Double call() throws Exception {
				ac = Applet.newAudioClip(this.getClass().getClassLoader().getResource("photo/finish.wav"));
				Gson gson = new Gson();
				Socket socket = null;
				config = Entrance.config;
				// 创建下载文件夹
				System.out.println(config.getDlLocation());
				dlFolder = new File(config.getDlLocation());
				dlFolder.mkdirs();
				while (!isShutdown) {
					// 执行传输
					for (int i = 0; i < list.size(); i++) {
						// 下载完成标记，当完成下载任务的时候播放音效
						isDownloading = Boolean.valueOf(config.getSound().toString());
						// 下载完成后一个任务之后先且慢
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
							// TODO 系统托盘 SystemTrayX.getIcon().setToolTip("");
							// 获取下载文件对象
							DownloadFile file = list.get(i);
							// 获取输出流，等等向服务器发送所要下载的文件等信息
							os = socket.getOutputStream();
							Request request = new Request();
							request.setKey("download");
							request.setValue(gson.toJson(file));
							request(gson.toJson(request).toString());
							// 获取输入流，通过输入流接受服务器发送来的文件，再对文件进行保存
							is = socket.getInputStream();
							BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
							// 服务器返回ready，表示准备好了，就开始下载对应的文件
							if (br.readLine().equals("ready")) {
								// 标记正在下载列表中的第几个
								iDownload = i;
								// 拼接下载路径
								String dlPath = dlFolder + file.getDlPath();
								// 接受文件流，接受服务器中发送来的数据
								DataInputStream dis = new DataInputStream(is);
								// 输出文件流，把从服务器上的数据保存到磁盘中
								FileOutputStream fos = new FileOutputStream(dlPath + File.separator + file.getName());
								// 接受数据的缓冲数组
								byte[] bytes = new byte[4096];
								// 标记已经传输的字节
								int length = 0;
								// 标记当前按文件的进度以及文件大小
								double progress = 0, fileSize = file.getSize();
								while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
									// 把从服务器中读取到的数据写入磁盘中
									fos.write(bytes, 0, length);
									fos.flush();
									// 计算已经传输了的大小
									progress += length;
									// 所有文件的传输总数据，用来计算下载速度
									transpeed += length;
									// 更新一下当前文件已经下载了多少字节
									updateValue(progress);
									// 更新一下当前文件下载的完成百分比
									updateProgress(progress / fileSize, 1);
								}
								fos.flush();
								// 传输完成
								if (fos != null) fos.close();
								if (dis != null) dis.close();
								// 添加到历史纪录
								Main.getIoHistories().add(new IOHistory(file.getName(), dlPath, true));
								updateMessage(String.valueOf(Math.random()));
							}
						}
					}
					// 播放音效
					if (isDownloading && Boolean.valueOf(config.getSound().toString())) {
						ac.play();
						isDownloading = false;
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

	/**
	 * 添加下载队列
	 * 
	 * @param files      目标文件列表（取于主界面视图）
	 * @param targetPath 目标路径
	 * @param dlPath     下载相对位置（相对于设置的下载文件夹，不可越级）
	 */
	public void add(List<FileCell> files, String targetPath, String dlPath) {
		String name;
		DownloadFile file;
		for (int i = 0; i < files.size(); i++) {
			name = files.get(i).getName();
			file = new DownloadFile(
				name.substring(name.indexOf(".") + 1),
				targetPath,
				dlPath,
				files.get(i).getSizeLong()
			);
			list.get().add(file);
		}
	}
	
	// 直接添加队列
	public void add(DownloadFile file) {
		list.get().add(file);
	}
	
	public static int getIndex() {
		return iDownload;
	}
	
	public void shutdown() {
		this.isShutdown = true;
	}
	
	public double getTranspeed() {
		return transpeed;
	}
	
	public static SimpleListProperty<DownloadFile> getListProperty() {
		return list;
	}
}