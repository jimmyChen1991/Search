package com.hhyg.TyClosing.global;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	private static int version = 8;
	private static DbOpenHelper mInstance = new DbOpenHelper(MyApplication
			.GetInstance().getApplicationContext(), "tyclosing.db");

	public static DbOpenHelper GetInstance() {
		return mInstance;
	}

	private DbOpenHelper(Context context, String name) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method st
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql0 = "drop table shoppingcart";
		String sql1 = "drop table deletetable";
		db.execSQL(sql0);
		db.execSQL(sql1);
	}

	public synchronized static void destoryInstance() {
		if (mInstance != null) {
			mInstance.close();
		}
	}
	
	public synchronized boolean insert(String tableName, String[] columnNames,
			String[] valueStrs) {
		boolean res = true;
		int columnCnt = columnNames.length;
		int valueCnt = valueStrs.length;
		if (columnCnt >= 0 && valueCnt == columnCnt) {
			ContentValues values = new ContentValues(valueCnt);
			for (int idx = 0; idx < columnCnt; idx++) {
				values.put(columnNames[idx], valueStrs[idx]);
			}

			long ret = getWritableDatabase().insert(tableName, null, values);
			if (ret < 0) {
				res = false;
			}
		} else {
			res = false;
		}

		return res;
	}

	private final int MAX_UNION_CNT = 500;

	public String[] genMultiRow(String tableName, String[] columns,
			String[][] values) {
		// calc sqlCnt
		int itemCnt = values.length;
		int sqlCnt;
		if (itemCnt % MAX_UNION_CNT == 0) {
			sqlCnt = itemCnt / MAX_UNION_CNT;
		} else {
			sqlCnt = itemCnt / MAX_UNION_CNT + 1;
		}

		String[] resStrArr = new String[sqlCnt];
		// contruct head
		StringBuilder headSb = new StringBuilder();
		headSb.append("insert into ");
		headSb.append(tableName);
		headSb.append(" (");
		int keyCnt = columns.length;
		for (int idx = 0; idx < keyCnt; idx++) {
			headSb.append(columns[idx]);
			if (idx != (keyCnt - 1)) {
				headSb.append(",");
			}
		}
		headSb.append(") ");
		String headStr = headSb.toString();
		// contruct sqls
		int dataCnt;
		int base;
		String[] rowValue;
		StringBuilder resStr;
		for (int sqlIdx = 0; sqlIdx < sqlCnt; sqlIdx++) {
			resStr = new StringBuilder();
			resStr.append(headStr);
			dataCnt = (sqlIdx < sqlCnt - 1) ? MAX_UNION_CNT
					: (itemCnt - (sqlCnt - 1) * MAX_UNION_CNT);
			base = sqlIdx * MAX_UNION_CNT;
			for (int dataIdx = 0; dataIdx < dataCnt; dataIdx++) {
				resStr.append("select ");
				rowValue = values[base + dataIdx];
				for (int keyIdx = 0; keyIdx < keyCnt; keyIdx++) {
					resStr.append(rowValue[keyIdx]);
					if (keyIdx != (keyCnt - 1)) {
						resStr.append(",");
					}
				}
				if (dataIdx != dataCnt - 1) {
					resStr.append(" union all ");
				}
			}
			resStrArr[sqlIdx] = resStr.toString();
		}
		return resStrArr;

	}

	public synchronized String[][] rawQuery(String sql, String[] keys) {
		int keyCnt = keys.length;
		Cursor cursor = null;
		cursor = getReadableDatabase().rawQuery(sql, null);
		int cnt = cursor.getCount();
		String[][] rstArr = new String[cnt][];
		String[] arr;
		String columnName;
		for (int idx = 0; idx < cnt; idx++) {
			cursor.moveToNext();
			arr = new String[keyCnt];
			for (int keyIdx = 0; keyIdx < keyCnt; keyIdx++) {
				columnName = keys[keyIdx];
				arr[keyIdx] = cursor.getString(cursor
						.getColumnIndex(columnName));
			}
			rstArr[idx] = arr;
		}
		cursor.close();
		cursor = null;

		return rstArr;
	}

	public synchronized int getCount(String sql) {

		int count = -1;
		Cursor cursor = null;
		cursor = getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}

		cursor.close();
		cursor = null;
		return count;
	}

	public synchronized void execuate(String sql) {
		getWritableDatabase().execSQL(sql);
	}

	public synchronized void deleteTable(String tableName) {
		getWritableDatabase().delete(tableName, null, null);
	}

	public boolean isTableExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			cursor.close();
			cursor = null;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

}
