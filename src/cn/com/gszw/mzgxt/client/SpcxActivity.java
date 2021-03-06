package cn.com.gszw.mzgxt.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.util.HttpUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SpcxActivity extends Activity {
	// 以下三变量在DataInit中初始化
	private String account;
	private String swjgdm;

	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;
	ProgressDialog pb1;
	ListView myListView;

	EditText pzhm;// 票证号码

	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 以下代码是在Activity启动时关闭输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_spcx);
		// 在这儿只需要调用这个方法就可以了

		DataInit();

	}

	void DataInit() {
		// 初始化本表单数据
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		account = bundle.getString("account");// 用户名称
		swjgdm = bundle.getString("swjgdm");// 税务机关代码
		// 后台返回的人员信息Key，在以后所有调用后台数据时，
		// 必须把这个Key值传给后台，否则，后台返回null
		userKey = bundle.getString("userKey");
		myListView = (ListView) findViewById(R.id.jk_list);
		TextView mTextView=(TextView)findViewById(R.id.txt_activity_public_title);
		mTextView.setText("税票查询");
		// 定义查询按钮
		Button btnCallBack=(Button)findViewById(R.id.btn_activity_public_exit);
		btnCallBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpcxActivity.this.finish();
			}
		});
		Button bt1 = (Button) findViewById(R.id.syscx_pz_bt);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 隐藏软键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(SpcxActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				v.setEnabled(false);
				if (pzhm.getText().length() == 0) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"请输入票证号码！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					processThread();// 开始查询票证信息

				}
				v.setEnabled(true);
			}
		});
		// 定义票证号码输入框
		pzhm = (EditText) findViewById(R.id.cx_pzhm);
		// pzhm.setText("01606499");
		// myListView
		myListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						// 调用通用JSP接口
						// 点击第一行申报信息后，再进入申报明细信息查询界面
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("userKey", userKey);
						bundle.putString("pzzl", mylist.get(arg2).get("zl"));
						bundle.putString("pzzb", mylist.get(arg2).get("zb"));
						bundle.putString("pzhm", mylist.get(arg2).get("pzhm"));
						bundle.putString("pzzb_dm",
								mylist.get(arg2).get("pzzb_dm"));
						bundle.putString("title", "纳税人票证信息");
						bundle.putString("url", HttpUtil.IpUrl
								+ "/server/PzList.jsp");
						intent.putExtras(bundle);
						// intent.setClass(NsrxxActivity.this,
						// LocationDw.class);
						intent.setClass(SpcxActivity.this, PzcxmxActivity.class);
						startActivity(intent);

					}
				});

	}

	// 定义Handler直接用，不需要修改
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 对于ＵＩ的更新，代码只能在这儿进行
			// 接受进程执行完成后的Message，刷新界面ＵＩ
			String jsonTaxPayerDatas = (String) msg.obj;

			if (jsonTaxPayerDatas.equals("-1")) {
				mylist.clear();

				SimpleAdapter mSchedule = new SimpleAdapter(
						SpcxActivity.this, // 没什么解释
						mylist,// 数据来源
						R.layout.activity_spcx_listview,// ListItem的XML实现
						// 动态数组与ListItem对应的子项
						new String[] { "zl", "zb", "pzhm", "zt", "sj" },
						new int[] { R.id.pzzl, R.id.pzzb, R.id.pzhm, R.id.kpje,
								R.id.kpsj });
				myListView.setAdapter(mSchedule);
				Toast toast = Toast.makeText(getApplicationContext(),
						"你所查询的票证不存在！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
				pb1.dismiss();
			} else {
				try {

					JSONArray array = new JSONArray(jsonTaxPayerDatas);
					mylist.clear();
					// 生成动态数组，并且转载数据
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("zl", obj.getString("zl"));
						map.put("zb", obj.getString("zb"));
						map.put("pzhm", obj.getString("pzhm"));
						map.put("sj", obj.getString("sj"));
						map.put("zt", obj.getString("zt"));
						map.put("pzzb_dm", obj.getString("pzzb_dm"));
						mylist.add(map);
					}
					SimpleAdapter mSchedule = new SimpleAdapter(
							SpcxActivity.this, // 没什么解释
							mylist,// 数据来源
							R.layout.activity_spcx_listview,// ListItem的XML实现
							new String[] { "zl", "zb", "pzhm", "zt", "sj",
									"pzzb_dm" }, new int[] { R.id.pzzl,
									R.id.pzzb, R.id.pzhm, R.id.kpje, R.id.kpsj,
									R.id.pzzb_dm });

					myListView.setAdapter(mSchedule);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
				pb1.dismiss();
			}

		}

	};

	// 主进程
	private void processThread() {
		// 构建一个下载进度条
		pb1 = ProgressDialog.show(SpcxActivity.this, "查询票证信息", "正在查询……");

		new Thread() {
			@Override
			public void run() {

				// 把耗时长的代码全放在这儿

				// 耗时代码开始
				// 耗时代码，是根据你所做工作的不同而不同，但在这儿不能有更新UI的代码
				// 本程序中，是查询银行信息，并把ＪＳＯＮ数据转成ArrayList
				String url = HttpUtil.SysPzCx;
				String result = null;
				// 获得请求对象

				// 调用HttpUtil类中的函数，返回HttpPost
				HttpPost req = HttpUtil.getHttpPost(url);

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

				// 需要三个参数，1、票证号码；2、所在税务机关；3、userKey
				params.add(new BasicNameValuePair("pzhm", pzhm.getText()
						.toString()));
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
				// 耗时代码结束
				// 定义一个Ｍessage将查询执行结果返回到Handel,用于写ＵＩ
				Message msg = new Message();
				msg.obj = result;

				handler.sendMessage(msg);
			}
		}.start();
	}

}
