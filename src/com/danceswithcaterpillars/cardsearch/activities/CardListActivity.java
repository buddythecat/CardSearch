package com.danceswithcaterpillars.cardsearch.activities;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.local.CardCursorAdapter;
import com.danceswithcaterpillars.cardsearch.content.local.db.CardDataHelper;
import com.danceswithcaterpillars.cardsearch.model.Card;
import com.danceswithcaterpillars.cardsearch.model.Deck;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * CardListActivity - 
 * This is the activity that is in charge of listing the cards in a deck.
 * 
 * When the activity is started, it polls the deck and lists all of the cards.
 * 
 * When a card is selected, the activity starts the EditCardInDectActivity
 * for the selected Card.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class CardListActivity extends ListActivity {
	/** TAG - The class name for logkitten */
	private static final String TAG = "CardSearchActivity";
	
	/** ADD_CARD - The Position of the ADD_CARD option in the activity menu */
	private static final int ADD_CARD = Menu.FIRST;
	/** BREAKDOWN - the position of BREAKDOWN option in the activity menu (1 after ADD_CARD) */
	private static final int BREAKDOWN = Menu.FIRST+1;
	
	/** FROM - The columns to poll from the cursor that retrieves the cards */
	private static final String[] FROM = {CARD_NAME, TYPE, SUBTYPE, POWER, TOUGHNESS};
	/** TO - the ID of the views to bind to the cursor */
	private static final int[] TO = {R.id.card_name, R.id.card_type, R.id.card_pow};
	
	/** current - the current deck, as retrieved from the application class */ 
	private Deck current;
	
    
	/*
	 * onCreate - 
	 * this overridden method is incharge of quite a few things for this class
	 * 	1) Set the contentView to the main.xml
	 * 	2) Set the currentDeck from the application class
	 * 	3) Build a cursor by polling the CardDatabaseProvider, passing the current DeckId as a parameter
	 * 	4) Build a CardCursorAdapter from the returned cursor
	 * 	5) Update the GUI, passing the returned adapter
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Retrieve the application class to store the current deck in the local varibale
        CardSearch application = (CardSearch)this.getApplication();
        current = application.currentDeck;
    	
        /*
         * Retrieve the cursor that contains the cards in this deck.  We're doing this by sending
         */
        Cursor cur = (new CardDataHelper(this.getApplicationContext())).getAllCardsFromDeck(current.getId());
        //Create an adapter from the cursor that was returned by the managedQuery to the CardDatabaseProvider
        CardCursorAdapter adapter = new CardCursorAdapter(this, R.layout.card_list_item_extended, cur, FROM, TO);
        
        //update the gui using the updateGui method, passing in the adapter
        this.updateGui(adapter);
        
        /*
         * Some simple logging, for error checking */
         Log.d(TAG, "Built Adapter");
        /*
         * Log.d(TAG, "Count: "+this.getListAdapter().getCount());
         */
    }
    
    /*
     * onCreateOptionsMenu - 
     * For this class, the onCreateOptionsMenu is in charge of building the menu, which contains only 2 options:
     * 	1) ADD_CARD - Add a card to the deck by shooting off a request to search for a card
     * 	2) BREAKDOWN - View the BreakDown of the deck (a pie chart showing the current color breakdown
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0, ADD_CARD, ADD_CARD, "Add Card").setIcon(android.R.drawable.ic_menu_search);
    	menu.add(1, BREAKDOWN, BREAKDOWN, "Deck Breakdown").setIcon(android.R.drawable.ic_menu_manage);
    	return true;
    }
    
    /*
     * onOptionsItemSelected(MenuItem item)
     * This method is in charge of handling events when a menu item is selected.
     * The two cases are:
     * 	1) ADD_CARD:
     * 		If the user selects to add a card to the deck, we simply call Activity.onSearchRequested(), which
     * 		starts the searchProvider, which will load to SearchForCard
     * 	2) BREAKDOWN:
     * 		If the user selects breakdown, we start an instance of DeckOverviewActivity, which will show a pie
     * 		chart of the current deck's mana-breakdown
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	//Add Card - start a search for the card to add
    	case ADD_CARD:
    		this.onSearchRequested();
    		return true;
    	//breakdown - show the current decks' breakdown
    	case BREAKDOWN:
    		this.startActivity(new Intent(this, DeckOverviewActivity.class));
    		return true;
    	}
    	//return the item selected
    	return super.onOptionsItemSelected(item);
    }
    
    /**
     * updateGui(final simpleCursorAdapter adap)
     * updateGui is in charge of updating the ListView's adapter.  Since this
     * must be done in it's own separate thread, this method calls Activity.runOnUiThread(Runnable r)
     * passing in an anonymous Runnable class that sets the ListAdapater for the ListActivity 
     * @param adap - the SimpleCursorAdapater to re-bind to the ListView
     */
    public void updateGui(final SimpleCursorAdapter adap){
    	/*
    	 * Create a new Runnable class that simply calls
    	 * ListActivity.setListAdapater(), passing in the supplied adapter.
    	 * All UI updates MUST be ran in a seperate thread, which is why we call 
    	 * Activity.runOnUiThread(), which handles that.
    	 */
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setListAdapter(adap);
			}
		});
    }
    
    /*
     * onListItemClick - 
     * This method handles the event that a card has been selected.  What this method does
     * is, first, call the method Activity.onListItemClick.  Secondly, we start to build a new
     * intent to show the EditCardInDeckActivity.  This intent should be packaged along with the 
     * selected card.
     * The Activity is then started
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id){
    	//Call the super listItemClick
		super.onListItemClick(l, v, position, id);
		//Initialize the Intent
		Intent i = new Intent(this, EditCardInDeckActivity.class);
		//Create the card from the adapter
		Card temp = Card.class.cast(l.getAdapter().getItem(position));
		//Pack the card into the Intent
		i.putExtra("Card", temp);
		//Start the activity
		this.startActivity(i);
	}
}