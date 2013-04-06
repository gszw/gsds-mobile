package cn.com.gszw.mzgxt.util;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 * 
 * @author xiaobo
 * @version
 */
public abstract class MD5EncryptUtils {

	private MD5EncryptUtils() {
	}

	public static String MD5Encode(String origin) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return byteArrayToString(md.digest(origin.getBytes()));
	}

	/**
	 * 转换字节数组为16进制字符串或者全数组字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			byte aB = b[i];
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param b
	 *            byte
	 * @return String
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 十六进制字符
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static void main(String[] args) {
		System.out.println("-------->" + MD5Encode("123"));
	}

	/**
	 * 把字符串的后n位用“*”号代替
	 * 
	 * @param str
	 *            要代替的字符串
	 * 
	 * @param n
	 *            代替的位数
	 * 
	 * @return
	 */

	public static String replaceSubString(String str, int n) {

		String sub = "";

		try {

			sub = str.substring(0, str.length() - n);

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < n; i++) {

				sb = sb.append("*");

			}

			sub += sb.toString();

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return sub;

	}

}