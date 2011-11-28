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

/**
 * CardCursorAdapater - 
 * A SimpleCursorAdapter who's specialized to handle a Cursor that contains a list of cards.
 * This adapter then binds to a specialized layout, which will often contain the information of the card, in detail.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class CardCursorAdapter extends SimpleCursorAdapter {
	/** TAG - the tag for this class for debugging */
	private static final String TAG = "CardCursorAdapter";
	/** adapterContext - the context inwhich this adapter is being used*/
	private Context adapterContext;
	/** adapterLayout - the ID of the layout bound to this adapter */
	private int adapterLayout;
	/** adapterCursor - the cursor that will be bound to this adapter */
	private Cursor adapterCursor;
	
	/**
	 * Constructor - 
	 * Creates a CardCursorAdapter that will bind to the parameters supplied
	 * This constructor may have to build a deck if the deck hasn't been instantiated.  The
	 * logic to do that is embedded here, as a flow control.
	 * 
	 * @param context - the context in which this adapter will be used
	 * @param layout - the ID of the layout xml which the adapter will use
	 * @param c - the cursor that contains the data for this adapter 
	 * @param from - the columns in the cursor which we will bind from
	 * @param to - the views which we will bind to
	 */
	public CardCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		//call the super's constructor
		super(context, layout, c, from, to);
		//set the context
		adapterContext = context;
		//set the layout id
		adapterLayout = layout;
		//get the current deck (when the adapter's created we're going to build the deck)
		Deck currentDeck = ((CardSearch)adapterContext.getApplicationContext()).currentDeck;
		//log out the size of the current deck (should normally be zero, but if not we don't have to build it)
		Log.i(TAG, "Deck Size: "+currentDeck.getCount());
		//the current deck is empty
		if(currentDeck.getCount()==0){
			//loop through the cursor and add each card to the deck (this loop will short-circuit if the cursor is empty)
			for(int i = 0; i<c.getCount(); i++){
				//log we're adding the card
				Log.i(TAG, "Added Card");
				//create and add the card
				currentDeck.addCard(new Card(c, i));
			}
		}
	}
	
	/**
	 * getView - 
	 * this method is responsible for binding the data to the views, and has been used (almost exactly) in other
	 * parts of this application.  
	 * @TODO encapsulate this somewhere
	 * @see android.widget.CursorAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//get the cursor
		adapterCursor = this.getCursor();
		//move to the position where we're binding
		adapterCursor.moveToPosition(position);
		
		//create a view to store the row
		View row;
		//check if we're using a converted view
		if(convertView == null){
			//if not, we need to get a LayoutInflator, and inflate the bound layout
			LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//set the inflated layout to the row's view
			row = vi.inflate(adapterLayout, null);
		}
		//we're using a converted view, have row reference this view.
		else{
			row = convertView;
		}
		
		//bind the card's name
		TextView view = (TextView)row.findViewById(R.id.card_name);
		//Log.d(TAG, "Col: "+adapterCursor.getColumnIndex(CARD_NAME));
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(CARD_NAME)));
		
		//bind the card's rule, basic stuff here, and it can be seen all around the program.  I won't re-explain because it takes time
		view = (TextView)row.findViewById(R.id.card_rule);
		String rule = ""+adapterCursor.getString(adapterCursor.getColumnIndex(RULE));

		if(rule.length()==0){
			view.setVisibility(View.GONE);
		}
		else{
			view.setVisibility(View.VISIBLE);
			view.setText(Card.buildCardRulesWithImages(rule, adapterContext));
		}
		
		//create the mana cost layout.  Same as the rule, if you want to see how this works, check other getView methods
		LinearLayout costLayout = (LinearLayout)row.findViewById(R.id.card_cost);
		costLayout.removeAllViewsInLayout();
		String manaCost = adapterCursor.getString(adapterCursor.getColumnIndex(COST));		
		Card.buildManaBar(new WeakReference<LinearLayout>(costLayout), manaCost);
		
		//get the combined type of the card, which isn't too bad.  Just don't append the '-' if it's only a single-type card
		String combinedType = adapterCursor.getString(adapterCursor.getColumnIndex(TYPE));
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE)).equals("")))
				combinedType+=" - "+adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE));
		view = (TextView)row.findViewById(R.id.card_type);
		view.setText(combinedType);
		
		//bind the power/toughness or loyalty for a planeswalker - see how this works in other getView methods
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
		
		//return the row
		return row;
	}
	
	/**
	 * getItem - 
	 * returns a Card from the specified position in the cursor
	 * @see android.widget.CursorAdapter#getItem(int)
	 */
	@Override
	public Card getItem(int position){
		return new Card(adapterCursor, position);
	}
}
