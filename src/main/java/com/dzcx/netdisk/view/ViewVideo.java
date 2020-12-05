package com.dzcx.netdisk.view;

import com.dzcx.netdisk.util.implement.GUIImp;
import com.dzcx.netdisk.util.uitools.AnchorPaneX;
import com.dzcx.netdisk.util.uitools.BorderX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewVideo extends Stage {

	// 视频标题，当前已播放时间，视频总时长，提示
	private Label videoTitle, timeNow, timeMax, tips;
	// 两个进度条，一个是视频播放进度，一个是音量控制
	private Slider pb, volume;
	// 按钮，关闭，上一个视频，切换播放状态，下一个视频，全屏模式
	private Button close, prev, toggle, next, full;
	// 真正播放区域
	private MediaView mediaView;
	// 控制面板
	private BorderPane ctrl;
	// 主面板以及头部面板
	private AnchorPane mainBox, header;

	public ViewVideo() {
		mainBox = new AnchorPane();
		
		AnchorPane main = new AnchorPane();
		// 播放区域，并且让播放区域占满整个屏幕。
        mediaView = new MediaView();
        AnchorPaneX.def(mediaView);
        // 控制面板
        ctrl = new BorderPane();
        // 文本
        videoTitle = new Label();
        videoTitle.setTextFill(Paint.valueOf("#F4F4F4"));
        videoTitle.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 18));
        videoTitle.setTextAlignment(TextAlignment.CENTER);
        videoTitle.setAlignment(Pos.CENTER);
        // 设置一下文本两边的边距,相当于让文本区域撑大
        AnchorPaneX.def(videoTitle, 0, 25);

        // 头部区域，存放视频标题以及关闭按钮
        header = new AnchorPane();
        // 关闭按钮，设为按钮的长宽
        close = new Button();
        close.setPrefSize(22, 22);
        close.setFocusTraversable(false);
        // 除了左边，其余部分贴近父容器
        AnchorPaneX.exLeft(close);
        // 设置按钮背景图片
		new GUIImp().setBgTp(close, "photo/close.png", 22, 0, 2);
		// 设置头部容器的宽度，这个战士无所谓
        header.setPrefWidth(860);
		header.setStyle("-fx-background-color: #AAAA");
		// 设置一下内边距，让里面的组件不要去到边缘
		header.setPadding(new Insets(2, 6, 2, 6));
		// 设置一下边框
		header.setBorder(new BorderX("#CCCC", BorderX.SOLID, 1).bottom());
		header.getChildren().addAll(videoTitle, close);

		// 相当于用来占位的，有没有应该也没问题
		Label nu11 = new Label("11111111111");

		// 底部的控制面板
		AnchorPane bottom = new AnchorPane();
		// 视频播放的进度条, 把进度条超出容器外
		pb = new Slider();
		AnchorPaneX.def(pb, -6, -4, null, -4);
		// 时间容器，存放已经播放的时间以及视频的完整的时间
		BorderPane timeBox = new BorderPane();
		timeNow = new Label("00:00");
		timeNow.setTextFill(Color.WHITE);
		timeNow.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
		timeMax = new Label("00:00");
		timeMax.setTextFill(Color.WHITE);
		timeMax.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
		timeBox.setLeft(timeNow);
		timeBox.setRight(timeMax);
		timeBox.setPadding(new Insets(4));
		// 设置距离上边6，两边8,把整个timeBox容器给撑大
		AnchorPaneX.def(timeBox, 6, 8, null, 8);
		// 提示文字
		tips = new Label();
		tips.setTextFill(Color.WHITE);
		// 控制面板，上一个，切换播放状态，下一个
		HBox playCtrl = new HBox();
		prev = new Button();
		prev.setPrefSize(22, 22);
		prev.setFocusTraversable(false);
		new GUIImp().setBgTp(prev, "photo/playPrev.png", 22, 0, 2);
		toggle = new Button();
		toggle.setPrefSize(32, 32);
		toggle.setFocusTraversable(false);
		new GUIImp().setBgTp(toggle, "photo/play.png", 32, 0, 0);
		next = new Button();
		next.setPrefSize(22, 22);
		next.setFocusTraversable(false);
		new GUIImp().setBgTp(next, "photo/playNext.png", 22, 0, 2);
		// 设置组件之间的间隔，组件都居中显示
		playCtrl.setSpacing(16);
		playCtrl.setAlignment(Pos.CENTER);
		playCtrl.getChildren().addAll(prev, toggle, next);
		AnchorPaneX.def(playCtrl, null, 0, 6, 0);

		// 音量调节和全屏的控制面板
		HBox otherCtrl = new HBox();
		ImageView volumeIcon = new ImageView(new Image("photo/volume.png"));
		volume = new Slider(0, 1, .5);
		volume.setPrefWidth(64);
		volume.setFocusTraversable(false);
		full = new Button();
		full.setPrefSize(32, 18);
		full.setFocusTraversable(false);
		HBox.setMargin(volume, new Insets(0, 16, 0, 4));
		new GUIImp().setBgTp(full, "photo/full.png", 32, 0, -2);
		otherCtrl.setAlignment(Pos.CENTER_RIGHT);
		otherCtrl.setPadding(new Insets(0, 16, 0, 0));
		otherCtrl.getChildren().addAll(volumeIcon, volume, full);
		// 贴着容器的右边,然后再距离上方一点点位置
		AnchorPaneX.def(otherCtrl, null, 0, 8, null);

		bottom.setStyle("-fx-background-color: #AAAA");
		bottom.setBorder(new BorderX("#CCCC", BorderX.SOLID, 1).top());
		bottom.setPrefHeight(64);
		bottom.getChildren().addAll(timeBox, tips, playCtrl, otherCtrl, pb);

		// 让控制面板占满整个面板
		ctrl.setTop(header);
		ctrl.setBottom(bottom);
		AnchorPaneX.def(ctrl);
        
		AnchorPaneX.def(main);
		DropShadow dropshadow = new DropShadow();
		dropshadow.setRadius(6);
		dropshadow.setOffsetX(0);
		dropshadow.setOffsetY(0);
		dropshadow.setSpread(.05);
		dropshadow.setColor(Color.valueOf("#000E"));
		main.setStyle("-fx-background-color: #000");
		main.setEffect(dropshadow);
		main.getChildren().addAll(mediaView, ctrl);
		
		mainBox.setPadding(new Insets(10));
		mainBox.setBackground(Background.EMPTY);
		mainBox.getChildren().add(main);
		
		Scene scene = new Scene(mainBox);
		scene.setFill(null);
		getIcons().add(new Image("photo/video.png"));
		setScene(scene);
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.APPLICATION_MODAL);
	}
	
	public void setVideoTitle(String value) {
		this.videoTitle.setText(value);
	}

	public AnchorPane getHeader() {
		return header;
	}

	public Slider getPb() {
		return pb;
	}
	
	public Label getTimeNow() {
		return timeNow;
	}

	public Label getTimeMax() {
		return timeMax;
	}
	
	public Label getTips() {
		return tips;
	}

	public Slider getVolume() {
		return volume;
	}

	public Button getClose() {
		return close;
	}

	public Button getPrev() {
		return prev;
	}

	public Button getToggle() {
		return toggle;
	}

	public Button getNext() {
		return next;
	}

	public Button getFull() {
		return full;
	}

	public MediaView getMediaView() {
		return mediaView;
	}

	public BorderPane getCtrl() {
		return ctrl;
	}
	
	public AnchorPane getMainBox() {
		return mainBox;
	}
}