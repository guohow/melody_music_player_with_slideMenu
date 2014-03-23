package app.guohow.melody.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import app.guohow.melody.Folder;
import app.guohow.melody.service.MelodyPlayer;
import app.guohow.melody.ui.MyToast;

public class QuerTools {
	private static SQLHelper dbHelper;
	// private static Context context;
	private static String DB_NAME = "mTable.db";
	private static int DB_VERSION = 1;
	private static int _index;
	public static SQLiteDatabase db;
	private static Cursor cursor;

	// 创建、查询
	public static int queryTable(Context context, String str, String str_1,
			String str_2) {
		int flag = 0;
		try {
			// 初始化&创建数据库
			dbHelper = new SQLHelper(context, DB_NAME, null, DB_VERSION);
			// 创建表
			// TABLE_NAME数据库名 *.db
			// SQLHelper.TABLE_NAME 表名
			db = dbHelper.getWritableDatabase(); // 自动调用SQLiteHelper.OnCreate()
			/* 查询表，得到cursor对象 */
			cursor = db.query(SQLHelper.TABLE_NAME, null, null, null, null,
					null, "title" + " DESC");
			cursor.moveToFirst();

			while (MelodyPlayer.hasEverPlayed && !cursor.isAfterLast()
					&& cursor.getString(1) != null && flag == 0) {
				String title = cursor.getString(1);
				System.out.println(title + "------------" + str);
				if (title.equals(str)) {
					System.out
							.println("true++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					flag = 1;
					db.delete(SQLHelper.TABLE_NAME, "title" + "=?",
							new String[] { str });
					Folder.UPDATE = 1;
					MyToast.makeText(context, "已移除", Toast.LENGTH_SHORT).show();
					break;
				} else {
					cursor.moveToNext();
				}
			}

			if (flag == 0) {
				ContentValues values = new ContentValues();
				values.put("title", str);
				values.put("artist", str_1);
				values.put("url", str_2);

				db.insert(SQLHelper.TABLE_NAME, null, values);
				Folder.UPDATE = 1;
				MyToast.makeText(context, "已添加", Toast.LENGTH_SHORT).show();
			}

			System.out.println("" + cursor);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// 当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表
			// 注：表以前数据将丢失
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
		return flag;// 0表示未查到

	}

	// 创建、查询
	public static int queryTableNormal(Context context, String str,
			String str_1, String str_2) {
		int flag_n = 0;
		try {
			// 初始化&创建数据库
			dbHelper = new SQLHelper(context, DB_NAME, null, DB_VERSION);
			// 创建表
			// TABLE_NAME数据库名 *.db
			// SQLHelper.TABLE_NAME 表名
			db = dbHelper.getWritableDatabase(); // 自动调用SQLiteHelper.OnCreate()
			/* 查询表，得到cursor对象 */
			cursor = db.query(SQLHelper.TABLE_NAME, null, null, null, null,
					null, "title" + " DESC");
			cursor.moveToFirst();

			while (MelodyPlayer.hasEverPlayed && !cursor.isAfterLast()
					&& cursor.getString(1) != null && flag_n == 0) {
				String title = cursor.getString(1);
				System.out.println(title + "------------" + str);
				if (title.equals(str)) {
					System.out
							.println("true++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					flag_n = 1;
				} else {
					cursor.moveToNext();
				}
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// 当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表
			// 注：表以前数据将丢失
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}
		return flag_n;// 0表示未查到

	}

}
