package com.dzcx.netdisk.entity;

public class MyConfig {

    private String dlLocation;

    private String cache;

    private String defaultUploadFolder;

    private Boolean openIOList;

    private Boolean sound;

    private Boolean exitOnClose;

    private Double width;

    private Double height;

    private int fontSize;

    private String fontFamily;

    private Double volume;

    private String ip;

    private int portPublic;

    private int portState;

    private int portUpload;

    private int portDownload;

    private int portHTTP;

    public MyConfig() {
    }

    public MyConfig(String defaultUploadFolder, Boolean openIOList, Boolean sound, Boolean exitOnClose, Double width, Double height, int fontSize, String fontFamily, Double volume, String ip, int portPublic, int portState, int portUpload, int portDownload, int portHTTP) {
        this.defaultUploadFolder = defaultUploadFolder;
        this.openIOList = openIOList;
        this.sound = sound;
        this.exitOnClose = exitOnClose;
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.volume = volume;
        this.ip = ip;
        this.portPublic = portPublic;
        this.portState = portState;
        this.portUpload = portUpload;
        this.portDownload = portDownload;
        this.portHTTP = portHTTP;
    }

    @Override
    public String toString() {
        return "MyConfig{" +
                "defaultUploadFolder='" + defaultUploadFolder + '\'' +
                ", openIOList=" + openIOList +
                ", sound=" + sound +
                ", exitOnClose=" + exitOnClose +
                ", width=" + width +
                ", height=" + height +
                ", fontSize=" + fontSize +
                ", fontFamily='" + fontFamily + '\'' +
                ", volume=" + volume +
                ", ip='" + ip + '\'' +
                ", portPublic=" + portPublic +
                ", portState=" + portState +
                ", portUpload=" + portUpload +
                ", portDownload=" + portDownload +
                ", portHTTP=" + portHTTP +
                '}';
    }


    public String getDlLocation() {
        return dlLocation;
    }

    public void setDlLocation(String dlLocation) {
        this.dlLocation = dlLocation;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getDefaultUploadFolder() {
        return defaultUploadFolder;
    }

    public void setDefaultUploadFolder(String defaultUploadFolder) {
        this.defaultUploadFolder = defaultUploadFolder;
    }

    public Boolean getOpenIOList() {
        return openIOList;
    }

    public void setOpenIOList(Boolean openIOList) {
        this.openIOList = openIOList;
    }

    public Boolean getSound() {
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public Boolean getExitOnClose() {
        return exitOnClose;
    }

    public void setExitOnClose(Boolean exitOnClose) {
        this.exitOnClose = exitOnClose;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPortPublic() {
        return portPublic;
    }

    public void setPortPublic(int portPublic) {
        this.portPublic = portPublic;
    }

    public int getPortState() {
        return portState;
    }

    public void setPortState(int portState) {
        this.portState = portState;
    }

    public int getPortUpload() {
        return portUpload;
    }

    public void setPortUpload(int portUpload) {
        this.portUpload = portUpload;
    }

    public int getPortDownload() {
        return portDownload;
    }

    public void setPortDownload(int portDownload) {
        this.portDownload = portDownload;
    }

    public int getPortHTTP() {
        return portHTTP;
    }

    public void setPortHTTP(int portHTTP) {
        this.portHTTP = portHTTP;
    }
}
