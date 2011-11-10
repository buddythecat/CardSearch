package com.danceswithcaterpillars.cardsearch.content;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.model.Card;
import com.danceswithcaterpillars.cardsearch.model.CardSearchReciever;
import com.danceswithcaterpillars.cardsearch.model.GetCardsTask;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class CardContentProvider extends ContentProvider implements CardSearchReciever{
	private LinkedList<Card> foundCards;
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		String query = uri.getLastPathSegment().toLowerCase();
		GetCardsTask task = new GetCardsTask(this, query);
		task.run();
		
		String[] columns = {
				BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA
		};
		MatrixCursor cursor = new MatrixCursor(columns);
		for(int i = 0; i<foundCards.size(); i++){
			String[] tmp = {Integer.toString(i), foundCards.get(i).getName(), foundCards.get(i).getCost(), foundCards.get(i).getName()};
			cursor.addRow(tmp);
		}
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void buildCardList(LinkedList<Card> cards) {
		this.foundCards = cards;
		
	}

}
