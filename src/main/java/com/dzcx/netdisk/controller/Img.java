package com.dzcx.netdisk.controller;

import com.dzcx.netdisk.request.ImgRequest;
import com.dzcx.netdisk.util.uitools.BorderX;
import com.dzcx.netdisk.view.ViewImg;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class Img extends ViewImg {

	private double ox = 0, oy = 0, cx = 0, cy = 0, scale = 1;

	public Img(String path) {
		// 先显示进度条
		super(path);

		// 设置一下图片的边缘阴影
		DropShadow dropshadow = new DropShadow();
		dropshadow.setRadius(5);
		dropshadow.setOffsetX(0);
		dropshadow.setOffsetY(0);
		dropshadow.setSpread(.05);
		dropshadow.setColor(Color.valueOf("#000000DD"));
		
		// 点击图像，收集坐标信息，判断下一步的动作是什么
		getMain().setOnMousePressed(event -> {
			getMain().setCursor(Cursor.CLOSED_HAND);
			ox = event.getX();
			oy = event.getY();
			cx = event.getScreenX();
			cy = event.getScreenY();
		});
		// 拖动图像
		getMain().setOnMouseDragged(event -> {
			setX(event.getScreenX() - ox);
			setY(event.getScreenY() - oy);
		});
		// 释放图像
		getMain().setOnMouseReleased(event -> {
			getMain().setCursor(Cursor.DEFAULT);
			// 当用户释放鼠标的时候，图片坐标没有改变过，则视为用户想要关闭图片
			if (cx == event.getScreenX() && cy == event.getScreenY())
				close();
		});
		// 滚轮事件
		getMain().addEventFilter(ScrollEvent.SCROLL, event -> {
			if (!(getWidth() + event.getDeltaY() < 16)) {
				if (getImg().getFitHeight() < getImg().getFitWidth()) {
					setX(getX() + -event.getDeltaY() / 2);
					setY(getY() + -(event.getDeltaY() / 2 * scale));
					setWidth(getWidth() + event.getDeltaY());
					setHeight(getHeight() + event.getDeltaY() * scale);
					getImg().setFitWidth(getWidth() - 10);
					getImg().setFitHeight(getHeight() - 10);
				} else {
					setX(getX() + -(event.getDeltaY() / 2 * scale));
					setY(getY() + -event.getDeltaY() / 2);
					setWidth(getWidth() + event.getDeltaY() * scale);
					setHeight(getHeight() + event.getDeltaY());
					getImg().setFitWidth(getWidth() - 10);
					getImg().setFitHeight(getHeight() - 10);
				}
			}
		});
		// 封装获取图片的请求
		ImgRequest request = new ImgRequest("getImg", path, true);
		// 把进度条的属性和请求的进度绑定在一起
		getPB().progressProperty().bind(request.progressProperty());
		// 把图片容器中的文本消息和请求的消息绑定在一起
		getOnload().textProperty().bind(request.messageProperty());
		// 监听请求的value属性，当收到value发生改变的情况下，监听器会发生响应
		request.valueProperty().addListener((obs, oldImg, newImg) -> {
			// 接受到的图片不为空
			if (newImg != null) {
				// 设置图片到视图变量当中
				ImageView img = new ImageView(newImg);
				setImg(img);
				// 获取图片的大小
				double imgW = Double.valueOf(newImg.getWidth());
				double imgH = Double.valueOf(newImg.getHeight());
				// stage的整体大小比图片的大小要大一点点
				setWidth(imgW + 10);
				setHeight(imgH + 10);
				Rectangle2D screen = Screen.getPrimary().getVisualBounds();
				setX(screen.getMaxX() / 2 - getWidth() / 2);
				setY(screen.getMaxY() / 2 - getHeight() / 2);
				// 当图片接受完成之后，把进度条给移除掉
				getMain().getChildren().remove(getPB());
				// 同时图片的容器把提示文本给去掉，更换给图片进行演示
				getImgBox().setBackground(Background.EMPTY);
				getImgBox().setBorder(BorderX.EMPTY);
				getImgBox().setCenter(img);
				getImgBox().setEffect(dropshadow);
			}
		});
		// 向服务器发送请求
		request.start();
	}

}