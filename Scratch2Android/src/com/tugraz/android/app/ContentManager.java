package com.tugraz.android.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;


import com.tugraz.android.app.filesystem.FileSystem;
import com.tugraz.android.app.parser.Parser;

/**
 * provides content
 * @author alex, niko
 *
 */
public class ContentManager extends Observable{
	
	private ArrayList<HashMap<String, String>> mContentArrayList;
	private ArrayList<ArrayList<HashMap<String, String>>> mSpritesAndBackgroundList;
	
	private FileSystem mFilesystem;
	private Parser mParser;
	private Context mCtx;
	private static final String mTempFile = "tempFile.txt";
	
	public ArrayList<HashMap<String, String>> getContentArrayList(){
		return mContentArrayList;
	}
	
	public ArrayList<ArrayList<HashMap<String, String>>> getSpritesAndBackground(){
		return mSpritesAndBackgroundList;
	}
	
	public void removeSprite(int position){
		mSpritesAndBackgroundList.remove(position);
	}
	
	public void clearSprites(){
		mSpritesAndBackgroundList.clear();		
	}
	
	public void addSprite(ArrayList<HashMap<String, String>> sprite)
	{
		mSpritesAndBackgroundList.add(sprite);
	}
	
	public void remove(int position){
		mContentArrayList.remove(position);
		setChanged();
		notifyObservers();
	}
	
	public void clear(){
		mContentArrayList.clear();
        setChanged();
		notifyObservers();
		
	}
	
	public void add(HashMap<String, String> map){
		mContentArrayList.add(map);
		setChanged();
		notifyObservers();
	}
	
	public ContentManager(){
		mContentArrayList = new ArrayList<HashMap<String, String>>();
		mFilesystem = new FileSystem();
		mParser = new Parser();
	}
	
	/**
	 * load content into data structure
	 */
	public void loadContent(){
		loadContent(mTempFile);
	}
	/**
	 * load content into data structure
	 */
	public void loadContent(String file){
		
		FileInputStream scratch = mFilesystem.createOrOpenFileInput(file, mCtx);
        
		if(scratch != null){
	        
			mSpritesAndBackgroundList.clear();
			mContentArrayList.clear();
			
			mSpritesAndBackgroundList.addAll((mParser.parse(scratch)));
	        mContentArrayList.addAll(mSpritesAndBackgroundList.get(0));

	        try {
				scratch.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        setChanged();
	        notifyObservers();
		} 

	}

	/**
	 * save content
	 */
	public void saveContent(){
		saveContent(mTempFile);	
	}
	
	/**
	 * save content
	 */
	public void saveContent(String file){
		FileOutputStream fd = mFilesystem.createOrOpenFileOutput(file, mCtx);

		String xml = mParser.toXml(mSpritesAndBackgroundList);
		
		try {
			//fd.write(xml.getBytes());
			fd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * test method
	 *
	 */
	public void testSet(){
		mContentArrayList.clear();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(BrickDefine.BRICK_ID, "1");
        map.put(BrickDefine.BRICK_TYPE, String.valueOf(BrickDefine.PLAY_SOUND));
        map.put(BrickDefine.BRICK_NAME, "Test3");
        map.put(BrickDefine.BRICK_VALUE, "/mnt/sdcard/See You Again.mp3");
        mContentArrayList.add(map);
    }
	
	/**
	 * test method
	 */
	public void setContentArrayList(ArrayList<HashMap<String, String>> list){
		mContentArrayList = list;
		setChanged();
		notifyObservers();
	}
	
	public void setSpritesAndBackgroundList(ArrayList<ArrayList<HashMap<String, String>>> spritesAndBackground){
		mSpritesAndBackgroundList = spritesAndBackground;
	}
	
	public void setObserver(Observer observer)
	{
		addObserver(observer);
	}
	public void setContext(Context context)
	{
		 mCtx = context;
	}

}
