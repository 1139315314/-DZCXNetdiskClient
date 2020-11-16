package com.dzcx.netdisk.util;

/**
 * <p>编码接口
 * <p>通常是一些字符编码操作或判定
 * 
 * @author Yeyu
 *
 */
public interface Encode {

	/**
	 * <p>更改字符串编码
	 * <p>示例: changeCharset("test", "GBK", "UTF-8");
	 * 
	 * @param data       字符串
	 * @param oldCharset 旧的字编码
	 * @param newCharset 新的字符编码
	 * @return           更改结果
	 */
	public String changeCharset(String data, String oldCharset, String newCharset);

	/**
	 * <p>编译字符串所有字符为 Unicode 编码
	 * <p>示例: enUnicodeAll("yeyu");
	 * 
	 * @param data 编译字符串
	 * @return     编译结果
	 */
	public String enUnicodeAll(String data);
	
	/**
	 * <p>编译字符串非半角字符为 Unicode 编码
	 * <p>示例: enUnicode("夜雨yeyu");
	 * 
	 * @param data 编译字符串
	 * @return     编译结果
	 */
	public String enUnicode(String data);

	/**
	 * <p>解码含有 Unicode 编码的字符串
	 * <p>示例: deUnicode("\u2000");
	 * 
	 * @param data 字符串
	 * @return     解码结果
	 */
	public String deUnicode(String data);

	/**
	 * <p>编译字符串为 Base64 编码
	 * <p>示例: enBase64("test");
	 * 
	 * @param data 字符串
	 * @return     编码结果
	 */
	public String enBase64(String data);
	
	/**
	 * <p>解码 Base64 字符串
	 * <p>示例: deBase64("9df8g4df==");
	 * 
	 * @param data 字符串
	 * @return     解码结果
	 */
	public String deBase64(String data);

	/**
	 * <p>计算字符串 MD5
	 * <p>示例: md5("test");
	 * 
	 * @param data    字符串
	 * @return encode 计算结果
	 */
	public String md5(String data);
	
	/**
	 * <p>判断字符串是否存在中文
	 * <p>示例: hasChinese("夜雨yeyu");
	 * 
	 * @param data  字符串
	 * @return true 为存在，否则不存在
	 */
	public boolean hasChinese(String data);
	
	/**
	 * <p>判断字符串是否只有数字
	 * <p>示例: isNumber("123");
	 * 
	 * @param data  字符串
	 * @return true 为只有数字，否则存在其他字符
	 */
	public boolean isNumber(String data);
	
	/**
	 * <p>是否为半角字符
	 * <p>示例: isHalfChar('a');
	 * 
	 * @param  data 字符
	 * @return true 为半角，否则为全角
	 */
	public boolean isHalfChar(char data);
	
	/**
	 * <p>URL 编码，返回 %20 这种格式，如果出现异常，返回原文本
	 * 
	 * @param url 链接
	 * @return    编码结果
	 */
	public String enURL(String url);
	
	/**
	 * <p>URL 解码，返回原文本，如果出现异常，返回编码文本
	 * 
	 * @param url 链接
	 * @return    解码结果
	 */
	public String deURL(String url);
	
	/**
	 * <p>MD5 编码加 Base64，已弃用
	 * 
	 * @param data 编码文本
	 * @return     编码结果
	 */
	public String generateBase(String data);
}