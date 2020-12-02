package com.dzcx.netdisk.controller;

import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.core.Download;
import com.dzcx.netdisk.core.NetSpeed;
import com.dzcx.netdisk.core.Upload;
import com.dzcx.netdisk.entity.FileCell;
import com.dzcx.netdisk.entity.IOHistory;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.entity.VideoInfo;
import com.dzcx.netdisk.request.PublicRequest;
import com.dzcx.netdisk.request.StateRequest;
import com.dzcx.netdisk.ui.FileListTable;
import com.dzcx.netdisk.ui.NavButton;
import com.dzcx.netdisk.ui.SystemTrayX;
import com.dzcx.netdisk.util.FileFormat;
import com.dzcx.netdisk.util.Interface.Tools;
import com.dzcx.netdisk.util.iUtil;
import com.dzcx.netdisk.util.implement.ToolsImp;
import com.dzcx.netdisk.util.uitools.TipsX;
import com.dzcx.netdisk.view.ViewMain;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {

	
	private Upload upload;
	private NetSpeed speed;
	private Download download;
//	private StateRequest state;
//	private IOHistoryLoader ioHistoryLoader = new IOHistoryLoader();
	private static List<IOHistory> ioHistories;
	
	private int searchIndex = 0;
	private Stage stage;
	private Label tips;
	private boolean isReload = false;
	private ViewMain view;
	private SystemTrayX tray;
	private FileListTable fileList;
	private Stack<String> prevStack, nextStack;
	
	private MyConfig config = Entrance.config;
	// 服务器的配置
	private Map<String, String> server;
	
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		view = new ViewMain();
		if (!config.getWidth().toString().equals("default"))
			view.setWidth(Double.valueOf(config.getWidth().toString()));
		if (!config.getHeight().toString().equals("default"))
			view.setHeight(Double.valueOf(config.getHeight().toString()));
		
		tips = view.getTipsPane().getTips();
		fileList = view.getFileList();
		
		prevStack = new Stack<String>(); // 返回栈
		nextStack = new Stack<String>(); // 前进栈
		prevStack.push(view.getPath().getText());
		getFileList(prevStack.peek());

		// TODO 设置系统托盘

		// 获取服务器配置
		getServerConfig();
		// 获取服务器的状态
		getStatus();

		// 上传核心
		upload = new Upload();
		upload.getFinish().addListener((obs, oldValue, newValue) -> getFileList(view.getPath().getText()));
		upload.exceptionProperty().addListener((obs, oldValue, newValue) -> newValue.printStackTrace());
		upload.start();
		// 下载核心
		download = new Download();
		download.exceptionProperty().addListener((obs, oldValue, newValue) -> newValue.printStackTrace());
		download.start();

		// 传输速度的监听
		netSpeed();

		// 双击文件列表
		fileList.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
				open();
		});



	}

	/**
	 * 打开选中项目
	 *
	 */
	private void open() {
		FileCell selected = fileList.getSelectionModel().getSelectedItem();
		if (selected != null) {
			String selectedItem = selected.getName();
			String format = selectedItem.substring(0, selectedItem.indexOf("."));
			final String fileName = selectedItem.substring(selectedItem.indexOf(".") + 1);
			if (format.equals("folder")) { // 文件夹
				String path = view.getPath().getText() + fileName + File.separator;
				prevStack.push(view.getPath().getText());
				getFileList(path);
				return;
			}
			if (view.getPath().getText().indexOf(server.get("publicFile")) == -1) { // 非公开外链
				if (FileFormat.isTextFile(format)) { // 文本预览
					TextEditor editor = new TextEditor(view.getPath().getText() + fileName);
					editor.getIsSave().addListener(event -> getFileList(view.getPath().getText()));
					return;
				}
				if (FileFormat.isImg(format)) { // 图片预览
					new Img(view.getPath().getText() + fileName);
					return;
				}
				if (FileFormat.isMP4(format)) { // 视频播放
					PublicRequest request = new PublicRequest("getMP4Info", view.getPath().getText() + fileName);
					YeyuUtils.gui().tips(tips, "正在获取视频信息...", 3000, TipsX.WARNING);
					request.messageProperty().addListener((tmp, o, n) -> {
						JsonObject jo = (new JsonParser()).parse(n).getAsJsonObject();
						VideoInfo video = new VideoInfo();
						video.setName(fileName);
						video.setUrl(view.getPath().getText() + fileName, config.get("ip").toString(), config.get("portHTTP").toString());
						video.setWidth(jo.get("width").getAsDouble());
						video.setHeight(jo.get("height").getAsDouble());
						video.setDeg(jo.get("deg").getAsInt());
						new Video(video, video.filter(fileList.getItems()), view.getPath().getText());
					});
					request.start();
					return;
				}
				if (FileFormat.isHTML(format)) { // 网页预览
					try {
						String path = view.getPath().getText() + selectedItem.substring(selectedItem.indexOf(".") + 1);
						path = path.replaceAll("\\\\", "/");
						path = YeyuUtils.encode().enURL(path).substring(1);
						String address = "http://" + config.getString("ip") + ":" + config.getString("portHTTP") + path;
						YeyuUtils.network().openURIInBrowser(new URL(address).toURI());
					} catch (MalformedURLException | URISyntaxException e) {
						e.printStackTrace();
					}
				}
				YeyuUtils.gui().tips(tips, rbx.r("notSupportOpen") + format + rbx.l("file"), 3000, TipsX.WARNING);
			}
		}
	}



	/**
	 * 网络传输速度
	 *
	 */
	private void netSpeed() {
		speed = new NetSpeed(upload, download, view.getNetSpeed());
		view.getTipsPane().getSpeed().textProperty().bind(speed.messageProperty());
		speed.start();
	}


	/**
	 * 获取文件列表
	 *
	 * @param path 路径
	 */
	private void getFileList(String path) {
		PublicRequest request = new PublicRequest("list", path);
		request.setOnRunning(event -> {
			view.getTipsPane().getTips().setText("正在获取文件列表");
		});
		request.messageProperty().addListener((tmp, oldValue, newValue) -> {
			if (!newValue.equals("") && !newValue.equals("null")) {
				SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				fileList.getItems().clear();
				fileList.refresh();
				JsonParser jp = new JsonParser();
				JsonElement jo = (JsonElement) jp.parse(newValue);
				JsonArray ja = jo.getAsJsonArray();
				JsonObject data;
				DecimalFormat format = new DecimalFormat("#,###");
				long fileSize = -1;
				for (int i = 0, l = ja.size(); i < l; i++) {
					data = ja.get(i).getAsJsonObject();
					fileSize = data.get("size").getAsLong();
					fileList.getItems().add(new FileCell(
							data.get("name").getAsString(),
							dataFormat.format(new Date(data.get("date").getAsLong())),
							iUtil.storageFormat(fileSize, format),
							fileSize
					));
				}
				view.getPath().setText(path);

				view.getTipsPane().setItems(ja.size());
				view.getTipsPane().setSelected(0);
				view.getTipsPane().getTips().setText("");
			}
		});
		request.setOnSucceeded(event -> view.getTipsPane().getTips().setText(""));
		request.setOnFailed(event -> view.getTipsPane().getTips().setText(""));
		request.start();
	}

	/**
	 * 获取服务器配置
	 *
	 */
	private void getServerConfig() {
		PublicRequest request = new PublicRequest("getConfig", "");
		request.messageProperty().addListener((obs, oldValue, newValue) -> {
			if (!newValue.equals("")) {
				JsonObject jo = (new JsonParser()).parse(newValue).getAsJsonObject();
				server = new HashMap<>();
				server.put("compressImg", jo.get("compressImg").getAsString());
				server.put("photo", jo.get("photo").getAsString());
				setNavList(jo.get("navList").getAsString());
				if (jo.get("publicFile") != null)
					server.put("publicFile", jo.get("publicFile").getAsString());
			}
		});
		request.start();
		isReload = false;
	}

	/**
	 * 设置侧边导航，导航内容从服务端获取
	 *
	 */
	private void setNavList(String navList) {
		Button btn;
		JsonArray ja = (new JsonParser()).parse(navList).getAsJsonArray();
		JsonObject jo;
		for (int i = 0; i < ja.size(); i++) {
			jo = ja.get(i).getAsJsonObject();
			for (Map.Entry<String, JsonElement> item : jo.entrySet()) {
				// 得到的对象拆分成key和value，key是文件夹的名称，value是对应的路径
				btn = new NavButton(item.getKey());
				btn.setOnAction(event -> {
					getFileList(File.separator + item.getValue().getAsString() + File.separator);
					prevStack.push(view.getPath().getText());
				});
				view.getNavBtns().getChildren().add(i + 1, btn);
			}
		}
	}

	// 里面有个匿名内部类，放里面不太行
	String diskUsed;
	String diskTotal;
	private void getStatus() {

		Tools tool = new ToolsImp();

		JsonParser jp = new JsonParser();
		DecimalFormat formatUse = new DecimalFormat("####");
		DecimalFormat formatTotal = new DecimalFormat("####");

		StateRequest stateRequest = new StateRequest();
		stateRequest.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null && !newValue.equals("")) {
					JsonObject jo = (JsonObject) jp.parse(newValue);
					view.getCpu().setValue(jo.get("cpuUse").getAsDouble());
					view.getMemory().setValue(jo.get("memUse").getAsDouble() / jo.get("memMax").getAsDouble());
					diskUsed = tool.storageFormat(jo.get("diskUse").getAsDouble(), formatUse);
					diskTotal = tool.storageFormat(jo.get("diskMax").getAsDouble(), formatTotal);
					view.getDisk().setValue(diskUsed + " / " + diskTotal);
				}
			}
		});
		stateRequest.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
				// TODO 给用户发出一些警告，告诉他们获取服务器的状态的时候发生了一些异常
				newValue.printStackTrace();
			}
		});
		stateRequest.start();

	}

	
	public ViewMain getView() {
		return view;
	}
	
	public static List<IOHistory> getIoHistories() {
		return ioHistories;
	}

	public static void setIoHistories(List<IOHistory> ioHistories) {
		Main.ioHistories = ioHistories;
	}
}