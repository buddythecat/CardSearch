package com.danceswithcaterpillars.cardsearch.model.deck;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.graphics.Color;

public class DeckBreakdownChart extends AbstractDeckChart{
	
	public static final class DeckChartConstants{
		public static final int BLACK = 0;
		public static final int BLUE = 1;
		public static final int GREEN = 2;
		public static final int RED = 3;
		public static final int WHITE = 4;
		public static final int GREY = 5;
		public static final int GOLD = 6;
		public static final int OTHER = 7;
	}
	
	
	public GraphicalView execute(Context ctx, double[] values){
		/*
		 * Values should be formated as such
		 * values[0] = BLACK
		 * values[1] = BLUE
		 * values[2] = GREEN
		 * values[3] = RED
		 * values[4] = WHITE
		 * values[5] = GREY
		 * values[6] = GOLD
		 */
		
		int[] colors = new int[]{Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.WHITE, Color.GRAY, Color.YELLOW, Color.TRANSPARENT};
		DefaultRenderer chartRenderer = this.buildCategoryRenderer(colors);
		
		chartRenderer.setZoomButtonsVisible(false);
		chartRenderer.setZoomEnabled(false);
		chartRenderer.setChartTitle("Deck Breakdown");
		chartRenderer.setChartTitleTextSize(12);
		return ChartFactory.getPieChartView(ctx, this.buildCategoryDataset("Deck Breakdown", values), chartRenderer);
	}
}
