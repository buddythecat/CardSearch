package com.danceswithcaterpillars.cardsearch.model;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * TODO comment me
 * @author snake
 *
 */
public class Card implements Parcelable{
	private static final String TAG = "Card";
	
	private String name;
	private String cost;
	private String type;
	private String subType;
	private String rule;
	private String power;
	private String toughness;
	private String loyalty;
	private JSONArray setInfo;
	private int quantity;
	private int deckId;
	private long id;
	
	public Card(String n, String cst, String typ, String subTyp, String rle, String str, String tough, long i, int quantity, int deckId, String loyalty){
		name = n;
		cost = cst;
		type = typ;
		subType= subTyp;
		rule = rle;
		power = str;
		toughness = tough;
		id = i;
		this.quantity = quantity;
		this.deckId = deckId;
		this.loyalty = loyalty;
		this.setInfo = new JSONArray();
	}
	
	public Card(Parcel in){
		readFromParcel(in);
	}
	
	public Card(Cursor c, int pos){
		if(c.moveToPosition(pos)){
			this.name=c.getString(c.getColumnIndex(CARD_NAME));
			this.cost=c.getString(c.getColumnIndex(COST));
			this.type=c.getString(c.getColumnIndex(TYPE));
			this.subType=c.getString(c.getColumnIndex(SUBTYPE));
			this.rule=c.getString(c.getColumnIndex(RULE));
			this.power=c.getString(c.getColumnIndex(POWER));
			this.toughness=c.getString(c.getColumnIndex(TOUGHNESS));
			this.id=c.getLong(c.getColumnIndex(_ID));
			this.quantity=c.getInt(c.getColumnIndex(QUANTITY));
			this.loyalty=c.getString(c.getColumnIndex(LOYALTY));
			try{
				this.setInfo=new JSONArray(c.getString(c.getColumnIndex(SET_INFO)));
			}catch(JSONException e){
				Log.v(TAG, "Error creating card from cursor: ", e);
			}
		}
	}
	
	public String getName()			{ return name; }
	public String getCost()			{ return cost; }
	public String getType()			{ return type; }
	public String getSubType()		{ return subType; }
	
	public String getFullType(){
		return type + " - " + subType;
	}
	
	public String getPowTough(){
		return power+"/"+toughness;
	}
	
	public String getRule()			{ return rule; }
	public String getPower()			{ return power; }
	public String getToughness()		{ return toughness; }
	public long getId()				{ return id; }
	public int getQuantity()		{ return quantity;}
	public int getDeckId()			{ return deckId;}
	public String getLoyalty()		{ return loyalty; }
	public JSONArray getSetInfo()	{ return setInfo; } 
	
	public ManaColor getColor(){
		//exclusive or to ensure we contain a color
		if(cost.contains("W") ^ cost.contains("G") ^ cost.contains("B") ^ cost.contains("U") ^ cost.contains("R")){
			//determine card color
			if(cost.contains("W"))
				return ManaColor.WHITE;
			else if(cost.contains("G"))
				return ManaColor.GREEN;
			else if(cost.contains("B"))
				return ManaColor.BLACK;
			else if(cost.contains("U"))
				return ManaColor.BLUE;
			else if(cost.contains("R"))
				return ManaColor.RED;
			//this case should be unreachable (since we check for the existance of one of these to enter this condition
			else
				return ManaColor.GREY;
		}
		//regular or to see if this is a multi-color card, or a colorless card
		else if(cost.contains("W") || cost.contains("G") || cost.contains("B") || cost.contains("U") || cost.contains("R")){
			return ManaColor.GOLD;
		}
		//else to catch if this is an artifact card
		else{
			return ManaColor.GREY;
		}
	}
	
	public void setName(String name)
		{ this.name = name; }
	public void setCost(String cost)
		{ this.cost = cost; }
	public void setType(String type)
		{ this.type = type; }
	public void setSubType(String subType)
		{ this.subType = subType; }
	public void setRule(String rule)
		{ this.rule = rule; }
	public void setPower(String power)
		{ this.power = power; }
	public void setToughness(String toughness)
		{ this.toughness = toughness; }
	public void setId(long i)
		{ this.id = i; }
	public void setQuantity(int qty)
		{ this.quantity = qty; }
	public void incQuantity()
		{ this.quantity++; }
	public void setDeckId(int dId)
		{ this.deckId = dId;}
	public void setLoyalty(String loyalty)
		{ this.loyalty = loyalty; }
	public void setSetInfo(JSONArray setInfo)
		{ this.setInfo = setInfo; }
	
	
	public String toString(){
		return "Card Name: "+this.name+"\n"+
				"Cost: "+this.cost+"\n"+
				"Type: "+this.type + " - "+this.subType+"\n"+
				"Stats: "+this.power+"/"+this.toughness+"\n"+
				"Rule: "+this.rule+"\n"+
				"Loyalty: "+this.loyalty+"\n"+
				"Quantity: "+this.quantity+"\n"+
				"Deck: "+this.deckId+"\n";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.cost);
		dest.writeString(this.power);
		dest.writeString(this.toughness);
		dest.writeString(this.type);
		dest.writeString(this.subType);
		dest.writeString(this.rule);
		dest.writeLong(this.id);
		dest.writeInt(this.quantity);
		dest.writeInt(this.deckId);
		dest.writeString(this.loyalty);
		dest.writeString(this.setInfo.toString());
	}
	
	private void readFromParcel(Parcel in){
		this.name = in.readString();
		this.cost = in.readString();
		this.power = in.readString();
		this.toughness = in.readString();
		this.type = in.readString();
		this.subType = in.readString();
		this.rule = in.readString();
		this.id = in.readLong();
		this.quantity = in.readInt();
		this.deckId = in.readInt();
		this.loyalty = in.readString();
		try{
			this.setInfo = new JSONArray(in.readString());
		}catch(JSONException e){
			Log.e(TAG, "Error parsing the card's set info", e);
		}
	}
	
	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
		public Card createFromParcel(Parcel in){
			return new Card(in);
		}
		public Card[] newArray(int size){
			return new Card[size];
		}
	};
	
	public static SpannableStringBuilder buildCardRulesWithImages(String rule, Context applicationContext){
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(rule);
		if(rule.contains("{")){
			if(rule.contains("{T}")){
				int tapLoc = rule.indexOf("{T}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.tap);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				ImageSpan tapImageSpan = new ImageSpan(d);
				builder.setSpan(tapImageSpan, tapLoc, tapLoc+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{W}")){
				int whiteMana = rule.indexOf("{W}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_white);
				d.setBounds(0,0,25,25);
				ImageSpan whiteImageSpan = new ImageSpan(d);
				builder.setSpan(whiteImageSpan, whiteMana, whiteMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{B}")){
				int blackMana = rule.indexOf("{B}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_black);
				d.setBounds(0,0,25,25);
				ImageSpan blackImageSpan = new ImageSpan(d);
				builder.setSpan(blackImageSpan, blackMana, blackMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{G}")){
				int greenMana = rule.indexOf("{G}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_green);
				d.setBounds(0,0,25,25);
				ImageSpan greenImageSpan = new ImageSpan(d);
				builder.setSpan(greenImageSpan, greenMana, greenMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{U}")){
				int blueMana = rule.indexOf("{U}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_blue);
				d.setBounds(0,0,25,25);
				ImageSpan blueImageSpan = new ImageSpan(d);
				builder.setSpan(blueImageSpan, blueMana, blueMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{R}")){
				int redMana = rule.indexOf("{R}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_red);
				d.setBounds(0,0,25,25);
				ImageSpan redImageSpan = new ImageSpan(d);
				builder.setSpan(redImageSpan, redMana, redMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{1}")){
				int xMana = rule.indexOf("{1}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_1);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{2}")){
				int xMana = rule.indexOf("{2}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_2);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{3}")){
				int xMana = rule.indexOf("{3}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_3);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{4}")){
				int xMana = rule.indexOf("{4}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_4);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{5}")){
				int xMana = rule.indexOf("{5}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_5);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{6}")){
				int xMana = rule.indexOf("{6}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_6);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{7}")){
				int xMana = rule.indexOf("{7}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_7);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(rule.contains("{X}")){
				int xMana = rule.indexOf("{X}");
				Drawable d = applicationContext.getResources().getDrawable(R.drawable.mana_x);
				d.setBounds(0,0,25,25);
				ImageSpan xImageSpan = new ImageSpan(d);
				builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return builder;
	}
	
	public static void buildManaBar(WeakReference<LinearLayout> costLayout, String manaCost){
		Context adapterContext = costLayout.get().getContext();
		
		ImageView thisMana = new ImageView(adapterContext);
		//Log.d(TAG, "length"+manaCost.length() + " cost: "+manaCost);
		
		for(int i = 0; i<manaCost.length(); i++){
			switch(manaCost.charAt(i)){
			case 'U':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setScaleType(ImageView.ScaleType.CENTER);
				thisMana.setImageResource(R.drawable.mana_blue);
				costLayout.get().addView(thisMana);
				break;
			case 'R':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setImageResource(R.drawable.mana_red);
				costLayout.get().addView(thisMana);
				break;
			case 'B':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setImageResource(R.drawable.mana_black);
				costLayout.get().addView(thisMana);
				break;
			case 'G':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setImageResource(R.drawable.mana_green);
				costLayout.get().addView(thisMana);
				break;
			case 'W':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(20);
				thisMana.setMaxWidth(20);
				thisMana.setImageResource(R.drawable.mana_white);
				costLayout.get().addView(thisMana);
				break;
			case '0':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_0);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '1':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_1);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '2':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_2);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '3':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_3);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '4':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_4);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '5':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_5);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '6':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_6);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '7':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_7);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case '8':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_8);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			case 'X':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_x);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.get().addView(thisMana);
				break;
			}
		}
	}
	
	public ContentValues makeContentValues(){
		 ContentValues values = new ContentValues();
		 values.put(CARD_NAME, this.getName());
		 values.put(COST, this.getCost());
		 values.put(TYPE, this.getType());
		 values.put(SUBTYPE, this.getSubType());
		 values.put(POWER, this.getPower());
		 values.put(TOUGHNESS, this.getToughness());
		 values.put(RULE, this.getRule());
		 values.put(DECK_ID, this.getDeckId());
		 values.put(QUANTITY, this.getQuantity());
		 values.put(LOYALTY, this.getLoyalty());
		 values.put(SET_INFO, this.getSetInfo().toString());
		 
		 return values;
	}
	
}
