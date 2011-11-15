package com.danceswithcaterpillars.cardsearch;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.danceswithcaterpillars.cardsearch.model.Card;
import com.danceswithcaterpillars.cardsearch.model.CardArrayAdapter;
import com.danceswithcaterpillars.cardsearch.model.CardSearchReciever;
import com.danceswithcaterpillars.cardsearch.model.GetCardsTask;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchForCard extends ListActivity implements CardSearchReciever{
	public static final String START_SEARCH = "Start_Search";
	private ExecutorService cardThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_tabs);
		
		this.setListAdapter(new ArrayAdapter<Card>(this, R.layout.card_list_item));
		
		Intent intent = this.getIntent();
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			cardThread = Executors.newSingleThreadExecutor();
			cardThread.execute(new GetCardsTask(this, query));
		}
		else if(Intent.ACTION_VIEW.equals(intent.getAction())){
			Uri uri = intent.getData();
			cardThread = Executors.newSingleThreadExecutor();
			cardThread.execute(new GetCardsTask(this, uri.toString()));
			
		}
		else if(START_SEARCH.equals(intent.getAction())){
			this.onSearchRequested();
		}
		else
			this.onSearchRequested();
	}

	@Override
	public void buildCardList(final LinkedList<Card> cards) {
		if(cards!=null){
			if(cards.size()==1){
				Intent i = new Intent(this, ViewCardActivity.class);
				i.putExtra("Card", cards.getFirst());
				this.startActivity(i);
			}
			this.runOnUiThread(new Runnable(){
				public void run(){
					CardArrayAdapter adapter = new CardArrayAdapter(SearchForCard.this, R.layout.card_list_item, cards);
					if(adapter!=null)
						getListView().setAdapter(adapter);
				}
			});
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ViewCardActivity.class);
		Card temp = Card.class.cast(l.getAdapter().getItem(position));
		i.putExtra("Card", temp);
		this.startActivity(i);
	}
	
}
