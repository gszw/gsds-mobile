package cn.com.gszw.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;



/**
*@Description: TODO(纳税人分户明细信息里的PagerAdapter自动适配Tab里面的Fragment)
*@Copyright: Copyright © 2013-2018
*@Company: www.gszw.com.cn 
*@Makedate:2013-4-4 下午1:18:52
*@author wangli
*/
public class SlidingFragmentAdapter extends PagerAdapter {

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction = null;
    
    private List<ContentFragment> mFragmentList = new ArrayList<ContentFragment>(3);
    
    public SlidingFragmentAdapter(FragmentManager fm,Bundle bundle,int x ) {
        mFragmentManager = fm;
        for(int i=0;i<x;i++){
        	mFragmentList.add(new ContentFragment("00"+i, bundle, i));
        }
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    //判断是否由对象生成 view
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    //此方法返回一个对象，告诉PagerAdapter选择哪一个对象
    public Object instantiateItem(ViewGroup container, int position) {
        if (mTransaction == null) {
            mTransaction = mFragmentManager.beginTransaction();
            
        }
        String name = getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
        	
            //mTransaction.attach(fragment);
        	  mTransaction.remove(fragment);
        	  fragment = getItem(position);
              mTransaction.add(container.getId(), fragment,
                      getTag(position));
            
        } else {
            fragment = getItem(position);
            mTransaction.add(container.getId(), fragment,
                    getTag(position));
        }
        return fragment;
    }

    @Override
    // 此方法是移除当前Object
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mTransaction == null) {
            mTransaction = mFragmentManager.beginTransaction();
        }
        mTransaction.detach((Fragment) object);
    }
    @Override
    //是在UI更新完成后的动作
    public void finishUpdate(ViewGroup container) {
        if (mTransaction != null) {
            mTransaction.commitAllowingStateLoss();
            mTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    } 
    public Fragment getItem(int position){
       return  mFragmentList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    protected  String getTag(int position){
        return mFragmentList.get(position).getText();
    }

}
