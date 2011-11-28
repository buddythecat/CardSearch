package com.danceswithcaterpillars.cardsearch.content;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.model.Card;

/**
 * CardSearchReceiver - 
 * this interface is very simple, and should be used to denote that a ListActivity
 * is capable or receiving search results from the GetCardsTask thread.
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public interface CardSearchReceiver {
	/**
	 * buildCardList - 
	 * build a list of cards from the result of the GetCardsTask thread
	 * @param cards - the returned list of cards.
	 */
	public void buildCardList(LinkedList<Card> cards);
}
