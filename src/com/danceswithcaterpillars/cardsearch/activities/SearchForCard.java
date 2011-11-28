package com.danceswithcaterpillars.cardsearch.activities;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.CardSearchReceiver;
import com.danceswithcaterpillars.cardsearch.content.cards.CardArrayAdapter;
import com.danceswithcaterpillars.cardsearch.content.cards.GetCardsTask;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * SearchForCard - 
 * This Activity is the one that shows the Search's Result.  When the user selects one of 
 * these results, it will bring the user to the ViewCardActivity with that Card focused.
 * 
 * It extends the ListActivity, and implements CardSearchReceiver
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class SearchForCard extends ListActivity implements CardSearchReceiver{
	/** START_SEARCH - start the search code */
	public static final String START_SEARCH = "Start_Search";
	/** cardThread  - the ExecutorService to execute the cardSearch thread */
	private ExecutorService cardThread;
	
	/*
	 * onCreate - 
	 * this overridden onCreate is responsible for building the adapter and
	 * listing the response from the SearchQuery.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_tabs);
		
		//Set the initial (empty) list
		this.setListAdapter(new ArrayAdapter<Card>(this, R.layout.card_list_item));
		
		//Get the Intent of this option
		Intent intent = this.getIntent();
		
		//check the Intent's Action, if the action is ACTION_SEARCH, we want to display a list of results.
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			//create a query from the Search's Query value (passed in with the intent)
			String query = intent.getStringExtra(SearchManager.QUERY);
			//Create a new thread executor
			cardThread = Executors.newSingleThreadExecutor();
			//execute the GetCardsTask, which is responsible for searching for the card details, passing this context, the query string, and true that we should get set details.
			cardThread.execute(new GetCardsTask(this, query, true));
		}
		//if the Intent's Action is ACTION_VIEW, the user has selected a card from the SearchSuggestions and view that card
		else if(Intent.ACTION_VIEW.equals(intent.getAction())){
			//first build the initial search'd list
			String query = intent.getStringExtra(SearchManager.QUERY);
			cardThread = Executors.newSingleThreadExecutor();
			cardThread.execute(new GetCardsTask(this, query, true));
			
			
			//get the data from the intent (which will be the card name)
			Uri uri = intent.getData();
			//create a new thread executor
			cardThread = Executors.newSingleThreadExecutor();
			//execute the thread and get the card details(this context, the card name, and true that we want details)
			cardThread.execute(new GetCardsTask(this, uri.toString(), true));
			//Once the thread returns, it will only contain one card, and we will then search on this card.
		}
		//if the intent's Action is START_SEARCH, then the user has requested another search
		else if(START_SEARCH.equals(intent.getAction())){
			//start the search
			this.onSearchRequested();
		}
		else
			//just in case we don't receive anthing, start another search
			this.onSearchRequested();
	}
	
	/*
	 * buildCardList - 
	 * for this Activity, the buildCardList can do 2 things:
	 * 	1) It may contain an actual list of cards to search for, in which case update the ui with these cards
	 * 	2) It may contain ONE card, which we then want to view.
	 * These two cases should be further differentiated, in that we should still get a full list of cards if the user
	 * only selects one.
	 * @see com.danceswithcaterpillars.cardsearch.content.CardSearchReceiver#buildCardList(java.util.LinkedList)
	 */
	@Override
	public void buildCardList(final LinkedList<Card> cards) {
		//make sure the list of cards returned isn't null
		if(cards!=null){
			//if only one card has been returned, view this card
			if(cards.size()==1){
				//create a new intent with the ViewCardActivity as the target
				Intent i = new Intent(this, ViewCardActivity.class);
				//add the card to the intent
				i.putExtra("Card", cards.getFirst());
				//start the Activity
				this.startActivity(i);
			}
			else{
			//if th user goes back, or the cardlist contains more then one card, update the UI to show all cards
				this.runOnUiThread(new Runnable(){
					public void run(){
						//create a new CardArrayAdapter from the results, binding to this context, to the card_list_item view, with the card list
						CardArrayAdapter adapter = new CardArrayAdapter(SearchForCard.this, R.layout.card_list_item, cards);
						//if the returned adapter exists, bind it to the listView
						if(adapter!=null){
							//binding occurs here
							getListView().setAdapter(adapter);
						}
					}
				});
				//done updating UI
			}
		}
		//else - our card list is empty, we should probably show a Toast or something...
	}
	
	/*
	 * onListItemClick - 
	 * if an item is selected, we should start the ViewCardActivity passing in the chosen card.
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		//create the new intent with the ViewCardActivity as the target
		Intent i = new Intent(this, ViewCardActivity.class);
		//get the card from the list
		Card temp = Card.class.cast(l.getAdapter().getItem(position));
		//pack the card into the intent
		i.putExtra("Card", temp);
		//start the activity
		this.startActivity(i);
	}
	
}
