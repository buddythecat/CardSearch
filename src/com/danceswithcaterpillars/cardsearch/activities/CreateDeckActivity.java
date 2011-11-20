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

public class CreateDeckActivity extends Activity {
	private EditText deckName;
	private Spinner deckType;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_create_view);
		this.initViews();
	}
	
	private void initViews(){
		deckName = (EditText)this.findViewById(R.id.deck_create_name);
		deckType = (Spinner)this.findViewById(R.id.deck_create_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.create_deck_type_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		deckType.setAdapter(adapter);
	}
	
	public void createDeck(View v){
		Deck created = new Deck(deckName.getText().toString(), deckType.getSelectedItem().toString());
		if(new DeckDataHelper(this).addDeckToDb(created)){
			this.setResult(RESULT_OK);
		}
		else
			this.setResult(RESULT_CANCELED);
		this.finish();
	}
}
