package jp.shiolier.android.balancebyopengl;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// タイトルバー非表示
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// スタートボタンのリスナーをセット
		Button btnStart = (Button)findViewById(R.id.btn_start);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
				startActivity(intent);
			}
		});

		// ランキングボタンのリスナーをセット
		Button btnRanking = (Button)findViewById(R.id.btn_ranking);
		btnRanking.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, RankingActivity.class);
				startActivity(intent);
			}
		});

		// 設定ボタンのリスナーをセット
		Button btnSetting = (Button)findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});

		Button btnAppManage = (Button)findViewById(R.id.btn_app_manage);
		btnAppManage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				Uri uri = Uri.fromParts("package", getPackageName(), null);
				intent.setData(uri);
				ComponentName component = ComponentName.unflattenFromString("com.android.settings/.applications.InstalledAppDetails");
				intent.setComponent(component);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences sharedPref =
				PreferenceManager.getDefaultSharedPreferences(this);
		String name = sharedPref.getString("name", "名無し");
		TextView txtWelcome = (TextView)findViewById(R.id.txt_name);
		txtWelcome.setText(name);
	}
}
