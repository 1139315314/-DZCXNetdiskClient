package com.dzcx.netdisk.util.Interface;

import java.io.File;

/**
 * 数据流相关
 * 
 * @author 夜雨
 *
 */
public interface IO {
	
	/**
	 * <p>获取自身 Jar 所在路径，如果是开发环境，则是 bin 路径，参数必须使用 this 关键字
	 * <p>示例：getJarAbsolutePath(this);
	 * 
	 * @param obj this 关键字
	 * @return    绝对路径
	 */
	public String getJarAbsolutePath(Object obj);

	/**
	 * <p>获取这个文件夹内所有文件名字符串
	 * <p>示例：getDirList(new File("C://"));
	 * 
	 * @param dir 文件夹 File 对象
	 * @return    文件列表
	 */
	public String[] getDirList(File dir);

	/**
	 * <p>读取文件返回字符串
	 * <p>示例：fileToString(new File("test.txt"));
	 * 
	 * @param file    文件对象
	 * @param charset 编码类型
	 * @return        读取结果
	 */
	public String fileToString(File file, String charset);

	/**
	 * <p>写入字符串到磁盘的文件
	 * <p>示例：stringToFile(new File("test.txt"), "test data");
	 * 
	 * @param file 文件对象
	 * @param data 写入的字符串
	 */
	public void stringToFile(File file, String data);

	/**
	 * <p>读取 Jar 内的文件，注意返回的是字符串
	 * <p>示例：jarFileToString("/net/imyeyu/test");
	 * 
	 * @param path Jar 内文件路径
	 * @return     读取结果
	 */
	public String jarFileToString(String path);
	
	/**
	 * <p>以字节形式读取 Jar 内文件到磁盘上（导出 Jar 文件）
	 * <p>示例：jarFileToDisk("/net/imyeyu/res/img.png", "img/img.png");
	 * 
	 * @param jarPath  Jar 内文件
	 * @param filePath 磁盘上的文件
	 */
	public void jarFileToDisk(String jarPath, String filePath);
	
	/**
	 * <p>隐藏文件（Windows 系统方法）
	 * <p>示例：hidenFile(files);
	 * 
	 * @param files 文件列表
	 */
	public void hidenFile(File[] files);
}
