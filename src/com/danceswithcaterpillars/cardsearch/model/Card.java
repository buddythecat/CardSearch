package com.danceswithcaterpillars.cardsearch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable{
	private String name;
	private String cost;
	private String type;
	private String subType;
	private String rule;
	private String power;
	private String toughness;
	private int quantity;
	private int deckId;
	private long id;
	
	public Card(String n, String cst, String typ, String subTyp, String rle, String str, String tough, long i, int quantity, int deckId){
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
		
	}
	
	public Card(Parcel in){
		readFromParcel(in);
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
	
	
	
	public String toString(){
		return "Card Name: "+this.name+"\n"+
				"Cost: "+this.cost+"\n"+
				"Type: "+this.type + " - "+this.subType+"\n"+
				"Stats: "+this.power+"/"+this.toughness+"\n"+
				"Rule: "+this.rule+"\n";
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
	}
	
	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
		public Card createFromParcel(Parcel in){
			return new Card(in);
		}
		public Card[] newArray(int size){
			return new Card[size];
		}
	};
}
