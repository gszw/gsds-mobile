package cn.com.gszw.mzgxt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtil {
	// 下面这个地址是服务器测试地址
	 public static final String IpUrl = "http://61.178.227.192:7008";
	// 下面这个地址是本地测试地址
	//public static final String IpUrl = "http://10.92.67.240:8080";
//	public static final String IpUrl = "http://192.168.10.100:8089";
	// 登陆地址
	public static final String loginURL = IpUrl + "/server/LoginServlet";
	// 按编码查询纳税人
	public static final String getNsrxxURL = IpUrl + "/server/QueryNsrxx";
	// 按名称查询纳税人
	public static final String getNsrxxListURL = IpUrl
			+ "/server/QueryNsrxxList";
	// 按名称查询纳税人总数
	public static final String getNsrxxTotalURL = IpUrl
			+ "/server/QueryNsrxxTotal";
	// 获得纳说人基本信息
	public static final String getNsrBasicInfo = IpUrl
			+ "/server/QueryNsrBasicInfo";

	// 获得纳说人银行信息
	public static final String getNsrYhList = IpUrl + "/server/QueryNsrYhList";

	// 获得纳说人分页数据
	public static final String getNsrList = IpUrl + "/server/GetNsrList";

	// 获得纳说人总条数
	public static final String getNsrCount = IpUrl + "/server/GetNsrCount";

	// 根所手机号，取税务人员编码
	public static final String Getswrysjh = IpUrl + "/server/GetSwrySjh";
	// 获得税收核定信息
	public static final String getHdssList = IpUrl + "/server/GetHdssList";
	// 获得纳税人停复业信息
	public static final String getNsrTfyxx = IpUrl + "/server/GetNsrTfyxx";
	// 获得纳税人申报信息总表
	public static final String GetSbList = IpUrl + "/server/GetSbList";
	// 获得纳税人缴款 信息总表
	public static final String GetJkList = IpUrl + "/server/GetJkList";
	// 判断登陆人员是不是专管员
	public static final String GetIfZgy = IpUrl + "/server/GetIfZgy";
	// 判断登陆人员是不是专管员
	public static final String SysPzCx = IpUrl + "/server/PzCxList";

	// 读取主界面信息
	public static final String GetDefault = IpUrl + "/server/GetDefault";

	// 用户注册信息
	public static final String Sjxxzc = IpUrl + "/server/Sjxxzc";

	// 用户注册信息
	public static final String GetNsrxxBybm = IpUrl + "/server/GetNsrxxBybm";

	// 查询通知公告信息
	public static final String Gettzgg = IpUrl + "/server/GetTzgg";

	public static final String Gettzggmx = IpUrl + "/server/GetTzggmx";
	
	public static final String LoginZgxt=IpUrl+"/server/LoginZgxt";

	// 获得get请求对象
	public static HttpGet getHttpGet(String url) {
		HttpGet req = new HttpGet(url);
		return req;
	}

	// 获得post请求对象
	public static HttpPost getHttpPost(String url) {
		HttpPost req = new HttpPost(url);
		return req;
	}

	// 通过get请求获得response对象
	public static HttpResponse getHttpResponse(HttpGet req)
			throws ClientProtocolException, IOException {
		HttpParams httpParams = new BasicHttpParams();// 创建Http参数对象

		int timeoutConnection = 30000;// 连接超时
		int timeoutSocket = 5000000;// 等待数据超时
		// HttpParams httpParams = client.getParams();
		// 设置参数
		HttpConnectionParams
				.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		// 获得求情对象
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpResponse resp = client.execute(req);
		return resp;
	}

	// 通过post请求获得response对象
	public static HttpResponse getHttpResponse(HttpPost req)
			throws ClientProtocolException, IOException {
		HttpParams httpParams = new BasicHttpParams();// 创建Http参数对象

		int timeoutConnection = 30000;// 连接超时
		int timeoutSocket = 5000000;// 等待数据超时
		// HttpParams httpParams = client.getParams();
		// 设置参数
		HttpConnectionParams
				.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		// 获得求情对象
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpResponse resp = client.execute(req);
		return resp;
	}

	// 通过url发送post请求，返回结果
	public static String getStringByPost(String url) {
		HttpPost req = HttpUtil.getHttpPost(url);
		String result = null;
		// StringBuffer buffer = new StringBuffer();
		try {
			HttpResponse resp = HttpUtil.getHttpResponse(req);
			if (resp.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = resp.getEntity();
				result = EntityUtils.toString(entity);

				return result;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "net_error";
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "net_error";
			return result;
		}
		return null;
	}

	// 通过url发送get请求，返回结果
	public static String getStringByGet(String url) {
		HttpGet req = HttpUtil.getHttpGet(url);
		String result = null;
		try {
			HttpResponse resp = HttpUtil.getHttpResponse(req);
			if (resp.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(resp.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "net_error";
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "net_error";
			return result;
		}
		return null;
	}

	// 通过HttpPost发送Post请求，返回结果
	public static String getStringByPost(HttpPost req) {
		String result = null;
		StringBuilder ls = new StringBuilder();
		try {
			HttpResponse resp = HttpUtil.getHttpResponse(req);
			HttpEntity entity = resp.getEntity();
			int s = resp.getStatusLine().getStatusCode();
			if (resp.getStatusLine().getStatusCode() == 200) { // 连接成功
				if (entity != null) {
					// 读入缓冲区
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							1024);
					String line = null;
					while ((line = reader.readLine()) != null) {
						ls.append(line);
					}
					reader.close();
					result = ls.toString().trim();
					return result;
				} else {
					return null;
				}
				// result = EntityUtils.toString(resp.getEntity()); //获得资源
				// result = result.replaceAll("\r\n|\n\r|\r|\n", "");
				// //去掉信息中的回车和换行
				// System.out.println(result);
				// return result; //为EditText设置内容
			} else {
				result = "net_error";
				// System.out.println("1111111111111111111111");
				return result;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "net_error";
			// System.out.println("2222222222222222222222");

			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "net_error";
			// System.out.println("3333333333333333333333");

			return result;
		}
	}

	public static boolean isNetworkAvailable(Activity mActivity) {
		Context context = mActivity.getApplicationContext();
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

	/**
	 * 检测网络是否存在
	 */
	public static void HttpTest(final Activity mActivity) {
		if (!isNetworkAvailable(mActivity)) {

			AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
					.setTitle("检测网络")
					.setMessage("抱歉,网络链接不存在！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									mActivity.finish();
								}
							}).create(); // 创建对话框
			alertDialog.show(); // 显示对话框

		}
	}

}
