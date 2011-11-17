package com.danceswithcaterpillars.cardsearch.content.local;

import com.danceswithcaterpillars.cardsearch.content.local.db.CardDataHelper;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DbConstants.*;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @author snake
 *	The framework for this content provider DOES COME FROM AN EXTERNAL SOURCE
 *	I HEAVILY relied on the site
 *	http://www.devx.com/wireless/Article/41133/1763/page/2
 *	for an example. 
 *	The original author this basis is Wei-Meng Lee.
 */
public class CardDatabaseProvider extends ContentProvider {
	public static final String TAG = "CardDatabaseContentProvider";
	public static final String PROVIDER_NAME = "com.danceswithcaterpillars.cardSearch.Cards";
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/cards");
	
	private static final int CARDS = 1;
	private static final int CARDS_ID = 2;
	
	private SQLiteDatabase cardDb;
	
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "cards", CARDS);
		uriMatcher.addURI(PROVIDER_NAME, "cards/#", CARDS_ID);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch(uriMatcher.match(uri)){
		case CARDS:
			count = cardDb.delete(TABLE_NAME, selection, selectionArgs);
			break;
		case CARDS_ID:
			String id = uri.getPathSegments().get(1);
			count = cardDb.delete(TABLE_NAME, 
					_ID + " = "+ id + 
					(!TextUtils.isEmpty(selection) ? " AND (" + selection + ")": ""), selectionArgs);
			break;
		default:
			Log.d(TAG, "Illegal Argument");
		}
		getContext().getContentResolver().notifyChange(uri,null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
		case CARDS:
			return "vnd.android.cursor.dir/vnd.danceswithcaterpillars.cards ";
		case CARDS_ID:
			return "vnd.android.cursor.item/vnd.danceswithcaterpillars.cards ";
		default:
			throw new IllegalArgumentException("Unsupported URI: "+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Cursor temp = query(uri, null, CARD_NAME+"='"+values.getAsString(CARD_NAME)+"'", null, null);
		if(!temp.moveToFirst()){
			Log.v(TAG, "Inserting a new card");
			long rowId = cardDb.insert(TABLE_NAME, null, values);
			if(rowId>0){
				Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(_uri, null);
				return _uri;
			}
			else
				return null;
		}
		else{
			Log.v(TAG, "Card already exists");
			int cardCount = temp.getInt(temp.getColumnIndex(QUANTITY));
			long id = temp.getLong(temp.getColumnIndex(_ID));
			values.remove(QUANTITY);
			values.put(QUANTITY, cardCount++);
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
			this.update(_uri, values, null, null);
			temp.close();
			return _uri;
		}
	}

	@Override
	public boolean onCreate() {
		Context ctx = this.getContext();
		CardDataHelper dbHelper = new CardDataHelper(ctx);
		cardDb = dbHelper.getWritableDatabase();
		return (cardDb!=null);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TABLE_NAME);
		
		if(uriMatcher.match(uri)==CARDS_ID){
			builder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
		}
		if(sortOrder == null || sortOrder== "")
			sortOrder = CARD_NAME;
		
		Cursor c = builder.query(cardDb, projection, selection, selectionArgs, null, null, sortOrder);
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch(uriMatcher.match(uri)){
		case CARDS:
			count = cardDb.update(TABLE_NAME, values, selection, selectionArgs);
			break;
		case CARDS_ID:
			count = cardDb.update(TABLE_NAME, values, 
					_ID + " = " + uri.getPathSegments().get(1) + 
					(!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), 
					selectionArgs);
			break;
		default:
			Log.d(TAG, "Illegal Argument");
		}
		getContext().getContentResolver().notifyChange(uri,  null);
		return count;
	}

}
