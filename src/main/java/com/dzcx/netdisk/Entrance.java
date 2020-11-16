package com.dzcx.netdisk;

import com.dzcx.netdisk.controller.Main;
import com.dzcx.netdisk.entity.MyConfig;
import javafx.application.Application;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class Entrance {

    public static MyConfig config;

    public static void main(String[] args) {
        config = getConfig("src/main/resources/iNetdisk.ini");
        System.out.println(config);
        Application.launch(Main.class, args);

    }

    // 读取配置文件
    private static MyConfig getConfig(String path) {
        Wini ini = null;
        try {
            ini = new Wini(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String defaultUploadFolder = ini.get("dev","defaultUploadFolder");
        Boolean openIOList = Boolean.valueOf(ini.get("dev","openIOList"));
        Boolean sound = Boolean.valueOf(ini.get("dev","sound"));
        Boolean exitOnClose = Boolean.valueOf(ini.get("dev","exitOnClose"));
        Double width = Double.valueOf(ini.get("dev", "width"));
        Double height = Double.valueOf(ini.get("dev", "height"));
        Integer fontSize = Integer.valueOf(ini.get("dev","fontSize"));
        String fontFamily = ini.get("dev","fontFamily");
        Double volume = Double.valueOf(ini.get("dev", "volume"));
        String ip = ini.get("dev","ip");
        Integer portPublic = Integer.valueOf(ini.get("dev","portPublic"));
        Integer portState = Integer.valueOf(ini.get("dev","portState"));
        Integer portUpload = Integer.valueOf(ini.get("dev","portUpload"));
        Integer portDownload = Integer.valueOf(ini.get("dev","portDownload"));
        Integer portHTTP = Integer.valueOf(ini.get("dev","portHTTP"));


        MyConfig config = new MyConfig(defaultUploadFolder, openIOList, sound, exitOnClose, width, height, fontSize,
                fontFamily, volume, ip, portPublic, portState, portUpload, portDownload, portHTTP);

        return config;
    }

}
