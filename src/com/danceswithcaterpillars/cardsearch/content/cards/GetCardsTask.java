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

import com.danceswithcaterpillars.cardsearch.content.CardSearchReceiver;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.util.Log;

/**
 * GetCardsTask - 
 * This is a simple thread that is in charge of retrieving a list of cards
 * from an online service located at
 * http://daccg.com/ajaxcardsearch.php
 * 
 * it will then return the result of this query to the caller by way of the caller's buildCardList method.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class GetCardsTask implements Runnable{
	/** TAG - the class name for logging */
	private static final String TAG = "GetCardsTask";
	/** cardTask - the CardSearchReceiver (or class which started the thread) */
	private final CardSearchReceiver cardTask;
	/** details - should the task get the set details from teh provider? */
	private boolean details;
	/** cardName - the query string's name */
	private String cardName;
	
	/**
	 * Constructor - 
	 * creates the GetCardsTask Object
	 * @param task - the Object to receive the results
	 * @param card - the Card to search for
	 * @param moreDetails - boolean to flag if we should get set details for a card
	 */
	public GetCardsTask(CardSearchReceiver task, String card, boolean moreDetails){
		this.cardTask = task; //set teh task
		this.cardName = card; //set the card name
		this.details = moreDetails; //set the details
	}
	
	/*
	 * Run the thread,
	 * which will return the results to the calling method
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		//get the cardlist and then return it back
		cardTask.buildCardList(this.getCardList());
	}
	
	/**
	 * getCardList - 
	 * query the online store to get a list of cards that match the query parameter
	 * @return - a LinkedList of cards as returned by the Query
	 */
	private LinkedList<Card> getCardList(){
		//set the connection
		HttpURLConnection con = null;
		//set the response
		String response = null;
		//set the cardList
		LinkedList<Card> cardList = null;
		
		//log that we're searching for a card
		Log.d(TAG, "Find card: "+cardName);
		//set the completion flag to false
		boolean completed = false;
		//continue to query until we've completed
		while(!completed){
		try{
			//if the thread is interrupted, kill the process
			if(Thread.interrupted())
				throw new InterruptedException();
			
			//encode the query part of the string
			String query = URLEncoder.encode(cardName, "UTF-8");
			//create a url string
			String urlString;
			//if we want to get details:
			if(details){
				//attach &setinfo=true to the end of the URL
				urlString = (
					"http://daccg.com/ajax_ccgsearch.php" + 
					"?cardname="+query+"&setinfo=true");
			}
			//if we don't want details:
			else{
				//do not append to the end of the string
				urlString = (
						"http://daccg.com/ajax_ccgsearch.php" + 
						"?cardname="+query+"*");
			}
			//log out the string we're using to query the service
			Log.d(TAG, urlString);
			//create the URL
			URL url = new URL(urlString);
			//initialize a connection to the service
			con = (HttpURLConnection)url.openConnection();
			//set the read timeout limit to 3000 millis
			con.setReadTimeout(3000);
			//set the connection timeout limit to 3000 millis
			con.setConnectTimeout(3000);
			//set the request method to POST (we're POSTing data to the service)
			con.setRequestMethod("POST");
			//flag the connection that we will be reading input
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
				//create a linkedlist of cards
				cardList = new LinkedList<Card>();
				//for each card in the list, create the card
				for(int i = 0; i<len; i++){
					//create a JSONObject for the card at position i in the JSONArray
					JSONObject thisCard = rootArray.getJSONObject(i);
					//log the card out to the console
					Log.d(TAG, thisCard.toString());
					//add the card to the list
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
					//if we want to have details
					if(details){
						//append the setinfo for the last card added
						cardList.getLast().setSetInfo(thisCard.getJSONArray("set"));
					}
					//log the last card out to the logcat
					Log.d(TAG, cardList.getLast().toString());
				}
			//if we hit an IO Exception, log and die
			}catch(IOException e){
				Log.d(TAG, "IOException", e);
			//if we hit an InterruptedException is hit, log and die
			}catch(InterruptedException e){
				Log.d(TAG, "InterruptedException", e);
			//if we hit a JSONException, log and die
			}catch(JSONException e){
				Log.d(TAG, "JSONException", e);
			}
			//we've gotten a list of cards at this point, set the completed flag to true
			completed = true;
		}
		//return the list of cards back to the caller
		return cardList;
	}
}
