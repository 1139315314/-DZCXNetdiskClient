package com.dzcx.netdisk.util.uitools;

import com.dzcx.netdisk.util.GUI;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class TipsX extends Service<String> {
	
	private Label tips;
	private String content;
	private int time = 3000;
	private int mode = 0;
	
	public static final int DEFAULT = 0;
	public static final int ERROR   = 1;
	public static final int WARNING = 2;

	public TipsX(Label tips, String content) {
		this.tips = tips;
		this.content = content;
	}
	
	public TipsX(Label tips, String content, int time, int mode) {
		this.tips = tips;
		this.content = content;
		this.time = time;
		this.mode = mode;
	}

	protected Task<String> createTask() {
		return new Task<String>() {
			protected String call() throws Exception {
				Thread.currentThread().setName("IUtils Tips");
				switch (mode) {
					case DEFAULT:
						tips.setTextFill(GUI.GRAY);
						break;
					case WARNING:
						tips.setTextFill(GUI.ORANGE);
						break;
					case ERROR:
						tips.setTextFill(GUI.RED);
						break;
				}
				updateValue(content);
				Thread.sleep(time);
				return "";
			}
		};
	}
}
