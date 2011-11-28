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

/**
 * DeckCursorAdapter - 
 * Another SimpleCursorAdapter which works VERY SIMILARLY to the CardCursorAdapter.
 * The comments on this will be sparse.  If you wish to see more details on how this will work,
 * please reference
 * @see com.danceswithcaterpillars.cardsearch.content.local.CardCursorAdapter
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class DeckCursorAdapter extends SimpleCursorAdapter {
	/** adapterContext - the context for this adapter */
	private Context adapterContext;
	/** adapterCursor - the cursor for this adapter */
	private Cursor adapterCursor;
	/** adapterLayout - the layout bound to this adapter */
	private int adapterLayout;
	
	/**
	 * Constructor - 
	 * creates a deck cursor adapter from the following parameters
	 * @param context - the context that this adpater is used within
	 * @param layout - the layout that this adpater is bound to
	 * @param c - the data which this adapter is bound to
	 * @param from - the columns of the cursor that we will bind with
	 * @param to - the views that we will bind these columns to
	 */
	public DeckCursorAdapter(Context context, int layout, Cursor c,	String[] from, int[] to) {
		//do what we always do
		super(context, layout, c, from, to);
		this.adapterContext = context;
		this.adapterLayout = layout;
	}
	
	/**
	 * getView - 
	 * Again, very very standard stuff here.  I really don't want to continue to comment this like a madman.  
	 * The method will create the views for the adapter.
	 * 
	 * I'd like to add a card-image for each deck, so I'll attach a todo on this method
	 * @TODO add an imageview for the card name
	 * @see android.widget.CursorAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//get the cursor
		adapterCursor = this.getCursor();
		//move the cursor to the current position
		adapterCursor.moveToPosition(position);
		//bind to the row
		View row;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(adapterLayout, null);
		}
		else{
			row = convertView;
		}
		
		//set the deck's name
		TextView view = (TextView)row.findViewById(R.id.deck_list_name);
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(DECK_NAME)));
		
		//set the deck's type
		view = (TextView)row.findViewById(R.id.deck_list_type);
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(TYPE)));
		
		//return the row
		return row;
	}
	
	/**
	 * getItem - 
	 * returns a deck for the item located at the supplied position in the cursor
	 * @see android.widget.CursorAdapter#getItem(int)
	 */
	@Override
	public Deck getItem(int position){
		adapterCursor.moveToPosition(position);
		return new Deck(adapterCursor);
	}
}
