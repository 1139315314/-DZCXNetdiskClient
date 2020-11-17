package com.dzcx.netdisk.ui;

import java.text.DecimalFormat;

import com.dzcx.netdisk.entity.PhotoInfo;
import com.dzcx.netdisk.util.implement.NetworkImp;
import com.dzcx.netdisk.util.iUtil;
import com.dzcx.netdisk.util.uitools.ToolTipsX;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 *   专门自己存放图片的布局
 */
public class PhotoInfoTable extends GridPane {
	
	private String[] labels = {
		"文件名",
		"文件位置",
		"文件大小",
		"宽度",
		"高度",
		"日期：",
		"相机厂商",
		"相机型号",
		"系统",
		"光圈",
		"曝光时间",
		"ISO 速度",
		"焦距",
		"经度",
		"维度",
		"海拔",
		"地理位置"
	};
	private Label[] label = new Label[labels.length];
	private Label[] data = new Label[labels.length];
	
	public PhotoInfoTable() {
		// 标签
		Color colorLabel = Color.GRAY;
		Insets dataPadding = new Insets(0, 0, 0, 6);
		ColumnConstraints colLabel = new ColumnConstraints(60);
		RowConstraints rowLabel = new RowConstraints(19);
		colLabel.setHalignment(HPos.RIGHT);
		getColumnConstraints().add(colLabel);
		for (int i = 0; i < label.length; i++) {
			label[i] = new Label();
			label[i].setTextFill(colorLabel);
			addColumn(0, label[i]);
			
			data[i] = new Label();
			data[i].setPadding(dataPadding);
			addColumn(1, data[i]);
			
			getRowConstraints().add(rowLabel);
		}
	}

	public void set(PhotoInfo info) {
		// 加载标签值
		for (int i = 0; i < label.length; i++) {
			label[i].setText(labels[i]);
		}
		int i = 0;
		data[i++].setText(info.getName());
		data[i].setTooltip(new ToolTipsX(info.getPos()));
		data[i++].setText(info.getPos());
		data[i++].setText(iUtil.storageFormat(Long.valueOf(info.getSize()), new DecimalFormat("#,###.##")));
		data[i++].setText(info.getWidth());
		data[i++].setText(info.getHeight());
		data[i++].setText(info.getDate());
		data[i++].setText(info.getMake());
		data[i++].setText(info.getCamera());
		data[i].setTooltip(new ToolTipsX(info.getOs()));
		data[i++].setText(info.getOs());
		data[i++].setText(info.getAperture());
		data[i++].setText(info.getToe() + "秒");
		data[i++].setText(info.getIso());
		data[i++].setText(info.getFocalLength() + "毫米");
		data[i++].setText(info.getLng());
		data[i++].setText(info.getLat());
		data[i++].setText(info.getAlt().replaceAll("metres", "米"));
		data[i++].setText("");
		
		// 根据经纬度获取地理位置
		if (!info.getLat().equals("") && !info.getLng().equals("")) {
			LocationService service = new LocationService(Double.valueOf(info.getLat()), Double.valueOf(info.getLng()));
			service.valueProperty().addListener((obs, oldValue, newValue) -> {
				if (!newValue.equals("")) {
					newValue = newValue.substring(newValue.indexOf("(") + 1, newValue.length() - 1);
					JsonParser jp = new JsonParser();
					JsonObject jo = (JsonObject) jp.parse(newValue);
					String result = jo.get("result").getAsJsonObject().get("formatted_address").getAsString();
					data[data.length - 1].setText(result);
					data[data.length - 1].setTooltip(new ToolTipsX(result));
				}
			});
			service.start();
		}
	}
	
	public void clear() {
		for (int i = 0; i < data.length; i++) {
			label[i].setText("");
			data[i].setText("");
		}
	}
}


class LocationService extends Service<String> {
	
	private double lat = -1, lng = -1;
	
	public LocationService(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	protected Task<String> createTask() {
		
		return new Task<String>() {
			protected String call() throws Exception {
				String par = "ak=wWYw0yCb8ntXmSgTxTx40vKR&callback=renderReverse&location=" + lat + "," + lng + "&output=json&pois=1";
				return new NetworkImp().doGet("http://api.map.baidu.com/geocoder/v2/", par);
			}
		};
	}
}