package com.danceswithcaterpillars.cardsearch.content.local;

import com.danceswithcaterpillars.cardsearch.R;

import static com.danceswithcaterpillars.cardsearch.content.local.db.DbConstants.*;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CardCursorAdapter extends SimpleCursorAdapter {
	private static final String TAG = "CardCursorAdapter";
	private Context adapterContext;
	private int adapterLayout;
	private Cursor adapterCursor;
	
	public CardCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		adapterContext = context;
		adapterLayout = layout;
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		adapterCursor = this.getCursor();
		adapterCursor.moveToPosition(position);
		View row;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(adapterLayout, null);
		}
		else{
			row = convertView;
		}
		
		TextView view = (TextView)row.findViewById(R.id.card_name);
		Log.d(TAG, "Col: "+adapterCursor.getColumnIndex(CARD_NAME));
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(CARD_NAME)));
		
		
		view = (TextView)row.findViewById(R.id.card_rule);
		String rule = adapterCursor.getString(adapterCursor.getColumnIndex(RULE));
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
				view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(RULE)));
			
			
		}
		LinearLayout costLayout = (LinearLayout)row.findViewById(R.id.card_cost);
		costLayout.removeAllViewsInLayout();
		String manaCost = adapterCursor.getString(adapterCursor.getColumnIndex(COST));
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
		
		//view.setText(manaCost);		
		
		String combinedType = adapterCursor.getString(adapterCursor.getColumnIndex(TYPE));
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE)).equals("")))
				combinedType+=" - "+adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE));
		view = (TextView)row.findViewById(R.id.card_type);
		view.setText(combinedType);
		
		String pow = "";
		view = (TextView)row.findViewById(R.id.card_pow);
		//case for a creature
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(POWER)).equals(""))){
			view.setVisibility(View.VISIBLE);
			pow = adapterCursor.getString(adapterCursor.getColumnIndex(POWER));
			pow += "/"+adapterCursor.getString(adapterCursor.getColumnIndex(TOUGHNESS));
			view.setText(pow);
		}
		//Case for a planeswalker
		else if(!(adapterCursor.getString(adapterCursor.getColumnIndex(LOYALTY)).equals(""))){
			pow = "Loyalty: ";
			pow += adapterCursor.getString(adapterCursor.getColumnIndex(LOYALTY));
			view.setText(pow);
		}
		//case for an instant or sorcery
		else{
			view.setVisibility(View.GONE);
		}
		
		
		return row;
	}

}
