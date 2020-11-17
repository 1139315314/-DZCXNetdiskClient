package com.dzcx.netdisk.util;

import com.dzcx.netdisk.util.implement.EncodeImp;
import javafx.scene.Node;

import java.text.DecimalFormat;
import java.util.Map;

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

    public static String storageFormat(double byteValue, DecimalFormat format) {
        if (byteValue == -1) return "";
        double value = byteValue;
        if (value < 1024) return format.format(value) + " B ";
        value = value / 1024;
        if (value < 10240) return format.format(value) + " KB";
        value = value / 1024;
        if (value < 10240) return format.format(value) + " MB";
        value = value / 1024;
        if (value < 1024) return format.format(value) + " GB";
        return format.format(value / 1024) + " TB";
    }

    public String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) return url;

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) continue;
            if (i != 0) {
                builder.append('&');
            }
            builder.append(key);
            builder.append('=');
            builder.append(new EncodeImp().enURL(value));
            i++;
        }
        return builder.toString();
    }
}
