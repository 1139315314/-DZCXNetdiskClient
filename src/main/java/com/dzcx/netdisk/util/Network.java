package com.dzcx.netdisk.util;

import com.dzcx.netdisk.util.entity.HTTPInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * Network interfaces
 * 
 * @author Yeyu
 *
 */
public interface Network {
	
	/**
	 * <p>使用 HashMap 发送 GET 请求
	 * <p>HashMap 会被转化为 URL 参数，合并到 URL 中
	 * 
	 * @param url    请求地址
	 * @param params 参数 HashMap 链表
	 * @param isSSL  是否为 HTTPS 请求
	 * @return       请求结果
	 * @throws ConnectException     连接失败
	 * @throws UnknownHostException 未知地址
	 * @throws Exception            请求异常
	 */
	public String doGet(String url, Map<String, String> params, boolean isSSL) throws ConnectException, UnknownHostException, Exception;

	/**
	 * <p>发送一个可以自定义请求头的 GET 请求
	 * 
	 * @param http                  请求信息
	 * @return                      请求结果
	 * @throws UnknownHostException 未知地址
	 * @throws ConnectException     连接失败
	 * @throws Exception            其他异常
	 */
	public String doGet(HTTPInfo http) throws UnknownHostException, ConnectException, Exception;
	
	/**
	 * <p>发送一个不带参数的 GET 请求
	 * <p>示例: doGet("https://www.imyeyu.net/api");
	 * 
	 * @param url                   请求地址
	 * @return                      返回结果
	 * @throws UnknownHostException 未知地址
	 * @throws ConnectException     连接失败
	 * @throws Exception            其他异常
	 */
	public String doGet(String url) throws UnknownHostException, ConnectException, Exception;
	
	/**
	 * <p>发送一个 GET 请求
	 * <p>示例: doGet("https://www.imyeyu.net/api", "key0=value&amp;key1=value1");
	 * 
	 * @param url                   请求地址
	 * @param param                 请求参数
	 * @return                      请求结果
	 * @throws UnknownHostException 未知地址
	 * @throws ConnectException     连接失败
	 * @throws Exception            其他异常
	 */
	public String doGet(String url, String param) throws UnknownHostException, ConnectException, Exception;
	
	/**
	 * <p>发送一个 POST 请求
	 * <p>示例: doPost("https://www.imyeyu.net/api", "key0=value&amp;key1=value1");
	 * 
	 * @param url                   请求地址
	 * @param param                 请求参数
	 * @return                      请求结果
	 * @throws UnknownHostException 未知地址
	 * @throws ConnectException     连接失败
	 * @throws Exception            其他异常
	 */
	public String doPost(String url, String param) throws UnknownHostException, ConnectException, Exception;
	
	/**
	 * <p>使用默认浏览器打开链接
	 * <p>示例: openURIInBrowser(new URL("https://www.imyeyu.net").toURI())
	 * 
	 * @param uri URI 对象
	 */
	public void openURIInBrowser(URI uri);
	
	/**
	 * <p>对一个地址测试 Ping 延时（Windows 系统方法）
	 * <p>示例：pingHostByCMD("www.imyeyu.net");
	 * <p>示例：pingHostByCMD("127.1.0.1");
	 * 
	 * @param host IP 地址
	 * @return     延时，如果连接失败返回 -1
	 */
	public int pingHostByCMD(String host);
	
	/**
	 * <p>下载一个来自 Internet 的文件
	 * <p>示例：downloadFile("https://www.imyeyu.net/downloads/test.jar", "D://TempFile", "file.jar");
	 * 
	 * @param internetURL 网络文件地址
	 * @param path        下载后保存路径
	 * @param fileName    下载后保存的文件名
	 * @throws MalformedURLException 网络地址错误
	 * @throws FileNotFoundException 找不到文件
	 * @throws IOException           获取错误
	 * @throws Exception             其他异常
	 */
	public void downloadFile(String internetURL, String path, String fileName) throws MalformedURLException, FileNotFoundException, IOException, Exception;
	
	/**
	 * 获取外网 IP，如果获取失败则返回本地 IP
	 * 
	 * @return ip IP 地址，如 192.168.1.1
	 * @throws Exception 获取异常
	 */
	public String getNetworkIp() throws Exception;
	
	/**
	 * 检测本机某个端口是否被占用
	 * 
	 * @param port
	 * @return
	 */
	public boolean isBusyPort(int port);
}
