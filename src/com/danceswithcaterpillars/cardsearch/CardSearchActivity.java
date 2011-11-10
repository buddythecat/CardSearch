package com.danceswithcaterpillars.cardsearch;

import com.danceswithcaterpillars.cardsearch.content.CardCursorAdapter;
import com.danceswithcaterpillars.cardsearch.content.CardDatabaseProvider;

import static com.danceswithcaterpillars.cardsearch.database.DbConstants.*;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class CardSearchActivity extends ListActivity {
	private static final String TAG = "CardSearchActivity";
	private static final int SEARCH = Menu.FIRST;
	private static final String[] FROM = {CARD_NAME, TYPE, SUBTYPE, POWER, TOUGHNESS};
	private static final int[] TO = {R.id.card_name, R.id.card_type, R.id.card_pow};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Cursor cur = managedQuery(CardDatabaseProvider.CONTENT_URI, 
        			null, null, null, null);
        CardCursorAdapter adapter = new CardCursorAdapter(this, R.layout.card_list_item_extended, cur, FROM, TO);
        this.updateGui(adapter);
        Log.d(TAG, "Built Adapter");
        Log.d(TAG, "Count: "+this.getListAdapter().getCount());
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0, SEARCH, SEARCH, "Search").setIcon(android.R.drawable.ic_menu_search);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case SEARCH:
    		this.onSearchRequested();
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
}