package com.danceswithcaterpillars.cardsearch.content.local.db;

import android.provider.BaseColumns;

/**
 * DeckDatabaseConstants - 
 * this class is responsible for storing constants for the deck database.
 * Most of the constants are very self-evident, so comments should not be
 * necessary.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public interface DeckDatabaseConstants extends BaseColumns{
	public static final int DATABASE_VERSION= 1;
	
	public static final String TABLE_NAME 	= "deckDb";
	public static final String DECK_NAME 	= "deckName";
	public static final String CARD_COUNT 	= "cardCount";
	public static final String COLOR		= "color";
	public static final String TYPE			= "type";

	public static final String CREATE_TABLE = 
			"CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( " + 
			_ID 		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			DECK_NAME 	+ " TEXT, "    +
			CARD_COUNT	+ " TEXT, "    +
			COLOR		+ " TEXT, "    +
			TYPE		+ " TEXT"     + 
			" );";
}
