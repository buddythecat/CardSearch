package com.danceswithcaterpillars.cardsearch.model;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DeckDatabaseConstants.*;

import java.util.LinkedList;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


public class Deck implements Parcelable{
	private LinkedList<Card> cards;
	private String name;
	private String type;
	private ManaColor color;
	private double[] breakDown;
	private int count;
	private long id;
	
	public static final String[] colorMap = new String[]{
		"black", "blue", "green", "red", "white", "grey", "gold", "other"
	};
	
	public static final int BLACK = 0;
	public static final int BLUE = 1;
	public static final int GREEN = 2;
	public static final int RED = 3;
	public static final int WHITE = 4;
	public static final int GREY = 5;
	public static final int GOLD = 6;
	public static final int OTHER = 7;
	
	/*8
	 * perhaps we should switch from using Card objects to ints... using the _ID to reference
	 * Nevermind, this is a bad idea because we need to get the overall color, and that requires a LList
	 */
	public Deck(long id, String name, LinkedList<Card> cards, String type){
		this.id = id;
		this.name = name;
		this.cards = cards;
		this.type = (type==null) ? "" : type;
		this.breakDown = new double[8];
		this.color = getOverallDeckColor();
		this.count = cards.size();
	}
	
	//Constructor for new deck
	public Deck(String name, String type){
		this.name = name;
		this.cards = new LinkedList<Card>();
		this.type = type;
		this.breakDown = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
		this.color = ManaColor.OTHER;
		this.count = 0;
	}
	
	public void clearDeckCards(){
		this.cards = new LinkedList<Card>();
		this.count = 0;
	}
	
	public Deck(Cursor c){
		this.id = c.getLong(c.getColumnIndex(_ID));
		this.name = c.getString(c.getColumnIndex(DECK_NAME));
		this.cards = new LinkedList<Card>();
		this.type = c.getString(c.getColumnIndex(TYPE));
		this.breakDown = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
		this.color = ManaColor.createFromString(c.getString(c.getColumnIndex(COLOR)));
		this.count = c.getInt(c.getColumnIndex(CARD_COUNT));
	}
	
	public Deck(Parcel in){
		//create from a parcel
		this.readFromParcel(in);
	}
	
	public String getColorAsString(){
		switch(color){
		case BLACK:
			return "black";
		case BLUE:
			return "blue";
		case GREEN:
			return "green";
		case RED:
			return "red";
		case WHITE:
			return "white";
		case GOLD:
			return "gold";
		case GREY:
			return "grey";
		case OTHER:
			return "other";
		default:
			return "other";
		}
	}
	
	public void updateColor(){
		color = getOverallDeckColor();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getCount(){
		return count;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public LinkedList<Card> getCards(){
		return cards;
	}
	
	public void setCards(LinkedList<Card> cards){
		this.cards = cards;
	}
	
	public void addCard(Card toAdd){
		cards.add(toAdd);
		count++;
	}
	
	public void removeCard(Card toRemove){
		if(cards.size()>0){
			cards.remove(cards.lastIndexOf(toRemove));
			count--;
		}
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public double[] getBreakdown(){
		this.getOverallDeckColor();
		return breakDown;
	}
	
	public ManaColor getOverallDeckColor(){
		if(cards!=null && cards.size()>0){
			//we need to reset the breakdown
			this.breakDown = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
			
			//then start the loop
			Card current= null;
			for(int i = 0; i<cards.size(); i++){
				current = cards.get(i);
				switch(current.getColor()){
				case RED:
					breakDown[ManaColor.RED.ordinal()]++;
					break;
				case WHITE:
					breakDown[ManaColor.WHITE.ordinal()]++;
					break;
				case BLUE:
					breakDown[ManaColor.BLUE.ordinal()]++;
					break;
				case GREEN:
					breakDown[ManaColor.GREEN.ordinal()]++;
					break;
				case BLACK:
					breakDown[ManaColor.BLACK.ordinal()]++;
					break;
				case GREY:
					breakDown[ManaColor.GREY.ordinal()]++;
					break;
				case GOLD:
					breakDown[ManaColor.GOLD.ordinal()]++;
					break;
				default:
					breakDown[ManaColor.OTHER.ordinal()]++;
					break;
				}
			}
			
			ManaColor mostColor = ManaColor.OTHER;
			double mostColorCount=0;
			
			for(int i = 0; i < breakDown.length; i++){
				if(breakDown[i]>mostColorCount){
					mostColor = ManaColor.values()[i];
					mostColorCount=breakDown[i];
				}
			}
			return mostColor;
		}
		return ManaColor.OTHER;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeList(this.cards);
		dest.writeString(this.type);
		dest.writeInt(this.color.ordinal());
		dest.writeDoubleArray(this.breakDown);
		dest.writeInt(this.count);
		dest.writeLong(this.id);
	}
	
	private void readFromParcel(Parcel in){
		this.name 	=	in.readString();
						in.readList(cards, null);
		this.type 	=	in.readString();
		this.color	=	ManaColor.values()[in.readInt()];
						in.readDoubleArray(breakDown);
		this.count 	=	in.readInt();
		this.id		=	in.readLong();
	}
	
	public static final Parcelable.Creator<Deck> CREATOR = new Parcelable.Creator<Deck>() {
		public Deck createFromParcel(Parcel in){
			return new Deck(in);
		}
		public Deck[] newArray(int size){
			return new Deck[size];
		}
	};
}
