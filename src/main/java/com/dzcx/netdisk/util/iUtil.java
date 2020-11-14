package com.dzcx.netdisk.util;

import javafx.scene.Node;

public class iUtil {

    // 设置节点的一些样式
    public static void setBg(Node node, String url, int width, int x, int y) {
        node.setStyle(
                "-fx-background-size: " + width + ";" +
                        "-fx-background-image: url('" + url + "');" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: " + x + " " + y
        );
    }
}
