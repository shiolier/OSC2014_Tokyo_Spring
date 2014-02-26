package jp.shiolier.android.balancebyopengl;

/**
 * プレイデータを取り扱うクラス
 * 
 */
public class PlayData {
	private String name;
	private int point;

	public PlayData(String name, int point) {
		this.name = name;
		this.point = point;
	}

	public String getName() {
		return name;
	}

	public int getPoint() {
		return point;
	}
}
