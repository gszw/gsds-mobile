package cn.com.gszw.mzgxt.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.util.HttpUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RkLjrkqkActivity extends Activity {
	// 以下三变量在DataInit中初始化
	private String swjgmc;
	private String username;
	private String account;// 用户帐号
	private String swjgdm;// 机关代码
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;
	// 进度条
	ProgressDialog pb1;
	WebView wv;
	// 报表ID号，默认是0,分月分税种
	// 入库模块预留30--39
	private String Table_Id = "30";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ljqk);

		// 添加搜索条件框得到或失去焦点的监听

		// tv_tj.setText("请输入查询条件!");
		pb1 = new ProgressDialog(RkLjrkqkActivity.this);
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");

		DataInit();
	}

	void DataInit() {
		// 初始化本表单数据
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		
		TextView mTextView=(TextView)findViewById(R.id.txt_activity_public_title);
		mTextView.setText("入库情况");
		Button btnExit = (Button) findViewById(R.id.btn_activity_public_exit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RkLjrkqkActivity.this.finish();
			}
		});

		account = bundle.getString("account");// 用户名称
		swjgdm = bundle.getString("swjgdm");// 税务机关代码
		swjgmc = bundle.getString("swjgmc");// 税务机关名称
		username = bundle.getString("username");// 人员姓名

		// 后台返回的人员信息Key，在以后所有调用后台数据时，
		// 必须把这个Key值传给后台，否则，后台返回null
		userKey = bundle.getString("userKey");
		wv = (WebView) findViewById(R.id.v_webview);
		wv.setBackgroundColor(0);

		pb1 = new ProgressDialog(RkLjrkqkActivity.this);
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");
		pb1.show();
		processThread();
	}

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

				String url = HttpUtil.IpUrl + "/server/GetPublicView";

				String result = "";
				// 获得请求对象

				// 调用HttpUtil类中的函数，返回HttpPost
				HttpPost req = HttpUtil.getHttpPost(url);

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("account", account));
				// 所有后台调用都要传这个参数过去
				params.add(new BasicNameValuePair("userKey", userKey));
				params.add(new BasicNameValuePair("TableId", Table_Id));
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
	// 以下代码是退出提示代码段
	private void showTips() {
		AlertDialog alertDialog = new AlertDialog.Builder(RkLjrkqkActivity.this)
				.setTitle("退出程序").setMessage("您确认要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						pb1.dismiss();
						RkLjrkqkActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}

	protected void onPause() {
		pb1.dismiss();
		super.onPause();
	}

}
