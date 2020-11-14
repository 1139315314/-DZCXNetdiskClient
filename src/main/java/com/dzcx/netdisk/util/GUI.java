package com.dzcx.netdisk.util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

/**
 * GUI 图形界面接口
 * 
 * @author Yeyu
 *
 */
public interface GUI {

	// JavaFX 的颜色系统
	
	/** 白色 */
	public final Paint WHITE       = Paint.valueOf("#FFF");
	/** 品红 */
	public final Paint RED         = Paint.valueOf("#F30");
	/** 纯黑 */
	public final Paint BLACK       = Paint.valueOf("#000");
	/** 橙色 */
	public final Paint ORANGE      = Paint.valueOf("#F60");
	/** 绿色 */
	public final Paint GREEN       = Paint.valueOf("#393");
	/** 灰色 */
	public final Paint GRAY        = Paint.valueOf("#666");
	/** 天蓝 */
	public final Paint BLUR        = Paint.valueOf("#008DCB");
	/** 亮灰 */
	public final Paint LIGHT_GRAY  = Paint.valueOf("#9B9B9B");
	/** 少女粉 */
	public final Paint PINK        = Paint.valueOf("#FF7A9B");
	/** 透明 */
	public final Paint TRANSPARENT = Paint.valueOf("#FFFFFF00");

	/**
	 * <p>JavaFX 标签文本提示
	 * <p>示例：tips(label, "error", 3000, Tips.ERROR);
	 * 
	 * @param tips       JavaFX Label 标签
	 * @param content    文本内容
	 * @param time       持续时间，单位：毫秒
	 * @param mode       文本样式，Tips 类静态变量：DEFAULT（默认），WARNING（橙色警告），ERROR（红色错误）
	 * @param keepString 是否保持原有字符串
	 */
	public void tips(Label tips, String content, int time, int mode, boolean keepString);
	public void tips(Label tips, String content, int time, int mode);
	public void tips(Label tips, String content);
	
	/**
	 * <p>显示 Debug 窗体输出内容（AWT）
	 * <p>例：debug("test");
	 * 
	 * @param content 输出内容
	 */
	public void debug(Object content);
	
	/**
	 * <p>当程序发生致命错误时把错误以图形界面弹窗的形式呈现，用户可以反馈错误或
	 * 结束程序，不要滥用，绝对不要把关键错误抛出。
	 * <p>示例：exception(new NullPointerException());
	 * 
	 * @param e exception
	 */
	public void exception(Exception e);
	
	/**
	 * JavaFX 设置组件背景，底色为默认
	 * 
	 * @param node  节点
	 * @param url   背景路径
	 * @param width 宽度
	 * @param x     X 轴偏移
	 * @param y     Y 轴偏移
	 */
	public void setBg(Node node, String url, int width, int x, int y);
	
	/**
	 * JavaFX 设置组件背景，底色为透明
	 * 
	 * @param node  节点
	 * @param url   背景路径
	 * @param width 宽度
	 * @param x     X 轴偏移
	 * @param y     Y 轴偏移
	 */
	public void setBgTp(Node node, String url, int width, int x, int y);
}
