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

/**
 * ImgCacheStore - 
 * This is the actual class that handles the storage and retrieval 
 * of images from the cache.  It does this by using two HashMaps.
 * 
 * The first HashMap stores : <image URI, image path>
 * The second HashMap stores: <image Uri, image>
 * 
 * It creates a cache file in the applications directory in a folder called cache,
 * and creates a static ImgCacheStore called INSTANCE that all operations are done on.
 * 
 * There are only two ways to access this method:
 * 
 * saveCacheFile
 * getCacheFile
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class ImgCacheStore {
	/** TAG - The class name for debugging */
	private static final String TAG = "ImgCacheStore";
	
	/** INSTANCE - the static instance of this class */
	private static ImgCacheStore INSTANCE = null;
	/** cacheMap - the Hashmap containing the URI and path to the file */
	private HashMap<String, String> cacheMap;
	/** bitmapMap - the Hashmap containing the URI and actual bitmap */
	private HashMap<String, Bitmap> bitmapMap;
	
	/** cacheDir - the directory in which we store the cacheDir */
	private static final String cacheDir = "/Android/data/com.danceswithcaterpillars.cardsearch/cache";
	/** CACHE_FILENAME - the filename of the cache file as found in the cacheDir */
	private static final String CACHE_FILENAME = ".cache";
	
	/**
	 * Constructor - 
	 * creates an ImgCacheStore and initializes all of the values for the instance
	 * variables.  This is a private method, as the only ways to access this class
	 * are the saveCacheFile and writeCacheFile commands.
	 * 
	 * SuppressWarning is used to suppress an unchecked cast.
	 */
	@SuppressWarnings("unchecked")
	private ImgCacheStore(){
		//initialize the two hashmaps
		cacheMap = new HashMap<String, String>();
		bitmapMap = new HashMap<String, Bitmap>();
		//set the full cache directory by getting the environments full external storage directory and appending the cacheDir
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		
		//check if the cache exists (using File.exists())
		if(!fullCacheDir.exists()){
			//we don't exist, log this
			Log.e(TAG, "Directory does not exist for cache");
			//start a new cache
			cleanCacheStart();
		}
		//We're going to try and read the cache to ensure it exists, and to try and load the old cache
		try{
			//open an input stream to capture the cache file and read it in.
			ObjectInputStream inputStrm = new ObjectInputStream(new FileInputStream(new File(fullCacheDir.toString(), CACHE_FILENAME)));
			//Here's where we have the need for suppress warnings, since we're reading the HashMap (cacheMap) in from the file.
			cacheMap = (HashMap<String, String>)inputStrm.readObject();
			//if this succeeds (no error thrown), we're going to close the stream, since we've successfuly restored the hasmap
			inputStrm.close();
		//On any error, we should rebuild the cache
		}catch (StreamCorruptedException e){
			//log the input stream error
			Log.e(TAG, "Corrupted Input Stream", e);
			//lets make a new cache
			cleanCacheStart();
		//We couldn't find a cache file, so naturally we're going to want to create one (perhaps a new card has been put in, or firs ttime run)
		}catch (FileNotFoundException e){
			//log it out
			Log.e(TAG, "Cache File Not Found", e);
			//create a new cache
			cleanCacheStart();
		//Perhaps the data has been corrupted, and we get thrown an IOException from the inputStream
		}catch (IOException e){
			//log that we had a problem reading the cache file
			Log.e(TAG, "I/O Exception reading cache", e);
			//lets make a new cache
			cleanCacheStart();
		//perhaps the file we read is being cast to a class we're not aware of (so, it's kinda corrupted)
		}catch (ClassNotFoundException e){
			//log it out
			Log.e(TAG, "Class Not Found", e);
			//then make a new cache
			cleanCacheStart();
		//This is being thrown in to remove the class cast exception
		}
	}
	
	/**
	 * cleanCacheStart -
	 * This method will rebuild a new cache file, save it to the disk, and start to reinstate
	 * all of the local variables.
	 * 
	 * This method is private, as it is only used internally
	 */
	private void cleanCacheStart(){
		//create a new cacheMap
		cacheMap = new HashMap<String, String>();
		//open a new file to the location
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		//make the directory that we instantiated above
		fullCacheDir.mkdirs();
		//The nomedia file tells the Camera (and other media-based applications) not to index these directories
		File noMedia = new File(fullCacheDir.toString(), ".nomedia");
		//lets try to write out a new file (the nomedia file)
		try{
			//create the noMedia file.
			noMedia.createNewFile();
			//log that we created the file
			Log.i(TAG, "Cache has been created");
		//if we couldn't create the .nomedia file, log it out but don't die.
		}catch (IOException e){
			//logging it out
			Log.e(TAG, "Couldn't create the .noMedia file", e);
		}
	}
	
	/**
	 * createInstance - 
	 * This is a synchronized method that creates an instance of the 
	 * image cache.  The synchronized keywords ensures that when this 
	 * thread is called, ONLY this method will be running in a thread.  No other
	 * synchronized method from this class will be able to be called.
	 * It will also ensure that, if this method were to be called in a sequence
	 * of calls, this MUST happen first, then subsequent calls/threads will be processed.
	 * 
	 * This is required since we don't want to do any reading/writing from the cache
	 * without having an instance of the cache being instantiated.  and since the instance
	 * is static and used across the application, we really only want this method to be called
	 * one at a time, one thread at a time, to ensure we don't lock up the file.
	 * 
	 * This method instantiates the static instance of the cache
	 */
	private synchronized static void createInstance(){
		//if we don't already have an assigned ImgCacheStore, we're going to create one.
		if(INSTANCE == null)
			INSTANCE = new ImgCacheStore();
		//if it does exist, we're not going to create it
	}
	
	/**
	 * getInstance - 
	 * we're going to get an instance of the ImgCacheStore, that way
	 * it can be used within the Cache class.  This will also check if the
	 * instance exists, and create it if necessary.
	 * @return the reference to this ImgCacheStore
	 */
	public static ImgCacheStore getInstance(){
		//check if the INSTANCE exists,
		if(INSTANCE == null)
			//if not, create an instance
			createInstance();
		//return this INSTANCE
		return INSTANCE;
	}
	
	/**
	 * saveCacheFile - 
	 * This is one of the two public methods that are used to access
	 * the ImgCache, and to save the file to the cache, using the uri 
	 * to reference this file, and the image to actually write to the disk.
	 * 
	 * When writing the image, it will compress said image.
	 * 
	 * TODO add a limit to the cache size... we don't want this to get bloated
	 * 
	 * @param cacheUri - the Uri that this file will be referenced by
	 * @param image - the image we will be saving
	 * @return - true if we could write it to disk
	 */
	public boolean saveCacheFile(String cacheUri, Bitmap image){
		//First check to see if the Cache already contains our image::
		if(cacheMap.containsKey(cacheUri))
			//if it is, we're not saving anything there, so let's just return true. ;]
			return true;
		
		//create a File that will reference the full directory path of the cache
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		//create the local filename by using the date with time appended
		String localFileName = new SimpleDateFormat("ddMyyhhmmssSS").format(new java.util.Date())+".PNG";
		//create a file whose path is the full cache directory, and then the local file name.
		File fileUri = new File(fullCacheDir.toString(), localFileName);
		//lets instantiate an outputstream that we're going to use to write this file to the disk
		FileOutputStream outputStream = null;
		try{
			//lets open the output stream to the file's path
			outputStream = new FileOutputStream(fileUri);
			//compress the image using .PNG, and use the outputStream to write it out
			image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			//flush the stream
			outputStream.flush();
			//close the stream
			outputStream.close();
			
			//now lets write the necessary values in the cacheMap and bitmapMap
			cacheMap.put(cacheUri, localFileName);
			
			//log this out
			Log.i(TAG, "Saved file: "+cacheUri+"\nURI: "+fileUri.toString()+")");
			bitmapMap.put(cacheUri, image);
			
			//now we're going to write out the cachemap to the disk (since we need to ensure that this gets saved every time)
			//so lets open an output stream
			ObjectOutputStream fileOutput = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fullCacheDir.toString(), CACHE_FILENAME))));
			//lets write the change
			fileOutput.writeObject(cacheMap);
			//lets close the stream
			fileOutput.close();
			//lets return true, since we've managed to write the file
			return true;
		}catch (FileNotFoundException e){
			//log that we couldn't write to the cache
			Log.e(TAG, "Cache was not found to write to.", e);
		}catch(IOException e){
			//log that we had trouble writing to the disk
			Log.e(TAG, "Encountered an IO Exception on writing cache out", e);
		}
		//return false if we didn't manage to write.
		return false;
	}
	
	/**
	 * getCacheFile - 
	 * this method will pull Bitmaps from the disk and return them back to whatever requests them.
	 * The files are referenced by their Uri.  
	 * @param cacheUri - the uri of the file to retrieve
	 * @return - the retrieved bitmap
	 * @throws FileNotFoundException - if the bitmap isn't found
	 */
	public Bitmap getCacheFile(String cacheUri) throws FileNotFoundException{
		//First case -> the bmp map already contains the file, in which case we need to return that file.
		if(bitmapMap.containsKey(cacheUri))
			return (Bitmap)bitmapMap.get(cacheUri);
		//The second case is that the cachemap doesn't contain the key, we want to break
		if(!cacheMap.containsKey(cacheUri))
			throw new FileNotFoundException("Cached image not available");
		
		//get the local file name
		String localFileName = cacheMap.get(cacheUri);
		//get the cache directory as a File
		File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(), cacheDir);
		//get the file using the file Uri
		File fileUri = new File(fullCacheDir.toString(), localFileName);
		
		// if the file doesn't exist, we should return null.  At some point this should be replaced with error handling
		if(!fileUri.exists())
			throw new FileNotFoundException("Cached image not available");
		
		//log that we have the bitmap in our hands
		Log.i(TAG, "File: "+cacheUri+" has been found in the cache and will be returned");
		//decode the file
		Bitmap bm = BitmapFactory.decodeFile(fileUri.toString());
		//put the file (that we now have access to) in the bitmapMap, for easy access
		bitmapMap.put(cacheUri, bm);
		//return the bitmap
		return bm;
	}
}
