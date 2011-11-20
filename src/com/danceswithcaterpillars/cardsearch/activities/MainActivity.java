package com.danceswithcaterpillars.cardsearch.activities;

import com.danceswithcaterpillars.cardsearch.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        if(this.getIntent().getAction().equals(Intent.ACTION_SEARCH))
        	this.onSearchRequested();
        
       	TabHost tabHost = this.getTabHost();
    	TabHost.TabSpec spec;
    	Intent intent;
    	
    	//bind for the createEntry activity
    	intent = new Intent().setClass(this, CardSearchActivity.class);
    	
        spec = tabHost.newTabSpec("ViewCards")
        			.setIndicator("View Cards")
        			.setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent(this, SearchForCard.class);
        intent.setAction(SearchForCard.START_SEARCH);
        //bind for the viewEntries activity
        spec = tabHost.newTabSpec("SearchForCards")
        			.setIndicator("Search For Cards")
        			.setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
	}
}
