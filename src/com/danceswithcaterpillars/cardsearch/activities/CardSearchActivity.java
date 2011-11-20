package com.danceswithcaterpillars.cardsearch.activities;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.local.CardCursorAdapter;
import com.danceswithcaterpillars.cardsearch.content.local.CardDatabaseProvider;
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

public class CardSearchActivity extends ListActivity {
	private static final String TAG = "CardSearchActivity";
	
	private static final int SEARCH = Menu.FIRST;
	private static final int BREAKDOWN = Menu.FIRST+1;
	
	private static final String[] FROM = {CARD_NAME, TYPE, SUBTYPE, POWER, TOUGHNESS};
	private static final int[] TO = {R.id.card_name, R.id.card_type, R.id.card_pow};
	
	private Deck current;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CardSearch application = (CardSearch)this.getApplication();
        current = application.currentDeck;
        //Deck current = Deck.class.cast(this.getIntent().getExtras().getParcelable("Deck"));
    	
        Cursor cur = managedQuery(CardDatabaseProvider.CONTENT_URI, 
        			null, DECK_ID+"=?", new String[]{String.valueOf(current.getId())}, null);
    	
        CardCursorAdapter adapter = new CardCursorAdapter(this, R.layout.card_list_item_extended, cur, FROM, TO);
        this.updateGui(adapter);
        
        Log.d(TAG, "Built Adapter");
        Log.d(TAG, "Count: "+this.getListAdapter().getCount());
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0, SEARCH, SEARCH, "Search").setIcon(android.R.drawable.ic_menu_search);
    	menu.add(1, BREAKDOWN, BREAKDOWN, "Deck Breakdown").setIcon(android.R.drawable.ic_menu_manage);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case SEARCH:
    		this.onSearchRequested();
    		return true;
    	case BREAKDOWN:
    		//before the breakdown, calculate our mana breakdown
    		//current.getBreakdown();
    		this.startActivity(new Intent(this, DeckOverviewActivity.class));
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    public void updateGui(final SimpleCursorAdapter adap){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(adap);
			}
		});
    }
    
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ViewCardActivity.class);
		Card temp = Card.class.cast(l.getAdapter().getItem(position));
		i.putExtra("Card", temp);
		this.startActivity(i);
	}
}