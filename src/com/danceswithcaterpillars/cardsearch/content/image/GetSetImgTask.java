package com.danceswithcaterpillars.cardsearch.content.image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


/**
 * GetSetImgTask - 
 * This is a symple ASyncTask that is used to fetch images for both cards,
 * and their associated sets.
 * 
 * The class implements an asyncronous task.  When the thread is created, it binds
 * to an ImageView using a WeakReference, and on completion it will fill this weak reference.
 * 
 * There is also an option embedded in this task to handle progress bars, and the swapping of image views
 * and their associated progress.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class GetSetImgTask extends AsyncTask<String, Integer, Bitmap> {
	/** TAG - the name of the class for logging */
	private static final String TAG = "GetSetImgTask";
	
	/** CARD_IMG - constant to flag that we're searching for a card */
	public static final String CARD_IMG 	= "card";
	/** SET_IMG - constant to flag that we're searchign for a set image */
	public static final String SET_IMG 		= "set image";
	
	/** SET_IMG_BASE_URL - the url which we will use to get the set images */
	private static final String SET_IMG_BASE_URL = "http://gatherer.wizards.com/Handlers/Image.ashx?type=symbol";
	/** CARD_IMG_BASE_URL - the url taht we will use to get the card images */
	private static final String CARD_IMG_BASE_URL = "http://magiccards.info/scans/en/";
	
	/** 
	 * setCorrection - this is a hashmap that I'm using to account for discrepancies in the
	 * differences in the set code between each site.  The hash map is set up so that the key
	 * for the map is the set code as given by daccg.com, the value is a pair of strings, the
	 * first one is for Wizards of The Coast's Gatherer service, the second is for magiccards.info
	 * scan service. 
	 */
	private static final HashMap<String, String[]> setCorrection = new HashMap<String, String[]>();
	static{
		//The setcorrection map is structured: <Key = Received Set><Value = Corrected Set{set image, card image}>
		setCorrection.put("usg", new String[]{"uz","us"});
		setCorrection.put("por", new String[]{"po", "po"});
		setCorrection.put("7ed", new String[]{"7e", "7e"});
		setCorrection.put("8ed", new String[]{"8e", "8e"});
		setCorrection.put("9ed", new String[]{"9e", "9e"});
		setCorrection.put("10ed", new String[]{"10e", "10e"});
		setCorrection.put("lgn", new String[]{"lgn", "lg"});
		setCorrection.put("inv", new String[]{"in", "in"});
		setCorrection.put("pcy", new String[]{"pc", "plc"});
		setCorrection.put("ucs", new String[]{"cg", "ud"});
	}
	
	/** url - the url of the image we're about to get */
	private String url;
	/** imgUri - the uri of the image we want to get */
	private String imgUri;
	/** imageViewReference - this is a WeakReference to the image view that will hold the retrieved image */
	private final WeakReference<ImageView> imageViewReference;
	/** progressBarReference - this is a WeakReference to the image view that will hold the progress bar (if any) */
	private final WeakReference<ProgressBar> progressBarReference;
	/** setInfo - this is a double-array of Strings that stores the set info as taken from the Card's JSONArray */
	private final String[][] setInfo;
	/** count - this is the count of somethign, I forget */
	private int count;
	
	/**
	 * Constructor - 
	 * creates a new task, and instantiates it's local variables
	 * @param imageView - the image view which we will tie to a weak reference 
	 * @param progress - the progressbar which we will tie to a weak reference (may be null)
	 * @param sets - all of the setinfo associated with this card
	 */
	public GetSetImgTask(ImageView imageView, ProgressBar progress, String[][] sets){
		//bind our references
		imageViewReference = new WeakReference<ImageView>(imageView);
		progressBarReference = new WeakReference<ProgressBar>(progress);
		//asign the set info
		setInfo = sets;
		//set the count to 0
		count = 0;
	}

	/**
	 * doInBackground - 
	 * this method is called after the thread is created and has permission to run.
	 * It's job is to 
	 * 	1) Create the URL which we will be using to retrieve the card.
	 * 		this is determined by the params[0] passed to the method, and uses
	 * 		the constants (publicly visible) that we declare above
	 * 	2) Create the URI which we will use to (perhaps) store the image in the cache
	 *  3) Try to get the image out of the cache - if this succeeds, we're done.
	 *  4) If we're not in the cache, try to get the image from the net
	 *  5) If we get the image successfuly, we're done, if not we must handle any errors
	 *  	- One specific error we should handle is an IOException, which means that 
	 *  		the image wasn't found online.  In this case, we should increment count, and
	 *  		call the function again.  This will spawn the method again, though it will search
	 *  		through the next set associated with the card.  This is thanks to faulty information
	 *  		as supplied by daccg.
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		//if the count is still less then the amount of sets associated with the cards, we should make a URI
		if(count<setInfo.length){
			//check to see which image we need
			if(params[0].equals(SET_IMG)){
				//create the url
				url = SET_IMG_BASE_URL;
				url += "&set="+((setCorrection.containsKey(setInfo[count][0].toLowerCase()))?setCorrection.get(setInfo[count][0].toLowerCase())[0]:setInfo[count][0].toLowerCase());
				url += "&size=small";
				url += "&rarity="+setInfo[count][1];
				
				//create the uri
				imgUri = "/set/"+setInfo[count][0]+"/"+setInfo[count][1];
			}
			//if we need the image of the card
			else if(params[0].equals(CARD_IMG)){
				//build the url
				url = CARD_IMG_BASE_URL;
				url += ((setCorrection.containsKey(setInfo[count][0].toLowerCase()))?setCorrection.get(setInfo[count][0].toLowerCase())[1]:setInfo[count][0].toLowerCase())+"/";
				url += setInfo[count][2]+".jpg";
				
				//build the uri
				imgUri = "/card/"+setInfo[count][0]+"/"+setInfo[count][2];
			}
			//if we receive a bad parameter, return null.
			else{
				return null;
			}
		}
		//Out of sets to try, we should die and return null
		else
			return null;
		//now we're going to get an image (since we have all of the details (assumedly)
		try{
			//try to get the file out of the cache first
			try{
				return(Cache.getCachFile(imgUri));
			//if the file isn't in the cache, the call above will throw a FileNotFoundException.  If this happens we're going on the net to get it
			}catch(FileNotFoundException e){
				//log out that we're downloading the image
				Log.i(TAG, "Bitmap not in Cache -- Downloading Bitmap");
				Log.i(TAG, "URL: "+url);
				//return the image
				return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
			}
		//if we have a problem getting the image due to a dagamed URL, log it and die.
		}catch(MalformedURLException e){
			Log.e(TAG, "Error fetching image", e);
			return null;
		//if we have a problem getting the image due to improper card information, this catch will trigger
		}catch(IOException e){
			//log it out
			Log.e(TAG, "Error reading image from URL", e);
			//increment the set number
			count++;
			//call the method again, and return whatever this recursive call does
			return doInBackground(params);
		}
	}
	
	/**
	 * onPostExecute - 
	 * Once we're done executing the task, we have to store the 
	 * cache'd image (if it was just downloaded), and then 
	 * place the image in it's associated view.  We may also 
	 * have to hide the progress bar
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override 
	protected void onPostExecute(Bitmap result){
		//if we have a progress bar, we have to set it to invisible
		if(progressBarReference.get()!=null)
			progressBarReference.get().setVisibility(View.GONE);
		//if the task was canceled, just set the result to null, and return it.
		if(isCancelled()){
			result = null;
		}
		//if there is an image view reference assigned to the task, we can continue
		if(imageViewReference != null){
			//get the image view stored in the reference
			ImageView iv = imageViewReference.get();
			//set the image view to visible
			iv.setVisibility(View.VISIBLE);
			//if our imageview exists...
			if(iv != null){
				//set the resultant bitmap to a new (scaled) version
				result = Bitmap.createScaledBitmap(result, (int)(result.getWidth()*1), (int)(result.getHeight()*1), true);
				//TODO replace the loading image and stuff to a mtg card back
				//if the result doesn't exist, we should set the image resource to a bit error logo... 
				if(result==null)
					iv.setImageResource(android.R.drawable.stat_sys_warning);
				//if the result does exist, set the image bitmap
				else
					iv.setImageBitmap(result);
			}
		}
		//write the file to our cache
		Cache.saveCacheFile(imgUri, result);
	}
	
	@Override
	protected void onPreExecute(){
		if(imageViewReference != null){
			ImageView iv = imageViewReference.get();
			if(iv != null){
				iv.setVisibility(View.GONE);
				iv.setImageResource(android.R.drawable.stat_sys_warning);
			}
		}
	}

}
