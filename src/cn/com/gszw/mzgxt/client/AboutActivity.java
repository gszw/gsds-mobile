package cn.com.gszw.mzgxt.client;

import cn.com.gszw.mzgxt.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		DataInit();
	}
	public void DataInit(){

		TextView mTextView=(TextView)findViewById(R.id.txt_activity_public_title);
		mTextView.setText("关于");
		Button btnExit = (Button) findViewById(R.id.btn_activity_public_exit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}

}
