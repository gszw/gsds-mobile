package cn.com.gszw.mzgxt.client;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cn.com.gszw.fragment.ContentFragment;
import cn.com.gszw.fragment.MenuFragment;
import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.slidingmenu.SlidingActivity;
import cn.com.gszw.mzgxt.slidingmenu.SlidingMenu;
import cn.com.gszw.mzgxt.util.HttpUtil;

/**
*@Description: TODO(分户明细中具体纳税人的详细信息)
*@Copyright: Copyright © 2013-2018
*@Company: www.gszw.com.cn 
*@Makedate:2013-4-3 下午9:53:05
*@author Administrator 
*/
public class DjNsryhscxActivity extends SlidingActivity {
	
	   @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setTitle("纳税人分户明细");

		setContentView(R.layout.frame_content);
		
        //得到主界面分户明细列表传输过来的纳税人数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

		
		   // set the Behind View
        setBehindContentView(R.layout.frame_menu);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        MenuFragment menuFragment = new MenuFragment(bundle);
        fragmentTransaction.replace(R.id.menu, menuFragment);
        fragmentTransaction.replace(R.id.content,new ContentFragment("摘 要",bundle.getString("nsrbm"), bundle.getString("nsrnbm"), bundle.getString("userKey")),"Welcome");
        fragmentTransaction.commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(50);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(60);
        sm.setFadeDegree(0.35f);
        //设置slding menu的几种手势模式
        //TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
        //TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,
        //你需要在屏幕边缘滑动才可以打开slding menu
        //TOUCHMODE_NONE 自然是不能通过手势打开啦
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        
        //设置actionbar的背景色
        getActionBar().setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.BackBar)); 
        getActionBar().show();
        //使用左上方icon可点，这样在onOptionsItemSelected里面才可以监听到R.id.home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slidingmain, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            //toggle就是程序自动判断是打开还是关闭
            toggle();
//          getSlidingMenu().showMenu();// show menu
//          getSlidingMenu().showContent();//show content
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
