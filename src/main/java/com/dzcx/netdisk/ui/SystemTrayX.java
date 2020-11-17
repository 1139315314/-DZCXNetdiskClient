package com.dzcx.netdisk.ui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import com.dzcx.netdisk.controller.Main;
import com.dzcx.netdisk.util.implement.GUIImp;
import javafx.application.Platform;
//import net.imyeyu.netdisk.core.Download;
//import net.imyeyu.netdisk.core.Upload;
//import net.imyeyu.netdisk.dialog.Confirm;
//import net.imyeyu.util.YeyuUtils;

public class SystemTrayX implements ActionListener {

	
	private Main main;
	private static TrayIcon icon;
	private MenuItem name, show, exit;
	private SystemTray tray = SystemTray.getSystemTray();

	public SystemTrayX(Main main) {
		this.main = main;
		
		PopupMenu menu = new PopupMenu();
		name = new MenuItem("DZCX Netdisk");
		show = new MenuItem("打开主界面");
		show.addActionListener(this);
		exit = new MenuItem("退出");
		exit.addActionListener(this);
		menu.add(name);
		menu.addSeparator();
		menu.add(show);
		menu.add(exit);

		ImageIcon img = new ImageIcon(getClass().getResource("photo/icon1.png"));
		icon = new TrayIcon(img.getImage(), "DZCX Netdisk", menu);
		dbClickEvent();
		try {
			tray.add(icon);
		} catch (AWTException e) {
			new GUIImp().exception(e);
		}
	}
	
	public void removeIcon() {
		tray.remove(icon);
	}
	
	private void dbClickEvent() {
		icon.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Platform.runLater(() -> {
						main.getView().show();
					});
				}
			}
		});
	}

	public void actionPerformed(ActionEvent eelement) {
		MenuItem menu = (MenuItem) eelement.getSource();
		if (menu == show) {
			Platform.runLater(() -> {
				main.getView().show();
			});
		}
		if (menu == exit) {
			Platform.runLater(() -> {
//				if (Download.getListProperty().size() != 0 || Upload.getListProperty().size() != 0) {
//					main.getView().show();
//					Confirm confirm = new Confirm(rbx.def("warn"), rbx.def("exitWhenIOListRunning"), rbx.def("confirm"), "ewarn");
//					confirm.initConfirm(confirm, new EventHandler<javafx.event.ActionEvent>() {
//						public void handle(javafx.event.ActionEvent event) {
//							Platform.exit();
//						}
//					});
//				} else {
//					Platform.exit();
//				}
			});
		}
	}
	
	public static TrayIcon getIcon() {
		return icon;
	}
}