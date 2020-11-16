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
import com.dzcx.netdisk.entity.FileCell;
import com.dzcx.netdisk.entity.IOHistory;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.request.PublicRequest;
import com.dzcx.netdisk.ui.FileListTable;
import com.dzcx.netdisk.ui.SystemTrayX;
import com.dzcx.netdisk.util.iUtil;
import com.dzcx.netdisk.view.ViewMain;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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

	
//	private Upload upload;
//	private NetSpeed speed;
//	private Download download;
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