package com.danceswithcaterpillars.cardsearch.model;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CardArrayAdapter extends ArrayAdapter<Card> {
	
	public CardArrayAdapter(Context context, int viewResourceId, LinkedList<Card> cards){
		super(context, viewResourceId, cards);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(R.layout.card_list_item, null);
		}
		else{
			row = convertView;
		}
		TextView view = (TextView) row.findViewById(R.id.card_short_name);
		view.setText(this.getItem(position).getName());
		view = (TextView) row.findViewById(R.id.card_short_type);
		view.setText(this.getItem(position).getFullType());
		view = (TextView) row.findViewById(R.id.card_short_cost);
		view.setText(this.getItem(position).getCost());
		
		return row;
	}
}
