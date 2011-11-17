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
 * For code involved in this package, please refer to these two pages:
 * 
 * http://www.flexjockey.com/2011/03/create-a-pretty-simple-cache-for-android/
 * http://www.flexjockey.com/2011/02/load-images-and-data-asynchronously-on-your-android-applications/
 * 
 * @author rtufano
 *
 */
public class GetSetImgTask extends AsyncTask<String, Integer, Bitmap> {
	private static final String TAG = "GetSetImgTask";
	
	public static final String CARD_IMG 	= "card";
	public static final String SET_IMG 		= "set image";
	
	private static final String SET_IMG_BASE_URL = "http://gatherer.wizards.com/Handlers/Image.ashx?type=symbol";
	private static final String CARD_IMG_BASE_URL = "http://magiccards.info/scans/en/";
	
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
	
	private String url;
	private String imgUri;
	private final WeakReference<ImageView> imageViewReference;
	private final WeakReference<ProgressBar> progressBarReference;
	private final String[][] setInfo;
	private int count;
	
	
	public GetSetImgTask(ImageView imageView, ProgressBar progress, String[][] sets){
		imageViewReference = new WeakReference<ImageView>(imageView);
		progressBarReference = new WeakReference<ProgressBar>(progress);
		setInfo = sets;
		count = 0;
	}
	/*
	 * Params should be formed as such:
	 * setInfo[0] -> set
	 * setInfo[1] -> rarity
	 * setInfo[2] -> cardNo
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		if(count<setInfo.length){
			if(params[0].equals(SET_IMG)){
				url = SET_IMG_BASE_URL;
				url += "&set="+((setCorrection.containsKey(setInfo[count][0].toLowerCase()))?setCorrection.get(setInfo[count][0].toLowerCase())[0]:setInfo[count][0].toLowerCase());
				url += "&size=small";
				url += "&rarity="+setInfo[count][1];
				
				imgUri = "/set/"+setInfo[count][0]+"/"+setInfo[count][1];
			}
			else if(params[0].equals(CARD_IMG)){
				url = CARD_IMG_BASE_URL;
				url += ((setCorrection.containsKey(setInfo[count][0].toLowerCase()))?setCorrection.get(setInfo[count][0].toLowerCase())[1]:setInfo[count][0].toLowerCase())+"/";
				url += setInfo[count][2]+".jpg";
				
				imgUri = "/card/"+setInfo[count][0]+"/"+setInfo[count][2];
			}
			else{
				return null;
			}
		}
		//Out of sets to try
		else
			return null;
		try{
			try{
				return(Cache.getCachFile(imgUri));
			}catch(FileNotFoundException e){
				Log.i(TAG, "Bitmap not in Cache -- Downloading Bitmap");
				Log.i(TAG, "URL: "+url);
				return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
			}
		}catch(MalformedURLException e){
			Log.e(TAG, "Error fetching image", e);
			return null;
		}catch(IOException e){
			Log.e(TAG, "Error reading image from URL", e);
			count++;
			return doInBackground(params);
		}
	}
	
	@Override 
	protected void onPostExecute(Bitmap result){
		if(progressBarReference.get()!=null)
			progressBarReference.get().setVisibility(View.GONE);
		if(isCancelled()){
			result = null;
		}
		if(imageViewReference != null){
			ImageView iv = imageViewReference.get();
			iv.setVisibility(View.VISIBLE);
			if(iv != null){
				result = Bitmap.createScaledBitmap(result, (int)(result.getWidth()*1), (int)(result.getHeight()*1), true);
				if(result==null)
					iv.setImageResource(android.R.drawable.stat_sys_warning);
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
