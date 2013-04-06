package cn.com.gszw.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.util.HttpHelper;
import cn.com.gszw.mzgxt.util.HttpUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Wscx_Fragment extends Fragment {

	// 以下代码是弹出单选按钮的定义
	private String[] areas = new String[] { "全部（默认）", "按纳税人编码", "按文书号" };
	private boolean[] areaState = new boolean[] { true, false, false };
	private RadioOnClick radioOnClick = new RadioOnClick(1);
	private ListView areaRadioListView;
	Button bt;
	///LinearLayout lu;
	private EditText tv_tj = null;
	private static int flag = 0;// 标志位,如果是第一次初始化activity
	// tv_tj显示"请输入条件",不是第一次获得焦点为""
	private int radioindex = 1;
	private String swrydm;
	private String userKey;
	ProgressDialog pb1;// 进度条
	String url;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ListView myListView;

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
				bt.setText("编码");
				tv_tj.setHint("请输入[" + areas[index] + "]条件项");
				break;
			case 2:
				bt.setText("文书号");
				tv_tj.setHint("请输入[" + areas[index] + "]条件项");
				break;
			}
			dialog.dismiss();
			//lu.setFocusable(true);
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
			AlertDialog ad = new AlertDialog.Builder(getActivity())
					.setTitle("选择查询条件项")
					.setSingleChoiceItems(areas, radioOnClick.getIndex(),
							radioOnClick).create();
			areaRadioListView = ad.getListView();
			ad.show();

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent intent = this.getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		swrydm = bundle.getString("swrydm");
		userKey = bundle.getString("userKey");
		pb1 = new ProgressDialog(getActivity());
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");
		// pb1.show();

		//lu = (LinearLayout) getActivity().findViewById(R.id.tv_LinearLayout);
		bt = (Button) getActivity().findViewById(R.id.bt_Radio);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.setOnClickListener(new RadioClickListener());
			}
		});
		Button bt1 = (Button) getActivity().findViewById(R.id.button2);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 隐藏软键盘
				
				  ((InputMethodManager)
				  getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
				  .hideSoftInputFromWindow(getActivity()
				  .getCurrentFocus().getWindowToken(),
				  InputMethodManager.HIDE_NOT_ALWAYS); v.setEnabled(false);
				 
				DataLoad();
				// v.setEnabled(true);
			}
		});
		tv_tj = (EditText) getActivity().findViewById(R.id.tv_tj);
		pb1 = new ProgressDialog(getActivity());
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");
		
	    myListView = (ListView)getActivity().findViewById(R.id.gg_list);
		DataLoad();
	}

	private void DataLoad() {
		// 网络链接检测
		HttpUtil.HttpTest(getActivity());
		processThread();
	}

	// 根据查询条件，生成查询所必要的Where条件
	private String setSql() {

		String str = "";
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
			case 1:// 按纳税人编码查询
				str = str + "  dj.nsrbm like '%" + ss + "%'";
				break;

			case 2:// 按文书号查询
				str = str + "  xx.wsh like '%" + ss + "%'";
				break;
			}
		}
		return str;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_wscx, container, false);
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
								getActivity(), // 没什么解释
								mylist,// 数据来源
								R.layout.activity_wscx_listview,// ListItem的XML实现
								// 动态数组与ListItem对应的子项
								new String[] { "xh","hjmc", "lcmc", "wsh","nsrbm","nsrmc","tjr","tjsj" },
								new int[] { R.id.xh,R.id.hjmc, R.id.lcmc, R.id.wsh,R.id.nsrbm,R.id.nsrmc, R.id.tjr, R.id.tjsj});
					   myListView.setAdapter(mSchedule);
						Toast toast = Toast.makeText(getActivity().getApplicationContext(),
								"你当前没有代办任务！", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
						//pb1.dismiss();
					} else {
						try {

							JSONArray array = new JSONArray(jsonTaxPayerDatas);
							//JSONArray array= SQLHelper.receiveByJSONArray("testTzgg");
							mylist.clear();
							// 生成动态数组，并且转载数据
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								//Bitmap image = getImage(obj.getString("xh"));
								map.put("xh", obj.getString("xh"));
								map.put("hjmc", obj.getString("hjmc"));
								map.put("lcmc", obj.getString("lcmc"));
								map.put("wsh", obj.getString("wsh"));
								map.put("nsrbm", obj.getString("nsrbm"));
								map.put("nsrmc", obj.getString("nsrmc"));
								map.put("tjr", obj.getString("tjr"));
								map.put("tjsj", obj.getString("tjsj"));
								mylist.add(map);
							}
							SimpleAdapter mSchedule = new SimpleAdapter(
									getActivity(), // 没什么解释
									mylist,// 数据来源
									R.layout.activity_wscx_listview,// ListItem的XML实现
									new String[] { "xh","hjmc", "lcmc", "wsh","nsrbm","nsrmc","tjr","tjsj" },
									new int[] { R.id.xh,R.id.hjmc, R.id.lcmc, R.id.wsh,R.id.nsrbm,R.id.nsrmc, R.id.tjr, R.id.tjsj});
						   myListView.setAdapter(mSchedule);
							myListView.setAdapter(mSchedule);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
						// 执行完成后关闭对话框 ，此对话框是在processThread中打开的，就是旋转ProgressDialog
						//pb1.dismiss();
					}

				}

			};
			


	// 主进程
	private void processThread() {
		// 构建一个下载进度条
		new Thread() {
			@Override
			public void run() {
				// 把耗时长的代码全放在这儿

				String result = "";
				// 获得请求对象
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// 所有后台调用都要传这个参数过去
				params.add(new BasicNameValuePair("sqlstr", setSql()));
				params.add(new BasicNameValuePair("userKey", userKey));
				params.add(new BasicNameValuePair("swrydm", swrydm));
				// 设置请求对象的编码
				result = HttpHelper.getStringByPost(getActivity(), "GetWsxx",
						params);
				Message msg = new Message();
				msg.obj = result;
				handler.sendMessage(msg);

			}
		}.start();
	}
}
