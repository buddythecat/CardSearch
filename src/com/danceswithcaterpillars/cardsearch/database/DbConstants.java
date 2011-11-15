package com.danceswithcaterpillars.cardsearch.database;

import android.provider.BaseColumns;

public interface DbConstants extends BaseColumns{
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
	public static final String CREATE_TABLE = 
			"CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ( " + 
			_ID 		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			CARD_NAME 	+ " TEXT, " +
			COST 		+ " TEXT, " +
			TYPE 		+ " TEXT, " +
			SUBTYPE 	+ " INTEGER," + 
			POWER 		+ " INTEGER, " +
			TOUGHNESS 	+ " INTEGER, " +
			RULE 		+ " TEXT, " +
			QUANTITY	+ " INTEGER, " +
			DECK_ID		+ " INTEGER " +
			" );";
}
