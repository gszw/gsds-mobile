package cn.com.gszw.mzgxt.client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class FWelcomeActivity extends Activity {

	/** Called when the activity is first created. */

	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.factivity_welcome);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);

		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.factivity_wel_1, null);
		View view2 = mLi.inflate(R.layout.factivity_wel_2, null);
		View view3 = mLi.inflate(R.layout.factivity_wel_3, null);

		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		final ArrayList<String> titles = new ArrayList<String>();
		titles.add("更精致的界面！");
		titles.add("更舒适的体验！");
		titles.add("更完善的功能！");
		TextView tv3 = (TextView) view3.findViewById(R.id.fwel03);
		tv3.setOnClickListener(new gotoWelcomeListener());

		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;

			}

			@Override
			public int getCount() {

				return views.size();

			}

			@Override
			public void destroyItem(View container, int position, Object object) {

				((ViewPager) container).removeView(views.get(position));

			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles.get(position);
				// return super.getPageTitle(position);

			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mViewPager.setAdapter(mPagerAdapter);

	}

	class gotoWelcomeListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// Toast.makeText(getApplicationContext(), "TEST",
			// Toast.LENGTH_SHORT).show();
			startActivity(new Intent(FWelcomeActivity.this,
					WelcomeActivity.class));

		}
	}

}
