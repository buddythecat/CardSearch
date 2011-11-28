package com.danceswithcaterpillars.cardsearch.content.image;

import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * Cache -
 * this class is designed to store images that have been fetched 
 * by the GetSetImgTask, and write them to the disk.  It is designed
 * to be a parent class, and to be implemented by the ImgCacheStore.
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class Cache {
	/** cache - the instance of the ImgCacheStore */
	private static ImgCacheStore cache = ImgCacheStore.getInstance();
	
	/**
	 * isCacheAvailable - 
	 * This method checks to make sure that the cache is available (readable)
	 * on the disk by making sure that the SD Card is available and mounted.
	 * @return true if we can write the cache to disk.
	 */
	public static boolean isCacheAvailable(){
		//get teh state of the external storage
		String state = Environment.getExternalStorageState();
		
		//check our mount state
		if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			//we can read from the disk, the cache is available.
			return true;
		}
		//it's not, return false.
		else{
			return false;
		}
	}
	
	/**
	 * isCacheWritable - 
	 * this checks if the sdcard is both mounted, and writable.
	 * @return true if we can write to the SDCard (or any external storage)
	 */
	public static boolean isCacheWritable(){
		//get the state of the external storage
		String state = Environment.getExternalStorageState();
		//test the media state
		if(Environment.MEDIA_MOUNTED.equals(state))
			//true if we can write
			return true;
		else
			//false if we cannot.
			return false;
	}
	
	/**
	 * saveCacheFile - 
	 * this method is used to write a portion of the cache (in this case an image) to
	 * the cache itself.  The method is passed the uri of the cache we'll be writing to,
	 * and the bitmap we will write.
	 * 
	 * @param cacheUri - the uri of the cache itself
	 * @param image - the image we will write to the cache
	 * @return true if we succesfully write, false if not.
	 */
	public static boolean saveCacheFile(String cacheUri, Bitmap image){
		//check if the cache is writable
		if(isCacheWritable())
			//write the cache out by accessing our static ImgCacheStore object.
			return cache.saveCacheFile(cacheUri, image);
		else
			//return false if the cache is un-writable
			return false;
	}
	
	/**
	 * getCacheFile - 
	 * This method is used to access the static ImgCacheStore and retrieve an image
	 * based upon it's cacheUri (the uri of the bitmap we want to retrieve)
	 * If no image is found, we return null.
	 * @param cacheUri - the uri of the cache we want to retrieve
	 * @return the Bitmap referenced by that Uri, or null if there was a problem accessing the store
	 * @throws FileNotFoundException - if the bitmap is not found
	 */
	public static Bitmap getCachFile(String cacheUri) throws FileNotFoundException{
		//if we have a cache, try to get the bitmap.
		if(isCacheAvailable())
			return cache.getCacheFile(cacheUri);
		//cache was not available, return null
		return null;
	}
	
}
