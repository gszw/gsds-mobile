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

import cn.com.gszw.mzgxt.util.HttpHelper;
import cn.com.gszw.mzgxt.util.HttpUtil;
import cn.com.gszw.mzgxt.util.MD5EncryptUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String swrydm, swrymc, xb, swjgdm, swjgmc, imei, passwrod, fhjg,
			userKey, autologin, remember, result,imsi;
	private Button btnLogin; // 登录按钮
	private EditText edtSwrydm, edtPassword;
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	private CheckBox chkAutoLogin, chkRemember;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 布局
		setContentView(R.layout.activity_login);
		// 获得组件信息
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyLog().build());
		}
		inialization();

		getRememberInfo();
		edtSwrydm.setText(swrydm);
		edtPassword.setText(passwrod);
		if ("0".equals(autologin)) {
			validateYHYZ();
		} else {
			if ("0".equals(remember)) {
				chkRemember.setChecked(true);
				chkAutoLogin.setChecked(false);
			}
		}

	}

	private void inialization() {
		btnLogin = (Button) findViewById(R.id.btnLogin);
		edtSwrydm = (EditText) findViewById(R.id.swrydm);
		edtPassword = (EditText) findViewById(R.id.password);
		chkAutoLogin = (CheckBox) findViewById(R.id.chkAutologin);
		chkRemember = (CheckBox) findViewById(R.id.chkRemember);
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		imsi=tm.getSubscriberId();
		imei = tm.getDeviceId();
		// 设置登录按钮监听
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				validateYHYZ();
			}

		});

		// 设置记住密码按钮改变时监听
		chkAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (chkAutoLogin.isChecked()) {
					chkRemember.setChecked(true);
				}
			}
		});
		chkRemember.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (chkRemember.isChecked()) {
					chkAutoLogin.setChecked(true);
				} else {
					chkAutoLogin.setChecked(false);
				}
			}
		});
	}

	/**
	 * 
	 * 验证登录用户名和密码是否正确，如果正确获取税务人员相关信息
	 * 
	 * @author 赵帆
	 * @date 2013-3-28
	 */
	private void validateYHYZ() {
		swrydm = edtSwrydm.getText().toString().trim();
		passwrod = edtPassword.getText().toString().trim();
		if ("".equals(swrydm) || "".equals(passwrod)) {
			showDialog("账户或密码不能为空");
		} else {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("swrydm", swrydm));
			params.add(new BasicNameValuePair("password", MD5EncryptUtils
					.MD5Encode(passwrod)));
			params.add(new BasicNameValuePair("imsi", imsi));
			params.add(new BasicNameValuePair("imei", imei));
			params.add(new BasicNameValuePair("telId", ""));
			try {
				result = HttpHelper.getStringByPost(LoginActivity.this,
						"LoginServlet", params);
				JSONObject mJsonObject = new JSONObject(result);
				if (mJsonObject != null) {
					fhjg = mJsonObject.getString("fhjg");
					// 用户信息及手机信息验证成功，进入系统
					if ("000".equals(fhjg)) {
						swrymc = mJsonObject.getString("swrymc");
						swjgdm = mJsonObject.getString("swjgdm");
						swjgmc = mJsonObject.getString("swjgmc");
						xb = mJsonObject.getString("xb");
						userKey = mJsonObject.getString("userKey");
						if (chkAutoLogin.isChecked()) {
							autologin = "0";
							remember = "0";
						} else {
							autologin = "1";
							if (chkRemember.isChecked()) {
								remember = "0";
							} else {
								remember = "1";
							}
						}
						setrememberInfo();
						Bundle bundle = new Bundle();
						bundle.putString("swrydm", swrydm);
						bundle.putString("swrymc", swrymc);
						bundle.putString("swjgdm", swjgdm);
						bundle.putString("swjgmc", swjgmc);
						bundle.putString("xb", xb);
						bundle.putString("userKey", userKey);
						startActivity(new Intent(LoginActivity.this,
								MainActivity.class).putExtras(bundle));
						LoginActivity.this.finish();
					}
					// 用户信息验证成功，但手机信息未通过审核，给出相应提示
					else if ("010".equals(fhjg)) {
						Toast.makeText(LoginActivity.this,
								"手机审核校验失败，请等待管理员审核！", Toast.LENGTH_LONG).show();
					}
					// 用户信息验证成功，但手机信息未注册，给出相应提示
					else if ("011".equals(fhjg)) {
						params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("swrydm", swrydm));
						params.add(new BasicNameValuePair("imsi", imsi));
						params.add(new BasicNameValuePair("imei", imei));
						result = HttpHelper.getStringByPost(LoginActivity.this,
								"Sjxxzc", params);
						mJsonObject = new JSONObject(result);
						if ("0110".equals(mJsonObject.getString("fhjg"))) {
							Toast.makeText(LoginActivity.this,
									"手机未成功注册，已将手机校验信息发送至管理员处审核！",
									Toast.LENGTH_LONG).show();
						} else if ("0111".equals(mJsonObject.getString("fhjg"))) {
							Toast.makeText(LoginActivity.this,
									"手机未成功注册，出现未知异常，请联系维护人员！",
									Toast.LENGTH_LONG).show();
						}
					}
					// 用户信息验证失败，税务人员编码或密码错误
					else if ("001".equals(fhjg)) {
						Toast.makeText(LoginActivity.this, "税务人员编码或密码错误！",
								Toast.LENGTH_LONG).show();
					}
					// 出现未知错误，给出未知错误提示
					else if ("999".equals(fhjg)) {
						Toast.makeText(LoginActivity.this, "系统未知错误，请报告至维护人员！",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(LoginActivity.this,
								mJsonObject.getString("fhjg"),
								Toast.LENGTH_LONG).show();
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
	}

	// 保存用户信息，用于实现自动登录功能
	private void setrememberInfo() {
		mSharedPreferences = getSharedPreferences("applicationInfo",
				Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mEditor.putString("password", passwrod);
		mEditor.putString("swrydm", swrydm);
		mEditor.putString("swrymc", swrymc);
		mEditor.putString("swjgdm", swjgdm);
		mEditor.putString("swjgmc", swjgmc);
		mEditor.putString("xb", xb);
		mEditor.putString("autologin", autologin);
		mEditor.putString("remember", remember);
		mEditor.commit();
	}

	// 获取用户信息，用于实现自动登录功能
	private void getRememberInfo() {
		mSharedPreferences = getSharedPreferences("applicationInfo",
				Context.MODE_PRIVATE);
			passwrod = mSharedPreferences.getString("password", null);
			swrydm = mSharedPreferences.getString("swrydm", null);
			swrymc = mSharedPreferences.getString("swrymc", null);
			swjgdm = mSharedPreferences.getString("swjgdm", null);
			swjgmc = mSharedPreferences.getString("swjgmc", null);
			xb = mSharedPreferences.getString("xb", null);
			autologin = mSharedPreferences.getString("autologin", null);
			remember = mSharedPreferences.getString("remember", null);

	}

	// 显示提示对话框，提示信息
	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		AlertDialog alert = builder.create();
		alert.show();

	}
}