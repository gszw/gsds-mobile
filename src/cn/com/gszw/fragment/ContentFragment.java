package cn.com.gszw.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.com.gszw.mzgxt.R;
import cn.com.gszw.mzgxt.client.PublicActivity;
import cn.com.gszw.mzgxt.util.HttpUtil;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


/**
*@Description: TODO(分户明细里详细纳税人信息里用于显示内容的)
*@Copyright: Copyright © 2013-2018
*@Company: www.gszw.com.cn 
*@Makedate:2013-4-3 下午11:30:45
*@author wangli
*/
public class ContentFragment extends Fragment {
    String text = null;
    ProgressDialog pb1;// 进度条
	private String nsrnbm;
	private String userKey;
	private String nsrbm;
	private String nsrmc;
	public  String url;// 获取数据的url;
	private String OtherTj;// 其他用户自定义参数
	int xh;
	WebView wv;
	String result = "";

    public ContentFragment(String text,Bundle bundle,int xh) {
        Log.e("gszw..ContentFragment..gouzao--start-", text);
        this.text = text;
        this.nsrbm = bundle.getString("nsrbm");
		this.nsrmc = bundle.getString("nsrmc");
		this.nsrnbm = bundle.getString("nsrnbm");
		this.userKey = bundle.getString("userKey");
		this.OtherTj = bundle.getString("OtherTj");
		this.url = bundle.getStringArrayList("url").get(xh);
         
    }
	public ContentFragment(String str1, String str2, String str3,String str4) {
		this.text=str1;
		this.nsrbm=str2;
		this.nsrnbm=str3;
		this.userKey=str4;
		System.out.println("-gouzao***--text"+text+",nsrbm="+nsrbm+",nsrnbm="+nsrnbm+",userKey="+userKey);
		
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.e("gszw..", "onCreate:"+text);
        

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("gszw..", "onCreateView:"+ text);

        //inflater the layout 
       
        View view = inflater.inflate(R.layout.fragment_text, null);
        TextView textView =(TextView)view.findViewById(R.id.textView);
        wv = (WebView)view.findViewById(R.id.cf_webview);
        if(!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
        pb1 = new ProgressDialog(getActivity());
		pb1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pb1.setMessage("数据载入中，请稍候！");
		pb1.show();
	
		processThread();
        return view;
    }

    public String getText(){
        return text;
    }
    
    @Override
    public void onDestroy() {
        Log.e("gszw..", "onDestroy:"+ text);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("gszw..", "onDetach:"+ text);
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.e("gszw..", "onPause:"+ text);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.e("gszw..", "onResume:"+ text);
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.e("gszw..", "onStart:"+ text);
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.e("gszw..", "onStop:"+ text);
        super.onStop();
    }
    
 // 定义Handler直接用，不需要修改
 	private Handler handler = new Handler() {
 		@Override
 		// 当有消息发送出来的时候就执行Handler的这个方法
 		public void handleMessage(Message msg) {
 			super.handleMessage(msg);
 			pb1.hide();
				wv.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);

 		}

 	};

 	// 主进程
 	private void processThread() {
 		// 构建一个下载进度条
 		// pb1 = ProgressDialog.show(PublicWebView.this, "查询银行信息", "正在查询……");
 		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 把耗时长的代码全放在这儿

 				
 				// 获得请求对象
 				// 调用HttpUtil类中的函数，返回HttpPost
 				if(url==null){
 					//如果为空，说明是首界面进来的,显示纳税人摘要信息
 					url = HttpUtil.IpUrl+ "/server/nsrzyxx.jsp";
 				}
 				System.out.println("processThread******----"+url);
 				HttpPost req = HttpUtil.getHttpPost(url);
 				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
 				params.add(new BasicNameValuePair("nsrnbm", nsrnbm));
 				params.add(new BasicNameValuePair("userKey", userKey));
 				params.add(new BasicNameValuePair("OtherTj", OtherTj));
 				// 设置请求对象的编码
 				try {
 					req.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
 					result = HttpUtil.getStringByPost(req);

 				} catch (UnsupportedEncodingException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 					result = "-1";
 				}

 				handler.sendEmptyMessage(0);
			}
		}).start();
 		
 	}

    
}
