package com.dzcx.netdisk.ui;

import com.dzcx.netdisk.util.uitools.BorderX;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ServerStateText extends HBox {

	private Label value;

	public ServerStateText(String labelText, double width, double height) {
		Label label = new Label(labelText);
		value = new Label();
		value.setPrefSize(width, height);
		value.setFont(new Font(13));

		setAlignment(Pos.CENTER_RIGHT);
		setSpacing(2);
		setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).top());
		getChildren().addAll(label, value);
	}

	public void setValue(String value) {
		this.value.setText(value);
	}
}