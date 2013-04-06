package cn.com.gszw.mzgxt.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.gszw.mzgxt.util.HttpUtil;

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
import android.widget.Toast;//

public class DhcxActivity extends Activity {
	private String account;// 用户帐号
	private String swjgdm;// 机关代码
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;

	private String nsrnbm;// 查询返回的纳税人内部码

	private String nsrbm;
	private String url;
	EditText editnsrbm;
	ProgressDialog pb1;

	TextView jg_nsrmc;
	TextView jg_scjydz;
	TextView jg_fr;
	TextView gljg;
	TextView zgy;
	TextView cwfzr;
	TextView bsy;

	public ProgressDialog pBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dhcx);
		// 初始化本表单数据
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		account = bundle.getString("account");// 用户名称
		swjgdm = bundle.getString("swjgdm");// 税务机关代码
		// 后台返回的人员信息Key，在以后所有调用后台数据时，
		// 必须把这个Key值传给后台，否则，后台返回null
		userKey = bundle.getString("userKey");
		nsrnbm = null;

		DataInit();

	}

	void DataInit() {

		editnsrbm = (EditText) findViewById(R.id.nsrbm);
		jg_nsrmc = (TextView) findViewById(R.id.jg_nsrmc);
		jg_scjydz = (TextView) findViewById(R.id.jg_scjydz);
		jg_fr = (TextView) findViewById(R.id.jg_fr);
		gljg = (TextView) findViewById(R.id.gljg);
		zgy = (TextView) findViewById(R.id.zgy);
		cwfzr = (TextView) findViewById(R.id.cwfzr);
		bsy = (TextView) findViewById(R.id.bsy);

		// editnsrbm.setText("262000800330");

		pb1 = new ProgressDialog(DhcxActivity.this);
		Button bt = (Button) findViewById(R.id.bt_cx);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (editnsrbm.getText().length() < 1) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"请输入纳税人编码！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {

					pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pb1.setMessage("数据载入中，请稍候！");
					pb1.show();
					nsrbm = editnsrbm.getText().toString();
					url = HttpUtil.GetNsrxxBybm;
					processThread();// 开始查询信息

				}
			}
		});
		Button bt1 = (Button) findViewById(R.id.bt_exit);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				DhcxActivity.this.finish();
			}
		});
		Button bt2 = (Button) findViewById(R.id.bt_yhs);
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (nsrnbm != null) {
					// 进入一户式查询
					Bundle bundle = new Bundle();
					Intent intent = new Intent(DhcxActivity.this,
							DjNsryhscxActivity.class);
					bundle.putString("nsrbm", nsrbm);
					bundle.putString("nsrmc", jg_nsrmc.getText().toString());
					bundle.putString("nsrnbm", nsrnbm);
					bundle.putString("userKey", userKey);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"请你先查询纳税人信息！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}

			}
		});

	}

	// 以下代码是Hanler示例代码，对于耗时的查询需要用以下代码给出进度条

	// 定义Handler直接用，不需要修改
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			super.handleMessage(msg);
			// 对于ＵＩ的更新，代码只能在这儿进行
			// 接受进程执行完成后的Message，刷新界面ＵＩ
			String jsonPages = (String) msg.obj;
			if (jsonPages == "-1") {
				Toast toast = Toast.makeText(getApplicationContext(),
						"你所要查询的纳税人不存在或不是你所管辖的户！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				jg_nsrmc.setText("");
				jg_scjydz.setText("");
				jg_fr.setText("");
				gljg.setText("");
				zgy.setText("");
				cwfzr.setText("");
				bsy.setText("");
				nsrnbm = null;
			} else {
				try {

					JSONObject jsonObject = new JSONObject(jsonPages);
					jg_nsrmc.setText(jsonObject.getString("nsrmc"));
					jg_scjydz.setText(jsonObject.getString("scjydz"));
					jg_fr.setText(jsonObject.getString("fr"));
					nsrnbm = jsonObject.getString("nsrnbm");

					gljg.setText(jsonObject.getString("gljg"));
					zgy.setText(jsonObject.getString("zgy"));
					cwfzr.setText(jsonObject.getString("cwfzr"));
					bsy.setText(jsonObject.getString("bsr"));
					// 生成动态数组，并且转载数据
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

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

				params.add(new BasicNameValuePair("nsrbm", nsrbm));
				params.add(new BasicNameValuePair("account", account));

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

				Message msg = new Message();
				msg.obj = result;
				handler.sendMessage(msg);

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
