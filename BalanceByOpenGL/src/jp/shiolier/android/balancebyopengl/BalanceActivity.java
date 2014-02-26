package jp.shiolier.android.balancebyopengl;

import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class BalanceActivity extends Activity {
	private BalanceView balanceView;
	private SensorManager sensorManager;
	private Chronometer chronometer;
	private long time = 0;
	private boolean isChangeAngle = false;
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// タイトルバーを非表示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ステータスバーを消す
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_balance);

		// バランスビューのインスタンス取得
		balanceView = (BalanceView)findViewById(R.id.balance_view);

		// クロノメーターのリスナーをセット
		chronometer = (Chronometer)findViewById(R.id.chronometer);
		chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				balanceView.changeCount();
			}
		});

		gestureDetector = new GestureDetector(this, new GestureListener());

		// センサーマネージャーのインスタンス取得
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	}

	// アクティビティ実行直前にコール
	@Override
	protected void onResume() {
		super.onResume();
		balanceView.onResume();

		// 加速度センサーのインスタンスを取得
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (sensor != null) {	// 取得できた場合
			// リスナーを登録
			sensorManager.registerListener(balanceView, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}

		chronometer.setBase(SystemClock.elapsedRealtime() - time);
		// クロノメータースタート
		chronometer.start();

		// スリープを無効にする
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	// アクティビティ終了直後にコール
	@Override
	protected void onPause() {
		super.onPause();
		balanceView.onPause();

		// リスナーの登録を取り消す
		sensorManager.unregisterListener(balanceView);

		// スリープ無効を取り消す
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// クロノメーターストップ
		chronometer.stop();
		// クロノメーターのタイマーを保持しておく
		time = SystemClock.elapsedRealtime() - chronometer.getBase();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// バックボタンが押された場合
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, getString(R.string.not_back), Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return false;
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			isChangeAngle = !isChangeAngle;
			TextView txtChangeAngle = (TextView)findViewById(R.id.txt_change_angle);
			txtChangeAngle.setText(isChangeAngle ? "ON" : "OFF");
			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (isChangeAngle) {
				balanceView.changeAngle(distanceX, distanceY);
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public void onShowPress(MotionEvent e) {
			balanceView.resetAngle();
			super.onShowPress(e);
		}
	}
}
