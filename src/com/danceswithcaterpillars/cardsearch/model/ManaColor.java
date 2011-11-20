package com.danceswithcaterpillars.cardsearch.model;

public enum ManaColor {
	BLACK, BLUE, GREEN, RED, WHITE, GREY, GOLD, OTHER;
	
	public static ManaColor createFromString(String mana){
		//can't switch on a string on low-version build enviros
		if(mana.equals("black"))
			return ManaColor.BLACK;
		else if(mana.equals("blue"))
			return ManaColor.BLUE;
		else if(mana.equals("green"))
			return ManaColor.GREEN;
		else if(mana.equals("red"))
			return ManaColor.GREEN;
		else if(mana.equals("white"))
			return ManaColor.WHITE;
		else if(mana.equals("grey"))
			return ManaColor.GREY;
		else if(mana.equals("gold"))
			return ManaColor.GOLD;
		else
			return ManaColor.OTHER;
	}
}
