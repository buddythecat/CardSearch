package com.danceswithcaterpillars.cardsearch.model;

import java.util.LinkedList;

import com.danceswithcaterpillars.cardsearch.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
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
		
		view = (TextView) row.findViewById(R.id.card_short_rule);
		String rule = this.getItem(position).getRule();
		Log.d(TAG, "Rule: '"+rule+"'");
		
		if(rule.length()==0){
			view.setVisibility(View.GONE);
		}
		else{
			SpannableStringBuilder builder = new SpannableStringBuilder();
			view.setVisibility(View.VISIBLE);
			if(rule.contains("{")){
				builder.append(rule);
				if(rule.contains("{T}")){
					int tapLoc = rule.indexOf("{T}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.tap);
					d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
					ImageSpan tapImageSpan = new ImageSpan(d);
					builder.setSpan(tapImageSpan, tapLoc, tapLoc+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{W}")){
					int whiteMana = rule.indexOf("{W}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_white);
					d.setBounds(0,0,25,25);
					ImageSpan whiteImageSpan = new ImageSpan(d);
					builder.setSpan(whiteImageSpan, whiteMana, whiteMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{B}")){
					int blackMana = rule.indexOf("{B}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_black);
					d.setBounds(0,0,25,25);
					ImageSpan blackImageSpan = new ImageSpan(d);
					builder.setSpan(blackImageSpan, blackMana, blackMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{G}")){
					int greenMana = rule.indexOf("{G}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_green);
					d.setBounds(0,0,25,25);
					ImageSpan greenImageSpan = new ImageSpan(d);
					builder.setSpan(greenImageSpan, greenMana, greenMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{U}")){
					int blueMana = rule.indexOf("{U}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_blue);
					d.setBounds(0,0,25,25);
					ImageSpan blueImageSpan = new ImageSpan(d);
					builder.setSpan(blueImageSpan, blueMana, blueMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{R}")){
					int redMana = rule.indexOf("{R}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_red);
					d.setBounds(0,0,25,25);
					ImageSpan redImageSpan = new ImageSpan(d);
					builder.setSpan(redImageSpan, redMana, redMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{1}")){
					int xMana = rule.indexOf("{1}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_1);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{2}")){
					int xMana = rule.indexOf("{2}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_2);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{3}")){
					int xMana = rule.indexOf("{3}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_3);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{4}")){
					int xMana = rule.indexOf("{4}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_4);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{5}")){
					int xMana = rule.indexOf("{5}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_5);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{6}")){
					int xMana = rule.indexOf("{6}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_6);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{7}")){
					int xMana = rule.indexOf("{7}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_7);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if(rule.contains("{X}")){
					int xMana = rule.indexOf("{X}");
					Drawable d = adapterContext.getResources().getDrawable(R.drawable.mana_x);
					d.setBounds(0,0,25,25);
					ImageSpan xImageSpan = new ImageSpan(d);
					builder.setSpan(xImageSpan, xMana, xMana+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				view.setText(builder);
			}
			else
				view.setText(rule);
		}
		
		
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
