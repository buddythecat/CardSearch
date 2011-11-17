package com.danceswithcaterpillars.cardsearch.content;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.model.Card;

public interface CardSearchReciever {
	public void buildCardList(LinkedList<Card> cards);
}
