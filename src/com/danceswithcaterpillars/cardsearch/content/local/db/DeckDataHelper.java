package com.danceswithcaterpillars.cardsearch.content.local.db;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DeckDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.model.Deck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DeckDataHelper -
 * This is a SQLiteOpenHelper that is very similar to the CardDataHelper, though this
 * class is much simpler, since the Deck database, by nature, is simpler.
 * 
 * This handles addition, deletion, updating and queries
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class DeckDataHelper extends SQLiteOpenHelper {
	/** TAG - The classname for logging */
	private static final String TAG = "DeckDataHelper";
	/** DATABASE_NAME - the name of the database file on disk */
	private static final String DATABASE_NAME = "deckData.db";
	
	/**
	 * Constructor -
	 * creates a Database Helper in the passed context
	 * @param context - the context in which this helper will be used
	 */
	public DeckDataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	/**
	 * onCreate -
	 * creates a database of decks
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}
	
	/**
	 * onUpgrade - 
	 * checks if a database exists, if it does, the table is removed, and a new one is created
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
		onCreate(db);
	}
	
	/**
	 * addDeckToDb - 
	 * add a deck to the database.
	 * the deck (should) be unique, but I don't think it's worth enforcing for some reason
	 * @param deck the deck to add
	 * @return true if the deck was added successfuly
	 */
	public boolean addDeckToDb(Deck deck){
		//get a writable database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//log that we're adding a deck
		Log.d(TAG, "Adding deck");
		//create a new set of content values
		ContentValues values = new ContentValues();
		
		//add the deck's data into the content values
		values.put(DECK_NAME, deck.getName());
		values.put(CARD_COUNT, deck.getCount());
		values.put(COLOR, deck.getColorAsString());
		values.put(TYPE, deck.getType());
		
		try{
			//try to insert the new row
			db.insertOrThrow(TABLE_NAME, null, values);
			//close the database
			db.close();
			//return true, since no error was thrown
			return true;
		//if we trigger this catch, the row wasn't enterred
		}catch(SQLException e){
			//log the error we've received when the deck was attempted to be inserted
			Log.v(TAG, e.getLocalizedMessage());
			//return false, we did not succesfully add the deck
			return false;
		}
	}
	
	/**
	 * updateDeckToDb - 
	 * this method is used to update the deck (which is passed to the method),
	 * whether that be deck name, breakdown, whatever.
	 * @param deck - the deck to update
	 * @return the amount of rows updated, 0 on a failure
	 */
	public int updateDeckToDb(Deck deck){
		//get a writable database
		SQLiteDatabase db = this.getWritableDatabase();
		//create a new set of content values
		ContentValues values = new ContentValues();
		
		//pack the values
		values.put(DECK_NAME, deck.getName());
		values.put(CARD_COUNT, deck.getCount());
		values.put(COLOR, deck.getColorAsString());
		values.put(TYPE, deck.getType());
		
		//set the where args - we're querying based on ID
		String[] whereArgs = new String[] {String.valueOf(deck.getId())};
		//query, and return the result
		return db.update(TABLE_NAME, values, _ID+"=?", whereArgs);
	}
	
	/**
	 * deleteDeckFromDb - 
	 * deletes the deck from the databased, based on the deck's ID.
	 * @param Deck the deck to delete
	 */
	public void deleteDeckFromDb(Deck deck){
		//get a writable database
		SQLiteDatabase db = this.getWritableDatabase();
		//set the whereArgs
		String[] whereArgs = new String[] {String.valueOf(deck.getId())};
		//delete the row
		db.delete(TABLE_NAME, _ID+"=?", whereArgs);
		//close the database
		db.close();
	}
	
	/**
	 * getAllDecks - 
	 * this method is designed to return a cursor which lists all of the decks the user currently has.
	 * @return A Cursor containing all of the decks currently being indexed by the program
	 */
	public Cursor getAllDecks(){
		//get a readable database
		SQLiteDatabase db = this.getReadableDatabase();
		//no columns, just query based on null since we will use all of them
		//order by deck name
		String order = DECK_NAME;
		//query the database
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, order);
		//return the Cursor
		return c;
	}
	
	/**
	 * getDeckByName -
	 * retrieves a deck from the databased based on that deck's name.  This method
	 * might want to be depreciated in the future, since the name's may not be unique.
	 * TODO depreciate this method and replace with getDeckById
	 * @return a cursor containing the Deck
	 */
	public Cursor getDeckByName(String name){
		//get a readable database
		SQLiteDatabase db = this.getReadableDatabase();
		//Open a cursor and fill it with the query's result
		Cursor c = db.query(TABLE_NAME, null, DECK_NAME+"="+name, null, null, null, DECK_NAME);
		//return the cursor
		return c;
	}
	
	/**
	 * getDeckById - 
	 * retrieves a deck from the database based on that deck's ID.  This method
	 * should most likely replace getDeckByName.  Though even this is probably imperfect
	 * and should be really surpased by getDeckByDeck... heh.
	 * @return a cursor containing the deck
	 */
	public Cursor getDeckById(long id){
		//get a readable database
		SQLiteDatabase db = this.getReadableDatabase();
		//Open a cursor and fill it with the query's result
		Cursor c = db.query(TABLE_NAME, null, _ID+"=?", new String[]{String.valueOf(id)}, null, null, DECK_NAME);
		//return the cursor
		return c;
	}	
}
