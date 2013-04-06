package cn.com.gszw.mzgxt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class NetworkTool {
	/**
	 * * ��ȡ��ַ����
	 ** 
	 * @param url
	 *            * @return * @throws Exception
	 */
	public static String getContent(String url) {

		StringBuilder sb = new StringBuilder();
		HttpParams httpParams = new BasicHttpParams();// 创建Http参数对象

		int timeoutConnection = 3000;// 连接超时
		int timeoutSocket = 8000;// 等待数据超时
		// HttpParams httpParams = client.getParams();
		// 设置参数
		HttpConnectionParams
				.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		// 获得求情对象
		HttpClient client = new DefaultHttpClient(httpParams);
		try {
			HttpResponse response = client.execute(new HttpGet(url));
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							8192);
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					reader.close();
				}

				return sb.toString();
			} else {
				return "net_error";
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return "net_error";

		}

	}

}
