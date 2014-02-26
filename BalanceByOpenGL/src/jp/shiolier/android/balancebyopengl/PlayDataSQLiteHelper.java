package jp.shiolier.android.balancebyopengl;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * プレイデータ保存用のSQLiteデータベースHelper
 * 
 */

public class PlayDataSQLiteHelper extends SQLiteOpenHelper {
	public static final String NAME_DB = "play_data.db";
	public static final String NAME_TABLE = "play_data";
	public static final String NAME_COLUMN_ID = "_id";
	public static final String NAME_COLUMN_NAME = "name";
	public static final String NAME_COLUMN_POINT = "point";
	private static final int DB_VERSION = 1;
	private static final String SQL_CREATE_TABLE =
			"CREATE TABLE " + NAME_TABLE + "(" +
					NAME_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					NAME_COLUMN_NAME + " TEXT," +
					NAME_COLUMN_POINT + " INTEGER" +
					");";

	public PlayDataSQLiteHelper(Context context) {
		super(context, NAME_DB, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);

		ContentValues values = new ContentValues();
		values.put(NAME_COLUMN_NAME, "開発者");
		values.put(NAME_COLUMN_POINT, 213);
		db.insert(NAME_TABLE, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * ランキング
	 * @return
	 * 		ORDER BY point DESC
	 */
	public ArrayList<PlayData> getDescData() {
		ArrayList<PlayData> array = new ArrayList<PlayData>();

		SQLiteDatabase db = getReadableDatabase();
		String[] column = new String[] {NAME_COLUMN_NAME, NAME_COLUMN_POINT};
		Cursor cursor = db.query(NAME_TABLE, column, null, null, null, null, NAME_COLUMN_POINT + " DESC");

		while (cursor.moveToNext()) {
			PlayData data = new PlayData(cursor.getString(0), cursor.getInt(1));
			array.add(data);
		}

		cursor.close();
		return array;
	}

	/**
	 * 
	 * @param data
	 * 		保存するPlayData
	 * @return
	 * 		成功したか否か
	 */
	public boolean insertData(PlayData data) {
		ContentValues values = new ContentValues();
		values.put(NAME_COLUMN_NAME, data.getName());
		values.put(NAME_COLUMN_POINT, data.getPoint());

		SQLiteDatabase db = getWritableDatabase();
		long result = db.insert(NAME_TABLE, null, values);

		if (result != -1) {
			return true;
		} else {
			return false;
		}
	}
}
