package cn.com.gszw.fragment;

import cn.com.gszw.mzgxt.client.R;
import cn.com.gszw.mzgxt.client.TzggmxActivity;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.gszw.mzgxt.util.AsyncImageLoader.ImageCallback;

import cn.com.gszw.mzgxt.util.AsyncImageLoader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.gszw.mzgxt.util.HttpUtil;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Tzgg_Fragment extends Fragment implements OnScrollListener {

	private String account;
	private String swjgdm;
	private String userKey;
	private AsyncImageLoader asyncImageLoader;
	private ProgressDialog pb1;
	private ListView listView;
	private LinearLayout list_footer;
	private LinearLayout loading;
	private Button moreButton;
	private List<JSONObject> list = new ArrayList<JSONObject>();
	private ExecutorService executorService;
	private int scrollState;
	private int lastItem;
	// 当前可见页面中的Item总数
	private int visibleItemCount;
	private int TOTAL_PAGE;// 当前已经加载的页数
	private int visibleLastIndex = 0; // 最后的可视项索引
	private MyAdapter adapter;
	private Handler handler;
	private boolean isScrolling = false;// 是否正在滚动
	private boolean isloading = false;// 判断是否正在加载中

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 初始化本表单数据
		Intent intent = this.getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		account = bundle.getString("swrydm");// 用户名称
		swjgdm = bundle.getString("swjgdm");// 税务机关代码
		userKey = bundle.getString("userKey");
		setUpViews();
		pb1 = new ProgressDialog(getActivity());// 生成一个进度条
		pb1.setProgressStyle(pb1.STYLE_SPINNER);
		pb1.setTitle("请稍等");
		pb1.setMessage("正在读取数据中!");
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("userKey", userKey);
				try {
					bundle.putString("xh", list.get(arg2).get("xh").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				intent.setClass(Tzgg_Fragment.this.getActivity(),
						TzggmxActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.main_tzgg, container, false);
	}

	private void setUpViews() {
		list_footer = (LinearLayout) LayoutInflater.from(getActivity())
				.inflate(R.layout.list_footer, null);
		moreButton = (Button) list_footer.findViewById(R.id.button);
		loading = (LinearLayout) list_footer.findViewById(R.id.linearLayout);
		loading.setVisibility(View.GONE);
		getData();
		// 更多按钮点击事件
		moreButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (lastItem == adapter.count
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 设置页面底部布局控件可见性
					moreButton.setVisibility(View.GONE);
					loading.setVisibility(View.VISIBLE);
					if (adapter.count <= list.size()) {
						// 使用Handler动态加载数据
						new Handler().postDelayed(new Runnable() {

							public void run() {
								adapter.notifyDataSetChanged();
								listView.setSelection(lastItem
										- visibleItemCount + 1);

							}
						}, 2000);
					}
				}
				if(list.size()-adapter.count>5){
					adapter.count += 5;
					moreButton.setVisibility(View.VISIBLE);
					loading.setVisibility(View.GONE);
				}
				else {
					adapter.count+=list.size()-adapter.count;
					listView.removeFooterView(list_footer);
				}
				}
		});
		listView = (ListView) getActivity().findViewById(R.id.gg_list);
		listView.addFooterView(list_footer);// 这儿是关键中的关键呀，利用FooterVIew分页动态加载
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		lastItem = firstVisibleItem + visibleItemCount - 1;
		if (adapter.count >= list.size()) {
			listView.removeFooterView(view);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		/*
		 * if (scrollState == SCROLL_STATE_IDLE && view.getLastVisiblePosition()
		 * == view.getCount() +1)
		 * 
		 * { handler.sendEmptyMessage(1); }
		 */
	}

	private void getData() {
		String result = null;
		// 后台HttpServlet定义，需要在HttpUtil类中定义
		String url = HttpUtil.Gettzgg;
		HttpPost req = HttpUtil.getHttpPost(url);
		List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
		// 所有后台调用都要传这个参数过去
		params1.add(new BasicNameValuePair("userKey", userKey));
		params1.add(new BasicNameValuePair("swjgdm", swjgdm));
		// 设置请求对象的编码
		try {
			req.setEntity(new UrlEncodedFormEntity(params1, HTTP.UTF_8));
			result = HttpUtil.getStringByPost(req);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "-1";
		}
		String jsonStr = result;
		try {
			JSONArray array = new JSONArray(jsonStr);
			if (array != null && array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					list.add(array.optJSONObject(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class MyAdapter extends BaseAdapter {
		private int count = 5;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return this.count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View contentView, ViewGroup parent) {
			View view = null;
			asyncImageLoader = new AsyncImageLoader();
			DataWrapper dataWrapper = new DataWrapper();
			if(position>=list.size()){
				return null;
			}
			JSONObject data = (JSONObject) list.get(position);
			view = View.inflate(getActivity(), R.layout.activity_tzgg_listview,
					null);
			dataWrapper.xh = (TextView) view.findViewById(R.id.xh);
			dataWrapper.image = (ImageView) view.findViewById(R.id.v_image);
			dataWrapper.title = (TextView) view.findViewById(R.id.title);
			dataWrapper.notice = (TextView) view.findViewById(R.id.notice);
			dataWrapper.create_time = (TextView) view
					.findViewById(R.id.create_time);
			view.setTag(dataWrapper);
			if (data != null) {
				try {
					dataWrapper.xh.setText(data.getString("xh"));
					dataWrapper.title.setText(data.getString("title"));
					dataWrapper.notice.setText(data.getString("notice"));
					dataWrapper.create_time.setText(data
							.getString("create_time"));
					Drawable cachedImage = asyncImageLoader.loadDrawable(
							data.getString("image"), dataWrapper.image,
							new ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable,
										ImageView imageView, String imageUrl) {
									imageView.setImageDrawable(imageDrawable);
								}
							});
					if (cachedImage == null) {
						dataWrapper.image
								.setImageResource(R.drawable.ic_launcher);
					} else {
						dataWrapper.image.setImageDrawable(cachedImage);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return view;
		}

	}

	static class DataWrapper {
		public TextView xh;
		public ImageView image;
		public TextView title;
		public TextView notice;
		public TextView create_time;
	}
}
