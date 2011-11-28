package com.danceswithcaterpillars.cardsearch.activities;

import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.local.db.DeckDataHelper;
import com.danceswithcaterpillars.cardsearch.model.Deck;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * CreateDeckActivity - 
 * This activity is in charge of creating a new Deck.
 * It's very simple, it allows for the user to enter in a
 * name for the deck, and the type of the deck.
 * For more information on Deck types for MTG, see:
 * http://en.wikipedia.org/wiki/Magic:_The_Gathering_deck_types
 *  
 * @author Rich "Dances With Cateprillars" Tufano
 *
 */
public class CreateDeckActivity extends Activity {
	/** deckName - the EditText that stores the name of the deck the user wants to create. This field is required, and does not have to be unique */
	private EditText deckName;
	/** deckType - the Spinner that stores the type of deck the user wants to create.  This is also required */
	private Spinner deckType;
	
	/*
	 * onCreate - 
	 * This is in charge of building the initial view
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_create_view);
		//Initialize all of the views
		this.initViews();
	}
	
	/**
	 * initViews - 
	 * This binds the views to their local variables.  This also fills the 
	 * spinner from the array stored in strings.xml
	 */
	private void initViews(){
		//bind deckName
		deckName = (EditText)this.findViewById(R.id.deck_create_name);
		//bind deckType
		deckType = (Spinner)this.findViewById(R.id.deck_create_type);
		//build adapter for deckType
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.create_deck_type_array, android.R.layout.simple_spinner_item);
		//set the dropDown view for the spinner
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		//set the adapater of the spinner to the adapter just created
		deckType.setAdapter(adapter);
	}
	
	/**
	 * createDeck - 
	 * This is a method called when the user hits the button that
	 * says "Create Deck".  It is in charge of adding the deck to the 
	 * database that stores them, and is also in charge of checking the input.
	 * If the user doesn't enter in a name for the deck, a Toast is shown informing them.
	 * 
	 * @param v - the view that was selected
	 */
	public void createDeck(View v){
		//Check that the deckname isn't empty
		if(!deckName.getText().equals("")){
			//Create a deck object and with the name and type
			Deck created = new Deck(deckName.getText().toString(), deckType.getSelectedItem().toString());
			//attempt to add the deck to the database
			if(new DeckDataHelper(this).addDeckToDb(created)){
				//if it succeeds, set the activity's result to OK
				this.setResult(RESULT_OK);
			}
			else
				//if the addition to the database fails, set the activity's result to CACNCELLED
				this.setResult(RESULT_CANCELED);
			//finish the activity (pop it off the activity stack)
			this.finish();
		}
		//the deckName was empty
		else{
			//show a toast and inform the user - do not finish the activity
			Toast.makeText(this, "You must name this deck", Toast.LENGTH_SHORT).show();
		}
	}
}
