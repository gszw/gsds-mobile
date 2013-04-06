package cn.com.gszw.mzgxt.client;

import cn.com.gszw.mzgxt.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SjzcActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sjzc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sjzc, menu);
		return true;
	}

}
