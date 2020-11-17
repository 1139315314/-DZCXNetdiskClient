package com.dzcx.netdisk.util.implement;

import com.dzcx.netdisk.util.Interface.Encode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;


public class EncodeImp implements Encode {

	public String changeCharset(String data, String oldCharset, String newCharset) {
		try {
			if (data != null) {
				byte[] bs = data.getBytes(oldCharset);
				return new String(bs, newCharset);
			}
		} catch (UnsupportedEncodingException e) {
			new GUIImp().exception(e);
		}
		return null;
	}

	public String enUnicodeAll(String data) {
		StringBuilder sb = new StringBuilder();
		char[] c = data.toCharArray();
		for (int i = 0, l = c.length; i < l; i++) {
			sb.append("\\u" + Integer.toHexString(c[i]));
		}
		return sb.toString();
	}

	public String enUnicode(String data) {
		StringBuilder sb = new StringBuilder();
		char[] c = data.toCharArray();
		for (int i = 0, l = c.length; i < l; i++) {
			if (!new EncodeImp().isHalfChar(c[i])) {
				sb.append("\\u" + Integer.toHexString(c[i]));
			} else {
				sb.append(c[i]);
			}
		}
		return sb.toString();
	}

	public String deUnicode(String data) {
		StringBuilder sb = new StringBuilder();
		String[] hex = data.split("\\\\u");
		int index = -1;
		for (int i = 1, l = hex.length; i < l; i++) {
			if (4 < hex[i].length()) {
				index = Integer.parseInt(hex[i].substring(0, 4), 16);
				sb.append((char) index);
				sb.append(hex[i].substring(4, hex[i].length()));
			} else {
				index = Integer.parseInt(hex[i], 16);
				sb.append((char) index);
			}
		}
		return sb.toString();
	}

	public String enBase64(String data) {
		String result;
		try {
			result = Base64.getEncoder().encodeToString(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			new GUIImp().exception(e);
			return null;
		}
		return result;
	}
	
	public String deBase64(String data) {
		String result;
		try {
			result = new String(Base64.getDecoder().decode(data), "UTF-8");
		} catch (IOException e) {
			new GUIImp().exception(e);
			return null;
		}
		return result;
	}

	public String md5(String data) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = new BigInteger(1, md.digest(data.getBytes())).toString(16);
			for (int i = 0; i < 32 - result.length(); i++) {
				result = "0" + result;
			}
		} catch (NoSuchAlgorithmException e) {
			new GUIImp().exception(e);
		}
		return result;
	}

	public boolean hasChinese(String data) {
		return Pattern.compile("[\u4e00-\u9fa5]").matcher(data).find();
	}

	public boolean isNumber(String data) { 
		return Pattern.compile("[0-9]*").matcher(data).matches();
	}

	public boolean isHalfChar(char data) {
		return (int) data < 129;
	}

	public String enURL(String url) {
        if (url == null) return "";
        StringBuilder sb = new StringBuilder();
        try {
        	String[] urls = url.split("/");
        	for (int i = 0; i < urls.length; i++) {
        		sb.append("/");
				sb.append(URLEncoder.encode(urls[i], "UTF-8"));
			}
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
	}
	
	public String deURL(String url) {
		if (url == null) return "";
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	public String generateBase(String data) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = Base64.getEncoder().encodeToString(md.digest(data.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			new GUIImp().exception(e);
		} catch (UnsupportedEncodingException e) {
			new GUIImp().exception(e);
		}
		return result;
	}
}
