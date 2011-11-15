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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewCardActivity extends Activity implements CardSearchReciever{
	private static final String TAG = "CardSearchReciever";
	
	private ExecutorService cardThread;
	
	private TextView name, power, rules, type;
	private LinearLayout costLayout;
	
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
				String manaCost = focusedCard.getCost();
				
				costLayout.removeAllViews();
				
				ImageView thisMana = new ImageView(getApplicationContext());
				Log.d(TAG, "length"+manaCost.length() + " cost: "+manaCost);
				for(int i = 0; i<manaCost.length(); i++){
					switch(manaCost.charAt(i)){
					case 'U':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						thisMana.setScaleType(ImageView.ScaleType.CENTER);
						thisMana.setImageResource(R.drawable.mana_blue);
						costLayout.addView(thisMana);
						break;
					case 'R':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						thisMana.setImageResource(R.drawable.mana_red);
						costLayout.addView(thisMana);
						break;
					case 'B':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						thisMana.setImageResource(R.drawable.mana_black);
						costLayout.addView(thisMana);
						break;
					case 'G':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						thisMana.setImageResource(R.drawable.mana_green);
						costLayout.addView(thisMana);
						break;
					case 'W':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setMaxHeight(20);
						thisMana.setMaxWidth(20);
						thisMana.setImageResource(R.drawable.mana_white);
						costLayout.addView(thisMana);
						break;
					case '0':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_0);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '1':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_1);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '2':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_2);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '3':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_3);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '4':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_4);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '5':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_5);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '6':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_6);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '7':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_7);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case '8':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_8);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					case 'X':
						thisMana = new ImageView(getApplicationContext());
						thisMana.setImageResource(R.drawable.mana_x);
						thisMana.setMaxHeight(10);
						thisMana.setMaxWidth(10);
						costLayout.addView(thisMana);
						break;
					}
				}
				power.setText((CharSequence)focusedCard.getPowTough());
				
				
				String rule = focusedCard.getRule();
				if(rule.length()==0 || rule==null){
					rules.setVisibility(View.GONE);
				}
				else{
					SpannableStringBuilder builder = new SpannableStringBuilder();
					rules.setVisibility(View.VISIBLE);
					if(rule.contains("{")){
						builder.append(rule);
						if(rule.contains("{T}")){
							int tapLoc = rule.indexOf("{T}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.tap);
							d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
							ImageSpan tapImageSpan = new ImageSpan(d);
							builder.setSpan(tapImageSpan, tapLoc, tapLoc+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{W}")){
							int whiteMana = rule.indexOf("{W}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_white);
							d.setBounds(0,0,25,25);
							ImageSpan whiteImageSpan = new ImageSpan(d);
							builder.setSpan(whiteImageSpan, whiteMana, whiteMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{B}")){
							int blackMana = rule.indexOf("{B}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_black);
							d.setBounds(0,0,25,25);
							ImageSpan blackImageSpan = new ImageSpan(d);
							builder.setSpan(blackImageSpan, blackMana, blackMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{G}")){
							int greenMana = rule.indexOf("{G}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_green);
							d.setBounds(0,0,25,25);
							ImageSpan greenImageSpan = new ImageSpan(d);
							builder.setSpan(greenImageSpan, greenMana, greenMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{U}")){
							int blueMana = rule.indexOf("{U}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_blue);
							d.setBounds(0,0,25,25);
							ImageSpan blueImageSpan = new ImageSpan(d);
							builder.setSpan(blueImageSpan, blueMana, blueMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{R}")){
							int redMana = rule.indexOf("{R}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_red);
							d.setBounds(0,0,25,25);
							ImageSpan redImageSpan = new ImageSpan(d);
							builder.setSpan(redImageSpan, redMana, redMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{1}")){
							int xMana = rule.indexOf("{1}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_1);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{2}")){
							int xMana = rule.indexOf("{2}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_2);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{3}")){
							int xMana = rule.indexOf("{3}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_3);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{4}")){
							int xMana = rule.indexOf("{4}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_4);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{5}")){
							int xMana = rule.indexOf("{5}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_5);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{6}")){
							int xMana = rule.indexOf("{6}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_6);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}rules.setText(builder);
						if(rule.contains("{7}")){
							int xMana = rule.indexOf("{7}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_7);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						if(rule.contains("{X}")){
							int xMana = rule.indexOf("{X}");
							Drawable d = getApplicationContext().getResources().getDrawable(R.drawable.mana_x);
							d.setBounds(0,0,25,25);
							ImageSpan xImageSpan = new ImageSpan(d);
							builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						rules.setText(builder);
					}
					else
						rules.setText(rule);
				//rules.setText((CharSequence)focusedCard.getRule());				
				}
				type.setText((CharSequence)focusedCard.getFullType());
			}
		});
	}
	
	private void initViews(){
		name = (TextView)this.findViewById(R.id.focused_card_name);
		costLayout = (LinearLayout)this.findViewById(R.id.focused_card_cost);
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
		 values.put(RULE, focusedCard.getRule());
		 values.put(DECK_ID, 0);
		 values.put(QUANTITY, 0);
		 values.put(LOYALTY, focusedCard.getLoyalty());
		 Log.d(TAG, "VAlues: "+values.toString());
		 
		 if(getContentResolver().insert(CardDatabaseProvider.CONTENT_URI, values)==null){
			 Log.d(TAG, "Failure! Did not insert card to database");
		 }
		 Intent i = new Intent(this, CardSearchActivity.class);
		 i.setAction(Intent.ACTION_VIEW);
		 this.startActivity(i);
	 }
}
