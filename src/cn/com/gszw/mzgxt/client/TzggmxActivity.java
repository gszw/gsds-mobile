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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.util.HttpUtil;

public class TzggmxActivity extends Activity {

	private String xh;
	private String userKey;
	TextView title ;
	TextView author;
	TextView create_time;
	TextView contxt;

	ProgressDialog pb1;// 进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tzggmx);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		xh = bundle.getString("xh");
		userKey = bundle.getString("userKey");
		DataInit();
	}
	
	public void DataInit(){
		TextView mTextView=(TextView)findViewById(R.id.txt_activity_public_title);
		mTextView.setText("公告详情");
		Button btnExit = (Button) findViewById(R.id.btn_activity_public_exit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TzggmxActivity.this.finish();
			}
		});
		title = (TextView)findViewById(R.id.title);
		author = (TextView)findViewById(R.id.author);
		create_time = (TextView)findViewById(R.id.create_time);
		contxt = (TextView)findViewById(R.id.contxt);
		pb1 = new ProgressDialog(TzggmxActivity.this);
		 processThread();
		
	}
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 接受进程执行完成后的Message，刷新界面ＵＩ
			String jsonPages = (String) msg.obj;
			if (jsonPages == "-1") {
				Toast toast = Toast.makeText(getApplicationContext(),
						"你所要查询的新闻信息不存在！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				title.setText("");
				author.setText("");
				contxt.setText("");
				create_time.setText("");
			} else {
			try {
				JSONObject obj = new JSONObject(jsonPages);
				System.out.println("输出点什么吧");
				title.setText(obj.getString("title"));
				author.setText(obj.getString("author"));
				contxt.setText(obj.getString("contxt"));
				create_time.setText(obj.getString("create_time"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    pb1.hide();

		}

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
			    String url =  HttpUtil.Gettzggmx;
				// 获得请求对象

				// 调用HttpUtil类中的函数，返回HttpPost
				HttpPost req = HttpUtil.getHttpPost(url);

				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userKey", userKey));
				params.add(new BasicNameValuePair("xh", xh));
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 返回
			pb1.dismiss();
		}
		return super.onKeyDown(keyCode, event);

	}

}
