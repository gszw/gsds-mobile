package cn.com.gszw.fragment;

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

import cn.com.gszw.mzgxt.client.FhmxActivity;
import cn.com.gszw.mzgxt.client.FpcxActivity;
import cn.com.gszw.mzgxt.client.GhfbActivity;
import cn.com.gszw.mzgxt.client.R;
import cn.com.gszw.mzgxt.client.RkLjrkqkActivity;
import cn.com.gszw.mzgxt.client.SbLjsbqkActivity;
import cn.com.gszw.mzgxt.client.SpcxActivity;
import cn.com.gszw.mzgxt.client.UpdateActivity;
import cn.com.gszw.mzgxt.client.ZsLjzsqkActivity;
import cn.com.gszw.mzgxt.client.ZtfxActivity;
import cn.com.gszw.mzgxt.util.HttpUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tjfx_Fragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.main_tjfx, container, false);
	}
	
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
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// 初始化本表单数据
			Intent intent = getActivity().getIntent();
			Bundle bundle = intent.getExtras();

			account = bundle.getString("account");// 用户名称
			swjgdm = bundle.getString("swjgdm");// 税务机关代码
			swjgmc = bundle.getString("swjgmc");// 税务机关名称
			username = bundle.getString("username");// 人员姓名

			// 后台返回的人员信息Key，在以后所有调用后台数据时，
			// 必须把这个Key值传给后台，否则，后台返回null
			userKey = bundle.getString("userKey");

			// 管户详细情况
			Button bt4 = (Button) getActivity().findViewById(R.id.xx_gh);
			bt4.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
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
			Button bt5 = (Button) getActivity().findViewById(R.id.xx_sb);
			bt5.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
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
			Button bt6 = (Button) getActivity().findViewById(R.id.xx_zs);
			bt6.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
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
			Button bt7 = (Button) getActivity().findViewById(R.id.xx_rk);
			bt7.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
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
			sj01 = (TextView) getActivity().findViewById(R.id.sj01);
			sj02 = (TextView) getActivity().findViewById(R.id.sj02);
			sj03 = (TextView) getActivity().findViewById(R.id.sj03);
			sj04 = (TextView) getActivity().findViewById(R.id.sj04);
			sj05 = (TextView) getActivity().findViewById(R.id.sj05);
			sj06 = (TextView) getActivity().findViewById(R.id.sj06);
			sj07 = (TextView) getActivity().findViewById(R.id.sj07);
			sj08 = (TextView) getActivity().findViewById(R.id.sj08);
			sj09 = (TextView) getActivity().findViewById(R.id.sj09);
			sj10 = (TextView) getActivity().findViewById(R.id.sj10);
			sj11 = (TextView) getActivity().findViewById(R.id.sj11);
			sj12 = (TextView) getActivity().findViewById(R.id.sj12);
			sj13 = (TextView) getActivity().findViewById(R.id.sj13);
			sj14 = (TextView) getActivity().findViewById(R.id.sj14);

			processThread();

		}
		// 选项菜单被关闭事件，菜单被关闭有三种情形，menu按钮被再次点击、back按钮被点击或者用户选择了某一个菜单项
		@Override
		public void onOptionsMenuClosed(Menu menu) {
			// Toast.makeText(this, "选项菜单关闭了", Toast.LENGTH_LONG).show();
		}

		// 以下代码是退出提示代码段
		private void showTips() {
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
					.setTitle("退出程序").setMessage("您确认要退出吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// pb1.dismiss();
							getActivity().finish();
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
			pb1 = ProgressDialog.show(getActivity(), "查询总体信息", "正在查询……");

			new Thread() {
				@Override
				public void run() {

					Message msg = new Message();
					String jsonTaxPayerDatas = getList();
					// 耗时代码结束
					// 定义一个Ｍessage将查询执行结果返回到Handel,用于写ＵＩ

					msg.obj = jsonTaxPayerDatas;
					handler.sendMessage(msg);
				}
			}.start();
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

		public void onPause() {
			pb1.dismiss();
			super.onPause();
		}


}
