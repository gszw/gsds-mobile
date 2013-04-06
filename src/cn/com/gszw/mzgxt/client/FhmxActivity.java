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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FhmxActivity extends Activity {
	/** Called when the activity is first created. */

	// 以下代码是弹出单选按钮的定义
	private String[] areas = new String[] { "全部(默认)", "按名称", "按编码", "按地址",
			"未申报(不分税种)", "未申报(分税种)", "附近纳税人(定位)" };
	private boolean[] areaState = new boolean[] { true, false, false, false,
			false, false, false };
	private RadioOnClick radioOnClick = new RadioOnClick(1);
	private ListView areaRadioListView;
	Button bt;
	LinearLayout lu;
	private EditText tv_tj = null;
	private static int flag = 0;// 标志位,如果是第一次初始化activity
	// tv_tj显示"请输入条件",不是第一次获得焦点为""
	private int radioindex = 1;
	// ProgressDialog pb2;// 进度条
	ProgressDialog pb1;

	private String zgy;// 如果是专管员，此变量为专管员代码，否则为空

	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			radioindex = index;

			switch (index) {
			case 0:
				bt.setText("全部");
				tv_tj.setHint("全部查找(默认)");
				break;
			case 1:
				bt.setText("名称");
				tv_tj.setHint("请输入[" + areas[index] + "]条件项");
				break;
			case 2:
				bt.setText("编码");
				tv_tj.setHint("请输入[" + areas[index] + "]条件项");
				break;
			case 3:
				bt.setText("地址");
				tv_tj.setHint("请输入[" + areas[index] + "]条件项");
				break;
			case 4:
				bt.setText("未申报");
				tv_tj.setHint("直接点搜索按钮");
				break;
			case 5:
				tv_tj.setHint("直接点搜索按钮");
				bt.setText("未申报");
				break;
			case 6:
				tv_tj.setHint("直接点搜索按钮");
				bt.setText("附近");
				break;
			}
			dialog.dismiss();
			lu.setFocusable(true);
		}
	}

	/**
	 * 单选弹出菜单窗口
	 * 
	 * @author xmz
	 * 
	 */
	class RadioClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog ad = new AlertDialog.Builder(FhmxActivity.this)
					.setTitle("选择查询条件项")
					.setSingleChoiceItems(areas, radioOnClick.getIndex(),
							radioOnClick).create();
			areaRadioListView = ad.getListView();
			ad.show();

		}
	}

	// 声明ListView对象
	// 以下三变量在DataInit中初始化
	private String account;// 用户帐号
	private String swjgdm;// 机关代码
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;
	int PageCount = 0;// 总页数
	int HsCount = 0;// 总户数
	int PageIndex = 1;// 当前页

	TextView txtPageInfo;
	ImageButton btnPageFirst, btnPagePrevious, btnPageNext, btnPageLast;

	ListView myListView;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

	// 以下代码是Hanler示例代码，对于耗时的查询需要用以下代码给出进度条

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fhmx);

		lu = (LinearLayout) findViewById(R.id.tv_LinearLayout);
		bt = (Button) findViewById(R.id.bt_Radio);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.setOnClickListener(new RadioClickListener());
			}
		});
		Button bt1 = (Button) findViewById(R.id.button2);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PageIndex = 1;
				// 隐藏软键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(FhmxActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				v.setEnabled(false);

				DataCount();

				DataLoad();
				v.setEnabled(true);
			}
		});
		tv_tj = (EditText) findViewById(R.id.tv_tj);
		pb1 = new ProgressDialog(FhmxActivity.this);
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");

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

		txtPageInfo = (TextView) findViewById(R.id.txtPageInfo);
		btnPageFirst = (ImageButton) findViewById(R.id.btnPageFirst);
		btnPagePrevious = (ImageButton) findViewById(R.id.btnPagePrevious);
		btnPageNext = (ImageButton) findViewById(R.id.btnPageNext);
		btnPageLast = (ImageButton) findViewById(R.id.btnPageLast);
		myListView = (ListView) findViewById(R.id.PayerListView);

		btnPageFirst.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PageIndex = 1;
				btnPageFirst.setEnabled(false);
				DataLoad();
				btnPageFirst.setEnabled(true);
			}
		});
		btnPagePrevious.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (PageIndex > 1) {
					PageIndex--;
					btnPagePrevious.setEnabled(false);
					DataLoad();
					btnPagePrevious.setEnabled(true);
				}
			}
		});
		btnPageNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (PageIndex < PageCount) {
					PageIndex++;
					v.setEnabled(false);
					DataLoad();
					v.setEnabled(true);
				}
			}
		});
		btnPageLast.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PageIndex = PageCount;
				// System.out.println("pagelast:" + PageIndex);
				btnPageLast.setEnabled(false);
				DataLoad();
				btnPageLast.setEnabled(true);

			}
		});

		// 添加点击
		// myListView
		myListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Bundle bundle = new Bundle();
						Intent intent = new Intent(FhmxActivity.this,
								DjNsryhscxActivity.class);
						bundle.putString("nsrbm", mylist.get(arg2).get("nsrbm"));
						bundle.putString("nsrmc", mylist.get(arg2).get("nsrmc"));
						bundle.putString("nsrnbm",
								mylist.get(arg2).get("nsrnbm"));
						bundle.putString("userKey", userKey);
						intent.putExtras(bundle);

						startActivity(intent);
					}
				});

		// 判断一个人员是不是专管员，如果是则返回该人员代码，否则返回空
		zgy = IfZgy(account);
		DataCount();

		DataLoad();

	}

	private void DataCount() {
		// 网络链接检测
		HttpUtil.HttpTest(FhmxActivity.this);
		pb1.show();
		processThread1();
	}

	private void DataLoad() {
		// 网络链接检测
		HttpUtil.HttpTest(FhmxActivity.this);
		pb1.show();
		processThread();
	}

	void DataPage() {
		if (PageCount < PageIndex) {

		}

	}

	// 判断一个人员是不是专管员，如果是，则返回该人员代码，否则返回空
	private String IfZgy(String dm) {
		String result = "";
		// 后台HttpServlet定义，需要在HttpUtil类中定义
		String url = HttpUtil.GetIfZgy;
		// 获得请求对象

		// 调用HttpUtil类中的函数，返回HttpPost
		HttpPost req = HttpUtil.getHttpPost(url);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("zgy_dm", dm));

		// 设置请求对象的编码
		try {
			req.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			result = HttpUtil.getStringByPost(req);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "{}";
		}

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(result);
			result = jsonObject.getString("Zgy_dm");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	// 根据查询条件，生成查询所必要的Where条件
	private String setSql() {

		// private TextView tv_tj=null;
		// private int radioindex;
		String str;
		// 以下注释暂时取销，为测试方便，正试上线前一定要加上，因为这是人员查询权限的控制
		// 如果是专管员，只能查询自已的管户情况，其他人，按征管职能权限查询

		if (!zgy.equals("")) {
			str = " xx.zgy_dm='" + zgy + "'";
		} else {

			str = " EXISTS (SELECT 1\n" + " FROM T_XT_SWRY_CX CX\n"
					+ " WHERE xx.GLJG_DM=CX.SWJG_DM\n" + " AND CX.SWRY_DM ='"
					+ account + "')";

		}

		SharedPreferences shareMSG = this.getSharedPreferences("xt_cxdy",
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_WRITEABLE);
		String cx = shareMSG.getString("cxdy", null);

		if (cx == null) {
			cx = "TTTFT";
			SharedPreferences.Editor editor = shareMSG.edit();
			editor.putString("cxdy", cx);
			editor.commit();
		}
		if (cx.charAt(0) == 'T') {
			str = str + " and xx.djlx_dm<>'0122' ";
		}
		if (cx.charAt(1) == 'T') {
			str = str + " and xx.djlx_dm<>'0104' ";
		}
		if (cx.charAt(2) == 'T') {
			str = str + " and xx.djlx_dm<>'0129' ";
		}
		if (cx.charAt(3) == 'T') {
			str = str + " and xx.dj_zt<>'30' ";
		}
		if (cx.charAt(4) == 'T') {
			str = str + " and xx.dj_zt not in ('50','51','52') ";
		}

		if (radioindex == 0) {
			str = str;
		} else {
			String ss = tv_tj.getText().toString();
			char leftstr = '!';
			if (ss.length() == 0) {
				leftstr = '!';
			} else {
				leftstr = ss.charAt(0);
			}

			switch (radioindex) {
			case 1:// 按纳税人名称查询

				if (ss.length() > 0) {
					str = str + " and xx.nsr_mc like '%" + ss + "%'";
				}
				break;

			case 2:// 按纳税人编码查询
				if (ss.length() > 0) {
					str = str + " and xx.nsrbm like '%" + ss + "%'";
				}
				break;
			case 3:// 按地址查询
				if (ss.length() > 0) {
					str = str + " and kz.SCJYDZ like '%" + ss + "%'";
				}
				break;

			case 4:// 未申报不分税种
				str = str + " and exists (select 1\n"
						+ "             from t_gs_sb_wsbsj_h w\n"
						+ "              where xx.nsrnbm=w.nsrnbm\n"
						+ "           )";

				break;
			case 5:// 未申报分税种
				str = str + " and exists (select 1\n"
						+ "             from t_gs_sb_wsbsj w\n"
						+ "              where xx.nsrnbm=w.nsrnbm\n"
						+ "           )";
				break;
			}
		}

		return str;
	}

	// 此方法用于与后台交互，查询信息
	public String getList() {
		String result = null;
		// 后台HttpServlet定义，需要在HttpUtil类中定义
		String url = HttpUtil.getNsrList;
		// 获得请求对象

		// 调用HttpUtil类中的函数，返回HttpPost
		HttpPost req = HttpUtil.getHttpPost(url);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("sqlstr", setSql()));

		params.add(new BasicNameValuePair("PageNumber", String
				.valueOf(PageIndex)));
		// 所有后台调用都要传这个参数过去
		params.add(new BasicNameValuePair("userKey", userKey));
		// 设置请求对象的编码
		try {
			req.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			result = HttpUtil.getStringByPost(req);
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// 此方法用于与后台交互，查询银行信息
	public String getCount() {
		String result = null;
		// 后台HttpServlet定义，需要在HttpUtil类中定义
		String url = HttpUtil.getNsrCount;
		// 获得请求对象

		// 调用HttpUtil类中的函数，返回HttpPost
		HttpPost req = HttpUtil.getHttpPost(url);

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("sqlstr", setSql()));
		// 所有后台调用都要传这个参数过去
		params.add(new BasicNameValuePair("userKey", userKey));
		// 设置请求对象的编码
		try {
			req.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			result = HttpUtil.getStringByPost(req);

			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 定义Handler直接用，不需要修改
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			// 对于ＵＩ的更新，代码只能在这儿进行
			// 接受进程执行完成后的Message，刷新界面ＵＩ

			if (msg.arg1 == 1) {
				String jsonPages = (String) msg.obj;

				try {

					JSONObject jsonObject = new JSONObject(jsonPages);
					PageCount = jsonObject.getInt("PageCount");
					HsCount = jsonObject.getInt("HsCount");

					if (HsCount == 0) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"提示" + "没有满足条件的数据！", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						mylist.clear();
						txtPageInfo.setText(PageIndex + "/" + PageCount + "页"
								+ HsCount + "户");
						SimpleAdapter mSchedule = new SimpleAdapter(
								FhmxActivity.this, // 没什么解释
								mylist,// 数据来源
								R.layout.mainitem,// ListItem的XML实现

								// 动态数组与ListItem对应的子项
								new String[] { "nsrbm", "nsrmc", "djzt",
										"sbqk", "nsrnbm" },

								// ListItem的XML文件里面的两个TextView ID
								new int[] { R.id.itemTitle, R.id.itemContent,
										R.id.djzt, R.id.sbqk, R.id.nsrnbm });
						myListView.setAdapter(mSchedule);
					} else {

						txtPageInfo.setText(PageIndex + "/" + PageCount + "页"
								+ HsCount + "户");
					}

					// 生成动态数组，并且转载数据
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast toast = Toast.makeText(getApplicationContext(),
							"自定义位置Toast" + e.toString(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
				// pb2.dismiss();
			} else {

				String jsonTaxPayerDatas = (String) msg.obj;

				if (jsonTaxPayerDatas.equals("-1")) {
					mylist.clear();
					txtPageInfo.setText(PageIndex + "/" + PageCount + "页"
							+ HsCount + "户");
					SimpleAdapter mSchedule = new SimpleAdapter(
							FhmxActivity.this, // 没什么解释
							mylist,// 数据来源
							R.layout.mainitem,// ListItem的XML实现

							// 动态数组与ListItem对应的子项
							new String[] { "nsrbm", "nsrmc", "djzt", "sbqk",
									"nsrnbm" },

							// ListItem的XML文件里面的两个TextView ID
							new int[] { R.id.itemTitle, R.id.itemContent,
									R.id.djzt, R.id.sbqk, R.id.nsrnbm });
					myListView.setAdapter(mSchedule);
					// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
					// pb1.hide();
				} else {
					try {
						txtPageInfo.setText(PageIndex + "/" + PageCount + "页"
								+ HsCount + "户");
						JSONArray array = new JSONArray(jsonTaxPayerDatas);
						mylist.clear();
						// 生成动态数组，并且转载数据
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("nsrbm", obj.getString("nsrbm"));
							map.put("nsrmc", obj.getString("nsrmc"));
							map.put("djzt", obj.getString("djzt"));
							map.put("sbqk", obj.getString("sbqk"));
							map.put("nsrnbm", obj.getString("nsrnbm"));
							mylist.add(map);
						}
						SimpleAdapter mSchedule = new SimpleAdapter(
								FhmxActivity.this, // 没什么解释
								mylist,// 数据来源
								R.layout.mainitem,// ListItem的XML实现

								// 动态数组与ListItem对应的子项
								new String[] { "nsrbm", "nsrmc", "djzt",
										"sbqk", "nsrnbm" },

								// ListItem的XML文件里面的两个TextView ID
								new int[] { R.id.itemTitle, R.id.itemContent,
										R.id.djzt, R.id.sbqk, R.id.nsrnbm });
						// new int[] { R.id.ItemTaxPayerNSRBM,
						// R.id.ItemTaxPayerNSRMC
						// });
						// 添加并且显示

						myListView.setAdapter(mSchedule);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

				}

			}
			pb1.hide();

		}

	};

	// 主进程
	private void processThread() {

		new Thread() {
			@Override
			public void run() {

				Message msg = new Message();

				// handler.sendMessage(msg);
				// 把耗时长的代码全放在这儿

				// 耗时代码开始
				// 耗时代码，是根据你所做工作的不同而不同，但在这儿不能有更新UI的代码
				// 本程序中，是查询银行信息，并把ＪＳＯＮ数据转成ArrayList

				String jsonTaxPayerDatas = getList();
				// 耗时代码结束
				// 定义一个Ｍessage将查询执行结果返回到Handel,用于写ＵＩ

				msg.obj = jsonTaxPayerDatas;

				msg.arg1 = 2;

				handler.sendMessage(msg);
			}
		}.start();
		// pb1.dismiss();
	}

	// 主进程
	private void processThread1() {
		// 构建一个下载进度条
		// pb2 = ProgressDialog.show(MainForm.this, "查询纳税人总户数", "正在查询……");
		new Thread() {
			@Override
			public void run() {
				// 把耗时长的代码全放在这儿
				Message msg = new Message();

				// handler.sendMessage(msg);
				// 耗时代码开始
				// 耗时代码，是根据你所做工作的不同而不同，但在这儿不能有更新UI的代码
				// 本程序中，是查询银行信息，并把ＪＳＯＮ数据转成ArrayList

				String jsonPages = getCount();
				// 耗时代码结束
				// 定义一个Ｍessage将查询执行结果返回到Handel,用于写ＵＩ

				msg.obj = jsonPages;

				msg.arg1 = 1;
				handler.sendMessage(msg);
			}
		}.start();
	}

	// 以下代码是系统主菜单实现模块
	// 点击Menu时，系统调用当前Activity的onCreateOptionsMenu方法，并传一个实现了一个Menu接口的menu对象供你使用
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "税票查询").setIcon(
				R.drawable.menu_001);
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以
		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "发票查询").setIcon(
				R.drawable.menu_002);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "系统设置").setIcon(
				R.drawable.menu_003);
		menu.add(Menu.NONE, Menu.FIRST + 4, 4, "版本更新").setIcon(
				R.drawable.menu_004);
		menu.add(Menu.NONE, Menu.FIRST + 5, 5, "关于..").setIcon(
				R.drawable.menu_005);
		menu.add(Menu.NONE, Menu.FIRST + 6, 6, "退出").setIcon(
				R.drawable.menu_exit);
		// return true才会起作用
		return true;
	}

	// 菜单项被选择事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:

			// 税票查询
			Bundle bundle = new Bundle();
			Intent intent4 = new Intent(FhmxActivity.this, SpcxActivity.class);
			bundle.putString("account", account);
			bundle.putString("swjgdm", swjgdm);
			bundle.putString("userKey", userKey);
			intent4.putExtras(bundle);
			startActivity(intent4);
			break;
		case Menu.FIRST + 2:
			Toast.makeText(this, "此功能正在开发中.....", Toast.LENGTH_LONG).show();
			break;
		case Menu.FIRST + 3:
			// Intent intent = new Intent(FhmxActivity.this, SysTab.class);
			// startActivity(intent);
			break;
		case Menu.FIRST + 4:
			Intent intent1 = new Intent(FhmxActivity.this, UpdateActivity.class);
			startActivity(intent1);
			break;
		case Menu.FIRST + 5:
			Toast.makeText(this, "此功能正在开发中.....", Toast.LENGTH_LONG).show();
			break;
		case Menu.FIRST + 6:
			this.showTips();
			// this.finish();
			break;
		}
		return false;
	}

	// 选项菜单被关闭事件，菜单被关闭有三种情形，menu按钮被再次点击、back按钮被点击或者用户选择了某一个菜单项
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// Toast.makeText(this, "选项菜单关闭了", Toast.LENGTH_LONG).show();
	}

	// 菜单被显示之前的事件
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Toast.makeText(this,
		// "选项菜单显示之前onPrepareOptionsMenu方法会被调用，你可以用此方法来根据打当时的情况调整菜单",
		// Toast.LENGTH_LONG).show();
		// 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用
		return true;
	}

	// 以下代码是退出提示代码段
	private void showTips() {

		pb1.dismiss();
		FhmxActivity.this.finish();
	}

	protected void onPause() {
		pb1.dismiss();
		super.onPause();
	}

}
