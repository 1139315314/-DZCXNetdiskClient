package com.dzcx.netdisk.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 其他工具类，一些杂七杂八的功能
 * 
 * @author 夜雨
 *
 */
public interface Tools {
	
	/**
	 * <p>精准剪切等宽字体的混合字符串，如字符串 "1一2二3三45" 和 "12345" 同时
	 * 剪切 4 位长度时会返回 1一2 和 1234，当然，必须是等宽字体才有效
	 * <p>示例: cutString("text", 2, false);
	 * 
	 * @param data   将被剪切的字符串
	 * @param length 剪切长度
	 * @param dot    是否追加 "..." 在返回字符串后面
	 * @return       剪切结果
	 */
	public String cutString(String data, int length, boolean dot);
	
	/**
	 * 随机一个 HashMap，可以限制返回数量或原数量返回，通常用于随机抽取
	 * 
	 * @param map   HashMap
	 * @param limit 返回数量
	 * @return      随即结果
	 */
	public Map<Object, Object> randomMap(Map<Object, Object> map, int limit);
	
	/**
	 * 随机一个值为 File 类型 HashMap
	 * 
	 * @param map HashMap
	 * @return    随即结果
	 */
	public Map<String, File> randomFileMap(Map<String, File> map);
	
	/**
	 * 根据 String 类型的键排序一个 HashMap
	 * 
	 * @param map HashMap
	 * @return    排序结果
	 */
	public Map<String, Object> sortMapByStringKey(Map<String, Object> map);
	
	/**
	 * 根据 Long 类型的键排序一个 HashMap
	 * 
	 * @param map HashMap
	 * @return    排序结果
	 */
	public Map<Long, String> sortMapByLongKey(Map<Long, String> map);
	
	/**
	 * 安全的移除一个键为 String, 值为 File 类型的 HashMap 的一个元素
	 * 
	 * @param map 操作的 HashMap
	 * @param key 移除键
	 * @return    移除后的 HashMap
	 */
	public Map<String, File> removeFileMapByKey(Map<String, File> map, String key);
	
	/**
	 * 获取系统物理内存大小，返回单位为 MB
	 * 
	 * @return 内存大小
	 */
	public int getSystemMemorySize();
	
	/**
	 * <p>检查一个进程是否在运行（Windows 系统方法）
	 * <p>示例：findProcess("javaw.exe", "Minecraft 1.14.2");
	 * 
	 * @param processName 进程名
	 * @param threadName  线程名
	 * @return true 为正在运行，否则未运行
	 */
	public boolean findProcess(String processName, String threadName);
	
	/**
	 * <p>设置字符串到系统剪切板
	 * <p>示例：setIntoClipboard("text");
	 * 
	 * @param content 将要设置的字符串
	 */
	public void setIntoClipboard(String content);
	
	/**
	 * 从剪切板获取文本内容
	 * 
	 * @return 剪切板文本
	 * @throws Exception 设置异常
	 */
	public String getIntoClipboard() throws Exception;
	
	/**
	 * <p>格式化一个储存容量
	 * <p>当值 等于 -1 时返回 ""
	 * 当值 &lt; 1 KB 时返回 999 B &lt;- 值和单位有个空格
	 * 当值 1 KB &lt; 10 MB 时返回 9,999 KB
	 * 当值 10 MB &lt; value &lt; 1GB 时返回 9,999 MB
	 * 当值 1000 GB &lt; value 时返回 1.111 TB （真的？）
	 * 
	 * @param byteValue 储存长度
	 * @param format    数值格式化，如果要插入 ',' 号，请传值 new DecimalFormat("#,###")
	 * @return          格式后字符串，如 "5,4321 KB"
	 */
	public String storageFormat(double byteValue, DecimalFormat format);
	
	/**
	 * <p>格式化字节数据为人看的网络速度
	 * <p>当值 = -1 时，返回 "0"
	 * 当值 &lt; 1 KB 时，返回 999 B &lt;- 值和单位有个空格
	 * 当值 &lt; 1.1 MB 时，返回 1,199 KB
	 * 当值 &gt; 1.1 MB 时，返回 9,999 MB
	 * 
	 * @param byteValue 字节长度
	 * @param format    数值格式化，如果要插入 ',' 号，请传值 new DecimalFormat("#,###")
	 * @return          格式化结果
	 */
	public String netSpeedFormat(double byteValue, DecimalFormat format);
	
	/**
	 * <p>将 HashMap 所有数据和 URL 组合成参数链接
	 * <p>示例: getUrlWithQueryString("http://imyeyu.met", params);
	 * 
	 * @param url    URL 地址
	 * @param params HashMap 参数链表
	 * @return       组合结果
	 */
	public String getUrlWithQueryString(String url, Map<String, String> params);
}
