package jp.shiolier.android.balancebyopengl;

import java.util.Random;

/**
 * 立方体
 * 
 */

public class Cube extends Polygon {
	public Cube(float red, float green, float blue, float alpha) {
		/*
		{
				red,	green,	blue,	alpha,		// 右下
				red,	green,	blue,	alpha,		// 左下
				red,	green,	blue,	alpha,		// 左上
				red,	green,	blue,	alpha,		// 右上
				red,	green,	blue,	alpha,		// 左下裏面
				red,	green,	blue,	alpha,		// 左下裏面
				red,	green,	blue,	alpha,		// 左上裏面
				red,	green,	blue,	alpha,		// 右上裏面
		}
		 */
		float[] colors = new float[COLOR_SIZE * 8];
		for (int i = 0; i < colors.length;) {
			colors[i++] = red;
			colors[i++] = green;
			colors[i++] = blue;
			// colors[i++] = alpha;
			colors[i++] = 1.0f;
		}

		initialize(colors);
	}

	public Cube() {
		float[] colors = new float[COLOR_SIZE * 8];
		Random rand = new Random();
		for (int i = 0; i < colors.length;) {
			colors[i++] = rand.nextFloat();
			colors[i++] = rand.nextFloat();
			colors[i++] = rand.nextFloat();
			colors[i++] = 1.0f;
		}

		initialize(colors);
	}

	private void initialize(float[] colors) {
		float[] vertices = {
				-1.0f,		-1.0f,		-1.0f,
				1.0f,		-1.0f,		-1.0f,
				1.0f,		1.0f,		-1.0f,
				-1.0f,		1.0f,		-1.0f,
				-1.0f,		-1.0f,		1.0f,
				1.0f,		-1.0f,		1.0f,
				1.0f,		1.0f,		1.0f,
				-1.0f,		1.0f,		1.0f,
		};

		byte[] indices = {
				0, 4, 5,    0, 5, 1,
				1, 5, 6,    1, 6, 2,
				2, 6, 7,    2, 7, 3,
				3, 7, 4,    3, 4, 0,
				4, 7, 6,    4, 6, 5,
				3, 0, 1,    3, 1, 2
		};

		arrayToBuffer(vertices, colors, indices);
	}
}
