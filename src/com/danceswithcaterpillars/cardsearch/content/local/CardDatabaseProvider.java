package com.danceswithcaterpillars.cardsearch.content.local;

import com.danceswithcaterpillars.cardsearch.content.local.db.CardDataHelper;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * CardDatabaseProvider - 
 * This is a ContentProvider that's used to query the database for Cards.
 * It very simply manages the CardDatabase and handles insertions, deletions,
 * queries, etc.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 */
public class CardDatabaseProvider extends ContentProvider {
	/** TAG - The class name for debugging */
	public static final String TAG = "CardDatabaseContentProvider";
	/** PROVIDER_NAME - the name for this provider */
	public static final String PROVIDER_NAME = "com.danceswithcaterpillars.cardSearch.Cards";
	/** CONTENT_URI - The full URI for this content provider */
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/cards");
	
	/** cardDb - SQLite Database for the Card Database */
	private SQLiteDatabase cardDb;
	/*
	 * delete - 
	 * Delete teh card based on the uri passed to the content provider.  The tail end of the URI is the card
	 * to remove.  The selection is the WHERE clause of the statement, and the selectionArgs is to fill in '?' in the selection
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//open a writable database
		cardDb = new CardDataHelper(this.getContext()).getWritableDatabase();
		
		//set the count to 0
		int count = cardDb.delete(TABLE_NAME, selection, selectionArgs);

		//notify a change to the called context
		getContext().getContentResolver().notifyChange(uri,null);
		//close the connection to the database
		cardDb.close();
		
		//return the count of entries deleted
		return count;
	}
	
	/*
	 * getType - 
	 * returns the type of this object, which in our case is a cursor of type cards (this is the MIME type for this object).
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		//we return the type vnd.android.cursor.dir because this (for the most part) returns mutliple entries
		return "vnd.android.cursor.dir/vnd.danceswithcaterpillars.cards ";
	}
	
	/*
	 * insert - 
	 * insert a card into the database
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//check to see if the card exists (we check against the cardName and the deck associated with that card
		Cursor temp = query(uri, null, CARD_NAME+"=? AND "+DECK_ID+"=?", new String[]{values.getAsString(CARD_NAME), values.getAsString(DECK_ID)}, null);
		//check to see the result of that cursor
		if(!temp.moveToFirst()){
			//log out that we're adding a new (unique) card
			Log.v(TAG, "Inserting a new (unique) card");
			//open up a new connection to the writable database.
			cardDb = (new CardDataHelper(getContext())).getWritableDatabase();
			
			//insert into the database
			long rowId = cardDb.insert(TABLE_NAME, null, values);
			//if the rowId is greater then zero, we've made a successful entry
			if(rowId>0){
				//we want to create a uri to send back to the calling method which points to the inserted item
				Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
				//we want to notify the resolver that we've a change
				getContext().getContentResolver().notifyChange(_uri, null);
				//return the _uri of the new item
				return _uri;
			}
			//if we haven't inserted anything:
			else
				//return null back to the calling method
				return null;
		}
		//If we hit this case, the card already exists in the database (same name/deck)
		else{
			//log out
			Log.v(TAG, "Card already exists");
			//get the quantity of the associated card 
			int cardCount = temp.getInt(temp.getColumnIndex(QUANTITY));
			//if there are less then 4 cards, increment the quantity (there can't be more then 4 of a card in the deck other then mana)
			if((cardCount<=4 && !temp.getString(temp.getColumnIndex(TYPE)).toLowerCase().equals("basic land")) || (temp.getString(temp.getColumnIndex(TYPE)).toLowerCase().equals("basic land"))){
				//get the id of the card to increment
				long id = temp.getLong(temp.getColumnIndex(_ID));
				//remove the old value of the quantity from the ContentValues
				values.remove(QUANTITY);
				//put the new value of the quantity (+1) into the CVs
				values.put(QUANTITY, cardCount++);
				//create the new uri to send back to the resolver
				Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
				//update the database
				this.update(_uri, values, null, null);
				//close the cursor (we're done)
				temp.close();
				//return the updated uri
				return _uri;
			}
			//we already have the maximum amount of cards, so just send back the uri as is...
			else{
				Log.i(TAG, "Already at max for this card: "+temp.getString(temp.getColumnIndex(CARD_NAME)));
				long id = temp.getLong(temp.getColumnIndex(_ID));
				Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
				return _uri;
			}
		}
	}
	
	/*
	 * onCreate - 
	 * initialize the content provider by instantiating the context, check
	 * that we can create a writable database, return true if we can, false if we cannot.
	 * then close that database back up.
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		//create a helper
		CardDataHelper dbHelper = new CardDataHelper(getContext());
		//get the writable database
		cardDb = dbHelper.getWritableDatabase();
		//check if the db exists
		if(cardDb!=null){
			//it does, close and return true
			cardDb.close();
			return true;
		}
		else{
			//it doesn't, return false
			return false;
		}
	}
	
	/*
	 * query - 
	 * query the database and return a cursor of cards caught by the query
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		//create an SQLiteQueryBuilder
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		//set the tables for the builder to the CardDb's Table Name
		builder.setTables(TABLE_NAME);
		//check the sortOrder
		if(sortOrder == null || sortOrder== "")
			sortOrder = CARD_NAME;
		//open up a readable database
		cardDb = (new CardDataHelper(getContext()).getReadableDatabase());
		//query the database
		Cursor c = builder.query(cardDb, projection, selection, selectionArgs, null, null, sortOrder);
		//notify the resolver, appending the uri
		c.setNotificationUri(getContext().getContentResolver(), uri);
		//close the db
		cardDb.close();
		//return the cursor
		return c;
	}
	
	/*
	 * update -
	 * update a card (by name and deckid) in the database with values passed in the form of ContentValues
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		//open a writable database
		cardDb = (new CardDataHelper(getContext()).getWritableDatabase());
		//write the update and the count of changed rows
		int count = cardDb.update(TABLE_NAME, values, selection, selectionArgs);
		//notify the resolver of a change
		getContext().getContentResolver().notifyChange(uri,  null);
		//close the database
		cardDb.close();
		//return the count
		return count;
	}

}
