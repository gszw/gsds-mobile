package cn.com.gszw.mzgxt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.gszw.fragment.Fhmx_Fragment;
import cn.com.gszw.fragment.Fhmx_Fragment.Fragment_Listener;
import cn.com.gszw.fragment.Tjfx_Fragment;
import cn.com.gszw.fragment.Tzgg_Fragment;
import cn.com.gszw.fragment.Wscx_Fragment;
import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.util.DummyTabContent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

public class MainActivity extends FragmentActivity implements Fragment_Listener {
	private String swrydm, swrymc, xb, swjgdm, swjgmc;
	private TabHost tabHost;
	TabWidget tabWidget;
	Bundle bundle;
	int CURRENT_TAB = 0; // 设置常量
	Tzgg_Fragment NoticeFragment;
	Fhmx_Fragment FenhuFragment;
	Wscx_Fragment WenshuFragment;
	Tjfx_Fragment TongjiFragment;
	android.support.v4.app.FragmentTransaction ft;
	RelativeLayout tabIndicator1, tabIndicator2, tabIndicator3, tabIndicator4,
			tabIndicator5;

	// 弹出设置菜单ListView的数据
	private String[] rightsz = new String[] { "系统设置", "版本更新", "重新登录", "关于",
			"退出登录", "关闭设置" };
	private int[] leftsz = new int[] { R.drawable.shezhi01,
			R.drawable.shezhi02, R.drawable.shezhi03, R.drawable.shezhi04,
			R.drawable.shezhi05, R.drawable.shezhi06 };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findTabView();
		tabHost.setup();

		TextView userinfo = (TextView) findViewById(R.id.title_userinfo);
		TextView txt_quyu = (TextView) findViewById(R.id.title_quyu);
		Intent intent = this.getIntent();
		bundle = intent.getExtras();
		swrydm = bundle.getString("swrydm");
		swjgdm = bundle.getString("swjgdm");
		swrymc=bundle.getString("swrymc");
//		switch (TextUtils.substring(swjgdm, 0, 5)) {
//		case "26209":
//			txt_quyu.setText("酒泉市");
//			break;
//		case "26277":
//			txt_quyu.setText("测试市");
//			break;
//		}
		xb = bundle.getString("xb");
		if ("1".equals(xb)) {
			userinfo.setText("欢迎 " + swrymc + " 先生进入移动征管系统");
		} else {
			userinfo.setText("欢迎 " + swrymc + " 女士进入移动征管系统");
		}

		// 弹出设置popupwindow
		View shezhi = this.getLayoutInflater().inflate(R.layout.shezhi_popup,
				null);
		final PopupWindow pop = new PopupWindow(shezhi, 150, 300, true);

		pop.setAnimationStyle(R.style.PopupAnimation);

		Button sz = (Button) findViewById(R.id.sys_sz);
		// 弹出的-listView数据设置，list集合的元素是map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < rightsz.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("leftpic", leftsz[i]);
			listItem.put("rightwz", rightsz[i]);
			listItems.add(listItem);
		}

		// 创建一个SimpleAdapter装载listview中图标和提示文字的数据
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.shezhi_popup, new String[] { "leftpic", "rightwz" },
				new int[] { R.id.left_pic, R.id.right });
		final ListView list = (ListView) shezhi.findViewById(R.id.sz_list);
		list.setAdapter(simpleAdapter);

		// 系统设置中 listview子元素选中的响应事件
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = (HashMap<String, Object>) list
						.getItemAtPosition(paramInt);
				Object t1 = map.get("leftpic");
				String t2 = (String) map.get("rightwz");

				// Toast.makeText(getApplicationContext(),
				// "你选择了--Integer值是--"+(Integer)t1+"--drawable值是--"+R.drawable.menu_exit+", 文字提示是"+t2,Toast.LENGTH_LONG).show();

				switch ((Integer) t1) {
				// 设置菜单-系统设置
				case R.drawable.shezhi01:
					Toast.makeText(getApplicationContext(), "写点什么吧？？",
							Toast.LENGTH_SHORT).show();
					break;

				// 设置菜单-版本更新
				case R.drawable.shezhi02:
					Toast.makeText(getApplicationContext(), "写点什么吧？？",
							Toast.LENGTH_SHORT).show();
					break;

				// 设置菜单-重新登录
				case R.drawable.shezhi03:
					pop.dismiss();
					SharedPreferences mSharedPreferences = getSharedPreferences(
							"applicationInfo", Context.MODE_PRIVATE);
					Editor mEditor = mSharedPreferences.edit();
					mEditor.putString("autologin", "1");
					mEditor.commit();
					startActivity(new Intent(MainActivity.this,
							LoginActivity.class));
					MainActivity.this.finish();
					break;

				// 设置菜单-关于
				case R.drawable.shezhi04:

					Toast.makeText(getApplicationContext(), "写点什么吧？？",
							Toast.LENGTH_SHORT).show();
					break;

				// 设置菜单-退出登录
				case R.drawable.shezhi05:
					MainActivity.this.showTips();
					break;

				// 设置菜单-退出
				case R.drawable.shezhi06:
					pop.dismiss();
					break;
				}
			}
		});

		// 以下拉列表显示出来
		sz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// pop.showAtLocation(findViewById(R.id.sys_sz), Gravity.RIGHT,
				// 0, 0);
				pop.showAsDropDown(arg0);

			}
		});

		/** 监听 */
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				NoticeFragment = (Tzgg_Fragment) fm.findFragmentByTag("通知公告");
				FenhuFragment = (Fhmx_Fragment) fm.findFragmentByTag("分户明细");
				WenshuFragment = (Wscx_Fragment) fm.findFragmentByTag("文书查询");
				TongjiFragment = (Tjfx_Fragment) fm.findFragmentByTag("统计分析");
				ft = fm.beginTransaction();

				/** 如果存在Detaches掉 */
				if (NoticeFragment != null)
					ft.detach(NoticeFragment);

				/** 如果存在Detaches掉 */
				if (FenhuFragment != null)
					ft.detach(FenhuFragment);

				/** 如果存在Detaches掉 */
				if (WenshuFragment != null)
					ft.detach(WenshuFragment);

				/** 如果存在Detaches掉 */
				if (TongjiFragment != null)
					ft.detach(TongjiFragment);

				/** 如果当前选项卡是1 */
				if (tabId.equalsIgnoreCase("通知公告")) {
					isTabNotice();
					CURRENT_TAB = 1;

					/** 如果当前选项卡是2 */
				} else if (tabId.equalsIgnoreCase("分户明细")) {
					isTabFenhu();
					CURRENT_TAB = 2;

					/** 如果当前选项卡是4 */
				} else if (tabId.equalsIgnoreCase("文书查询")) {
					isTabWenshu();
					CURRENT_TAB = 3;

					/** 如果当前选项卡是5 */
				} else if (tabId.equalsIgnoreCase("统计分析")) {
					isTabTongji();
					CURRENT_TAB = 4;
				} else {
					switch (CURRENT_TAB) {
					case 1:
						isTabNotice();
						break;
					case 2:
						isTabFenhu();
						break;
					case 3:
						isTabWenshu();
						break;
					case 4:
						isTabTongji();
						break;
					default:
						isTabNotice();
						break;
					}

				}
				ft.commit();
			}

		};
		// 设置初始选项卡
		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(tabChangeListener);
		initTab();
		/** 设置初始化界面 */
		tabHost.setCurrentTab(0);

	}

	// 判断当前
	public void isTabNotice() {

		if (NoticeFragment == null) {
			ft.add(R.id.realtabcontent, new Tzgg_Fragment(), "通知公告");
		} else {
			ft.attach(NoticeFragment);
		}
	}

	public void isTabFenhu() {

		if (FenhuFragment == null) {
			ft.add(R.id.realtabcontent, new Fhmx_Fragment(), "分户明细");
		} else {
			ft.attach(FenhuFragment);
		}
	}

	public void isTabWenshu() {

		if (WenshuFragment == null) {
			ft.add(R.id.realtabcontent, new Wscx_Fragment(), "文书查询");
		} else {
			ft.attach(WenshuFragment);
		}
	}

	public void isTabTongji() {

		if (TongjiFragment == null) {
			ft.add(R.id.realtabcontent, new Tjfx_Fragment(), "统计分析");
		} else {
			ft.attach(TongjiFragment);
		}
	}

	/**
	 * 找到Tabhost布局
	 */
	public void findTabView() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		LinearLayout layout = (LinearLayout) tabHost.getChildAt(0);
		TabWidget tw = (TabWidget) layout.getChildAt(1);

		// --1-- 通知公告
		tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		TextView tvTab1 = (TextView) tabIndicator1.getChildAt(1);
		ImageView ivTab1 = (ImageView) tabIndicator1.getChildAt(0);
		ivTab1.setBackgroundResource(R.drawable.selector_notice);
		tvTab1.setText(R.string.maintab1);

		// --2-- 分户明细
		tabIndicator2 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		TextView tvTab2 = (TextView) tabIndicator2.getChildAt(1);
		ImageView ivTab2 = (ImageView) tabIndicator2.getChildAt(0);
		ivTab2.setBackgroundResource(R.drawable.selector_fenhu);
		tvTab2.setText(R.string.maintab2);

		// --3-- 票据查询
		tabIndicator3 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator_piaoju, tw, false);
		TextView tvTab3 = (TextView) tabIndicator3.getChildAt(1);
		ImageView ivTab3 = (ImageView) tabIndicator3.getChildAt(0);
		ivTab3.setBackgroundResource(R.drawable.selector_piaoju);
		tvTab3.setText(R.string.maintab3);

		// --4-- 文书查询
		tabIndicator4 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		TextView tvTab4 = (TextView) tabIndicator4.getChildAt(1);
		ImageView ivTab4 = (ImageView) tabIndicator4.getChildAt(0);
		ivTab4.setBackgroundResource(R.drawable.selector_wenshu);
		tvTab4.setText(R.string.maintab4);

		// --5-- 统计分析
		tabIndicator5 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		TextView tvTab5 = (TextView) tabIndicator5.getChildAt(1);
		ImageView ivTab5 = (ImageView) tabIndicator5.getChildAt(0);
		ivTab5.setBackgroundResource(R.drawable.selector_tongji);
		tvTab5.setText(R.string.maintab5);
	}

	/**
	 * 初始化选项卡
	 * 
	 * */
	public void initTab() {

		TabHost.TabSpec tSpecNotice = tabHost.newTabSpec("通知公告");
		tSpecNotice.setIndicator(tabIndicator1);
		tSpecNotice.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecNotice);

		TabHost.TabSpec tSpecFenhu = tabHost.newTabSpec("分户明细");
		tSpecFenhu.setIndicator(tabIndicator2);
		tSpecFenhu.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecFenhu);

		TabHost.TabSpec tSpecPiaoju = tabHost.newTabSpec("票据查询");
		tSpecPiaoju.setIndicator(tabIndicator3);
		tSpecPiaoju.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecPiaoju);

		// 拍照按钮监听事件，弹出dialog
		tabIndicator3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final Dialog choose = new Dialog(MainActivity.this,
						R.style.draw_dialog);
				choose.setContentView(R.layout.piaoju_dialog);
				// 设置背景模糊参数
				WindowManager.LayoutParams winlp = choose.getWindow()
						.getAttributes();
				winlp.alpha = 0.9f; // 0.0-1.0
				choose.getWindow().setAttributes(winlp);
				choose.show();// 显示弹出框
				ImageButton mImageButton_fp = (ImageButton) choose
						.findViewById(R.id.choose_fpcx);
				ImageButton mimImageButton_sp = (ImageButton) choose
						.findViewById(R.id.choose_spcx);
				mImageButton_fp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("swrydm", swrydm);
						bundle.putString("swjgdm", swjgdm);
						startActivity(new Intent(MainActivity.this,
								FpcxActivity.class).putExtras(bundle));
						choose.dismiss();
					}
				});
				mimImageButton_sp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("swrydm", swrydm);
						bundle.putString("swjgdm", swjgdm);
						startActivity(new Intent(MainActivity.this,
								SpcxActivity.class).putExtras(bundle));
						choose.dismiss();
					}
				});
			}
		});

		TabHost.TabSpec tSpecWenshu = tabHost.newTabSpec("文书查询");
		tSpecWenshu.setIndicator(tabIndicator4);
		tSpecWenshu.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecWenshu);

		TabHost.TabSpec tSpecTjfx = tabHost.newTabSpec("统计分析");
		tSpecTjfx.setIndicator(tabIndicator5);
		tSpecTjfx.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecTjfx);

	}

	// 菜单-设置的退出提示代码段
	private void showTips() {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("退出程序").setMessage("您确认要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// pb1.dismiss();
						MainActivity.this.finish();
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
	 * (non-Javadoc)
	 * 
	 * @see cn.com.gszw.fragment.Fhmx_Fragment.Fragment_Listener#MainBundle()
	 * fragment的回调函数，fragment的mcallback调用此方法可得到Acitivity的Bundle对象
	 */
	@Override
	public Bundle MainBundle() {
		// TODO Auto-generated method stub
		return this.bundle;
	}

}
