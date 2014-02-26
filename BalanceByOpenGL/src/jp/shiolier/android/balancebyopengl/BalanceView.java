package jp.shiolier.android.balancebyopengl;

import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

/**
 * GLSurfaceViewを継承したOpenGL描画用のView
 * 兼
 * Rendererを実装したOpenGLレンダラ
 * 兼
 * SensorEventListenerを実装して加速度センサーのイベント処理を行うリスナー
 *
 */

public class BalanceView extends GLSurfaceView implements Renderer, SensorEventListener {
	private Context context;
	private Cube cube;
	private Sphere sphere;
	// 転がるときの回転具合
	private float rotation;
	// 加速度
	private float[] accel = new float[]{0.0f, 0.0f, 0.0f};
	// 速度
	private float[] speed = new float[]{0.0f, 0.0f, 0.0f};
	// 位置
	private float[] position = new float[]{0.0f, 0.0f, 0.0f};
	private float boardSide = 3.0f;
	private float cubeBottomRadius = 0.6f;
	// 加速度制御用の値
	private static final float K = 0.0004f;
	private float angleX, angleY;
	// ランダム加速度を加える秒数
	private static final int ADD_RANDOM_ACCEL_SECOND = 1;
	// クロノメーターに表示されている値とカウントには2秒の誤差がある
	private int point = -2;


	public BalanceView(Context context) {
		super(context);
		initialize(context);
	}

	public BalanceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	private void initialize(Context context) {
		// layoutファイルのエラー対策
		if (isInEditMode()) {
			return;
		}

		// レンダラをセット
		setRenderer(this);

		this.context = context;

		SharedPreferences sharedPref = 
				PreferenceManager.getDefaultSharedPreferences(context);

		// Cubeインスタンス生成
		if (sharedPref.getBoolean("board_color_random", true)) {
			cube = new Cube();
		} else {
			float red = sharedPref.getInt("board_color_red", 200) / 255.0f;
			float green = sharedPref.getInt("board_color_green", 200) / 255.0f;
			float blue = sharedPref.getInt("board_color_blue", 200) / 255.0f;
			float alpha = sharedPref.getInt("board_color_alpha", 255) / 255.0f;

			cube = new Cube(red, green, blue, alpha);
			MyLog.d(String.format("cube alpha = %f", alpha));
		}

		// Sphereインスタンス生成
		if (sharedPref.getBoolean("ball_color_random", true)) {
			sphere = new Sphere();
		} else {
			float red = sharedPref.getInt("ball_color_red", 200) / 255.0f;
			float green = sharedPref.getInt("ball_color_green", 200) / 255.0f;
			float blue = sharedPref.getInt("ball_color_blue", 200) / 255.0f;
			float alpha = sharedPref.getInt("ball_color_alpha", 255) / 255.0f;

			sphere = new Sphere(red, green, blue, alpha);
			MyLog.d(String.format("sphere alpha = %f", alpha));
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		update();

		// 画面をクリア
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// カレント座標をスタックに保存
		gl.glPushMatrix();
		{
			// 回転
			gl.glRotatef(angleX, 0, 1, 0);
			gl.glRotatef(angleY, 1, 0, 0);

			// カレント座標をスタックに保存
			gl.glPushMatrix();
			{
				// カレント座標を移動
				gl.glTranslatef(position[0], position[1], position[2]);
				// 回転
				gl.glRotatef(rotation, position[1], -position[0], 0);
				// 球体描画
				sphere.draw(gl);
			}
			// スタックから座標を取り出して、カレント座標に設定
			gl.glPopMatrix();

			// カレント座標を移動
			gl.glTranslatef(0.0f, 0.0f, 1.1f);
			// 拡大
			gl.glScalef(boardSide, boardSide, 0.1f);
			// 描画
			cube.draw(gl);
		}
		// スタックから座標を取り出して、カレント座標に設定
		gl.glPopMatrix();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// 表示領域の設定
		gl.glViewport(0, 0, width, height);

		// 投影変換のスタック
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		// 透視投影
		float ratio = (float) width / height;
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

		// 幾何変換のスタック
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// 視点の設定
		/*
		 * 引数
		 * 		1:GL10インスタンス
		 * 		2,3,4:視点の位置(x,y,z)
		 * 		5,6,7:視点の方向(x,y,z)
		 * 		8,9,10:視点の上の位置(x,y,z)
		 * 下記の設定だと
		 * 		(0,0,-5)から(0,0,0)を見ていて、上は(0,1,0)
		 * 第9引数の 1.0f を -1.0f にすると視点が上下逆さまになる
		 */
		GLU.gluLookAt(gl, 0.0f, 0.0f, -5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// クオリティの設定
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		// カリング
		gl.glEnable(GL10.GL_CULL_FACE);
		// スムーズシェーディング
		gl.glShadeModel(GL10.GL_SMOOTH);
		// 隠面処理
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// 配列の有効化
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		// 背景色の設定
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// 画面の傾きに合わせて加速度を加える
		accel[0] = event.values[0] * K;
		accel[1] = -event.values[1] * K;
	}

	public void changeAngle(float x, float y) {
		angleX += x;
		angleY -= y;
	}

	public void resetAngle() {
		angleX = 0;
		angleY = 0;
	}

	private void update() {
		// 加速度から位置を求める
		for (int i = 0; i < 3; i++) {
			speed[i] += accel[i];
			position[i] += speed[i];
		}

		// 球体の回転
		// (x2乗 + y2乗) × 180 / π
		rotation = (float) (Math.sqrt(position[0] * position[0] +
				position[1] * position[1]) *
				180 / Math.PI);

		// 落下判定
		if (Math.abs(position[0]) > boardSide + cubeBottomRadius
				|| Math.abs(position[1]) > boardSide + cubeBottomRadius) {
			accel[2] = 98f * K;
		}

		// 落下したら場合
		if (position[2] > 10) {
			gameOver();
		}
	}

	// クロノメーターカウントの値が変更された時に呼ばれる
	public void changeCount() {
		point++;
		if (point % ADD_RANDOM_ACCEL_SECOND == 0) {
			addRandomAccel(point / ADD_RANDOM_ACCEL_SECOND);
		}
	}

	// levelに応じたランダムな加速度を加える
	private void addRandomAccel(int level) {
		// ランダムな加速度を設定
		Random random = new Random();
		// + or - かを決めるためのランダムなboolean値を生成
		boolean isMinusX = random.nextBoolean();
		boolean isMinusY = random.nextBoolean();
		// 0~1未満までのランダムな値を生成して 2 * K をかける
		double randomAccelX = Math.random() * level * K;
		double randomAccelY = Math.random() * level * K;
		// isMinus が trueなら値を負の数にする
		if (isMinusX)	randomAccelX = -randomAccelX;
		if (isMinusY)	randomAccelY = -randomAccelY;

		// 加える
		accel[0] += randomAccelX;
		accel[1] += randomAccelY;
	}

	private void gameOver() {
		Intent intent = new Intent(context, ResultActivity.class);
		intent.putExtra("point", point);
		context.startActivity(intent);
	}
}
