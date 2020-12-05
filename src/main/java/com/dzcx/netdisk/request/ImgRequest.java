package com.dzcx.netdisk.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.entity.Request;
import com.dzcx.netdisk.util.implement.ToolsImp;
import com.google.gson.Gson;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;


public class ImgRequest extends Service<Image> {

	private MyConfig config = Entrance.config;
	private InputStream is;
	private OutputStream os;
	
	private String key, value;
	private Socket socket;
	private String ip; int port;
	private boolean isCompressImg = false;
	private DecimalFormat format = new DecimalFormat("#,###");

	/**
	 * isCompressImg 为 true 时不执行压缩
	 * 
	 * @param key
	 * @param value
	 * @param isCompressImg
	 * @param
	 */
	public ImgRequest(String key, String value, boolean isCompressImg) {
		this.key = key;
		this.value = value;
		this.isCompressImg = isCompressImg;
		ip = config.getIp();
		port = Integer.valueOf(config.getPortPublic());
	}

	protected Task<Image> createTask() {
		return new Task<Image>() {
			
			private int reTry = 0;
			private Image img = null;
			
			protected Image call() throws Exception {
				updateMessage("正在连接");
				return getImg();
			}
			
			private Image getImg() throws Exception {
				try {
					if (reTry == 3) return null; // 3 次重连失败
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), 1000);
				} catch (SocketTimeoutException e) {
					reTry++;
					return getImg();
				}
				if (socket.isConnected()) {
					// 封装请求
					Request request = new Request();
					request.setKey(key);
					request.setValue(value);
					// 向服务器发送请求
					os = socket.getOutputStream();
					os.write((new Gson().toJson(request) + "\r\n").getBytes("UTF-8"));
					// 获取socket的输入流
					is = socket.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					String result = br.readLine(), onload = "加载中";
					// 接受到图片的大小后
					if (result.startsWith("size")) {
						// 得到文件的大小
						long fileSize = Integer.valueOf(result.substring(4));
						// 获取输出流，告诉服务器已经准备好接受数据了。
						os = socket.getOutputStream();
						os.write("ready\r\n".getBytes("UTF-8"));
						is = socket.getInputStream();
						// 先把服务器发送过来的数据保存到一个缓冲区当中
						ByteArrayOutputStream cloneBAOS = new ByteArrayOutputStream();
						byte[] buffer = new byte[4096];
						int l = 0;
						double progress = 0;
						while ((l = is.read(buffer)) > -1) {
							// 把所有的数据都暂存到缓冲区当中
							cloneBAOS.write(buffer, 0, l);
							cloneBAOS.flush();
							// 记录一下已经传输了的文件大小，并且通过已经记录的文件总大小，计算出传输进度
							progress += l;
							updateProgress(progress / fileSize, 1);
							// 对图片的stage中的文本进行更改为进度
							updateMessage(onload + formatOnload(progress) + " / " + formatOnload(fileSize));
						}
						cloneBAOS.flush();

						// 当我们只想要获取原图的数据的时候，直接把这些数据发送给客户端
						if (isCompressImg) { // 直接收图
							InputStream isOut = new ByteArrayInputStream(cloneBAOS.toByteArray());
							img = new Image(isOut);
							isOut.close();
						} else { // 当我们想要获取压缩图片的时候，就进行对应的压缩算法然后发送给客户端
							InputStream isIfx = new ByteArrayInputStream(cloneBAOS.toByteArray());
							InputStream isOutx = new ByteArrayInputStream(cloneBAOS.toByteArray());
							img = new Image(isIfx);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							if (img.getWidth() < img.getHeight()) {
								Thumbnails.of(isOutx).width(128).toOutputStream(baos);
							} else {
								Thumbnails.of(isOutx).height(128).toOutputStream(baos);
							}
							img = new Image(os2is(baos));
							isOutx.close();
							isIfx.close();
							baos.close();
							System.gc();
						}
						cloneBAOS.close();
						os.flush();
						os.close();
					}
				}
				return img;
			}
		};
	}
	
	private String formatOnload(double v) {
		return new ToolsImp().storageFormat(v, format);
	}

	// 输出转输入
	private ByteArrayInputStream os2is(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}
}