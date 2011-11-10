package com.danceswithcaterpillars.cardsearch.content;

import com.danceswithcaterpillars.cardsearch.R;
import static com.danceswithcaterpillars.cardsearch.database.DbConstants.*;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		//view = (TextView)row.findViewById(R.id.card_rule);
		//view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(RULE)));
		view = (TextView)row.findViewById(R.id.card_cost);
		view.setText(adapterCursor.getString(adapterCursor.getColumnIndex(COST)));		
		
		String combinedType = adapterCursor.getString(adapterCursor.getColumnIndex(TYPE));
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE)).equals("")))
				combinedType+=" - "+adapterCursor.getString(adapterCursor.getColumnIndex(SUBTYPE));
		view = (TextView)row.findViewById(R.id.card_type);
		view.setText(combinedType);
		
		String pow = "";
		if(!(adapterCursor.getString(adapterCursor.getColumnIndex(POWER)).equals(""))){
			pow = adapterCursor.getString(adapterCursor.getColumnIndex(POWER));	
			pow += "/"+adapterCursor.getString(adapterCursor.getColumnIndex(TOUGHNESS));
		}
		view = (TextView)row.findViewById(R.id.card_pow);
		view.setText(pow);
		
		return row;
	}

}
