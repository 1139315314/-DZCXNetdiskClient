package com.dzcx.netdisk.util.implement;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.dzcx.netdisk.util.Interface.GUI;
import com.dzcx.netdisk.util.entity.Debug;
import com.dzcx.netdisk.util.entity.Error;
import com.dzcx.netdisk.util.uitools.TipsX;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class GUIImp implements GUI {
	
	private void doTips(Label tips, String content, int time, int mode, boolean keepText) {
		Paint oldPaint = tips.getTextFill();
		String oldText = tips.getText();
		TipsX tipsX = new TipsX(tips, content, time, mode);
		tipsX.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (!newValue.equals("")) {
				tips.setText(newValue);
			} else {
				if (tips.getText().equals(content)) {
					if (keepText) {
						tips.setText(oldText);
					} else {
						tips.setText("");
					}
				}
				tips.setTextFill(keepText ? oldPaint : GUI.GRAY);
			}
		});
		tipsX.start();
	}
	
	public void tips(Label tips, String content, int time, int mode, boolean keepText) {
		doTips(tips, content, time, mode, keepText);
	}

	public void tips(Label tips, String content, int time, int mode) {
		doTips(tips, content, time, mode, false);
	}

	public void tips(Label tips, String content) {
		doTips(tips, content, 3000, TipsX.DEFAULT, false);
	}
	
	public void debug(Object content) {
		new Thread () {
			public void run() {
				Debug debug = new Debug();
				debug.setDebug(content);
				debug.setVisible(true);
			}
		}.start();
	}
	
	public void exception(Exception e) {
		Error error = new Error();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		error.error("Exception: \n" + sw.toString());
		error.setVisible(true);
	}
	
	public void setBg(Node node, String url, int width, int x, int y) {
		node.setStyle(
			"-fx-background-size: " + width + ";" +
			"-fx-background-image: url('" + url + "');" +
			"-fx-background-insets: 0;" +
			"-fx-background-repeat: no-repeat;" +
			"-fx-background-position: " + x + " " + y
		);
	}
	
	public void setBgTp(Node node, String url, int width, int x, int y) {
		node.setStyle(
			"-fx-background-size: " + width + ";" +
			"-fx-background-image: url('" + url + "');" +
			"-fx-background-color: transparent;" +
			"-fx-background-insets: 0;" +
			"-fx-background-repeat: no-repeat;" +
			"-fx-background-position: " + x + " " + y
		);
	}
}