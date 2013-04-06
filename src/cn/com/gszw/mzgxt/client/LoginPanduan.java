package cn.com.gszw.mzgxt.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**
*@Description: TODO(默认第一个访问的界面，如果第一次运行，则显示引导界面，否则到欢迎界面)
*@Copyright: Copyright © 2013-2018
*@Company: www.gszw.com.cn 
*@Makedate:2013-4-5 下午9:38:20
*@author Administrator 
*/
public class LoginPanduan extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//读取SharedPreferences中需要的数据
		SharedPreferences	preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),FWelcomeActivity.class);
            startActivity(intent);
            finish();
        }else{
        	 Intent intent = new Intent();
             intent.setClass(getApplicationContext(),WelcomeActivity.class);
             startActivity(intent);
             finish();
        }
      
        Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();


	}

}
