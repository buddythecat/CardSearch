package com.danceswithcaterpillars.cardsearch.model.deck;

import java.util.LinkedList;

import android.database.Cursor;

import com.danceswithcaterpillars.cardsearch.model.Card;

public class Deck {
	private LinkedList<Card> cards;
	private String name;
	private String type;
	private String color;
	private int[] breakDown;
	private int count;
	
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
	
	public Deck(String name, LinkedList<Card> cards, String type){
		this.name = name;
		this.cards = cards;
		this.type = (type==null) ? "" : type;
		this.breakDown = new int[5];
		this.color = getColor();
		this.count = cards.size();
	}
	
	public Deck(Cursor c){
		//create a deck from cursor
	}
	
	public String getColor(){
		return color;
	}
	
	public void updateColor(){
		color = getColor();
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
	
	public String getOverallDeckColor(){
		if(cards!=null && cards.size()>0){
			Card[] tempArray = (Card[])cards.toArray();
			for(Card current : tempArray){
				switch(current.getColor()){
				case "red":
					breakDown[RED]++;
					break;
				case "white":
					breakDown[WHITE]++;
					break;
				case "blue":
					breakDown[BLUE]++;
					break;
				case "green":
					breakDown[GREEN]++;
					break;
				case "black":
					breakDown[BLACK]++;
					break;
				case "grey":
					breakDown[GREY]++;
					break;
				case "gold":
					breakDown[GOLD]++;
					break;
				default:
					breakDown[OTHER]++;
					break;
				}
			}
			
			int mostColor = OTHER;
			int mostColorCount=0;
			
			for(int i = 0; i<breakDown.length; i++){
				if(breakDown[i]>mostColorCount){
					mostColor = i;
					mostColorCount=breakDown[i];
				}	
			}
			return colorMap[mostColor];
		}
		return "error";
	}
}
