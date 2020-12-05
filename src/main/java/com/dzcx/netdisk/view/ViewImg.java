package com.dzcx.netdisk.view;

import com.dzcx.netdisk.util.uitools.AnchorPaneX;
import com.dzcx.netdisk.util.uitools.BorderX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *  当双击网盘的图片的实收，系统就会立刻显示图片视图，然后显示加载进度条页面
 *  当进度条读取满的时候，就会显示对应的图片
 */
public class ViewImg extends Stage {

	// 主视图
	private AnchorPane main;
	// 图片的箱子，用来存放加载进度文本或者图片的（相互替换）
	private BorderPane imgBox;
	// 图片对象，用来展示图片的
	private ImageView img;
	// 加载进度的文本
	private Label onload;
	// 加载进度条
	private ProgressBar pb;

	public ViewImg(String path) {
		main = new AnchorPane();

		// 进度条
		pb = new ProgressBar();
		AnchorPaneX.def(pb, -4, -6, -4, -6);

		// 图片的容器
		imgBox = new BorderPane();
		// 让图片的容器紧贴着主视图。（主视图有多大，图片就有多大）
		AnchorPaneX.def(imgBox);

		// 加载中的文本，文本设置居中
		onload = new Label();
		onload.setAlignment(Pos.CENTER);

		// 对图片的容器设置一下边框，并且设置一下内边距，让图片在中间，图片的容器放入加载中
		imgBox.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).def());
		imgBox.setPadding(new Insets(5));
		imgBox.setCenter(onload);
		// 把进度条和图片的容器放在大的容器里面
		main.getChildren().addAll(pb, imgBox);
		main.setBackground(Background.EMPTY);
		// 读取相应的css以及图片，最后用stage来展示
		Scene scene = new Scene(main);
		scene.getStylesheets().add(this.getClass().getResource("/css/ioList.css").toExternalForm());
		getIcons().add(new Image("photo/photo.png"));
		setScene(scene);
		setTitle("预览图片" + " - " + path);
		setWidth(220);
		setHeight(48);
		initStyle(StageStyle.TRANSPARENT);
		setResizable(false);
		show();
	}
	
	public void setImg(ImageView img) {
		this.img = img;
	}
	
	public Label getOnload() {
		return onload;
	}

	public ImageView getImg() {
		return img;
	}
	
	public ProgressBar getPB() {
		return pb;
	}

	public AnchorPane getMain() {
		return main;
	}
	
	public BorderPane getImgBox() {
		return imgBox;
	}
}