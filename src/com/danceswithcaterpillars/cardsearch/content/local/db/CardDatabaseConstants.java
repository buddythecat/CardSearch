package com.danceswithcaterpillars.cardsearch.content.local.db;

import android.provider.BaseColumns;

/**
 * CardDatabaseConstants - 
 * This interface is used to store the constants for the card database.
 * All of the constants are very self-evident, so I will not be heavily 
 * commenting this file.
 * @author Rich "Dances With Caterpillars"
 *
 */
public interface CardDatabaseConstants extends BaseColumns{
	public static final int DATABASE_VERSION= 5;
	
	public static final String TABLE_NAME 	= "cardDb";
	public static final String CARD_NAME 	= "cardName";
	public static final String COST 		= "cost";
	public static final String TYPE 		= "type";
	public static final String SUBTYPE 		= "subtype";
	public static final String POWER 		= "power";
	public static final String TOUGHNESS 	= "toughness";
	public static final String RULE 		= "rule";
	public static final String QUANTITY 	= "quantity";
	public static final String DECK_ID		= "deck_id";
	public static final String LOYALTY		= "loyalty";
	public static final String SET_INFO		= "set_info";
	public static final String MULTI		= "multi";
	public static final String CREATE_TABLE = 
			"CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( " + 
			_ID 		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			CARD_NAME 	+ " TEXT, "    +
			COST 		+ " TEXT, "    +
			TYPE 		+ " TEXT, "    +
			SUBTYPE 	+ " TEXT,"     + 
			POWER 		+ " TEXT, "    +
			TOUGHNESS 	+ " TEXT, "    +
			RULE 		+ " TEXT, "    +
			QUANTITY	+ " INTEGER, " +
			DECK_ID		+ " INTEGER, " +
			LOYALTY		+ " TEXT, "    +
			SET_INFO	+ " TEXT, "    +
			MULTI		+ " TEXT "	   +
			" );";
}
