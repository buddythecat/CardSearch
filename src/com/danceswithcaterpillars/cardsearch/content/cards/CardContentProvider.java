package com.danceswithcaterpillars.cardsearch.content.cards;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.content.CardSearchReceiver;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * CardContentProvider - 
 * this is a content provider built to query the online database of cards.  It is used for
 * search suggestions in the cardsearch.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class CardContentProvider extends ContentProvider implements CardSearchReceiver{
	/** foundCards - a linked list containing the cards returned by the search */
	private LinkedList<Card> foundCards;
	/*
	 * delete - 
	 * this method is unused, since we cannot delete any cards from the online store
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}
	
	/*
	 * getType - 
	 * this method is also unused, though it will return the type as "Card"
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		return "vnd.android.cursor.dir/vnd.danceswithcaterpillars.cards ";
	}
	
	/*
	 * insert - 
	 * this method is also unsued, since we cannot manipulate the online store of cards
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	/*
	 * onCreate - 
	 * this method is also unused, since we won't be creating, or instantiating a physical store of cards.
	 * THOUGH - in the future this method might be used to Create all the thread executors to query the
	 * online store.
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		return false;
	}
	
	/*
	 * query - 
	 * this method returns a cursor with the results from the query to the online datastore.  
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		//get the query parameter from the end segment of the URI, and convert it to all lowercase
		String query = uri.getLastPathSegment().toLowerCase();
		//create a new GetCardsTask, telling it that this is the context, passing the query string, and not to get set details
		GetCardsTask task = new GetCardsTask(this, query, false);
		//run the task, this thread will not continue until the task is completed
		task.run();
		
		/*
		 * set the columns for the Cursor.
		 * THESE COLUMNS ARE REQUIRED BY THE SEARCH MANAGER to fill for custom search suggestions
		 */
		String[] columns = {
				BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA
		};
		//Create a MatrixCursor to store the Columns
		MatrixCursor cursor = new MatrixCursor(columns);
		//iterate through the list of found cards
		for(int i = 0; i<foundCards.size(); i++){
			//for each card that was found, fill a temporary string array as follows
			/*
			 * temp[0] - index - _ID 
			 * temp[1] - name - SUGGEST_COLUMN_TEXT_1
			 * temp[2] - cost - SUGGEST_COLUMN_TEXT_2
			 * temp[3] - name - SUGGEST_COLUMN_INTENT_DATA
			 */
			String[] tmp = {Integer.toString(i), foundCards.get(i).getName(), foundCards.get(i).getCost(), foundCards.get(i).getName()};
			//add the row to the matrixCursor
			cursor.addRow(tmp);
		}
		//return the MatrixCursor
		return cursor;
	}
	/*
	 * update - 
	 * unused, since we will not be manipulating the online store of cards
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
	/*
	 * buildCardList - 
	 * When the task is completed, this function is called, which fills the card list from the task.
	 * Once this method is completed, and the cards have been set, the thread returns back to it's position
	 * in CardContentProvider.query()
	 * @see com.danceswithcaterpillars.cardsearch.content.CardSearchReceiver#buildCardList(java.util.LinkedList)
	 */
	@Override
	public void buildCardList(LinkedList<Card> cards) {
		//set the list of cards
		this.foundCards = cards;
		
	}

}
