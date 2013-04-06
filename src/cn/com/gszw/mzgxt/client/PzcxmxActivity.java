package cn.com.gszw.mzgxt.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.com.gszw.mzgxt.util.HttpUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

public class PzcxmxActivity extends Activity {

	private String title;
	private String pzzl;
	private String userKey;
	private String pzzb;
	private String pzhm;
	private String url;// 获取数据的url;
	private String pzzb_dm;

	ProgressDialog pb1;// 进度条
	WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window win = getWindow();

		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_public);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		pzzl = bundle.getString("pzzl");
		pzzb = bundle.getString("pzzb");
		pzhm = bundle.getString("pzhm");
		title = bundle.getString("title");
		pzzb_dm = bundle.getString("pzzb_dm");

		// 后台返回的人员信息Key，在以后所有调用后台数据时，
		// 必须把这个Key值传给后台，否则，后台返回null
		userKey = bundle.getString("userKey");
		url = bundle.getString("url");

		// 在上方显示纳税人名称和编码
		TextView v_nsrbm = (TextView) findViewById(R.id.web_nsrbm);
		v_nsrbm.setText("票证种类：" + pzzl);
		TextView v_nsrmc = (TextView) findViewById(R.id.web_nsrmc);
		v_nsrmc.setText("票证号码：" + pzzb + "::" + pzhm);

		wv = (WebView) findViewById(R.id.v_webview);
		wv.setBackgroundColor(0);

		pb1 = new ProgressDialog(PzcxmxActivity.this);
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");
		pb1.show();
		processThread();
	}

	// 以下代码是Hanler示例代码，对于耗时的查询需要用以下代码给出进度条

	// 定义Handler直接用，不需要修改
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pb1.hide();

		}

	};

	// 主进程
	private void processThread() {
		// 构建一个下载进度条
		// pb1 = ProgressDialog.show(PublicWebView.this, "查询银行信息", "正在查询……");
		new Thread() {
			@Override
			public void run() {
				// 把耗时长的代码全放在这儿

				String result = "";
				// 获得请求对象

				// 调用HttpUtil类中的函数，返回HttpPost
				HttpPost req = HttpUtil.getHttpPost(url);

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("pzhm", pzhm));
				// 所有后台调用都要传这个参数过去
				params.add(new BasicNameValuePair("userKey", userKey));
				params.add(new BasicNameValuePair("pzzb_dm", pzzb_dm));
				// 设置请求对象的编码
				try {
					req.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					result = HttpUtil.getStringByPost(req);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = "-1";
				}

				wv.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);

				handler.sendEmptyMessage(0);

			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 返回
			pb1.dismiss();
		}
		return super.onKeyDown(keyCode, event);

	}

}
