package com.antics.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {
	//Ants
	public static final String KEY_TYPE = "ant_type"; // 0 Builder 1 Collector 2 Queen
	public static final String KEY_X = "positionX";	// co-ordinate
	public static final String KEY_Y = "positionY"; // co-ordinate
	public static final String KEY_GX = "graphicsX";
	public static final String KEY_GY = "graphicsY";
	public static final String KEY_HUNGER = "hunger"; // hunger level
	public static final String KEY_FOOD = "food";	// 0 for no 1 for yes
	
	//Tiles
	
	
	private static final String DB_NAME = "AntWorld";	
	private static final String DB_TABLE = "ants";
	private static final int DB_VERSION = 2;
	
	private static final String DB_CREATE = 
			"CREATE TABLE IF NOT EXISTS ants (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "ant_type INTEGER, positionX INTEGER, positionY INTEGER, food INTEGER);";
	
	private final Context context;
	
	private DatabaseHelper DBHelper;
	private static SQLiteDatabase db;
	
	public Database(Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		DatabaseHelper(Context context){
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			try{
				db.execSQL(DB_CREATE);
			}catch (SQLException e){
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int oldVers, int newVers) {
			Log.w("ASDatabase", "Upgrading database to new version - will destroy all old data.");
			db.execSQL("DROP TABLE IF EXISTS ants");
			onCreate(db);
		}
	}
	
	// Open database for communication
	public Database open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	// Close
	public void close(){
		DBHelper.close();
	}
	
	// Add an ant to the database
	public long insertAnt(int type, int x, int y, int hunger, int food){
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, type);
		values.put(KEY_X, x);
		values.put(KEY_Y, y);
		values.put(KEY_HUNGER, hunger);
		values.put(KEY_FOOD, food);
		return db.insert(DB_TABLE, null, values);
	}
	
	public Cursor getAll(){
		return db.query(DB_TABLE, new String[] {KEY_TYPE, KEY_X, KEY_Y, KEY_FOOD}, 
				null, null, null, null, null);
	}
	
	public void deleteAll(){
		db.rawQuery("DELETE FROM " + DB_TABLE, null);
	}
}
