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
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FpcxActivity extends Activity {
	private String swrydm,swjgdm;
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;

	private String fpdm;// 发票代码
	private String fphm;// 发票号码
	private String num;// 发票密码或是金额
	private String url;
	EditText editfpdm;
	EditText editfphm;
	EditText editnum;
	ProgressDialog pb1;
	WebView wv;

	public ProgressDialog pBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fpcx);
		// 初始化本表单数据
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		swrydm = bundle.getString("swrydm");// 用户名称
		swjgdm = bundle.getString("swjgdm");// 税务机关代码
		userKey = bundle.getString("swrydm");

		DataInit();

	}

	void DataInit() {

		editfpdm = (EditText) findViewById(R.id.fpdm);
		editfphm = (EditText) findViewById(R.id.fphm);
		TextView mTextView=(TextView)findViewById(R.id.txt_activity_public_title);
		mTextView.setText("发票查询");
		wv = (WebView) findViewById(R.id.v_webview);
		wv.setBackgroundColor(0);

		// 调用初始化注意事项页面
		wv.loadUrl(HttpUtil.IpUrl + "/FP_init.html");

		pb1 = new ProgressDialog(FpcxActivity.this);
		Button btnSearch = (Button) findViewById(R.id.btn_fpcx_search);
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (editfpdm.getText().length() < 5) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"请输入发票代码，至少是后五位！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (editfphm.getText().length() == 0) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"请输入发票号码！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {

					pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pb1.setMessage("数据载入中，请稍候！");
					pb1.show();
					fpdm = editfpdm.getText().toString();
					fphm = editfphm.getText().toString();
					url = HttpUtil.IpUrl + "/server/FP_cxjg.jsp";
					processThread();// 开始查询信息

				}
			}
		});
		int width=(getWindowManager().getDefaultDisplay().getWidth()-btnSearch.getWidth()-6)/2;
		editfpdm.setWidth(width);
		Button btnExit = (Button) findViewById(R.id.btn_activity_public_exit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FpcxActivity.this.finish();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 如果是20，说明是CaptureActivity返回的数据
		if (20 == resultCode) {
			String BarcodeFormat = data.getExtras().getString("BarcodeFormat");
			String Barcode = data.getExtras().getString("Barcode");
			showDialog("返回数据：" + BarcodeFormat + Barcode);
			// 在此解析XML
			fpdm = Barcode.substring(0, 12);
			fphm = Barcode.substring(0, 12);
			num = Barcode.substring(0, 12);

			showDialog(Barcode);
			if (Barcode.equals("")) {
				editfpdm.setText(fpdm);
				editfphm.setText(fphm);
				editnum.setText(num);

				url = HttpUtil.IpUrl + "/server/FP_cxjg.jsp";
				processThread();// 开始查询信息
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
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

				params.add(new BasicNameValuePair("fpdm", fpdm));
				params.add(new BasicNameValuePair("fphm", fphm));
				params.add(new BasicNameValuePair("num", "-1"));

				// 所有后台调用都要传这个参数过去
				params.add(new BasicNameValuePair("userKey", userKey));
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

	// 显示提示对话框，提示信息
	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}

				});
		AlertDialog alert = builder.create();
		alert.show();

	}

}