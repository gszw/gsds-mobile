package cn.com.gszw.mzgxt.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {
	private final int SPLASH_DELAY_TIME = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				startActivity(new Intent(WelcomeActivity.this,
						LoginActivity.class));
				WelcomeActivity.this.finish();
			}
		}, SPLASH_DELAY_TIME);

	}

}
