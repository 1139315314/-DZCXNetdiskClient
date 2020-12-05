package com.dzcx.netdisk.view;

import com.dzcx.netdisk.entity.IOCell;
import com.dzcx.netdisk.ui.IOFinishList;
import com.dzcx.netdisk.ui.IOList;
import com.dzcx.netdisk.ui.NavButton;
import com.dzcx.netdisk.util.uitools.BorderX;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;



public class ViewIO extends Stage {

	
	private IOList uploadList, downloadList;
	private IOFinishList finishList;
	private NavButton upload, download, finish, clear;
	private BorderPane main, left;
	
	public ViewIO() {
		
		// 导航
		left = new BorderPane();
		VBox nav = new VBox();
		upload = new NavButton("上传", 100, 32);
		download = new NavButton("下载", 100, 32);
		finish = new NavButton("已完成列表", 100, 32);

		nav.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).bottom());
		nav.getChildren().addAll(download, upload, finish);
		
		clear = new NavButton("清除所有", 100, 26);
		clear.setPadding(new Insets(0, 0, 4, 0));
		clear.setBorder(Border.EMPTY);
		
		left.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).right());
		left.setCenter(nav);
		left.setBottom(clear);
		
		// 上传列表
		ObservableList<IOCell> obsUploadList = FXCollections.observableArrayList(new Callback<IOCell, Observable[]>() {
			public Observable[] call(IOCell param) {
				return new SimpleDoubleProperty[] {param.getSizeProperty(), param.getPercentProperty()};
			}
		});
		uploadList = new IOList(obsUploadList);
		
		// 下载列表
		ObservableList<IOCell> obsDownloadList = FXCollections.observableArrayList(new Callback<IOCell, Observable[]>() {
			public Observable[] call(IOCell param) {
				return new SimpleDoubleProperty[] {param.getSizeProperty(), param.getPercentProperty()};
			}
		});
		downloadList = new IOList(obsDownloadList);
		
		// 已完成列表
		finishList = new IOFinishList();


		main = new BorderPane();
		main.setId("main");
		main.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).top());
		main.setLeft(left);
		
		Scene scene = new Scene(main);
		scene.getStylesheets().add(this.getClass().getResource("/css/ioList.css").toExternalForm());
		setTitle("传输列表");
		getIcons().add(new Image("photo/ioListTitle.png"));
		setMinWidth(460);
		setMinHeight(270);
		setWidth(460);
		setHeight(270);
		initModality(Modality.APPLICATION_MODAL);
		setScene(scene);
		show();
		
		upload();
	}

	public void upload() {
		left.setBottom(null);
		main.setCenter(uploadList);
	}
	
	public void download() {
		left.setBottom(null);
		main.setCenter(downloadList);
	}
	
	public void finish() {
		left.setBottom(clear);
		main.setCenter(finishList);
	}

	public IOList getUploadList() {
		return uploadList;
	}

	public IOList getDownloadList() {
		return downloadList;
	}

	public IOFinishList getFinishList() {
		return finishList;
	}

	public NavButton getUpload() {
		return upload;
	}

	public NavButton getDownload() {
		return download;
	}

	public NavButton getFinish() {
		return finish;
	}

	public NavButton getClear() {
		return clear;
	}

	public BorderPane getMain() {
		return main;
	}
}