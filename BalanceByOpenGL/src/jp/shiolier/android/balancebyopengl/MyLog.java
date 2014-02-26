package jp.shiolier.android.balancebyopengl;

import android.util.Log;

/**
 * ログ出力用クラス
 * 
 */
public class MyLog {
	private static final String TAG = "MyDebug";

	public static void d(String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}
}
