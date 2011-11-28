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

/**
 * CardArrayAdapter - 
 * This is a specialized adapter that binds to a LinkedList of Card objects.
 * This is usually only used by the GetCardsTask, which returns a linkedList of Cards
 * from the online database
 * 
 * It binds itself to the supplied view, and then fills the relevant details
 *  
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class CardArrayAdapter extends ArrayAdapter<Card> {
	/** TAG - the classes name for loggin */
	private static final String TAG = "CardArrayAdapter";
	/** adapterContext - the context in which the adapter is to be sed */
	private Context adapterContext; 
	
	/**
	 * CardArrayAdapater - 
	 * the constructor for this adapter.  It calls the superclass's adapter, and
	 * binds some of the resources to instance vars.
	 * @param context - the context in which the adapter is to be used
	 * @param viewResourceId - the view resource which will be filled
	 * @param cards	- the linked list of cards that will fill this adapter
	 */
	public CardArrayAdapter(Context context, int viewResourceId, LinkedList<Card> cards){
		//call the super's constructor
		super(context, viewResourceId, cards);
		//set the adapter context
		this.adapterContext = context;
	}
	
	/*
	 * getView - 
	 * fills the row for each card in the LinkedList.
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//current row
		View row;
		//if we're not converting a view, we need to get a new row
		if(convertView == null){
			//get a LayoutInflator
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//inflate the view and set it to the row
			row = vi.inflate(R.layout.card_list_item, null);
		}
		//we're converting a view
		else{
			//have the row reference the convertView
			row = convertView;
		}
		//fill the name
		TextView view = (TextView) row.findViewById(R.id.card_short_name);
		view.setText(this.getItem(position).getName());
		//fill the type
		view = (TextView) row.findViewById(R.id.card_short_type);
		view.setText(this.getItem(position).getFullType());
		
		//fill the rule by using Card.buildCardRulesWithImages()
		view = (TextView) row.findViewById(R.id.card_short_rule);
		String rule = this.getItem(position).getRule();
		Log.d(TAG, "Rule: '"+rule+"'");
		//if the length is zero, just set the rule's view to invisibile
		if(rule.length()==0){
			view.setVisibility(View.GONE);
		}
		//if not, we need to build the card's rule.
		else{
			//set the rule to visible
			view.setVisibility(View.VISIBLE);
			//set the text of the rule by calling the buildCardRulesWithImages method
			view.setText(Card.buildCardRulesWithImages(rule, adapterContext));
		}
		
		//bind the cost layout, and fill the mana symbols by calling the buildManaBar method
		LinearLayout costLayout = (LinearLayout) row.findViewById(R.id.card_short_cost);
		costLayout.removeAllViews();
		String manaCost = this.getItem(position).getCost();
		
		Card.buildManaBar(new WeakReference<LinearLayout>(costLayout), manaCost);
		
		//fill the power/toughness OR planeswalker loyalty
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
		
		//return the row
		return row;
	}
}
