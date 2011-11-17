package com.danceswithcaterpillars.cardsearch.content.cards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.danceswithcaterpillars.cardsearch.content.CardSearchReciever;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.util.Log;
		
public class GetCardsTask implements Runnable{
	private static final String TAG = "GetCardsTask";
	private final CardSearchReciever cardTask;
	private boolean details;
	private String cardName;
	
	public GetCardsTask(CardSearchReciever task, String card, boolean moreDetails){
		this.cardTask = task;
		this.cardName = card;
		this.details = moreDetails;
	}
	
	public void run(){
		cardTask.buildCardList(this.getCardList());
		//cardTask.setText(result);
	}
	
	private LinkedList<Card> getCardList(){
		HttpURLConnection con = null;
		String response = null;
		LinkedList<Card> cardList = null;
		
		Log.d(TAG, "Find card: "+cardName);
		boolean completed = false;
		while(!completed){
		try{
			if(Thread.interrupted())
				throw new InterruptedException();
			String query = URLEncoder.encode(cardName, "UTF-8");
			String urlString;
			if(details){
				urlString = (
					"http://daccg.com/ajax_ccgsearch.php" + 
					"?cardname="+query+"&setinfo=true");
			}
			else{
				urlString = (
						"http://daccg.com/ajax_ccgsearch.php" + 
						"?cardname="+query+"*");
			}
			Log.d(TAG, urlString);
			URL url = new URL(urlString);
			con = (HttpURLConnection)url.openConnection();
			con.setReadTimeout(3000);
			con.setConnectTimeout(3000);
			con.setRequestMethod("POST");
			con.setDoInput(true);
			
			//start the connection
			con.connect();
			
			//check if it's been interrupted
			if(Thread.interrupted())
				throw new InterruptedException();
				//Read results
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				//we will only read one line since the returned JSON is all returned in one line.
				response = reader.readLine();
				Log.d("GetCardsTask", response);
				reader.close();
				//parse the JSON
				JSONArray rootArray = new JSONArray(response);
				int len = rootArray.length();
				Log.d(TAG, String.valueOf(len));
				cardList = new LinkedList<Card>();
				for(int i = 0; i<len; i++){
					JSONObject thisCard = rootArray.getJSONObject(i);
					Log.d(TAG, "Loyalty: "+thisCard.getString("loyalty"));
					cardList.add(new Card(
							thisCard.getString("name"),
							thisCard.getString("cost"),
							thisCard.getString("type"),
							thisCard.getString("subtype"),
							thisCard.getString("rule"),
							thisCard.getString("power"),
							thisCard.getString("toughness"),
							i,
							0,
							0,
							thisCard.getString("loyalty")));
					if(details){
						cardList.getLast().setSetInfo(thisCard.getJSONArray("set"));
					}
					Log.d(TAG, cardList.getLast().toString());
				}
				
			}catch(IOException e){
				Log.d(TAG, "IOException", e);
			}catch(InterruptedException e){
				Log.d(TAG, "InterruptedException", e);
			}catch(JSONException e){
				Log.d(TAG, "JSONException", e);
			}
		completed = true;
		}
		return cardList;
	}
}
