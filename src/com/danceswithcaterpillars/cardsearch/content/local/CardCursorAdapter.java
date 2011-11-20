package com.danceswithcaterpillars.cardsearch.content.local;

import java.lang.ref.WeakReference;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.model.Card;
import com.danceswithcaterpillars.cardsearch.model.Deck;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CardCursorAdapter extends SimpleCursorAdapter {
	private static final String TAG = "CardCursorAdapter";
	private Context adapterContext;
	private int adapterLayout;
	private Cursor adapterCursor;
	
	public CardCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		adapterContext = context;
		adapterLayout = layout;
		Deck currentDeck = ((CardSearch)adapterContext.getApplicationContext()).currentDeck;
		Log.i(TAG, "Deck Size: "+currentDeck.getCount());
		if(currentDeck.getCount()==0){
			for(int i = 0; i<c.getCount(); i++){
				Log.i(TAG, "Added Card");
				currentDeck.addCard(new Card(c, i));
			}
		}
		//currentDeck.clearDeckCards();
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		
		adapterCursor = this.getCursor();
		adapterCursor.moveToPosition(position);
		
		//currentDeck.addCard(this.getItem(position));
		View row;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(adapterLayout, null);
		}
		else{
			row = convertView;
		}
		
		TextView view = (TextView)row.findViewById(R.id.card_name);
		Log.d(TAG, "Col: "+adapterCursor.getColumnIndex(CARD_NAME));
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(CARD_NAME)));
		
		
		view = (TextView)row.findViewById(R.id.card_rule);
		String rule = ""+adapterCursor.getString(adapterCursor.getColumnIndex(RULE));

		if(rule.length()==0){
			view.setVisibility(View.GONE);
		}
		else{
			view.setVisibility(View.VISIBLE);
			view.setText(Card.buildCardRulesWithImages(rule, adapterContext));
		}
		
		
		LinearLayout costLayout = (LinearLayout)row.findViewById(R.id.card_cost);
		costLayout.removeAllViewsInLayout();
		String manaCost = adapterCursor.getString(adapterCursor.getColumnIndex(COST));		
		Card.buildManaBar(new WeakReference<LinearLayout>(costLayout), manaCost);
		
		
		String combinedType = adapterCursor.getString(adapterCursor.getColumnIndex(TYPE));
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE)).equals("")))
				combinedType+=" - "+adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE));
		view = (TextView)row.findViewById(R.id.card_type);
		view.setText(combinedType);
		
		String pow = "";
		view = (TextView)row.findViewById(R.id.card_pow);
		//case for a creature
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(POWER)).equals(""))){
			view.setVisibility(View.VISIBLE);
			pow = adapterCursor.getString(adapterCursor.getColumnIndex(POWER));
			pow += "/"+adapterCursor.getString(adapterCursor.getColumnIndex(TOUGHNESS));
			view.setText(pow);
		}
		//Case for a planeswalker
		else if(!(adapterCursor.getString(adapterCursor.getColumnIndex(LOYALTY)).equals(""))){
			pow = "Loyalty: ";
			pow += adapterCursor.getString(adapterCursor.getColumnIndex(LOYALTY));
			view.setText(pow);
		}
		//case for an instant or sorcery
		else{
			view.setVisibility(View.GONE);
		}
		
		return row;
	}
	
	
	public Card getItem(int position){
		return new Card(adapterCursor, position);
	}
}
