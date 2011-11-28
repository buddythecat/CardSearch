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
import android.widget.Toast;

/**
 * DeckListActivity - 
 * This Activity is designed to show the user a list of their decks.
 * From this Activity, the user can choose to either:
 * 	1) View a deck and it's cards
 * 	2) Add a new deck
 * 	3) Remove a deck
 * 	4) Edit a deck and it's details
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */

public class DeckListActivity extends ListActivity {
	/** TO - the views to bind the cursor adapter to */
	private final int[] TO = {R.id.deck_list_name, R.id.deck_list_type};
	/** FROM - the columns to pull from the cursor */
	private final String[] FROM = {DECK_NAME, TYPE};
    
	/** NEW_DECK - the menu option to create a new Deck */
	private static final int NEW_DECK = Menu.FIRST;
	
	/*
	 * onCreate - 
	 * this method builds the Activity, sets the contentView and initializes the adapter
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		//create the Activity and set the content view
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_list_view);
		//initialize the adapters
		this.initAdapter();
	}
	
	/*
	 * onCreateOptionsMenu - 
	 * add the menu items to the deck, which are:
	 * 	1) NEW_DECK - Add a new deck to the database
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//Add the NEW_DECK option to the menu, and set the icon to android's 'add' icon
		menu.add(0, NEW_DECK, NEW_DECK, "New Deck").setIcon(android.R.drawable.ic_menu_add);
		return true;
	}
	
	/*
	 * onOptionsItemSelected - 
	 * This method is designed handle when menuItem's are selected, which at the moment has 
	 * the following cases:
	 * 	1) NEW_DECK - starts the CreateDeckActivity with the requestCode NEW_DECK
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	//if the item's ID is NEW_DECK, start the CreateDeckActivity with the requestCode of NEW_DECK
    	case NEW_DECK:
    		this.startActivityForResult(new Intent(this, CreateDeckActivity.class),NEW_DECK);
    		//return true
    		return true;
    	}
    	//this should be unreachable, but is requried
    	return super.onOptionsItemSelected(item);
    }
	
   /*
    * onActivityResult - 
    * this is for when CreateDeckActivity (whose request code is NEW_DECK) returns from with it's status code, which can be
    * one of two options:
    * 	1) RESULT_OK - The deck was added successfully
    * 	2) RESULT_CANCELLED - the deck wasn't added
    * If the result is OK, we should update the UI thread with the new adapter.  If not, why bother?
    * 
    * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
    */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent i){
	   //Check to see if the requestCode is for NEW_DECK and that the result was OK
	   if(requestCode==NEW_DECK && resultCode==RESULT_OK){
		   //A deck was added to the database - we need to update the list.  Start a new thread to update the UI
		   this.runOnUiThread(new Runnable(){
				public void run(){
					//Create a new cursor that contains all of the decks in our database
					Cursor c = (new DeckDataHelper(getApplicationContext())).getAllDecks();
					//build a DeckCursorAdapater from this cursor, and bind it to the Columns and Views
					DeckCursorAdapter adapt = new DeckCursorAdapter(getApplicationContext(), R.layout.deck_list_item, c, FROM, TO);
					//Check to make sure our adapters not null! If it isn't, lets set bind the adapter to the view
					if(adapt !=null)
						getListView().setAdapter(adapt);
				}
			});
	   }
	   //If the CreateDeckActivity comes back with the RESULT_CANCELLED, let's just show a toast notifying the user that no deck was created
	   else if(requestCode==NEW_DECK && resultCode==RESULT_CANCELED){
		   Toast.makeText(this, "No deck added", Toast.LENGTH_SHORT).show();
	   }
   }
    
   /**
    * initAdapater - 
    * this method is in charge of initializing the adapters when the activity is first created.
    * It queries the Deck Database and returns a cursor containing all of the decks ordered by their name.
    * This Cursor is then bound to a DeckCursorAdapater, which is tied to the views and columns specified, and 
    * is then bound to the ListView for this Activity.
    */
   private void initAdapter(){
	   //Get the cursor from the Deck Database
	   Cursor c = (new DeckDataHelper(this)).getAllDecks();
	   //Build a DeckCursorAdapter from this Cursor
	   DeckCursorAdapter adapt = new DeckCursorAdapter(getApplicationContext(), R.layout.deck_list_item, c, FROM, TO);
	   //Set the ListViews adapter to the DeckCursorAdapter
	   this.setListAdapter(adapt);
   }
	
   /*
    * onListItemClick - 
    * this method's job is to handle when a user selects a deck from the list of decks.
    * It should create a new Intent whose target is the CardListActivity, whose action is "Manage Deck".
    * The method then builds the Deck from the Adapater, and sets the Application class's currentDeckId and currentDeck
    * to the Deck build from the Adapter.
    * The Deck is then packed up into the Intent, and the Activity is started.
    * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
    */
   @Override
   protected void onListItemClick(ListView l, View v, int position, long id){
	   super.onListItemClick(l, v, position, id);
	   Intent i = new Intent(this, CardListActivity.class);
	   i.setAction("Manage Deck");
	   Deck temp = (Deck)(l.getAdapter().getItem(position));
	   ((CardSearch)this.getApplication()).currentDeckId = temp.getId();
	   ((CardSearch)this.getApplication()).currentDeck = temp;
	   i.putExtra("Deck", temp);
	   this.startActivity(i);
   }
	
}
