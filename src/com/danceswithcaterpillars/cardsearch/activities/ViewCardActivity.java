package com.danceswithcaterpillars.cardsearch.activities;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.CardSearchReceiver;
import com.danceswithcaterpillars.cardsearch.content.cards.GetCardsTask;
import com.danceswithcaterpillars.cardsearch.content.image.GetSetImgTask;
import com.danceswithcaterpillars.cardsearch.content.local.CardDatabaseProvider;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewCardActivity extends Activity implements CardSearchReceiver{
	private static final String TAG = "CardSearchReciever";
	
	private ExecutorService cardThread;
	//main views
	private TextView name, power, rules, type;
	private LinearLayout costLayout;
	//set info views
	private TextView rarity, setnum, artist;
	private ImageView setcode, cardImage;
	private ProgressBar cardProgress;
	
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
			cardThread.execute(new GetCardsTask(this, uri.toString(), true));
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
				
				
				String manaCost = focusedCard.getCost();
				costLayout.removeAllViews();
				Card.buildManaBar(new WeakReference<LinearLayout>(costLayout), manaCost);
				
				
				String pow = focusedCard.getPower();
				//case for a creature
				if(!pow.equals("")){
					power.setVisibility(View.VISIBLE);
					pow += "/"+focusedCard.getToughness();
					power.setText(pow);
				}
				//Case for a planeswalker
				else if(!focusedCard.getLoyalty().equals("")){
					power.setVisibility(View.VISIBLE);
					pow = "Loyalty: ";
					pow += focusedCard.getLoyalty();
					power.setText(pow);
				}
				//case for an instant or sorcery
				else{
					power.setVisibility(View.GONE);
				}
				
				String rule = focusedCard.getRule();
				if(rule.length()==0 || rule==null){
					rules.setVisibility(View.GONE);
				}
				else{
					rules.setVisibility(View.VISIBLE);
					rules.setText(Card.buildCardRulesWithImages(rule, getApplicationContext()));	
				}
				
				
				type.setText((CharSequence)focusedCard.getFullType());
				
				
				JSONArray setInfo = focusedCard.getSetInfo();
				Log.i(TAG, setInfo.toString());
				if(setInfo.length()>0){
					try{
						JSONObject firstSet = setInfo.getJSONObject(0);
						//String setCode = firstSet.getString("setcode");
						String[][] setInfos = new String[setInfo.length()][3];
						for(int i = 0; i<setInfo.length(); i++){
							firstSet = setInfo.getJSONObject(i);
							setInfos[i][0] = firstSet.getString("setcode");
							setInfos[i][1] = firstSet.getString("rarity");
							setInfos[i][2] = firstSet.getString("number");
						}
						if(focusedCard.getMulti()==null)
							setInfos[0][2] += "a";
						GetSetImgTask retriever = new GetSetImgTask(setcode, null, setInfos);
						retriever.execute(new String[]{
								GetSetImgTask.SET_IMG});
						//setcode.setText(firstSet.getString("setcode"));
						
						retriever = new GetSetImgTask(cardImage, cardProgress, setInfos);
						retriever.execute(new String[]{
								GetSetImgTask.CARD_IMG});
						rarity.setText(firstSet.getString("rarity"));
						setnum.setText(firstSet.getString("number"));
						artist.setText(firstSet.getString("artist"));
						//setLayout.setVisibility(View.VISIBLE);
					}catch(JSONException e){
						Log.e(TAG, "Error parsing set info", e);
					}
				}
			}
		});
	}
	
	private void initViews(){
		name = (TextView)this.findViewById(R.id.focused_card_name);
		costLayout = (LinearLayout)this.findViewById(R.id.focused_card_cost);
		power = (TextView)this.findViewById(R.id.focused_card_powTough);
		rules = (TextView)this.findViewById(R.id.focused_card_rules);
		type = (TextView)this.findViewById(R.id.focused_card_type);
		
		cardImage = (ImageView)this.findViewById(R.id.focused_card_img);
		cardProgress = (ProgressBar)this.findViewById(R.id.focused_card_img_prog);
		
		setcode = (ImageView)this.findViewById(R.id.focused_card_set_code);
		rarity = (TextView)this.findViewById(R.id.focused_card_set_rarity);
		setnum = (TextView)this.findViewById(R.id.focused_card_set_num);
		artist = (TextView)this.findViewById(R.id.focused_card_set_artist);
	}
	
	 public void addCard(View v){
		 ContentValues values = new ContentValues();
		 values.put(CARD_NAME, focusedCard.getName());
		 values.put(COST, focusedCard.getCost());
		 values.put(TYPE, focusedCard.getType());
		 values.put(SUBTYPE, focusedCard.getSubType());
		 values.put(POWER, focusedCard.getPower());
		 values.put(TOUGHNESS, focusedCard.getToughness());
		 values.put(RULE, focusedCard.getRule());
		 values.put(DECK_ID, ((CardSearch)this.getApplication()).currentDeck.getId());
		 values.put(QUANTITY, 1);
		 values.put(LOYALTY, focusedCard.getLoyalty());
		 values.put(SET_INFO, focusedCard.getSetInfo().toString());
		 Log.d(TAG, "VAlues: "+values.toString());
		 
		 ((CardSearch)this.getApplication()).currentDeck.addCard(focusedCard);
		 
		 if(getContentResolver().insert(CardDatabaseProvider.CONTENT_URI, values)==null){
			 Log.d(TAG, "Failure! Did not insert card to database");
		 }
		 Intent i = new Intent(this, CardListActivity.class);
		 i.setAction(Intent.ACTION_VIEW);
		 this.startActivity(i);
	 }
}
