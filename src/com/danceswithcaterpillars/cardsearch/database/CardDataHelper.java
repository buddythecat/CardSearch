package com.danceswithcaterpillars.cardsearch.database;

import static com.danceswithcaterpillars.cardsearch.database.DbConstants.*;

import com.danceswithcaterpillars.cardsearch.model.Card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CardDataHelper extends SQLiteOpenHelper {
	private static final String TAG = "CardDataHelper";
	private static final String DATABASE_NAME = "songsData.db";
	private static final int DATABASE_VERSION = 2;
	
	
	public CardDataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
		db.execSQL(CREATE_TABLE);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
		onCreate(db);
	}
	
	public void addCardToDb(Card card){
		SQLiteDatabase db = this.getWritableDatabase();
		
		//If the card doesn't exist, add it.
		Cursor test = db.query(TABLE_NAME, getColumns(), CARD_NAME+"="+card.getName(), null , null, null, CARD_NAME); 
		if(test.moveToFirst()){
			Log.d(TAG, "Adding Card");
			ContentValues values = new ContentValues();
				
			values.put(CARD_NAME, card.getName());
			values.put(COST, card.getCost());
			values.put(TYPE, card.getType());
			values.put(SUBTYPE, card.getSubType());
			values.put(TOUGHNESS, card.getToughness());
			values.put(POWER, card.getPower());
			values.put(RULE, card.getRule());
			values.put(QUANTITY, 0);
			values.put(DECK_ID, 0);
			
			try{
				db.insertOrThrow(TABLE_NAME, null, values);
				db.close();
			}catch(SQLException e){
				Log.v(TAG, e.getLocalizedMessage());
			}
		}//If the card does exist, increment it's count
		else{
			Log.d(TAG, "Updating Card: "+card.toString());
			card.incQuantity();
			this.updateCardToDb(card);
		}
	}
	
	public int updateCardToDb(Card card){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
			
		values.put(CARD_NAME, card.getName());
		values.put(COST, card.getCost());
		values.put(TYPE, card.getType());
		values.put(SUBTYPE, card.getSubType());
		values.put(TOUGHNESS, card.getToughness());
		values.put(POWER, card.getPower());
		values.put(RULE, card.getRule());
		values.put(QUANTITY, card.getQuantity());
		values.put(DECK_ID, card.getDeckId());
		
		String[] whereArgs = new String[] {String.valueOf(card.getName())};
		return db.update(TABLE_NAME, values, CARD_NAME+"=?", whereArgs);
	}
	
	public void deleteCardFromDb(Card card){
		SQLiteDatabase db = this.getWritableDatabase();
		String[] whereArgs = new String[] {String.valueOf(card.getId())};
		db.delete(TABLE_NAME, _ID+"=?", whereArgs);
		db.close();
	}
	
	public Cursor getAllCards(){
		SQLiteDatabase db = this.getReadableDatabase();
		String [] columns = new String[]{_ID, CARD_NAME, COST, TYPE, SUBTYPE, POWER, TOUGHNESS, RULE, QUANTITY, DECK_ID};
		String order = "name";
		Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, order);
		//Cursor c = db.rawQuery("SELECT * FROM"+TABLE_NAME, new String[]{});
		return c;
	}
	
	public String[] getColumns(){
		return new String[]{_ID, CARD_NAME, COST, TYPE, SUBTYPE, POWER, TOUGHNESS, RULE, QUANTITY, DECK_ID};
	}
	public String[] getReducedColumns(){
		return new String[]{_ID, CARD_NAME, COST, TYPE, SUBTYPE, POWER, TOUGHNESS, RULE};
	}

}
