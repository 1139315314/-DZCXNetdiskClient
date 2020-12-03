package com.dzcx.netdisk.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.dzcx.netdisk.Entrance;
import com.dzcx.netdisk.dialog.Confirm;
import com.dzcx.netdisk.entity.MyConfig;
import com.dzcx.netdisk.request.PublicRequest;
import com.dzcx.netdisk.request.TextRequest;
import com.dzcx.netdisk.util.implement.IOImp;
import com.dzcx.netdisk.view.ViewTextEditor;
import com.google.gson.Gson;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;

public class TextEditor extends ViewTextEditor {

	private MyConfig config = Entrance.config;
	
	private int fontSize = 12;
	private String fontFamily = "System";
	private String path, oldData = "";
	private SimpleBooleanProperty isSave = new SimpleBooleanProperty(false);

	public TextEditor(String path) {
		super(path);
		this.path = path;
		fontSize = Integer.valueOf(config.getFontSize());
		fontFamily = config.getFontFamily();
		getLine().setFont(Font.font(fontFamily, fontSize));
		getTextArea().setFont(Font.font(fontFamily, fontSize));
		
		// 重新载入云端文件
		getReload().setOnAction(event -> getData());
		// 保存至云端
		getSaveCloud().setOnAction(event -> setData());
		// 缓存到本地
		getSaveCache().setOnAction(event -> {
			(new File("text")).mkdirs();
			setCache("text" + File.separator + path.substring(path.lastIndexOf(File.separator)));
			setLines("已缓存本地 - ");
		});
		// 同步保存
		getSaveSync().setOnAction(event -> {
			(new File("text")).mkdirs();
			setCache("text" + File.separator + path.substring(path.lastIndexOf(File.separator)));
			setData();
		});
		// 另存为
		getSaveAs().setOnAction(event -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("选择文件夹");
			File dir = directoryChooser.showDialog(null);
			if (dir != null) {
				setCache(dir + File.separator + path.substring(path.lastIndexOf(File.separator)));
				setLines("已保存 - ");
			}
		});
		// 关闭
		getClose().setOnAction(event -> {
			if (!oldData.trim().equals(getTextArea().getText().trim())) {
				confirm();
			} else {
				close();
			}
		});
		// 撤销
		getCancel().setOnAction(event -> getTextArea().cancelEdit());
		// 重做
		getRedo().setOnAction(event -> getTextArea().redo());
		// 全选
		getSelectAll().setOnAction(event -> getTextArea().selectAll());
		// 复制
		getCopy().setOnAction(event -> getTextArea().copy());
		// 粘贴
		getPaste().setOnAction(event -> getTextArea().paste());
		// 剪切
		getCut().setOnAction(event -> getTextArea().cut());
		
		// 字号
		for (int i = 0; i < getFontSize().length; i++) {
			getFontSize()[i].setOnAction(event -> {
				MenuItem item = (MenuItem) event.getSource();
				fontSize = Integer.valueOf(item.getText());
				getTextArea().setFont(Font.font(fontFamily, fontSize));
				getLine().requestFocus();
				getTextArea().requestFocus();
			});
		}
		// 字体
		for (int i = 0; i < getFontFamily().length; i++) {
			getFontFamily()[i].setOnAction(event -> {
				MenuItem item = (MenuItem) event.getSource();
				fontFamily = item.getText();
				getTextArea().setFont(Font.font(fontFamily, fontSize));
				getLine().requestFocus();
				getTextArea().requestFocus();
			});
		}
		// 字体绑定
		getLine().fontProperty().bind(getTextArea().fontProperty());
		// 输入监听
		getTextArea().setOnKeyPressed(event -> {
			setLines("");
			getLine().setScrollTop(getTextArea().getScrollTop());
		});
		// 滚动绑定
		getTextArea().scrollTopProperty().addListener((obs, oldValue, newValue) -> {
			getLine().setScrollTop(newValue.doubleValue());
		});
		// 关闭事件
		setOnCloseRequest(event -> {
			if (!oldData.trim().equals(getTextArea().getText().trim())) {
				event.consume();
				confirm();
			} else {
				setConfig();
				close();
			}
		});
		
		// 获取数据
		getData();
	}
	
	/**
	 * 阻止窗体关闭
	 * 
	 */
	private void confirm() {
		Confirm confirm = new Confirm("提示", "已修改文档内容，是否保存到云端", true);
		confirm.initConfirm(confirm, cevent -> {
			setData();
			setConfig();
			confirm.close();
			this.close();
		});
		confirm.initDeny(confirm, devent -> {
			setConfig();
			confirm.close();
			this.close();
		});
	}
	
	/**
	 * 保存编辑器样式设置
	 * 
	 */
	private void setConfig() {
		config.setFontFamily(fontFamily);
		config.setFontSize(fontSize);
	}
	
	/**
	 * 保存至云端
	 * 
	 */
	private void setData() {
		if (!oldData.trim().equals(getTextArea().getText().trim())) {
			Map<String, Object> map = new HashMap<>();
			map.put("file", path);
			map.put("data", getTextArea().getText());
			PublicRequest request = new PublicRequest("setText", new Gson().toJson(map));
			request.valueProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue.equals("finish")) {
					isSave.setValue(!isSave.getValue());
					setLines("已保存到云端 - " + " - ");
					oldData = getTextArea().getText();
				}
			});
			request.start();
		}
	}
	
	/**
	 * 保存至本地
	 * 
	 */
	private void setCache(String path) {
		new IOImp().stringToFile(new File(path), getTextArea().getText());
	}
	
	/**
	 * 设置行数
	 * 
	 * @param content
	 */
	private void setLines(String content) {
		StringBuffer sb = new StringBuffer();
		int lines = getTextArea().getText().split("\r\n|[\r\n]").length;
		for (int i = 1; i < lines + 1; i++) {
			if (i != lines) {
				sb.append(i + "\r\n");
				getLine().appendText(i + "\r\n");
			} else {
				sb.append(i);
			}
		}
		int j = 0;
		while (j < 32) { // 保持出现滚动条
			sb.append("\r\n");
			j++;
		}
		getLine().setText(sb.toString());
		getTips().setText(content + "行数：" + lines + "长度：" + getTextArea().getText().length());
	}
	
	/**
	 * 获取文本数据
	 * 
	 */
	private void getData() {
		// 保存文本的变量
		StringBuffer sb = new StringBuffer();
		// 发送获取文本的请求
		TextRequest request = new TextRequest("getText", path);
		// 发送请求之后，监听服务器发送过来的文本数据，用string来保存起来
		request.messageProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null)
				sb.append(newValue + "\r\n");
		});
		// 监听value的属性，当收到了finsh信号之后，设置文本内容和文本行号
		request.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue.equals("finish")) {
				if (4 < sb.length()) {
					getTextArea().setText(sb.substring(0, sb.length() - 4));
				} else {
					getTextArea().setText(sb.toString());
				}
				// 先保存下来目前所有的文本
				oldData = getTextArea().getText();
				// 读取完文本数据之后设置行号
				setLines("");
			}
		});
		request.start();
	}

	public SimpleBooleanProperty getIsSave() {
		return isSave;
	}
}