package com.danceswithcaterpillars.cardsearch;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.danceswithcaterpillars.cardsearch.database.DbConstants.*;

import com.danceswithcaterpillars.cardsearch.content.CardDatabaseProvider;
import com.danceswithcaterpillars.cardsearch.model.Card;
import com.danceswithcaterpillars.cardsearch.model.CardSearchReciever;
import com.danceswithcaterpillars.cardsearch.model.GetCardsTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ViewCardActivity extends Activity implements CardSearchReciever{
	private static final String TAG = "CardSearchReciever";
	
	private ExecutorService cardThread;
	
	private TextView name, cost, power, rules, type;
	
	private Card focusedCard;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_card);
		
		this.initViews();
			
		Intent intent = this.getIntent();

		if(Intent.ACTION_VIEW.equals(intent.getAction())){
			Uri uri = intent.getData();
			cardThread = Executors.newSingleThreadExecutor();
			cardThread.execute(new GetCardsTask(this, uri.toString()));
		}
		else{
			Log.d(TAG, "Card was passed");
			focusedCard = intent.getExtras().getParcelable("Card");
			this.fillCardDetails();
		}
	}
	@Override
	public void buildCardList(LinkedList<Card> cards) {
		focusedCard = cards.getFirst();
		this.fillCardDetails();
	}
	
	private void fillCardDetails(){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				name.setText((CharSequence)focusedCard.getName());
				cost.setText((CharSequence)focusedCard.getCost());
				power.setText((CharSequence)focusedCard.getPowTough());
				rules.setText((CharSequence)focusedCard.getRule());
				type.setText((CharSequence)focusedCard.getFullType());
			}
		});
	}
	
	private void initViews(){
		name = (TextView)this.findViewById(R.id.focused_card_name);
		cost = (TextView)this.findViewById(R.id.focused_card_cost);
		power = (TextView)this.findViewById(R.id.focused_card_powTough);
		rules = (TextView)this.findViewById(R.id.focused_card_rules);
		type = (TextView)this.findViewById(R.id.focused_card_type);
	}
	
	 public void addCard(View v){
		 ContentValues values = new ContentValues();
		 values.put(CARD_NAME, focusedCard.getName());
		 values.put(COST, focusedCard.getCost());
		 values.put(TYPE, focusedCard.getType());
		 values.put(SUBTYPE, focusedCard.getSubType());
		 values.put(POWER, focusedCard.getPower());
		 values.put(TOUGHNESS, focusedCard.getToughness());
		 //values.put(RULE, focusedCard.getRule());
		 Log.d(TAG, "VAlues: "+values.toString());
		 
		 if(getContentResolver().insert(CardDatabaseProvider.CONTENT_URI, values)==null){
			 Log.d(TAG, "Failure! Did not insert card to database");
		 }
		 Intent i = new Intent(this, CardSearchActivity.class);
		 i.setAction(Intent.ACTION_VIEW);
		 this.startActivity(i);
	 }
}
