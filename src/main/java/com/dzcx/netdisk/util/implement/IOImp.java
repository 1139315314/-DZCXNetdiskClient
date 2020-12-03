package com.dzcx.netdisk.util.implement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.dzcx.netdisk.util.Interface.IO;


public class IOImp implements IO {
	
	public String getJarAbsolutePath(Object obj) {
		String path = obj.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = new EncodeImp().deURL(path);
		if (path.endsWith(".jar")) {
			path = path.substring(0, path.lastIndexOf(File.separator) + 1);
		}
		path = new File(path).getAbsolutePath();
		return path;
	}

	public String[] getDirList(File dir) {
		if (dir.isDirectory()) {
			File[] next = dir.listFiles();
			if (next != null) {
				String[] fileName = new String[next.length];
				for (int i = 0, l = next.length; i < l; i++) {
					fileName[i] = next[i].getName();
				}
				return fileName;
			}
		}
		return null;
	}

	public String fileToString(File file, String charset) {
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, charset);
			BufferedReader br = new BufferedReader(isr);
			String input;
			while ((input = br.readLine()) != null) {
				sb.append(input + "\r\n");
			}
			br.close();
			isr.close();
			fis.close();
		} catch (UnsupportedEncodingException e) {
			new GUIImp().exception(e);
		} catch (IOException e) {
			new GUIImp().exception(e);
		}
		if (0 < sb.length()) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return "";
		}
	}

	public void stringToFile(File file, String data) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(data);
			bw.flush();
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			new GUIImp().exception(e);
		} catch (Exception e) {
			new GUIImp().exception(e);
		}
	}

	public String jarFileToString(String path) {
		StringBuffer sb = new StringBuffer();
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String input;
			while ((input = br.readLine()) != null) {
				sb.append(input + "\r\n");
			}
			br.close();
			isr.close();
			is.close();
		} catch (UnsupportedEncodingException e) {
			new GUIImp().exception(e);
		} catch (IOException e) {
			new GUIImp().exception(e);
		}
		return sb.toString();
	}
	
	public void jarFileToDisk(String jarPath, String filePath) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(jarPath);
			FileOutputStream fos = new FileOutputStream(filePath);
			byte input[] = new byte[128];
			int l = -1;
			while ((l = is.read(input)) != -1) {
				fos.write(input, 0, l);
			}
			fos.close();
			is.close();
		} catch (IOException e) {
			new GUIImp().exception(e);
		}
	}

	public void hidenFile(File[] files) {
		try {
			for (int i = 0, l = files.length; i < l; i++) {
				if (!files[i].isHidden()) {
					if (files[i].isFile()) {
						if (files[i].exists()) {
							Runtime.getRuntime().exec("attrib " + "\"" + files[i].getAbsolutePath() + "\""+ " +H");
						}
					} else {
						if (files[i].exists()) {
							Runtime.getRuntime().exec("attrib " + "\"" + files[i].getAbsolutePath() + "\""+ " +H");
						}
					}
				}
			}
		} catch (IOException e) {
			new GUIImp().exception(e);
		}
	}
}
