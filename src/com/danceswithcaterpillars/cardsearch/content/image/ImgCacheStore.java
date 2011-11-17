package com.danceswithcaterpillars.cardsearch.content.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class ImgCacheStore {
	private static final String TAG = "ImgCacheStore";
	
	private static ImgCacheStore INSTANCE = null;
	private HashMap<String, String> cacheMap;
	private HashMap<String, Bitmap> bitmapMap;
	private static final String cacheDir = "/Android/data/com.danceswithcaterpillars.cardsearch/cache";
	private static final String CACHE_FILENAME = ".cache";
	
	@SuppressWarnings("unchecked")
	private ImgCacheStore(){
		cacheMap = new HashMap<String, String>();
		bitmapMap = new HashMap<String, Bitmap>();
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		
		if(!fullCacheDir.exists()){
			Log.e(TAG, "Directory does not exist for cache");
			cleanCacheStart();
		}
		try{
			ObjectInputStream inputStrm = new ObjectInputStream(new FileInputStream(new File(fullCacheDir.toString(), CACHE_FILENAME)));
			cacheMap = (HashMap<String, String>)inputStrm.readObject();
			inputStrm.close();
			//On any error, we should rebuild the cache
		}catch (StreamCorruptedException e){
			Log.e(TAG, "Corrupted Input Stream", e);
			cleanCacheStart();
		}catch (FileNotFoundException e){
			Log.e(TAG, "Cache File Not Found", e);
			cleanCacheStart();
		}catch (IOException e){
			Log.e(TAG, "I/O Exception reading cache", e);
			cleanCacheStart();
		}catch (ClassNotFoundException e){
			Log.e(TAG, "Class Not Found", e);
			cleanCacheStart();
		}
	}
	
	private void cleanCacheStart(){
		cacheMap = new HashMap<String, String>();
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		fullCacheDir.mkdirs();
		//The nomedia file tells the Camera (and other media-based applications) not to index these directories
		File noMedia = new File(fullCacheDir.toString(), ".nomedia");
		try{
			noMedia.createNewFile();
			Log.i(TAG, "Cache has been created");
		}catch (IOException e){
			Log.e(TAG, "Couldn't create the .noMedia file", e);
		}
	}
	
	//The synchronized keywords allows for async calls
	private synchronized static void createInstance(){
		if(INSTANCE == null){
			INSTANCE = new ImgCacheStore();
		}
		//else
	}
	
	//Return this (static) instance of the Cache
	public static ImgCacheStore getInstance(){
		if(INSTANCE == null)
			createInstance();
		return INSTANCE;
	}
	
	public boolean saveCacheFile(String cacheUri, Bitmap image){
		//First check to see if the Cache already contains our image::
		if(cacheMap.containsKey(cacheUri)){
			return true;
		}
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		String localFileName = new SimpleDateFormat("ddMyyhhmmssSS").format(new java.util.Date())+".PNG";
		File fileUri = new File(fullCacheDir.toString(), localFileName);
		FileOutputStream outputStream = null;
		try{
			outputStream = new FileOutputStream(fileUri);
			image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
			cacheMap.put(cacheUri, localFileName);
			Log.i(TAG, "Saved file: "+cacheUri+"\nURI: "+fileUri.toString()+")");
			bitmapMap.put(cacheUri, image);
			
			ObjectOutputStream fileOutput = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fullCacheDir.toString(), CACHE_FILENAME))));
			fileOutput.writeObject(cacheMap);
			fileOutput.close();
			return true;
		}catch (FileNotFoundException e){
			Log.e(TAG, "Cache was not found to write to.", e);
		}catch(IOException e){
			Log.e(TAG, "Encountered an IO Exception on writing cache out", e);
		}
		return false;
	}
	
	public Bitmap getCacheFile(String cacheUri) throws FileNotFoundException{
		//First case -> the bmp map already contains the file, in which case we need to return that file.
		if(bitmapMap.containsKey(cacheUri))
			return (Bitmap)bitmapMap.get(cacheUri);
		//The second case is that the cachemap doesn't contain the key, we want to break
		if(!cacheMap.containsKey(cacheUri))
			throw new FileNotFoundException("Cached image not available");
		
		String localFileName = cacheMap.get(cacheUri);
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		File fileUri = new File(fullCacheDir.toString(), localFileName);
		
		// if the file doesn't exist, we should return null.  At some point this should be replaced with error handling
		if(!fileUri.exists())
			throw new FileNotFoundException("Cached image not available");
		
		Log.i(TAG, "File: "+cacheUri+" has been found in the cache and will be returned");
		Bitmap bm = BitmapFactory.decodeFile(fileUri.toString());
		bitmapMap.put(cacheUri, bm);
		return bm;
		
	}
}
