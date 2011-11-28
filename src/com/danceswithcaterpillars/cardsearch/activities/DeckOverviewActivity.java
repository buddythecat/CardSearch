package com.danceswithcaterpillars.cardsearch.activities;

import org.achartengine.GraphicalView;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.model.deck.DeckBreakdownChart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * DeckOverviewActivity - 
 * This activity's goal is to show the overall breakdown of the deck in terms of
 * Color Breakdown (ie: what color cards make up the status of the deck);
 * It uses the achartengine package in order to show the pie chart.
 * 
 * the achartengine can be found at this address:
 * http://code.google.com/p/achartengine/
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class DeckOverviewActivity extends Activity {
	/** TAG - The Log tag for this activity */
	private final static String TAG = "DeckOverviewActivity";
	/** chartContainer - the layout containing the chart */
	private LinearLayout chartContainer;
	/** chart - the DeckBreakdownChart that contains the chart */
	private DeckBreakdownChart chart;
	
	/*
	 * onCreate - 
	 * Creates the main view for this activity.  
	 * It then initializes the layout, gets the breakdown from the deck
	 * stored in the application class, and then creates the chart.  The chart
	 * then is added to the layout.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_overview);
		
		//We need to initizlize and bind to the parent view for the activity
		chartContainer = (LinearLayout)this.findViewById(R.id.chart);
		//get a local instance variable for the application class
		CardSearch app = (CardSearch)this.getApplication();
		//populate the values for the chart by calling Deck.getBreakdown() on the currentDeck stored in the application class.
		double[] testValues = app.currentDeck.getBreakdown();
		//For testing purposes, output the values of the breakdown
		Log.v(TAG, "testing  "+testValues[0]+", "+testValues[1]+", "+testValues[2]+", "+testValues[3]+", "+testValues[4]+", "+testValues[5]+", "+testValues[6]+", "+testValues[7]);
		
		//Create a new DeckBreakdownChart
		chart = new DeckBreakdownChart();
		/*
		 * Create a GraphicalView by calling DeckBreakdownChart.execute(context, values)
		 * GraphicalView can be found @see org.achartengine.GraphicalView
		 */
		GraphicalView graph = chart.execute(this, testValues);
		//Add the graphicalView to the linearLayout
		chartContainer.addView(graph);
	}
}
