package kz.sdu.cyclingtraining;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "results";
	private static final String TABLE = "result";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE + "(id INTEGER PRIMARY KEY,"
				+ "duration TEXT,distance TEXT,speed TEXT,dt TEXT,list TEXT)");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

	public void addResult(Result result) {
		SQLiteDatabase db = this.getWritableDatabase();

		if (result.getList().size() > 0) {
			ContentValues values = new ContentValues();
			values.put("duration", result.getDuration());
			values.put("distance", result.getDistance());
			values.put("speed", result.getSpeed());
			values.put("dt", result.getDT());
			ArrayList<LatLng> list = result.getList();
			String listToString = list.get(0).latitude + "-"
					+ list.get(0).longitude;
			for (int i = 1; i < list.size(); i++) {
				listToString += ("#" + list.get(i).latitude + "-" + list.get(i).longitude);
			}
			values.put("list", listToString);

			db.insert(TABLE, null, values);
		}
		db.close();
	}

	public ArrayList<Result> getAllResults() {
		ArrayList<Result> result = new ArrayList<Result>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				String duration = cursor.getString(1);
				String distance = cursor.getString(2);
				String speed = cursor.getString(3);
				String dt = cursor.getString(4);
				String listInString = cursor.getString(5);
				ArrayList<LatLng> list = new ArrayList<LatLng>();
				String[] ll = listInString.split("#");
				for (int i = 0; i < ll.length; i++) {
					String[] l = ll[i].split("-");
					list.add(new LatLng(Double.parseDouble(l[0]), Double
							.parseDouble(l[1])));
				}

				Result r = new Result(id, dt, duration, distance, speed, list);
				result.add(r);
			} while (cursor.moveToNext());
		}
		db.close();
		return result;
	}
}
