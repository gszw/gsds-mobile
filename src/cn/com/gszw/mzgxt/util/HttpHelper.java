/**
 * @Title: 甘肃地税移动征管系统
 * @Package cn.com.gszw.mzgxt.util 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 赵帆
 * @date 2013-4-1 上午9:04:00 
 * @version V1.0   
 */
package cn.com.gszw.mzgxt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Copyright: Copyright © 2013-2018
 * @Company: www.gszw.com.cn
 * @Makedate:2013-4-1 上午9:04:00
 * @author 赵帆
 */
public class HttpHelper extends Thread {
	//private static final String IPURL_STRING = "http://61.178.227.192:7008/server/";
	private static final String IPURL_STRING = "http://10.92.67.240:8080/server/";
	private static String result = null;
	private static HttpPost mHttpPost = null;
	private static HttpParams mHttpParams = null;
	private static HttpClient mHttpClient = null;
	private static HttpResponse mHttpResponse = null;
	private static HttpEntity mHttpEntity = null;
	private static BufferedReader mBufferedReader = null;

	private static HttpResponse getHttpResponse(String urlString,
			List<NameValuePair> parameters) {
		mHttpPost = new HttpPost(urlString);
		mHttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(mHttpParams, 10000);
		HttpConnectionParams.setSoTimeout(mHttpParams, 30000);
		mHttpClient = new DefaultHttpClient(mHttpParams);
		try {
			mHttpPost
					.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			mHttpResponse = mHttpClient.execute(mHttpPost);
		} catch (UnsupportedEncodingException e) {
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return mHttpResponse;
	}

	/**
	 * 向服务器发送POST请求，并接受服务器返回信息
	 * 
	 * @author 赵帆
	 * @date 2013年4月2日
	 */
	public static String getStringByPost(Activity activity,
			String httpServletNameOrJSPName, List<NameValuePair> parameters) {
		if (validateNetState(activity)) {
			String mUrlString = IPURL_STRING + httpServletNameOrJSPName;
			StringBuilder ls = new StringBuilder();
			try {
				mHttpResponse = HttpHelper.getHttpResponse(mUrlString,
						parameters);
				if (mHttpResponse != null) {
					if (mHttpResponse.getStatusLine().getStatusCode() == 200) { // 连接成功
						mHttpEntity = mHttpResponse.getEntity();
						if (mHttpEntity != null) {
							// 读入缓冲区
							mBufferedReader = new BufferedReader(
									new InputStreamReader(
											mHttpEntity.getContent(),
											HTTP.UTF_8), 1024);
							String line = null;
							while ((line = mBufferedReader.readLine()) != null) {
								ls.append(line);
							}
							mBufferedReader.close();
							result = ls.toString().trim();
							return result;
						} else {
							return "{" + '"' + "fhjg" + '"' + ":" + '"'
									+ "未获取到相关信息" + '"' + "}";
						}
					} else {
						return "{" + '"' + "fhjg" + '"' + ":" + '"'
								+ "与服务器连接失败" + '"' + "}";
					}
				} else {
					return "{" + '"' + "fhjg" + '"' + ":" + '"' + "与服务器连接超时"
							+ '"' + "}";
				}
			} catch (ClientProtocolException e) {
				return "{" + '"' + "fhjg" + '"' + ":" + '"' + "与服务器连接失败" + '"'
						+ "}";

			} catch (IOException e) {
				return "{" + '"' + "fhjg" + '"' + ":" + '"' + "未获取到相关信息" + '"'
						+ "}";
			}

		} else {
			return "{" + '"' + "fhjg" + '"' + ":" + '"' + "未找到可用的网络连接，请检查网络设置" + '"'
					+ "}";
		}
	}

	private static boolean validateNetState(Activity activity) {
		Context context = activity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
