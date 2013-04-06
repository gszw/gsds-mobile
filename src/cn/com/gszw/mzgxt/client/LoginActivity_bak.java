package cn.com.gszw.mzgxt.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import cn.com.gszw.mzgxt.util.HttpUtil;

public class LoginActivity_bak extends Activity {
	private Button concelBtn, loginBtn;
	private EditText account_edit, password_edit;
	private CheckBox cb;// 记住密码
	private int flag = 0;// 1: 自动登陆
	private CheckBox auto;// 自动登陆
	private String account = "";
	private String password = "";
	private String username = "";
	private String swjgdm = "";
	private String swjgmc = "";
	private String xb = "";
	ProgressDialog pb1;
	private String userKey = "";// 后台传出的人员Key信息

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 布局
		setContentView(R.layout.login_bak);
		// 设置渐变颜色
		GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM,
				new int[] { R.color.c3red, R.color.c7yellow });
		getWindow().setBackgroundDrawable(grad);

		// 网络链接检测
		HttpUtil.HttpTest(LoginActivity_bak.this);
		pb1 = new ProgressDialog(LoginActivity_bak.this);
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("正在验证用户信息，请稍候！");
		// 获得组件信息
		concelBtn = (Button) findViewById(R.id.cancel_bt);
		loginBtn = (Button) findViewById(R.id.login_bt);
		account_edit = (EditText) findViewById(R.id.account_edit);

		password_edit = (EditText) findViewById(R.id.password_edit);

		cb = (CheckBox) findViewById(R.id.cbRemember);
		auto = (CheckBox) findViewById(R.id.auto_login);
		checkIfRemember();// 检查是否记录密码，自动登陆
		// 以下两行，王朝玉增加，目的是为了调试方便，在软件上线前应取

		// 设置登录按钮监听
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (validate()) {
					if (login()) {
						LoadMain();
					}
				}
			}

		});
		// 设置取消按钮监听
		concelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pb1.dismiss();
				finish();

			}

		});

	}

	// 调动主界面
	private void LoadMain() {
		Intent intent = new Intent(LoginActivity_bak.this,
				MainActivity_bak.class);
		// 传递用户数据到MainActivity
		Bundle bundle = new Bundle();
		bundle.putString("username", username);// 人员姓名
		bundle.putString("swjgmc", swjgmc);// 所在税务机关
		bundle.putString("account", account);// 帐号
		bundle.putString("swjgdm", swjgdm);// 所在税务机关代码
		bundle.putString("xb", xb);// 性别

		bundle.putString("userKey", userKey);// 人员Key
		intent.putExtras(bundle);
		startActivity(intent);
		pb1.dismiss();
		finish();
	}

	// 登录
	public boolean login() {
		account = account_edit.getText().toString();
		password = password_edit.getText().toString();

		// 网络链接检测
		HttpUtil.HttpTest(LoginActivity_bak.this);

		pb1.show();
		String result = queryUser(account, password);// 返回JSON格式的字符串
		pb1.hide();

		if ((result == null) || result.equals("")) {
			showDialog("用户不存在或是密码错误！");
			return false;
		} else if (result.equals("net_error")) {
			showDialog("网络异常！");
			return false;
		} else {
			// System.out.println(result + "*************************");
			try {
				JSONObject obj = new JSONObject(result);
				account = obj.getString("account");
				password = obj.getString("password");
				username = obj.getString("username");
				swjgdm = obj.getString("swjgdm");
				swjgmc = obj.getString("swjgmc");
				userKey = obj.getString("userKey");
				xb = obj.getString("Xb");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 记住密码
			if (cb.isChecked()) {
				// 自动登陆
				if (auto.isChecked()) {
					flag = 1;
				}
				saveUserMSG();
			}
			// 如果手机已注册，但没有审批，给出提示
			if (account.equals("-1")) {
				showDialog("您的手机已注注册，正在等待管理员审批！！");
				return false;
			}
			// 如果手机还没有注册，需要先注册
			if (account.equals("-2")) {

				// Intent intent = new Intent(LoginActivity.this,
				// Login_Zc.class);
				Intent intent = new Intent(LoginActivity_bak.this,
						MainActivity_bak.class);
				Bundle bundle = new Bundle();

				bundle.putString("account", account_edit.getText().toString());// 帐号
				bundle.putString("telId", GetsjId());// 手机Id
				bundle.putString("userKey", userKey);// 人员Key
				intent.putExtras(bundle);
				startActivity(intent);
				// 验证，
				return false;
			}

			if ((swjgmc == null) || swjgmc.equals("")) {
				showDialog("用户不存在或是密码错误");
				return false;
			}
			return true;
		}
	}

	// 记住用户信息
	public void saveUserMSG() {
		// 共享信息
		SharedPreferences shareMSG = this.getSharedPreferences("user_msg",
				MODE_WORLD_WRITEABLE);
		// SharedPreferences shareMSG = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = shareMSG.edit();
		editor.putInt("flag", flag);
		editor.putString("account", account);
		editor.putString("username", username);
		editor.putString("password", password);
		editor.putString("swjgdm", swjgdm);
		editor.putString("swjgmc", swjgmc);
		editor.commit();
	}

	// 读取用户信息
	public void checkIfRemember() {
		SharedPreferences shareMSG = this.getSharedPreferences("user_msg",
				MODE_WORLD_WRITEABLE);
		// SharedPreferences shareMSG = getPreferences(MODE_PRIVATE);
		String account = shareMSG.getString("account", null);// 帐号
		int flag = shareMSG.getInt("flag", 0);// 帐号
		if (account != null && !account.equals("")) {
			account_edit.setText(shareMSG.getString("account", null));
			password_edit.setText(shareMSG.getString("password", null));
		}
		if (flag == 1) {
			if (validate()) {
				if (login()) {
					LoadMain();
				}
			}
		}
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

	// 对用户账户和密码验证
	private boolean validate() {
		account = account_edit.getText().toString();
		password = password_edit.getText().toString();
		if (account == null || account.equals("")) {
			showDialog("账户不能为空！");
			return false;
		}
		if (password == null || password.equals("")) {
			showDialog("密码不能为空！");
			return false;
		}

		return true;
	}

	// 查询用户
	public String queryUser(String account, String password) {
		// String queryString = "account="+account+"&password="+password;

		String url = HttpUtil.loginURL;
		String result = null;
		HttpPost req = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("telId", GetsjId()));
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

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Intent intent = new Intent(LoginActivity.this,
		// ExitSystemActivity.class);
		Intent intent = new Intent(LoginActivity_bak.this,
				MainActivity_bak.class);
		startActivity(intent);
		return false;
	}

	// 读取本机手机唯一标识码
	public String GetsjId() {
		// 读取手机号
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		String tel = tm.getLine1Number() + "::" + tm.getSimSerialNumber();
		return tel;
	}

}