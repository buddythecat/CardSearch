package com.danceswithcaterpillars.cardsearch.model;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardArrayAdapter extends ArrayAdapter<Card> {
	private static final String TAG = "CardArrayAdapter";
	private Context adapterContext; 
	
	public CardArrayAdapter(Context context, int viewResourceId, LinkedList<Card> cards){
		super(context, viewResourceId, cards);
		this.adapterContext = context;
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
		
		
		LinearLayout costLayout = (LinearLayout) row.findViewById(R.id.card_short_cost);
		costLayout.removeAllViews();
		String manaCost = this.getItem(position).getCost();
		ImageView thisMana = new ImageView(adapterContext);
		Log.d(TAG, "length"+manaCost.length() + " cost: "+manaCost);
		for(int i = 0; i<manaCost.length(); i++){
			switch(manaCost.charAt(i)){
			case 'U':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setScaleType(ImageView.ScaleType.CENTER);
				thisMana.setImageResource(R.drawable.mana_blue);
				costLayout.addView(thisMana);
				break;
			case 'R':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setImageResource(R.drawable.mana_red);
				costLayout.addView(thisMana);
				break;
			case 'B':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setImageResource(R.drawable.mana_black);
				costLayout.addView(thisMana);
				break;
			case 'G':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				thisMana.setImageResource(R.drawable.mana_green);
				costLayout.addView(thisMana);
				break;
			case 'W':
				thisMana = new ImageView(adapterContext);
				thisMana.setMaxHeight(20);
				thisMana.setMaxWidth(20);
				thisMana.setImageResource(R.drawable.mana_white);
				costLayout.addView(thisMana);
				break;
			case '0':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_0);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '1':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_1);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '2':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_2);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '3':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_3);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '4':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_4);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '5':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_5);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '6':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_6);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '7':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_7);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case '8':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_8);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			case 'X':
				thisMana = new ImageView(adapterContext);
				thisMana.setImageResource(R.drawable.mana_x);
				thisMana.setMaxHeight(10);
				thisMana.setMaxWidth(10);
				costLayout.addView(thisMana);
				break;
			}
		}
				
		return row;
	}
}
