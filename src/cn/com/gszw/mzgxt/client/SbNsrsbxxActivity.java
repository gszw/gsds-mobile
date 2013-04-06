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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SbNsrsbxxActivity extends Activity {
	// 以下三变量在DataInit中初始化
	private String nsrbm;
	private String nsrmc;
	private String nsrnbm;
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;
	ProgressDialog pb1;
	ListView myListView;

	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sbnsrsbxx);
		// 在这儿只需要调用这个方法就可以了

		DataInit();

	}

	void DataInit() {
		// 初始化本表单数据
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		nsrbm = bundle.getString("nsrbm");
		nsrmc = bundle.getString("nsrmc");
		nsrnbm = bundle.getString("nsrnbm");// 用户名称
		// 后台返回的人员信息Key，在以后所有调用后台数据时，
		// 必须把这个Key值传给后台，否则，后台返回null
		userKey = bundle.getString("userKey");
		myListView = (ListView) findViewById(R.id.sb_list);
		// 添加点击
		// 在上方显示纳税人名称和编码jk
		TextView v_nsrbm = (TextView) findViewById(R.id.jk_nsrbm);
		v_nsrbm.setText("纳税人编码：" + nsrbm);
		TextView v_nsrmc = (TextView) findViewById(R.id.jk_nsrmc);
		v_nsrmc.setText("纳税人名称：" + nsrmc);

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
						bundle.putString("nsrnbm", nsrnbm);
						bundle.putString("nsrmc", nsrmc);
						bundle.putString("nsrbm", nsrbm);
						bundle.putString("title", "纳税人申报明细信息");
						bundle.putString("url", HttpUtil.IpUrl
								+ "/server/nsrsbxx.jsp");
						bundle.putString("OtherTj", mylist.get(arg2)
								.get("sbrq"));
						intent.putExtras(bundle);
						// intent.setClass(NsrxxActivity.this,
						// LocationDw.class);
						intent.setClass(SbNsrsbxxActivity.this,
								PublicActivity.class);
						startActivity(intent);
					}
				});
		processThread();
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
						SbNsrsbxxActivity.this, // 没什么解释
						mylist,// 数据来源
						R.layout.activity_sbnsrsbxx_listview,// ListItem的XML实现
						// 动态数组与ListItem对应的子项
						new String[] { "sbrq", "sbpzs", "sbsz", "sbje", "wkpje" },
						new int[] { R.id.sbrq, R.id.sbpzs, R.id.sbsz,
								R.id.sbje, R.id.wkpje });
				myListView.setAdapter(mSchedule);
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
						map.put("sbrq", obj.getString("sbrq"));
						map.put("sbpzs", obj.getString("sbpzs"));
						map.put("sbsz", obj.getString("sbsz"));
						map.put("sbje", obj.getString("sbje"));
						map.put("wkpje", obj.getString("wkpje"));

						mylist.add(map);
					}
					SimpleAdapter mSchedule = new SimpleAdapter(
							SbNsrsbxxActivity.this, // 没什么解释
							mylist,// 数据来源
							R.layout.activity_sbnsrsbxx_listview,// ListItem的XML实现

							// 动态数组与ListItem对应的子项
							new String[] { "sbrq", "sbpzs", "sbsz", "sbje",
									"wkpje" }, new int[] { R.id.sbrq,
									R.id.sbpzs, R.id.sbsz, R.id.sbje,
									R.id.wkpje });
					// new int[] { R.id.ItemTaxPayerNSRBM,
					// R.id.ItemTaxPayerNSRMC
					// });
					// 添加并且显示

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
		pb1 = ProgressDialog.show(SbNsrsbxxActivity.this, "查询纳税人信息", "正在查询……");

		new Thread() {
			@Override
			public void run() {

				// 把耗时长的代码全放在这儿

				// 耗时代码开始
				// 耗时代码，是根据你所做工作的不同而不同，但在这儿不能有更新UI的代码
				// 本程序中，是查询银行信息，并把ＪＳＯＮ数据转成ArrayList
				String url = HttpUtil.GetSbList;
				String result = null;
				// 获得请求对象

				// 调用HttpUtil类中的函数，返回HttpPost
				HttpPost req = HttpUtil.getHttpPost(url);

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("nsrnbm", nsrnbm));

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
