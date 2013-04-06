package cn.com.gszw.mzgxt.client;

import cn.com.gszw.mzgxt.R;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Config {
	private static final String TAG = "Config";
	public static final String UPDATE_SERVER = "http://10.92.67.240:8080/server/";
	public static final String UPDATE_APKNAME = "Mzgxt.apk";
	public static final String UPDATE_VERJSON = "ver.json";
	public static final String UPDATE_SAVENAME = "Mzgxt.apk";

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"cn.com.gszw.mzgxt.clientt", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"cn.com.gszw.mzgxt.client", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}

	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}
}