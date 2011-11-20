package com.danceswithcaterpillars.cardsearch.activities;

//import static com.danceswithcaterpillars.cardsearch.model.deck.DeckBreakdownChart.DeckChartConstants.*;

import org.achartengine.GraphicalView;

import com.danceswithcaterpillars.cardsearch.CardSearch;
import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.model.deck.DeckBreakdownChart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class DeckOverviewActivity extends Activity {
	private final static String TAG = "DeckOverviewActivity";
	private LinearLayout chartContainer;
	private DeckBreakdownChart chart;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_overview);
		
		chartContainer = (LinearLayout)this.findViewById(R.id.chart);
		CardSearch app = (CardSearch)this.getApplication();
		double[] testValues = app.currentDeck.getBreakdown();
		Log.v(TAG, "testing  "+testValues[0]+", "+testValues[1]+", "+testValues[2]+", "+testValues[3]+", "+testValues[4]+", "+testValues[5]+", "+testValues[6]+", "+testValues[7]);
				
		chart = new DeckBreakdownChart();
		
		GraphicalView graph = chart.execute(this, testValues);
		chartContainer.addView(graph);
	}
}
