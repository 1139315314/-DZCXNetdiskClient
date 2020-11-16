package com.dzcx.netdisk.util.uitools;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorPaneX extends AnchorPane {
	
	/**
	 * 四边完全贴紧父级组件
	 * 
	 * @param node 组件
	 */
	public static void def(Node node) {
		setTopAnchor(node, 0d);
		setLeftAnchor(node, 0d);
		setRightAnchor(node, 0d);
		setBottomAnchor(node, 0d);
	}
	
	/**
	 * 设置 AnchorPane 四边边距
	 * 
	 * @param node 组件
	 * @param size 大小
	 */
	public static void def(Node node, Number size) {
		setTopAnchor(node, size.doubleValue());
		setLeftAnchor(node, size.doubleValue());
		setRightAnchor(node, size.doubleValue());
		setBottomAnchor(node, size.doubleValue());
	}
	
	/**
	 * 设置 AnchorPane 上下和左右边距
	 * 
	 * @param node      组件
	 * @param topBottom 上下
	 * @param leftRight 左右
	 */
	public static void def(Node node, Number topBottom, Number leftRight) {
		if (topBottom != null) setTopAnchor(node, topBottom.doubleValue());
		if (leftRight != null) setLeftAnchor(node, leftRight.doubleValue());
		if (leftRight != null) setRightAnchor(node, leftRight.doubleValue());
		if (topBottom != null) setBottomAnchor(node, topBottom.doubleValue());
	}
	
	/**
	 * 设置 AnchorPane 四边间距，传值 null 为不设定
	 * 
	 * @param node   组件
	 * @param top    上
	 * @param right  右
	 * @param bottom 下
	 * @param left   左
	 */
	public static void def(Node node, Number top, Number right, Number bottom, Number left) {
		if (top != null) setTopAnchor(node, top.doubleValue());
		if (left != null) setLeftAnchor(node, left.doubleValue());
		if (right != null) setRightAnchor(node, right.doubleValue());
		if (bottom != null) setBottomAnchor(node, bottom.doubleValue());
	}

	/**
	 * 除了上边，其他贴紧父级组件
	 * 
	 * @param node 组件
	 */
	public static void exTop(Node node) {
		setLeftAnchor(node, 0d);
		setRightAnchor(node, 0d);
		setBottomAnchor(node, 0d);
	}

	/**
	 * 除了左边，其他贴紧父级组件
	 * 
	 * @param node 组件
	 */
	public static void exLeft(Node node) {
		setTopAnchor(node, 0d);
		setRightAnchor(node, 0d);
		setBottomAnchor(node, 0d);
	}

	/**
	 * 除了右边，其他贴紧父级组件
	 * 
	 * @param node 组件
	 */
	public static void exRight(Node node) {
		setTopAnchor(node, 0d);
		setLeftAnchor(node, 0d);
		setBottomAnchor(node, 0d);
	}

	/**
	 * 除了下边，其他贴紧父级组件
	 * 
	 * @param node 组件
	 */
	public static void exBottom(Node node) {
		setTopAnchor(node, 0d);
		setLeftAnchor(node, 0d);
		setRightAnchor(node, 0d);
	}
}