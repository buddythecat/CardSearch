package com.danceswithcaterpillars.cardsearch;


import com.danceswithcaterpillars.cardsearch.model.Deck;

import android.app.Application;

public class CardSearch extends Application {
	//-1 should be the default state.
	public long currentDeckId = -1;
	public Deck currentDeck;
}
