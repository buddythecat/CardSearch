package com.danceswithcaterpillars.cardsearch;

import static com.danceswithcaterpillars.cardsearch.model.deck.DeckBreakdownChart.DeckChartConstants.*;

import org.achartengine.GraphicalView;

import com.danceswithcaterpillars.cardsearch.model.deck.DeckBreakdownChart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class DeckOverviewActivity extends Activity {
	private LinearLayout chartContainer;
	private DeckBreakdownChart chart;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.deck_overview);
		
		chartContainer = (LinearLayout)this.findViewById(R.id.chart);
		
		double[] testValues = new double[6];
		testValues[BLACK] = 14;
		testValues[BLUE] = 0;
		testValues[GREEN] = 18;
		testValues[RED] = 0;
		testValues[WHITE] = 0;
		testValues[GREY] = 8;
		
		GraphicalView graph = chart.execute(this, testValues);
		chartContainer.addView(graph);
	}
}
