package cn.com.gszw.mzgxt.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ZtfxActivity extends Activity {
	// 以下三变量在DataInit中初始化
	private String swjgmc;
	private String username;
	private String account;// 用户帐号
	private String swjgdm;// 机关代码
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;
	TextView sj01;
	TextView sj02;
	TextView sj03;
	TextView sj04;
	TextView sj05;
	TextView sj06;
	TextView sj07;
	TextView sj08;
	TextView sj09;
	TextView sj10;
	TextView sj11;
	TextView sj12;
	TextView sj13;
	TextView sj14;
	ProgressDialog pb1;// 进度条

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ztfx);
		// 初始化本表单数据
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		account = bundle.getString("account");// 用户名称
		swjgdm = bundle.getString("swjgdm");// 税务机关代码
		swjgmc = bundle.getString("swjgmc");// 税务机关名称
		username = bundle.getString("username");// 人员姓名

		// 后台返回的人员信息Key，在以后所有调用后台数据时，
		// 必须把这个Key值传给后台，否则，后台返回null
		userKey = bundle.getString("userKey");

		// 在上方显示纳税人名称和编码

		// 网络链接检测
		HttpUtil.HttpTest(ZtfxActivity.this);

		Button bt1 = (Button) findViewById(R.id.index);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ZtfxActivity.this,
						FhmxActivity.class);

				// 传递用户数据到MainActivity
				Bundle bundle = new Bundle();
				bundle.putString("username", username);// 人员姓名
				bundle.putString("swjgmc", swjgmc);// 所在税务机关
				bundle.putString("account", account);// 帐号
				bundle.putString("swjgdm", swjgdm);// 所在税务机关代码

				bundle.putString("userKey", userKey);// 人员Key
				intent.putExtras(bundle);
				startActivity(intent);
				;
			}
		});
		Button bt2 = (Button) findViewById(R.id.exit);
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ZtfxActivity.this.finish();
			}
		});
		Button bt3 = (Button) findViewById(R.id.ref);
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				processThread();
			}
		});
		// 管户详细情况
		Button bt4 = (Button) findViewById(R.id.xx_gh);
		bt4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ZtfxActivity.this,
						GhfbActivity.class);

				// 传递用户数据到Default_SbMx
				Bundle bundle = new Bundle();
				bundle.putString("username", username);// 人员姓名
				bundle.putString("swjgmc", swjgmc);// 所在税务机关
				bundle.putString("account", account);// 帐号
				bundle.putString("swjgdm", swjgdm);// 所在税务机关代码

				bundle.putString("userKey", userKey);// 人员Key
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 申报明细情况
		Button bt5 = (Button) findViewById(R.id.xx_sb);
		bt5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ZtfxActivity.this,
						SbLjsbqkActivity.class);

				// 传递用户数据到Default_SbMx
				Bundle bundle = new Bundle();
				bundle.putString("username", username);// 人员姓名
				bundle.putString("swjgmc", swjgmc);// 所在税务机关
				bundle.putString("account", account);// 帐号
				bundle.putString("swjgdm", swjgdm);// 所在税务机关代码

				bundle.putString("userKey", userKey);// 人员Key
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 征收明细情况
		Button bt6 = (Button) findViewById(R.id.xx_zs);
		bt6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ZtfxActivity.this,
						ZsLjzsqkActivity.class);

				// 传递用户数据到Default_SbMx
				Bundle bundle = new Bundle();
				bundle.putString("username", username);// 人员姓名
				bundle.putString("swjgmc", swjgmc);// 所在税务机关
				bundle.putString("account", account);// 帐号
				bundle.putString("swjgdm", swjgdm);// 所在税务机关代码

				bundle.putString("userKey", userKey);// 人员Key
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 入库明细情况
		Button bt7 = (Button) findViewById(R.id.xx_rk);
		bt7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ZtfxActivity.this,
						RkLjrkqkActivity.class);

				// 传递用户数据到Default_SbMx
				Bundle bundle = new Bundle();
				bundle.putString("username", username);// 人员姓名
				bundle.putString("swjgmc", swjgmc);// 所在税务机关
				bundle.putString("account", account);// 帐号
				bundle.putString("swjgdm", swjgdm);// 所在税务机关代码

				bundle.putString("userKey", userKey);// 人员Key
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		sj01 = (TextView) findViewById(R.id.sj01);
		sj02 = (TextView) findViewById(R.id.sj02);
		sj03 = (TextView) findViewById(R.id.sj03);
		sj04 = (TextView) findViewById(R.id.sj04);
		sj05 = (TextView) findViewById(R.id.sj05);
		sj06 = (TextView) findViewById(R.id.sj06);
		sj07 = (TextView) findViewById(R.id.sj07);
		sj08 = (TextView) findViewById(R.id.sj08);
		sj09 = (TextView) findViewById(R.id.sj09);
		sj10 = (TextView) findViewById(R.id.sj10);
		sj11 = (TextView) findViewById(R.id.sj11);
		sj12 = (TextView) findViewById(R.id.sj12);
		sj13 = (TextView) findViewById(R.id.sj13);
		sj14 = (TextView) findViewById(R.id.sj14);

		processThread();

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
			Intent intent4 = new Intent(ZtfxActivity.this, SpcxActivity.class);
			bundle.putString("account", account);
			bundle.putString("swjgdm", swjgdm);
			bundle.putString("userKey", userKey);
			intent4.putExtras(bundle);
			startActivity(intent4);
			break;
		case Menu.FIRST + 2:
			// 发票查询
			Bundle bundle1 = new Bundle();
			Intent intent2 = new Intent(ZtfxActivity.this, FpcxActivity.class);
			bundle1.putString("account", account);
			bundle1.putString("swjgdm", swjgdm);
			bundle1.putString("userKey", userKey);
			intent2.putExtras(bundle1);
			startActivity(intent2);
			break;
		case Menu.FIRST + 3:
			// Intent intent = new Intent(ZtfxActivity.this, SysTab.class);
			// startActivity(intent);
			break;
		case Menu.FIRST + 4:
			Intent intent1 = new Intent(ZtfxActivity.this, UpdateActivity.class);
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

	// 以下代码是退出提示代码段
	private void showTips() {
		AlertDialog alertDialog = new AlertDialog.Builder(ZtfxActivity.this)
				.setTitle("退出程序").setMessage("您确认要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// pb1.dismiss();
						ZtfxActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}

	// 定义Handler直接用，不需要修改
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			// 对于ＵＩ的更新，代码只能在这儿进行
			// 接受进程执行完成后的Message，刷新界面ＵＩ
			String jsonPages = (String) msg.obj;
			try {

				JSONObject jsonObject = new JSONObject(jsonPages);
				sj01.setText(jsonObject.getString("sj01"));
				sj02.setText(jsonObject.getString("sj02"));
				sj03.setText(jsonObject.getString("sj03"));
				sj04.setText(jsonObject.getString("sj04"));
				sj05.setText(jsonObject.getString("sj05"));
				sj06.setText(jsonObject.getString("sj06"));
				sj07.setText(jsonObject.getString("sj07"));
				sj08.setText(jsonObject.getString("sj08"));
				sj09.setText(jsonObject.getString("sj09"));
				sj10.setText(jsonObject.getString("sj10"));
				sj11.setText(jsonObject.getString("sj11"));
				sj12.setText(jsonObject.getString("sj12"));
				sj13.setText(jsonObject.getString("sj13"));
				sj14.setText(jsonObject.getString("sj14"));

				// 生成动态数组，并且转载数据
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
			// pb2.dismiss();
			pb1.hide();

		}

	};

	// 主进程
	private void processThread() {
		pb1 = ProgressDialog.show(ZtfxActivity.this, "查询总体信息", "正在查询……");

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
				handler.sendMessage(msg);
			}
		}.start();
		// pb1.dismiss();
	}

	// 此方法用于与后台交互，查询信息
	public String getList() {
		String result = null;
		// 后台HttpServlet定义，需要在HttpUtil类中定义
		String url = HttpUtil.GetDefault;
		// 获得请求对象

		// 调用HttpUtil类中的函数，返回HttpPost
		HttpPost req = HttpUtil.getHttpPost(url);

		// 设置表单参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("userKey", userKey));
		params.add(new BasicNameValuePair("qt", ""));

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

	protected void onPause() {
		pb1.dismiss();
		super.onPause();
	}

}
