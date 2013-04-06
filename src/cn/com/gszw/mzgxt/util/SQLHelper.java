package cn.com.gszw.mzgxt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SQLHelper {

	private static Socket mSocket;
	private static PrintWriter mPrintWriter;
	private static BufferedReader mBufferedReader;
	private static String receiveValue;
	private static final String serverIpAddress = "10.92.67.231";
	private static final int serverPort = 8089;

	/**
	 * 向服务器发送不带参数的查询请求，并将返回结果转换为数组类型的JSON格式数据
	 * 
	 * @author 赵帆
	 * @date 2013-3-27
	 * @return_type JSONArray
	 */
	public static JSONArray receiveByJSONArray(String sqlName) {
		try {
			mSocket = new Socket(serverIpAddress, serverPort);
			mPrintWriter = new PrintWriter(mSocket.getOutputStream());
			mPrintWriter.println(sqlName + "[NoParamsSql]");
			mPrintWriter.flush();
			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			receiveValue = mBufferedReader.readLine();
			mPrintWriter.close();
			mBufferedReader.close();
			mSocket.close();
			if ("0".equals(receiveValue.toString())) {
				return null;
			} else {
				return new JSONArray(receiveValue);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向服务器发送不带参数的查询请求，并将返回结果转换为对象类型的JSON格式数据
	 * 
	 * @author 赵帆
	 * @date 2013-3-27
	 * @return_type JSONObject
	 */
	public static JSONObject receiveByJSON(String sqlName) {
		try {
			mSocket = new Socket(serverIpAddress, serverPort);
			mPrintWriter = new PrintWriter(mSocket.getOutputStream());
			mPrintWriter.println(sqlName + "[NoParamsSql]");
			mPrintWriter.flush();
			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			receiveValue = mBufferedReader.readLine();
			mPrintWriter.close();
			mBufferedReader.close();
			mSocket.close();
			if ("0".equals(receiveValue.toString())) {
				return null;
			} else {
				return new JSONObject(receiveValue);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向服务器发送带参数的查询请求，并将返回结果转换为数组类型的JSON格式数据
	 * 
	 * @author 赵帆
	 * @date 2013-3-27
	 * @return_type JSONArray
	 */
	public static JSONArray receiveByJSONArray(String sqlName,
			String[] sqlParams) {
		StringBuilder mStringBuilder = new StringBuilder();
		for (String sqlParam : sqlParams) {
			mStringBuilder.append("@" + sqlParam);
		}
		try {
			mSocket = new Socket(serverIpAddress, serverPort);
			mPrintWriter = new PrintWriter(mSocket.getOutputStream());
			mPrintWriter.println(sqlName + mStringBuilder + "[ParamsSql]");
			mPrintWriter.flush();
			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			receiveValue = mBufferedReader.readLine();
			mPrintWriter.close();
			mBufferedReader.close();
			mSocket.close();
			if ("0".equals(receiveValue.toString())) {
				return null;
			} else {
				return new JSONArray(receiveValue);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向服务器发送带参数的查询请求，并将返回结果转换为对象类型的JSON格式数据
	 * 
	 * @author 赵帆
	 * @date 2013-3-27
	 * @return_type JSONArray
	 */
	public static JSONObject receiveByJSON(String sqlName, String[] sqlParams) {
		StringBuilder mStringBuilder = new StringBuilder();
		for (String sqlParam : sqlParams) {
			mStringBuilder.append("@" + sqlParam);
		}
		try {
			mSocket = new Socket(serverIpAddress, serverPort);
			mPrintWriter = new PrintWriter(mSocket.getOutputStream());
			mPrintWriter.println(sqlName + mStringBuilder + "[ParamsSql]");
			mPrintWriter.flush();
			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			receiveValue = mBufferedReader.readLine();
			mPrintWriter.close();
			mBufferedReader.close();
			mSocket.close();
			if ("0".equals(receiveValue.toString())) {
				return null;
			} else {
				return new JSONObject(receiveValue);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
