package com.dzcx.netdisk.ui;

import com.dzcx.netdisk.entity.FileCell;
import com.dzcx.netdisk.util.FileFormat;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class FileListTable extends TableView<FileCell> {
	
	private TableColumn<FileCell, String> colName;
	
	public FileListTable() {
		colName = new TableColumn<FileCell, String>("文件名称");
		TableColumn<FileCell, String> colDate = new TableColumn<FileCell, String>("文件日期");
		TableColumn<FileCell, String> colSize = new TableColumn<FileCell, String>("文件大小");
		colName.setPrefWidth(450);
		colDate.setPrefWidth(140);
		colSize.setPrefWidth(100);

		colName.setCellValueFactory(new PropertyValueFactory<FileCell, String>("name"));
		colName.setCellFactory(new Callback<TableColumn<FileCell, String>, TableCell<FileCell, String>>() {
			public TableCell<FileCell, String> call(TableColumn<FileCell, String> param) {
				TableCell<FileCell, String> cell = new TableCell<FileCell, String>(){
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty && item != null) {
							HBox box = new HBox();
							ImageView img;
							if (item.substring(0, item.indexOf(".")).equals("folder")) {
								img = new ImageView("photo/folder.png");
							} else {
								img = new ImageView(FileFormat.getImage(item.substring(item.lastIndexOf(".") + 1)));
							}
							Label label = new Label(item.substring(item.indexOf(".") + 1)); 
							label.setPadding(new Insets(0, 0, 0, 4));
							box.getChildren().addAll(img, label);
							this.setGraphic(box);
						}
					}
				};
				return cell;
			}
		});
		colDate.setCellValueFactory(new PropertyValueFactory<FileCell, String>("date"));
		colSize.setCellValueFactory(new PropertyValueFactory<FileCell, String>("size"));
		getSelectionModel().setCellSelectionEnabled(false);
		
		colDate.setStyle("-fx-alignment: center");
		colSize.setStyle("-fx-alignment: center-right;-fx-font-family: 'Consolas'");
		
		getColumns().add(colName);
		getColumns().add(colDate);
		getColumns().add(colSize);
		
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public TableColumn<FileCell, String> getColName() {
		return colName;
	}
}