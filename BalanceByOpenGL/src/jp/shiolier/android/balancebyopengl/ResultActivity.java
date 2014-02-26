package jp.shiolier.android.balancebyopengl;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// タイトルバー非表示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);

		// ランキングボタンのリスナーをセット
		Button btnRanking = (Button)findViewById(R.id.btn_ranking);
		btnRanking.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ResultActivity.this, RankingActivity.class);
				startActivity(intent);
			}
		});

		Button btnTop = (Button)findViewById(R.id.btn_top);
		btnTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				toTop();
			}
		});

		Button btnOneMore = (Button)findViewById(R.id.btn_one_more);
		btnOneMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ResultActivity.this, BalanceActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				startActivity(intent);
			}
		});

		// 結果表示用テキストビューのインスタンス取得
		TextView txtResult = (TextView)findViewById(R.id.txt_result);

		// インテント取得
		Intent intent = getIntent();
		// 「point」の値を取得
		int point = intent.getIntExtra("point", -1);
		// 正常に取得できたら表示＆登録
		if (point != -1) {
			txtResult.setText(String.format("%d point", point));

			SharedPreferences sharedPref =
					PreferenceManager.getDefaultSharedPreferences(this);
			String name = sharedPref.getString("name", "名無し");
			PlayData data = new PlayData(name, point);

			PlayDataSQLiteHelper helper = new PlayDataSQLiteHelper(this);
			helper.insertData(data);
			helper.close();
		} else {
			txtResult.setText("点数不明");
		}
	}

	// キーダウンイベント
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// バックボタンが押された場合はスタート画面へ遷移
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toTop();
			return true;
		}
		return false;
	}

	private void toTop() {
		// インテント生成
		Intent intent = new Intent(ResultActivity.this, MainActivity.class);
		// 遷移先Activityとスタックの先頭Activityが同じ場合は新しく作らない
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 遷移先Activity以外のActivityをクリアする
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// インテント発行
		startActivity(intent);
	}
}
