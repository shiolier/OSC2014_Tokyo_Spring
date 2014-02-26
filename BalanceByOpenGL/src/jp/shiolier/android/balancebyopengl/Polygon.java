package jp.shiolier.android.balancebyopengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/**
 * 多面体描画用の抽象クラス
 * 
 */

public abstract class Polygon {
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private ByteBuffer indexBuffer;

	public static final int FLOAT_SIZE = Float.SIZE / 8;
	public static final int VERTEX_SIZE = 3;
	public static final int COLOR_SIZE = 4;

	/**
	 * 配列をバッファへ変換する
	 * @param vertices
	 * 		頂点配列
	 * @param colors
	 * 		カラー配列
	 * @param indices
	 * 		インデックス配列
	 */
	public void arrayToBuffer(float[] vertices, float[] colors, byte[] indices) {
		vertexBuffer = createFloatBuffer(vertices);
		colorBuffer = createFloatBuffer(colors);
		indexBuffer = createByteBuffer(indices);
	}

	/**
	 * FloatBufferの生成
	 * @param array
	 * 		生成もとの配列
	 * @return
	 * 		生成されたFloatBuffer
	 */
	public FloatBuffer createFloatBuffer(float[] array) {
		// ダイレクトバッファー作成(ダイレクトはヒープ外にメモリを確保)
		// 非ダイレクトより高速に読み書きができる(?)
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * FLOAT_SIZE);
		// バイト順序をハードウェアのネイティブバイト順序に変更
		byteBuffer.order(ByteOrder.nativeOrder());
		// 作成したByteBufferをFloatBufferに変換したものを取得
		// こうすることによりダイレクトなFloatBufferが得られる
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);
		return floatBuffer; 
	}

	/**
	 * ByteBufferの生成
	 * @param array
	 * 		生成もとの配列
	 * @return
	 * 		生成されたByteBuffer
	 */
	public ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(array.length);
		buffer.put(array).position(0);
		return buffer;
	}

	/**
	 * 描画
	 * 
	 * @throws RuntimeException
	 * 		Bufferを初期化していない
	 */
	public void draw(GL10 gl) {
		if (vertexBuffer == null || colorBuffer == null || indexBuffer == null) {
			throw new RuntimeException("Buffers don't initialize yet.");
		}
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(VERTEX_SIZE, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(COLOR_SIZE, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indexBuffer.capacity(), GL10.GL_UNSIGNED_BYTE, indexBuffer);
	}
}
