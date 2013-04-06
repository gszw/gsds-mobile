package cn.com.gszw.fragment;





import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import cn.com.gszw.mzgxt.client.DjNsryhscxActivity;
import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.slidingmenu.SlidingMenu;
import cn.com.gszw.mzgxt.util.HttpUtil;

/**
*@Description: TODO(分户明细里详细纳税人信息里用于显示滑动菜单的)
*@Copyright: Copyright © 2013-2018
*@Company: www.gszw.com.cn 
*@Makedate:2013-4-3 下午10:21:20
*@author wangli
*/
public class MenuFragment extends PreferenceFragment implements OnPreferenceClickListener{
    int index = -1;
    private ViewPager mViewPager = null;
    private FrameLayout mFrameLayout = null;
    private DjNsryhscxActivity   mActivity = null;
    Bundle bundle;
    
    
    
	public MenuFragment(Bundle bundle) {
		this.bundle=bundle;
	}


	@Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //set the preference xml to the content view
        mActivity = (DjNsryhscxActivity)getActivity();
        mViewPager = (ViewPager)mActivity.findViewById(R.id.viewpager);
        mFrameLayout = (FrameLayout)mActivity.findViewById(R.id.content);
        
        addPreferencesFromResource(R.xml.menu);
        //add listener
        findPreference("a").setOnPreferenceClickListener(this);//基本情况
        findPreference("b").setOnPreferenceClickListener(this);//核定情况
        findPreference("c").setOnPreferenceClickListener(this);//认定情况
        findPreference("d").setOnPreferenceClickListener(this);//申报缴款
        findPreference("e").setOnPreferenceClickListener(this);//发票相关
        findPreference("f").setTitle(bundle.getString("nsrmc"));//设置显示标题纳税人名称及编码
        findPreference("f").setSummary(bundle.getString("nsrbm"));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if("a".equals(key)) {
            //基本情况
            //如果当前Content里面显示的就是该菜单的内容，则直接显示内容
        	 if(index == 0) {
                 ((DjNsryhscxActivity)getActivity()).getSlidingMenu().toggle();
                 return true;
             }
             index = 0;
             mFrameLayout.setVisibility(View.GONE);
             //把Viewpager显示出来
             
             mViewPager.setVisibility(View.VISIBLE);
             //初始化FragmentAdapter，并且设置给Viewpager
            // bundle.putString("url", HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
             ArrayList<String> urllist = new ArrayList<String>();
             urllist.add(HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
             urllist.add(HttpUtil.IpUrl+ "/server/nsryhxx.jsp");             
             bundle.putStringArrayList("url",urllist);
             bundle.putString("OtherTj", "");
             //传2表示 会产生2个contentFragment
             SlidingFragmentAdapter demoFragmentAdapter = new SlidingFragmentAdapter(mActivity.getFragmentManager(),bundle,2);
             mViewPager.setOffscreenPageLimit(2);
             mViewPager.setAdapter(demoFragmentAdapter);
             mViewPager.setOnPageChangeListener(onPageChangeListener);
             
             //顶部的ActionBar设置成TAB模式，并且 添加上TAB的标题
             ActionBar actionBar = mActivity.getActionBar();
             actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
             actionBar.removeAllTabs();
             actionBar.addTab(actionBar.newTab()
                     .setText("基础信息")             
                     .setTabListener(tabListener));

             actionBar.addTab(actionBar.newTab()
                     .setText("银行信息")
                     .setTabListener(tabListener));      	
        	
        	
        }else if("b".equals(key)) {
        	//核定情况
        	//如果当前Content里面显示的就是该菜单的内容，则直接显示内容
       	 if(index == 1) {
                ((DjNsryhscxActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 1;
            mFrameLayout.setVisibility(View.GONE);
            //把Viewpager显示出来       
            mViewPager.setVisibility(View.VISIBLE);
            //初始化FragmentAdapter，并且设置给Viewpager
           // bundle.putString("url", HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
            ArrayList<String> urllist = new ArrayList<String>();
            urllist.add(HttpUtil.IpUrl+ "/server/nsrhdxx.jsp");
            urllist.add(HttpUtil.IpUrl+ "/server/nsrdfshdxx.jsp");             
            bundle.putStringArrayList("url",urllist);
            bundle.putString("OtherTj", "");
            //传2表示 会产生2个contentFragment
            SlidingFragmentAdapter demoFragmentAdapter = new SlidingFragmentAdapter(mActivity.getFragmentManager(),bundle,2);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(demoFragmentAdapter);
            mViewPager.setOnPageChangeListener(onPageChangeListener);
            
            //顶部的ActionBar设置成TAB模式，并且 添加上TAB的标题
            ActionBar actionBar = mActivity.getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            actionBar.addTab(actionBar.newTab()
                    .setText("主税信息")             
                    .setTabListener(tabListener));

            actionBar.addTab(actionBar.newTab()
                    .setText("三税信息")
                    .setTabListener(tabListener));    
        }else if("c".equals(key)) {
        	//认定情况
        	//如果当前Content里面显示的就是该菜单的内容，则直接显示内容
       	 if(index == 2) {
                ((DjNsryhscxActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 2;
            mFrameLayout.setVisibility(View.GONE);
            //把Viewpager显示出来
            mViewPager.setVisibility(View.VISIBLE);
            //初始化FragmentAdapter，并且设置给Viewpager
           // bundle.putString("url", HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
            ArrayList<String> urllist = new ArrayList<String>();
            urllist.add(HttpUtil.IpUrl+ "/server/nsrjmxx.jsp");
            urllist.add(HttpUtil.IpUrl+ "/server/nsrtfyxx.jsp");   
            urllist.add(HttpUtil.IpUrl+ "/server/nsrjbzq.jsp"); 
            bundle.putStringArrayList("url",urllist);
            bundle.putString("OtherTj", "");
            //传3表示 会产生3个contentFragment
            SlidingFragmentAdapter demoFragmentAdapter = new SlidingFragmentAdapter(mActivity.getFragmentManager(),bundle,3);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(demoFragmentAdapter);
            mViewPager.setOnPageChangeListener(onPageChangeListener);
            
            //顶部的ActionBar设置成TAB模式，并且 添加上TAB的标题
            ActionBar actionBar = mActivity.getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            actionBar.addTab(actionBar.newTab()
                    .setText("减免信息")             
                    .setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab()
                    .setText("停业信息")
                    .setTabListener(tabListener));  
            actionBar.addTab(actionBar.newTab()
                    .setText("简并征期")
                    .setTabListener(tabListener)); 
        }else if("d".equals(key)) {
        	//申报缴款--------------------------------------------------------------------------还需要重新写
            //如果当前Content里面显示的就是该菜单的内容，则直接显示内容
        	 if(index == 3) {
                 ((DjNsryhscxActivity)getActivity()).getSlidingMenu().toggle();
                 return true;
             }
             index = 3;
             mFrameLayout.setVisibility(View.GONE);
             //把Viewpager显示出来
             mViewPager.setVisibility(View.VISIBLE);
             //初始化FragmentAdapter，并且设置给Viewpager
            // bundle.putString("url", HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
             ArrayList<String> urllist = new ArrayList<String>();
             urllist.add(HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
             urllist.add(HttpUtil.IpUrl+ "/server/nsryhxx.jsp");             
             bundle.putStringArrayList("url",urllist);
             bundle.putString("OtherTj", "");
             //传2表示 会产生2个contentFragment
             SlidingFragmentAdapter demoFragmentAdapter = new SlidingFragmentAdapter(mActivity.getFragmentManager(),bundle,2);
             mViewPager.setOffscreenPageLimit(2);
             mViewPager.setAdapter(demoFragmentAdapter);
             mViewPager.setOnPageChangeListener(onPageChangeListener);
             
             //顶部的ActionBar设置成TAB模式，并且 添加上TAB的标题
             ActionBar actionBar = mActivity.getActionBar();
             actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
             actionBar.removeAllTabs();
             actionBar.addTab(actionBar.newTab()
                     .setText("申报情况")             
                     .setTabListener(tabListener));

             actionBar.addTab(actionBar.newTab()
                     .setText("缴款数据")
                     .setTabListener(tabListener)); 
        }else if("e".equals(key)) {
        	//发票相关
        	//如果当前Content里面显示的就是该菜单的内容，则直接显示内容
       	 if(index == 4) {
                ((DjNsryhscxActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 4;
            mFrameLayout.setVisibility(View.GONE);
            //把Viewpager显示出来
            mViewPager.setVisibility(View.VISIBLE);
            //初始化FragmentAdapter，并且设置给Viewpager
           // bundle.putString("url", HttpUtil.IpUrl+ "/server/nsrjbxx.jsp");
            ArrayList<String> urllist = new ArrayList<String>();
            urllist.add(HttpUtil.IpUrl+ "/server/nsrfphdxx.jsp");
            urllist.add(HttpUtil.IpUrl+ "/server/nsrfpjcxx.jsp");   
            urllist.add(HttpUtil.IpUrl+ "/server/nsrfpfx.jsp"); 
            bundle.putStringArrayList("url",urllist);
            bundle.putString("OtherTj", "");
            //传3表示 会产生3个contentFragment
            SlidingFragmentAdapter demoFragmentAdapter = new SlidingFragmentAdapter(mActivity.getFragmentManager(),bundle,3);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(demoFragmentAdapter);
            mViewPager.setOnPageChangeListener(onPageChangeListener);
            
            //顶部的ActionBar设置成TAB模式，并且 添加上TAB的标题
            ActionBar actionBar = mActivity.getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            actionBar.addTab(actionBar.newTab()
                    .setText("发票核定")             
                    .setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab()
                    .setText("发票领用")
                    .setTabListener(tabListener));  
            actionBar.addTab(actionBar.newTab()
                    .setText("发票分析")
                    .setTabListener(tabListener));
        }
        //anyway , show the sliding menu
        ((DjNsryhscxActivity)getActivity()).getSlidingMenu().toggle();
        return false;
    }
    
    private cn.com.gszw.mzgxt.slidingmenu.SlidingMenu getSlidingMenu() {
        return mActivity.getSlidingMenu();
    }
    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
    	//viewpager页面切换的时候会触发 ，在里面会去改变 tab的聚焦情况
        @Override
        public void onPageSelected(int position) {
        	//用于设置ActionBar的聚焦tab
            getActivity().getActionBar().setSelectedNavigationItem(position);
            switch (position) {
                case 0:
                	//判断了SLidingMenu的手势力模式，如果ViewPager已经滑到了最左边，则把手势设置成全屏的，这样更往左滑动的时候，就会打开Menu
                    getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                default:
                    getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    break;
            }
        }

    };
    //监听当点击tab改变页面的时候，Viewpager的内容也得随着改变
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mViewPager.getCurrentItem() != tab.getPosition())
                mViewPager.setCurrentItem(tab.getPosition());
            
            
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    };
    
}
