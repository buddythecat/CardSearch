package com.danceswithcaterpillars.cardsearch.content.cards;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		
		view = (TextView) row.findViewById(R.id.card_short_rule);
		String rule = this.getItem(position).getRule();
		Log.d(TAG, "Rule: '"+rule+"'");
		
		if(rule.length()==0){
			view.setVisibility(View.GONE);
		}
		else{
			view.setVisibility(View.VISIBLE);
			view.setText(Card.buildCardRulesWithImages(rule, adapterContext));
		}
		
		
		LinearLayout costLayout = (LinearLayout) row.findViewById(R.id.card_short_cost);
		costLayout.removeAllViews();
		String manaCost = this.getItem(position).getCost();
		
		Card.buildManaBar(new WeakReference<LinearLayout>(costLayout), manaCost);
		
		
		String pow = "";
		view = (TextView)row.findViewById(R.id.card_short_pow);
		//case for a creature
		if(!(this.getItem(position).getPower().equals(""))){
			view.setVisibility(View.VISIBLE);
			pow = this.getItem(position).getPower();
			pow += "/"+this.getItem(position).getToughness();
			view.setText(pow);
		}
		//Case for a planeswalker
		else if(!(this.getItem(position).getLoyalty().equals(""))){
			view.setVisibility(View.VISIBLE);
			pow = "Loyalty: ";
			pow += this.getItem(position).getLoyalty();
			view.setText(pow);
		}
		//case for an instant or sorcery
		else{
			view.setVisibility(View.GONE);
		}
				
		return row;
	}
}
