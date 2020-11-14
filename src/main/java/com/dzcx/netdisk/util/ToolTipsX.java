package com.dzcx.netdisk.util;

import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class ToolTipsX extends Tooltip {

	public ToolTipsX() {
		super();
		setFont(Font.font(14));
	}

	public ToolTipsX(String text) {
		super(text);
		setFont(Font.font(14));
	}
}