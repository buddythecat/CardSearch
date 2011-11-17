package com.danceswithcaterpillars.cardsearch.content.image;

import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.os.Environment;

public class Cache {
	private static ImgCacheStore cache = ImgCacheStore.getInstance();
	
	public static boolean isCacheAvailable(){
		String state = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean isCacheWritable(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean saveCacheFile(String cacheUri, Bitmap image){
		if(isCacheWritable())
			return cache.saveCacheFile(cacheUri, image);
		else
			return false;
	}
	
	public static Bitmap getCachFile(String cacheUri) throws FileNotFoundException{
		if(isCacheAvailable())
			return cache.getCacheFile(cacheUri);
		//else
		return null;
	}
	
}
