package com.danceswithcaterpillars.cardsearch.content.local.db;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DeckDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.model.Card;
import com.danceswithcaterpillars.cardsearch.model.deck.Deck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DeckDataHelper extends SQLiteOpenHelper {
	private static final String TAG = "DeckDataHelper";
	private static final String DATABASE_NAME = "deckData.db";
	
	public DeckDataHelper(Context context) {
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
	
	public void addDeckToDb(Deck deck){
		SQLiteDatabase db = this.getWritableDatabase();
		
		//If the card doesn't exist, add it.
		Cursor test = db.query(TABLE_NAME, getColumns(), DECK_NAME+"="+deck.getName(), null , null, null, DECK_NAME); 
		if(test.moveToFirst()){
			Log.d(TAG, "Adding Card");
			ContentValues values = new ContentValues();
				
			values.put(DECK_NAME, deck.getName());
			values.put(CARD_COUNT, deck.getCount());
			values.put(COLOR, deck.getColor());
			values.put(TYPE, deck.getType());
			
			try{
				db.insertOrThrow(TABLE_NAME, null, values);
				db.close();
			}catch(SQLException e){
				Log.v(TAG, e.getLocalizedMessage());
			}
		}//If the card does exist, increment it's count
		else{
			Log.d(TAG, "Deck already exists: "+deck.toString());
		}
	}
	
	public int updateDeckToDb(Deck deck){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
			
		values.put(DECK_NAME, deck.getName());
		values.put(CARD_COUNT, deck.getCount());
		values.put(COLOR, deck.getColor());
		values.put(TYPE, deck.getType());
		
		String[] whereArgs = new String[] {String.valueOf(deck.getName())};
		return db.update(TABLE_NAME, values, DECK_NAME+"=?", whereArgs);
	}
	
	public void deleteDeckFromDb(Card card){
		SQLiteDatabase db = this.getWritableDatabase();
		String[] whereArgs = new String[] {String.valueOf(card.getId())};
		db.delete(TABLE_NAME, _ID+"=?", whereArgs);
		db.close();
	}
	
	public Cursor getAllDecks(){
		SQLiteDatabase db = this.getReadableDatabase();
		String [] columns = new String[]{_ID, DECK_NAME, CARD_COUNT, COLOR, TYPE};
		String order = DECK_NAME;
		Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, order);
		//Cursor c = db.rawQuery("SELECT * FROM"+TABLE_NAME, new String[]{});
		return c;
	}
	
	public Cursor getDeckByName(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, getColumns(), DECK_NAME+"="+name, null, null, null, DECK_NAME);
		return c;
	}
	
	public String[] getColumns(){
		return new String[]{_ID, DECK_NAME, CARD_COUNT, COLOR, TYPE};
	}

}
