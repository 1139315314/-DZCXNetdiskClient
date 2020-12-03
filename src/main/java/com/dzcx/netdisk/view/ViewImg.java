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

public class ViewImg extends Stage {

	
	private Label onload;
	private ImageView img;
	private AnchorPane main;
	private BorderPane imgBox;
	private ProgressBar pb;

	public ViewImg(String path) {
		main = new AnchorPane();
		
		pb = new ProgressBar();
		AnchorPaneX.def(pb, -4, -6, -4, -6);
		
		imgBox = new BorderPane();
		AnchorPaneX.def(imgBox);
		
		onload = new Label();
		onload.setAlignment(Pos.CENTER);
		imgBox.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).def());
		imgBox.setPadding(new Insets(5));
		imgBox.setCenter(onload);
		
		main.getChildren().addAll(pb, imgBox);
		main.setBackground(Background.EMPTY);
		
		Scene scene = new Scene(main);
		scene.setFill(null);
		scene.getStylesheets().add(this.getClass().getResource("css/ioList.css").toExternalForm());
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