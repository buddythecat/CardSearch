package com.danceswithcaterpillars.cardsearch.content.image;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


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
	
	private static final String SET_IMG_BASE_URL = "http://gatherer.wizards.com/Handles/Image.ashx?type=symbol";
	private static final String CARD_IMG_BASE_URL = "http;//redsunsoft.com/magic/images/";
	
	private String url;
	private String imgUri;
	private final WeakReference<ImageView> imageViewReference;
	
	public GetSetImgTask(ImageView imageView){
		imageViewReference = new WeakReference<ImageView>(imageView);
	}
	/*
	 * Params should be formed as such:
	 * params[0] -> type
	 * params[1] -> set
	 * params[2] -> rarity
	 * params[3] -> cardNo
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		if(params[0].equals(SET_IMG)){
			url = SET_IMG_BASE_URL;
			url += "&set="+params[1];
			url += "&size=small";
			url += "&rarity="+params[2];
			
			imgUri = "/set/"+params[1]+"/"+params[2];
		}
		else if(params[0].equals(CARD_IMG)){
			url = CARD_IMG_BASE_URL;
			url += params[1]+"/";
			url += params[3]+".jpg";
			
			imgUri = "/card/"+params[1]+"/"+params[3];
		}
		else{
			return null;
		}
		try{
			return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
		}catch(MalformedURLException e){
			Log.e(TAG, "Error fetching image", e);
			return null;
		}catch(IOException e){
			Log.e(TAG, "Error reading image from URL", e);
			return null;
		}
	}
	
	@Override 
	protected void onPostExecute(Bitmap result){
		if(isCancelled()){
			result = null;
		}
		if(imageViewReference != null){
			ImageView iv = imageViewReference.get();
			if(iv != null){
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
				iv.setImageResource(android.R.drawable.stat_sys_warning);
			}
		}
	}

}
