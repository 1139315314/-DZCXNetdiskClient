package com.dzcx.netdisk.util;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;

public class SeparatorX extends Separator {

	public SeparatorX(boolean isVertical) {
		if (isVertical) setOrientation(Orientation.VERTICAL);
	}
	
	public void setPadding(double width) {
		setPadding(new Insets(width));
	}
	
	public SeparatorX(boolean isVertical, double spacing) {
		if (isVertical) setOrientation(Orientation.VERTICAL);
		setPadding(new Insets(0, spacing, 0, spacing));
	}
	
	public SeparatorX(boolean isVertical, double spacing, double height) {
		if (isVertical) setOrientation(Orientation.VERTICAL);
		setPadding(new Insets(0, spacing, 0, spacing));
		setPrefHeight(height);
	}
	
	public void setPadding(double top, double right, double bottom, double left) {
		setPadding(new Insets(top, right, bottom, left));
	}
}
