package com.danceswithcaterpillars.cardsearch.content.local.db;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.model.Card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * CardDataHelper - 
 * This class is responsible for controlling the card database.
 * It's main functions are to:
 * 	1) Add Cards
 *  2) Remove Cards
 *  3) Update Cards
 *  4) Get All Cards from a Deck
 *  5) Get Columns for cursors
 *  
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class CardDataHelper extends SQLiteOpenHelper {
	/** TAG - the class name for logging */
	private static final String TAG = "CardDataHelper";
	/** DATABASE_NAME - the database name on the disk */
	private static final String DATABASE_NAME = "cardData.db";
	
	/**
	 * Constructor - 
	 * creates a CardDataHelper by calling the super classes
	 * constructor.
	 * @param context - the context in which the instance will be used
	 */
	public CardDataHelper(Context context) {
		//call the super-classes constructor, passing the context, database name, no factory, and version
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * onCreate - 
	 * Checks to ensure that the table exists, and if not, it will create the table
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create a new one
		db.execSQL(CREATE_TABLE);
	}
	
	/**
	 * onUpgrade - 
	 * this method is called if we're upgrading to a new version of the database.
	 * we should rop the old table, and then call the onCreate again.
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//drop the table if it exists
		db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
		//create a new table
		onCreate(db);
	}
	
	/**
	 * addCardToDb(Card card, long deckId)
	 * add a card to the database by passing it a Card object.
	 * This method should do a few checks to ensure that:
	 * 	1) the card doesn't exist in the specified deck
	 * if the card does exist, we should pass the card to the
	 * update method, and increment the quantity
	 * @param card - the card to add to the database
	 * @param deckId - the Id of the deck we will add the card to
	 */
	public void addCardToDb(Card card, long deckId){
		//get a writable database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//If the card doesn't exist, add it.
		Cursor test = db.query(TABLE_NAME, null, CARD_NAME+"=? AND "+DECK_ID+"=?", new String[]{card.getName(), String.valueOf(deckId)}, null, null, CARD_NAME);
		//if the cursor is empty (if moveToFirst returns false), we should add the card
		if(!test.moveToFirst()){
			//log it out
			Log.d(TAG, "Adding Card");
			//create a set of ContentValues
			ContentValues values = card.makeContentValues();
			
			//try to insert the card into the database
			try{
				//insert into the table
				db.insertOrThrow(TABLE_NAME, null, values);
				//close the connection if we were succesful
				db.close();
			//if we weren't successfuly, catch the exception here
			}catch(SQLException e){
				//log out the message
				Log.v(TAG, e.getLocalizedMessage());
			}
		}
		//If the card does exist, check to see if we're going to increment it's count
		else{
			//make sure that we're allowed to increment the quantity
			if(card.getType().toLowerCase().equals("basic land") || card.getQuantity()<=4){
				Log.d(TAG, "Updating Card: "+card.toString());
				card.incQuantity();
				this.updateCardToDb(card, deckId);
			}
			//if we can't increase the quantity, we should just log it out...
			else{
				Log.d(TAG, "Could not increase the quantity of cards in the db");
			}
		}
	}
	
	/**
	 * updateCardToDb(Card card, long deckId)
	 * this method is used to update a card located in a deck.
	 * @param card - the card we're going to update in the database
	 * @param deckId - the deck's Id where the card is located
	 * @return - the amount of cards updated (since most of these cards are unique, it should usually only return 1)
	 */
	public int updateCardToDb(Card card, long deckId){
		//get a writable database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//create a set of ContentValues for the card we'll be updating
		ContentValues values = card.makeContentValues();		
		//set the whereArgs for the query
		String[] whereArgs = new String[] {card.getName(), String.valueOf(deckId)};
		//update the database and return the amount of rows changed
		return db.update(TABLE_NAME, values, CARD_NAME+"=? AND "+DECK_ID+"=?", whereArgs);
	}
	
	/**
	 * deleteCardFromDb(Card card) - 
	 * this should delete the passed card from the database by removing
	 * the entry's whose cardName and deckId are the same as the passed card
	 * @param card - the card whose record should be removed
	 */
	public void deleteCardFromDb(Card card){
		//get a writeable database
		SQLiteDatabase db = this.getWritableDatabase();
		//set the whereArgs
		String[] whereArgs = new String[] {String.valueOf(card.getId())};
		//delete
		db.delete(TABLE_NAME, _ID+"=?", whereArgs);
		//close the database
		db.close();
	}
	
	/**
	 * getAllCardsFromDeck(long deckId) - 
	 * this method is designed to return a cursor containing all of the cards that 
	 * are associated with a given deckId
	 * @param deckId - the deckId that we want to pull the cards from
	 * @return - a Cursor containing all of the returned cards
	 */
	public Cursor getAllCardsFromDeck(long deckId){
		//get a readable database
		SQLiteDatabase db = this.getReadableDatabase();
		//sort by the name
		String order = "name";
		//build the cursor by querying
		Cursor c = db.query(TABLE_NAME, null, DECK_ID+"=?", new String[]{String.valueOf(deckId)}, null, null, order);
		//return the cursor
		return c;
	}
}
