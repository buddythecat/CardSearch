package com.danceswithcaterpillars.cardsearch.activities;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DeckDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.local.DeckCursorAdapter;
import com.danceswithcaterpillars.cardsearch.content.local.db.DeckDataHelper;
import com.danceswithcaterpillars.cardsearch.model.Deck;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class DeckListActivity extends ListActivity {
	private final int[] TO = {R.id.deck_list_name, R.id.deck_list_type};
	private final String[] FROM = {DECK_NAME, TYPE};
    
	private static final int NEW_DECK = Menu.FIRST;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_list_view);
		
		this.initAdapter();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, NEW_DECK, NEW_DECK, "New Deck").setIcon(android.R.drawable.ic_menu_add);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case NEW_DECK:
    		this.startActivityForResult(new Intent(this, CreateDeckActivity.class),NEW_DECK);
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
	
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent i){
	   if(requestCode==NEW_DECK && resultCode==RESULT_OK){
		   this.runOnUiThread(new Runnable(){
				public void run(){
					Cursor c = (new DeckDataHelper(getApplicationContext())).getAllDecks();
					DeckCursorAdapter adapt = new DeckCursorAdapter(getApplicationContext(), R.layout.deck_list_item, c, FROM, TO);
					if(adapt !=null)
						getListView().setAdapter(adapt);
				}
			});
	   }
	   else if(requestCode==NEW_DECK && resultCode==RESULT_CANCELED){
		   //response back on an error
		   //no need to re-render the list
	   }
   }
    
	private void initAdapter(){
		Cursor c = (new DeckDataHelper(this)).getAllDecks();
		DeckCursorAdapter adapt = new DeckCursorAdapter(getApplicationContext(), R.layout.deck_list_item, c, FROM, TO);
		this.setListAdapter(adapt);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, CardSearchActivity.class);
		i.setAction("Manage Deck");
		Deck temp = (Deck)(l.getAdapter().getItem(position));
		((CardSearch)this.getApplication()).currentDeckId = temp.getId();
		((CardSearch)this.getApplication()).currentDeck = temp;
		i.putExtra("Deck", temp);
		this.startActivity(i);
	}
	
}
