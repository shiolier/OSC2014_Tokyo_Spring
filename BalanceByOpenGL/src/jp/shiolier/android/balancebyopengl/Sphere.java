package jp.shiolier.android.balancebyopengl;

import java.util.Random;

/**
 * 球体
 * 
 */
public class Sphere extends Polygon {
	private final float RADIUS = 1.0f;
	private final static int HORIZONTAL = 10;
	private final static int VERTICAL = 5;

	public Sphere(float red, float green, float blue, float alpha) {
		float[] colors = new float[(2 + (VERTICAL - 1) * HORIZONTAL) * COLOR_SIZE];
		for (int i = 0; i < colors.length;) {
			colors[i++] = red;
			colors[i++] = green;
			colors[i++] = blue;
			// colors[i++] = alpha;
			colors[i++] = 1.0f;
		}

		initialize(colors);
	}

	public Sphere() {
		float[] colors = new float[(2 + (VERTICAL - 1) * HORIZONTAL) * COLOR_SIZE];
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
		// 分割数から角度のきざみを計算
		double theta = 2 * Math.PI / HORIZONTAL;
		double phi = Math.PI / VERTICAL;
		int pt = 0;
		int num = (VERTICAL - 1) * HORIZONTAL;

		float[] vertices = new float[(2 + num) * VERTEX_SIZE];
		for (int i = 0; i < VERTICAL + 1; i++) {
			double y = -RADIUS * Math.cos(i * phi);
			double r = Math.sqrt(RADIUS * RADIUS - y * y);
			if (i == 0) { // 一番上部
				vertices[pt++] = 0;
				vertices[pt++] = -RADIUS;
				vertices[pt++] = 0;
			} else if (i == VERTICAL) { // 底部
				vertices[pt++] = 0;
				vertices[pt++] = RADIUS;
				vertices[pt++] = 0;
			} else {
				// 分割数だけ三角形を生成
				for (int j = 0; j < HORIZONTAL; j++) {
					vertices[pt++] = (float) (r * Math.cos(j * theta));
					vertices[pt++] = (float) y;
					vertices[pt++] = (float) (r * Math.sin(j * theta));
				}
			}
		}

		byte[] indices = new byte[num * 2 * VERTEX_SIZE];
		pt = 0;
		for (int i = 0; i < VERTICAL; i++) {
			int m = (i - 1) * HORIZONTAL;
			for (int j = 0; j < HORIZONTAL; j++) {

				if (i == 0) { // 一番上部
					indices[pt++] = 0;
					indices[pt++] = (byte) ((j + 1) % HORIZONTAL + 1);
					indices[pt++] = (byte) (j + 1);
				} else if (i == VERTICAL - 1) { // 底部
					indices[pt++] = (byte) (j + 1 + m);
					indices[pt++] = (byte) ((j + 1 + m) % HORIZONTAL + 1 + m);
					indices[pt++] = (byte) (1 + m + HORIZONTAL);
				} else {
					// 側面の頂点を指定
					indices[pt++] = (byte) (j + 1 + m);
					indices[pt++] = (byte) ((j + 1) % HORIZONTAL + 1 + m);
					indices[pt++] = (byte) (j + 1 + HORIZONTAL + m);

					indices[pt++] = (byte) ((j + 1) % HORIZONTAL + 1 + HORIZONTAL + m);
					indices[pt++] = (byte) (j + 1 + HORIZONTAL + m);
					indices[pt++] = (byte) ((j + 1) % HORIZONTAL + 1 + m);
				}
			}
		}

		arrayToBuffer(vertices, colors, indices);
	}
}