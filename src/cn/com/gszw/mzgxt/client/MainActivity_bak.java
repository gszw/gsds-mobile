package cn.com.gszw.mzgxt.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.util.HttpUtil;
import cn.com.gszw.mzgxt.util.NetworkTool;

public class MainActivity_bak extends Activity {
	// 以下三变量在DataInit中初始化
	private String swjgmc;
	private String username;
	private String account;// 用户帐号
	private String swjgdm;// 机关代码
	// 后台返回的人员信息Key，在以后所有调用后台数据时，
	// 必须把这个Key值传给后台，否则，后台返回null
	private String userKey;
	// 功能图片
	int[] gn_images = { R.drawable.main_01, R.drawable.main_02,
			R.drawable.main_03, R.drawable.main_04, R.drawable.main_05,
			R.drawable.main_06 };
	// 功能名称
	int[] gn_names = { R.string.main_01, R.string.main_02, R.string.main_03,
			R.string.main_04, R.string.main_05, R.string.main_06 };

	// 以下几个变量是软件版本更新时用到的
	private static final String TAG = "Update";
	public ProgressDialog pBar;
	private Handler handler = new Handler();
	private int newVerCode = 0;
	private String newVerName = "";
	private int i_count;

	// 实现按钮功能
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_bak);
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

		// 获得纳税人信息
		GridView gn_grid = (GridView) findViewById(R.id.gn_grid);

		// 显示标题
		/*
		 * TextView v_title = (TextView) findViewById(R.id.web_title);
		 * 
		 * String welcome = getString(R.string.welcome); String man =
		 * getString(R.string.man); String woman = getString(R.string.woman);
		 * 
		 * if ("1".equals(bundle.getString("xb"))) { v_title.setText(welcome +
		 * username + man); } else { v_title.setText(welcome + username +
		 * woman); }
		 * 
		 * // 在上方显示纳税人名称和编码 String location = getString(R.string.location);
		 * TextView v_nsrbm = (TextView) findViewById(R.id.web_nsrbm);
		 * v_nsrbm.setText(location + swjgmc);
		 */
		// 显示登陆人员名称
		TextView v_username = (TextView) findViewById(R.id.title_pt);
		v_username.setText(username);

		// 实现按钮功能
		Button bt1 = (Button) findViewById(R.id.reset);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});

		Button bt2 = (Button) findViewById(R.id.update);
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent8 = new Intent(MainActivity_bak.this,
						UpdateActivity.class);
				startActivity(intent8);
			}
		});

		Button bt3 = (Button) findViewById(R.id.relogin);
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity_bak.this,
						LoginActivity.class));
				MainActivity_bak.this.finish();
			}
		});

		Button bt4 = (Button) findViewById(R.id.about);
		bt4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});

		Button bt5 = (Button) findViewById(R.id.exit);
		bt5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MainActivity_bak.this.showTips();
			}
		});

		// 创建适配器
		SimpleAdapter adapter = new SimpleAdapter(this, generateGridData(),
				R.layout.gn_grid, new String[] { "image", "name" }, new int[] {
						R.id.gn_image, R.id.gn_name });
		gn_grid.setAdapter(adapter);
		gn_grid.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 网络链接检测
				HttpUtil.HttpTest(MainActivity_bak.this);

				switch (arg2) {

				case 0:// 分户明细
					Intent intent = new Intent(MainActivity_bak.this,
							FhmxActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username", username);// 人员姓名
					bundle.putString("swjgmc", swjgmc);// 所在税务机关
					bundle.putString("account", account);// 帐号
					bundle.putString("swjgdm", swjgdm);// 所在税务机关代码
					bundle.putString("userKey", userKey);// 人员Key
					intent.putExtras(bundle);
					startActivity(intent);
					;
					break;

				case 1:// 总体分析
					Intent intent1 = new Intent(MainActivity_bak.this,
							ZtfxActivity.class);

					// 传递用户数据到MainActivity
					Bundle bundle1 = new Bundle();
					bundle1.putString("username", username);// 人员姓名
					bundle1.putString("swjgmc", swjgmc);// 所在税务机关
					bundle1.putString("account", account);// 帐号
					bundle1.putString("swjgdm", swjgdm);// 所在税务机关代码

					bundle1.putString("userKey", userKey);// 人员Key
					intent1.putExtras(bundle1);
					startActivity(intent1);
					break;

				case 2:// 发票查询
						// 发票查询
					Bundle bundle9 = new Bundle();
					Intent intent9 = new Intent(MainActivity_bak.this,
							FpcxActivity.class);
					bundle9.putString("account", account);
					bundle9.putString("swjgdm", swjgdm);
					bundle9.putString("userKey", userKey);
					intent9.putExtras(bundle9);
					startActivity(intent9);
					break;

				case 3:// 税票查询
					Bundle bundle5 = new Bundle();
					Intent intent5 = new Intent(MainActivity_bak.this,
							SpcxActivity.class);
					bundle5.putString("account", account);
					bundle5.putString("swjgdm", swjgdm);
					bundle5.putString("userKey", userKey);
					intent5.putExtras(bundle5);
					startActivity(intent5);
					break;

				case 4:// 一户式查询
					Intent intent4 = new Intent(MainActivity_bak.this,
							DhcxActivity.class);

					// 传递用户数据到Default_SbMx
					Bundle bundle4 = new Bundle();
					bundle4.putString("username", username);// 人员姓名
					bundle4.putString("swjgmc", swjgmc);// 所在税务机关
					bundle4.putString("account", account);// 帐号
					bundle4.putString("swjgdm", swjgdm);// 所在税务机关代码

					bundle4.putString("userKey", userKey);// 人员Key
					intent4.putExtras(bundle4);
					startActivity(intent4);
					break;
				case 5:// 管户分布
					Intent intent2 = new Intent(MainActivity_bak.this,
							GhfbActivity.class);

					// 传递用户数据到Default_SbMx
					Bundle bundle2 = new Bundle();
					bundle2.putString("username", username);// 人员姓名
					bundle2.putString("swjgmc", swjgmc);// 所在税务机关
					bundle2.putString("account", account);// 帐号
					bundle2.putString("swjgdm", swjgdm);// 所在税务机关代码

					bundle2.putString("userKey", userKey);// 人员Key
					intent2.putExtras(bundle2);
					startActivity(intent2);

					break;

				/*
				 * case 6: // 版本更新 Intent intent8 = new
				 * Intent(MainActivity.this, UpdateActivity.class);
				 * startActivity(intent8); break;
				 * 
				 * case 7: MainActivity.this.showTips(); break;
				 */
				}

			}

		});
		// 判断系统当前版本是不是最新版本，如果不是，需要在线更新
		// if (getServerVerCode()) {
		// int vercode = Config.getVerCode(this);
		//
		// if (newVerCode > vercode) {
		// doNewVersionUpdate();
		// }
		// }
	}

	// 生成Gridview数据
	public List<HashMap<String, Object>> generateGridData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		int rowNum = gn_images.length;
		for (int i = 0; i < rowNum; i++) {
			// 把每一个功能菜单Gridiew存在hashmap中
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", gn_images[i]);
			map.put("name", this.getResources().getString(gn_names[i]));
			list.add(map);
		}
		return list;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.showTips();
			return false;
		}
		return false;
	}

	// 以下代码是退出提示代码段
	private void showTips() {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_bak.this)
				.setTitle("退出程序").setMessage("您确认要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// pb1.dismiss();
						MainActivity_bak.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}

	/*
	 * // 以下代码是系统主菜单实现模块 //
	 * 点击Menu时，系统调用当前Activity的onCreateOptionsMenu方法，并传一个实现了一个Menu接口的menu对象供你使用
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
	 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定 4、文本，菜单的显示文本
	 * 
	 * menu.add(Menu.NONE, Menu.FIRST + 1, 1, "税票查询").setIcon(
	 * R.drawable.menu_001); // setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以 //
	 * android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的 menu.add(Menu.NONE, Menu.FIRST + 2,
	 * 2, "发票查询").setIcon( R.drawable.menu_002); menu.add(Menu.NONE, Menu.FIRST
	 * + 3, 3, "系统设置").setIcon( R.drawable.menu_003); menu.add(Menu.NONE,
	 * Menu.FIRST + 4, 4, "重新登录").setIcon( R.drawable.menu_004);
	 * menu.add(Menu.NONE, Menu.FIRST + 5, 5, "关于..").setIcon(
	 * R.drawable.menu_005); menu.add(Menu.NONE, Menu.FIRST + 6, 6,
	 * "退出").setIcon( R.drawable.menu_exit); // return true才会起作用 return true; }
	 */

	/*
	 * // 菜单项被选择事件
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case Menu.FIRST + 1:
	 * 
	 * // 税票查询 Bundle bundle = new Bundle(); Intent intent4 = new
	 * Intent(MainActivity.this, SpcxActivity.class);
	 * bundle.putString("account", account); bundle.putString("swjgdm", swjgdm);
	 * bundle.putString("userKey", userKey); intent4.putExtras(bundle);
	 * startActivity(intent4); break; case Menu.FIRST + 2: // 发票查询 Bundle
	 * bundle1 = new Bundle(); Intent intent2 = new Intent(MainActivity.this,
	 * FpcxActivity.class); bundle1.putString("account", account);
	 * bundle1.putString("swjgdm", swjgdm); bundle1.putString("userKey",
	 * userKey); intent2.putExtras(bundle1); startActivity(intent2); break;
	 * 
	 * case Menu.FIRST + 3: // Intent intent = new Intent(MainActivity.this,
	 * SysTab.class); // startActivity(intent); break; case Menu.FIRST + 4:
	 * SharedPreferences shareMSG = this.getSharedPreferences("user_msg",
	 * MODE_WORLD_WRITEABLE); // SharedPreferences shareMSG =
	 * getPreferences(MODE_PRIVATE); SharedPreferences.Editor editor =
	 * shareMSG.edit(); editor.putInt("flag", 0); editor.commit();
	 * startActivity(new Intent(MainActivity.this, LoginActivity_bak.class));
	 * MainActivity.this.finish(); break;
	 * 
	 * case Menu.FIRST + 5: Toast.makeText(this, "此功能正在开发中.....",
	 * Toast.LENGTH_LONG).show(); break; case Menu.FIRST + 6: this.showTips();
	 * // this.finish(); break; } return false; }
	 * 
	 * // 选项菜单被关闭事件，菜单被关闭有三种情形，menu按钮被再次点击、back按钮被点击或者用户选择了某一个菜单项
	 * 
	 * @Override public void onOptionsMenuClosed(Menu menu) { //
	 * Toast.makeText(this, "选项菜单关闭了", Toast.LENGTH_LONG).show(); }
	 * 
	 * // 菜单被显示之前的事件
	 * 
	 * @Override public boolean onPrepareOptionsMenu(Menu menu) { //
	 * Toast.makeText(this, //
	 * "选项菜单显示之前onPrepareOptionsMenu方法会被调用，你可以用此方法来根据打当时的情况调整菜单", //
	 * Toast.LENGTH_LONG).show(); //
	 * 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用 return true; }
	 */
	// 以下代码是软件版本更新代码，在人员登陆成功后，要验证软件版本，如果不是最新，需要更新
	private boolean getServerVerCode() {

		try {
			// System.out.println("Begin getServerVerCode!=2");

			String verjson = NetworkTool.getContent(Config.UPDATE_SERVER
					+ Config.UPDATE_VERJSON);
			if (verjson != null && !verjson.equals("net_error")) {
				JSONArray array = new JSONArray(verjson);
				if (array.length() > 0) {
					JSONObject obj = array.getJSONObject(0);
					try {
						newVerCode = Integer.parseInt(obj.getString("verCode"));

						newVerName = obj.getString("verName");
						// System.out.println("");
					} catch (Exception e) {
						newVerCode = -1;
						newVerName = "";
						return false;
					}
				}
			} else {
				// showDialog("检查版本信息失败，网络异常！");
			}
			// System.out.println("getServerVerCode:" + verjson);

		} catch (Exception e) {
			// showDialog("检查版本信息失败，网络异常！");
			MainActivity_bak.this.finish();
			;

		}

		return true;
	}

	private void doNewVersionUpdate() {
		int verCode = Config.getVerCode(this);
		String verName = Config.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(", 请你更新！！");
		Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								i_count = 0;
								// 创建ProgressDialog对象
								pBar = new ProgressDialog(MainActivity_bak.this);
								// 设置进度条风格，风格为长形
								pBar.setProgressStyle

								(ProgressDialog.STYLE_HORIZONTAL);
								// 设置ProgressDialog 标题
								pBar.setTitle("提示");
								// 设置ProgressDialog 提示信息
								pBar.setMessage("正在下载新版本，请稍等...");
								// 设置ProgressDialog 标题图标
								// pBar.setIcon(R.drawable.img2);
								// 设置ProgressDialog 进度条进度
								pBar.setProgress(1300);
								// 设置ProgressDialog 的进度条是否不明确
								pBar.setIndeterminate(false);
								// 设置ProgressDialog 是否可以按退回按键取消
								pBar.setCancelable(true);
								// 让ProgressDialog显示
								pBar.show();
								downFile(Config.UPDATE_SERVER
										+ Config.UPDATE_APKNAME);
							}
						}).create();
		// 创建 // 显示对话框
		dialog.show();
	}

	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								Config.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);

							count += ch;
							pBar.setProgress(i_count++);
							if (length > 0) {

							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	boolean isNetworkAvailable(Activity mActivity) {
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
