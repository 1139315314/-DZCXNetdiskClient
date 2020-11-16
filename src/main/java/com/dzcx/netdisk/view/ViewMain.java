package com.dzcx.netdisk.view;

import java.io.File;

import com.dzcx.netdisk.ui.*;
import com.dzcx.netdisk.util.uitools.BorderX;
import com.dzcx.netdisk.util.uitools.SeparatorX;
import com.dzcx.netdisk.util.iUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ViewMain extends Stage {

	
	private VBox navBtns;
	private Button open, propertie, sync, upload, download, zip, unzip, newFolder, rename, move, copy, delete, ioList, setting, photo, publicFile, prev, next, parent, refresh, root, toPublic, search;
	private TipsPane tipsPane;
	private MenuItem mOpen, mRefresh, mNewFolder, mNewText, mDownload, mZip, mUnzip, mMove, mCopy, mRename, mDelete, mProperties;
	private TextField path, searchField;
	private ButtonBar fileCtrlBtns;
	private ContextMenu menu;
	private FileListTable fileList;
	private ServerStateText disk;
	private ServerStateChart cpu, memory, netSpeed;
	
	public ViewMain() {
		BorderPane topPane = new BorderPane();
		// 文件控制面板
		FlowPane ctrlLeft = new FlowPane();
		// 文件读取
		ButtonBar fileLoadBtns = new ButtonBar();
		open = new Button("查看");
		propertie = new Button("详情");
		fileLoadBtns.addAll(open, propertie);
		ctrlLeft.setPrefWidth(128);
		ctrlLeft.setAlignment(Pos.CENTER);
		ctrlLeft.getChildren().add(fileLoadBtns);
		
		// 文件操作
		FlowPane ctrlCenter = new FlowPane();
		fileCtrlBtns = new ButtonBar();
		sync = new Button("同步");
		iUtil.setBg(sync, "photo/sync.png", 16, 5, 5);
		upload = new Button("上传");
		iUtil.setBg(upload, "photo/upload.png", 16, 5, 5);
		download = new Button("下载");
		iUtil.setBg(download, "photo/download.png", 16, 5, 5);
		zip = new Button("压缩");
		unzip = new Button("解压");
		newFolder = new Button("新建文件夹");
		rename = new Button("重命名");
		move = new Button("移动");
		copy = new Button("复制");
		delete = new Button("删除");
		fileCtrlBtns.addAll(sync, upload, download, zip, unzip, newFolder, rename, move, copy, delete);
		ctrlCenter.getChildren().addAll(new SeparatorX(true, 10, 48), fileCtrlBtns);
		
		// 设置
		FlowPane ctrlRight = new FlowPane();
		ButtonBar settingBtn = new ButtonBar();
		ioList = new Button("传输列表");
		iUtil.setBg(ioList, "photo/ioList.png", 16, 5, 5);
		setting = new Button("设置");
		settingBtn.addAll(ioList, setting);
		ctrlRight.setPrefWidth(120);
		ctrlRight.setAlignment(Pos.CENTER);
		ctrlRight.getChildren().add(settingBtn);

		topPane.setPadding(new Insets(0, 16, 0, 16));
		topPane.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).vertical());
		topPane.setLeft(ctrlLeft);
		topPane.setCenter(ctrlCenter);
		topPane.setRight(ctrlRight);
		
		BorderPane leftPane = new BorderPane();
		// 左侧导航
		navBtns = new VBox();
		photo = new NavButton("图片管理器");
		publicFile = new NavButton("公开外链");
		navBtns.getChildren().addAll(photo, publicFile);
		// 服务器状态面板
		VBox states = new VBox();
		Label server = new Label("服务器的状态", new ImageView("photo/state.png"));
		server.setPrefWidth(155);
		server.setPadding(new Insets(0, 0, 4, 6));
		cpu = new ServerStateChart("处理器", 1.1, 90, 32);
		memory = new ServerStateChart("内存", 1.1, 90, 32);
		disk = new ServerStateText("磁盘", 96, 32);
		netSpeed = new ServerStateChart("网络", 90, 32);
		states.setPadding(new Insets(4, 0, 0, 0));
		states.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).top());
		states.getChildren().addAll(server, cpu, memory, disk, netSpeed);
		
		leftPane.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).right());
		leftPane.setCenter(navBtns);
		leftPane.setBottom(states);
		
		// 文件列表面板
		BorderPane filePane = new BorderPane();
		
		// 路径操作面板
		BorderPane pathCtrlPane = new BorderPane();
		ButtonBar pathCtrlBtns = new ButtonBar();
		prev = new Button();
		prev.setPrefWidth(26);
		iUtil.setBg(prev, "photo/prev.png", 16, 5, 5);
		next = new Button();
		next.setPrefWidth(26);
		iUtil.setBg(next, "photo/next.png", 16, 5, 5);
		parent = new Button();
		parent.setPrefWidth(26);
		iUtil.setBg(parent, "photo/parent.png", 16, 5, 5);
		refresh = new Button();
		refresh.setPrefWidth(26);
		iUtil.setBg(refresh, "photo/refresh.png", 16, 5, 5);
		root = new Button();
		root.setPrefWidth(26);
		iUtil.setBg(root, "photo/home.png", 16, 5, 5);
		toPublic = new Button();
		toPublic.setPrefWidth(26);
		iUtil.setBg(toPublic, "photo/share.png", 16, 5, 5);
		pathCtrlBtns.setBorder(false);
		pathCtrlBtns.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).right());
		pathCtrlBtns.addAll(prev, next, parent, refresh, root, toPublic);
		pathCtrlPane.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).bottom());
		// 路径
		path = new TextField(File.separator);
		path.setStyle("-fx-background-insets: 0");
		// 搜索
		HBox searchPane = new HBox();
		searchField = new TextField();
		searchField.setStyle("-fx-background-insets: 0");
		searchField.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).horizontal());
		search = new Button();
		search.setPrefWidth(26);
		iUtil.setBg(search, "photo/search.png", 16, 5, 5);
		searchPane.getChildren().addAll(searchField, search);
		
		pathCtrlPane.setLeft(pathCtrlBtns);
		pathCtrlPane.setCenter(path);
		pathCtrlPane.setRight(searchPane);
		
		// 文件列表
		fileList = new FileListTable();
		fileList.setPadding(Insets.EMPTY);
		fileList.setStyle("-fx-background-insets: 0");
		
		// 右键菜单
		String empty = "              ";
		menu = new ContextMenu();
		mOpen = new MenuItem("打开");
		mRefresh = new MenuItem("刷新", new ImageView("photo/refresh.png"));
		mRefresh.setAccelerator(new KeyCodeCombination(KeyCode.F5));
		Menu newFile = new Menu("新建");
		mNewFolder = new MenuItem("新建文件夹", new ImageView("photo/folder.png"));
		mNewFolder.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN));
		mNewText = new MenuItem("新建文本", new ImageView("photo/txt.png"));
		newFile.getItems().addAll(mNewFolder, mNewText);
		mDownload = new MenuItem("下载", new ImageView("photo/download.png"));
		mZip = new MenuItem("压缩", new ImageView("photo/7z.png"));
		mUnzip = new MenuItem("解压");
		mMove = new MenuItem("移动");
		mMove.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
		mCopy = new MenuItem("复制");
		mCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
		mRename = new MenuItem("重命名" + empty);
		mRename.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
		mDelete = new MenuItem("删除", new ImageView("photo/delete.png"));
		mDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
		mProperties = new MenuItem("属性");
		mProperties.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));

		menu.getItems().addAll(mOpen, mRefresh, newFile, new SeparatorMenuItem(), mDownload, mZip, mUnzip, new SeparatorMenuItem(), mMove, mCopy, mRename, mDelete, new SeparatorMenuItem(), mProperties);
		fileList.setContextMenu(menu);
		
		// 提示信息栏
		tipsPane = new TipsPane();
		tipsPane.setBorder(new BorderX("#B5B5B5", BorderX.SOLID, 1).top());
		
		filePane.setTop(pathCtrlPane);
		filePane.setCenter(fileList);
		filePane.setBottom(tipsPane);
		
		// 主面板
		BorderPane main = new BorderPane();
		main.setTop(topPane);
		main.setLeft(leftPane);
		main.setCenter(filePane);
		// 主窗体
		Scene scene = new Scene(main);
		scene.getStylesheets().add(this.getClass().getResource("/css/serverState.css").toExternalForm());
		getIcons().add(new Image("photo/icon.png"));
		setTitle("DZCX Netdisk");
		setMinWidth(880);
		setMinHeight(520);
		setWidth(880);
		setHeight(520);
		setScene(scene);
		show();
		
		fileList.requestFocus();
	}

	public Button getOpen() {
		return open;
	}

	public Button getPropertie() {
		return propertie;
	}
	
	public ButtonBar getFileCtrlBtns() {
		return fileCtrlBtns;
	}
	
	public Button getSync() {
		return sync;
	}

	public Button getUpload() {
		return upload;
	}

	public Button getDownload() {
		return download;
	}
	
	public Button getZip() {
		return zip;
	}
	
	public Button getUnZip() {
		return unzip;
	}

	public Button getNewFolder() {
		return newFolder;
	}

	public Button getRename() {
		return rename;
	}

	public Button getMove() {
		return move;
	}

	public Button getCopy() {
		return copy;
	}

	public Button getDelete() {
		return delete;
	}

	public Button getIoList() {
		return ioList;
	}

	public Button getSetting() {
		return setting;
	}

	public VBox getNavBtns() {
		return navBtns;
	}

	public Button getPhoto() {
		return photo;
	}

	public Button getPublicFile() {
		return publicFile;
	}

	public Button getPrev() {
		return prev;
	}

	public Button getNext() {
		return next;
	}

	public Button getParent() {
		return parent;
	}

	public Button getRefresh() {
		return refresh;
	}
	
	public Button getRoot() {
		return root;
	}
	
	public Button getToPublic() {
		return toPublic;
	}

	public Button getSearch() {
		return search;
	}

	public FileListTable getFileList() {
		return fileList;
	}

	public TipsPane getTipsPane() {
		return tipsPane;
	}

	public TextField getPath() {
		return path;
	}

	public TextField getSearchField() {
		return searchField;
	}

	public ServerStateChart getCpu() {
		return cpu;
	}

	public ServerStateChart getMemory() {
		return memory;
	}

	public ServerStateText getDisk() {
		return disk;
	}
	
	public ServerStateChart getNetSpeed() {
		return netSpeed;
	}
	
	public ContextMenu getMenu() {
		return menu;
	}

	public MenuItem getmOpen() {
		return mOpen;
	}
	
	public MenuItem getmRefresh() {
		return mRefresh;
	}
	
	public MenuItem getmNewFolder() {
		return mNewFolder;
	}
	
	public MenuItem getmNewText() {
		return mNewText;
	}

	public MenuItem getmDownload() {
		return mDownload;
	}

	public MenuItem getmZip() {
		return mZip;
	}

	public MenuItem getmUnZip() {
		return mUnzip;
	}

	public MenuItem getmMove() {
		return mMove;
	}

	public MenuItem getmCopy() {
		return mCopy;
	}

	public MenuItem getmRename() {
		return mRename;
	}

	public MenuItem getmDelete() {
		return mDelete;
	}

	public MenuItem getmProperties() {
		return mProperties;
	}
}