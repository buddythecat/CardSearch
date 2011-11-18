package com.danceswithcaterpillars.cardsearch.model.deck;

import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class AbstractDeckChart {
	
	protected DefaultRenderer buildCategoryRenderer(int[] colors){
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(12);
		renderer.setLegendTextSize(12);
		renderer.setMargins(new int[]{20,30,15,0});
		for(int color : colors){
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
	
	protected CategorySeries buildCategoryDataset(String title, double[] values){
		CategorySeries series = new CategorySeries(title);
		int k = 0;
		for (double value: values){
			series.add("Color: " + ++k, value);
		}
		return series;
	}
	
}
