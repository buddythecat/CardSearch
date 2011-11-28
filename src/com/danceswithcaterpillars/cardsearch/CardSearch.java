package com.danceswithcaterpillars.cardsearch;


import com.danceswithcaterpillars.cardsearch.model.Deck;

import android.app.Application;

/**
 * CardSearchApplication - 
 * This acts as the underlying application class. 
 * At the moment its current purpose is to simply keep track of the current deck we're examining.
 * @author Rich "Dances With Caterpillars"
 *
 */
public class CardSearch extends Application {
	/** currentDeckId - the ID of the current deck being examined; a value of -1 indicates that no deck is in focus */
	public long currentDeckId = -1;
	/** currenDeck - the Deck Object that's currently being examined */
	public Deck currentDeck;
}
