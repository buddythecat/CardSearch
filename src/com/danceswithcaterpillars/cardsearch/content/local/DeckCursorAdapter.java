package com.danceswithcaterpillars.cardsearch.content.local;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DeckDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.model.Deck;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DeckCursorAdapter extends SimpleCursorAdapter {
	private Context adapterContext;
	private Cursor adapterCursor;
	private int adapterLayout;
	
	public DeckCursorAdapter(Context context, int layout, Cursor c,	String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.adapterContext = context;
		this.adapterLayout = layout;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		adapterCursor = this.getCursor();
		adapterCursor.moveToPosition(position);
		View row;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(adapterLayout, null);
		}
		else{
			row = convertView;
		}
		
		TextView view = (TextView)row.findViewById(R.id.deck_list_name);
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(DECK_NAME)));
		
		view = (TextView)row.findViewById(R.id.deck_list_type);
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(TYPE)));
		
		return row;
	}
	
	public Deck getItem(int position){
		adapterCursor.moveToPosition(position);
		return new Deck(adapterCursor);
	}
}
