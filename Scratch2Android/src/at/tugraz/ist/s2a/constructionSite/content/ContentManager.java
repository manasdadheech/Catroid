package at.tugraz.ist.s2a.constructionSite.content;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import at.tugraz.ist.s2a.ConstructionSiteActivity;
import at.tugraz.ist.s2a.R;
import at.tugraz.ist.s2a.utils.Utils;
import at.tugraz.ist.s2a.utils.filesystem.FileSystem;
import at.tugraz.ist.s2a.utils.parser.Parser;

/**
 * provides content
 * @author alex, niko, thomas
 *
 */
public class ContentManager extends Observable{
	
	private ArrayList<HashMap<String, String>> mCurrentSpriteList;
	private ArrayList<String> mContentGalleryList;
	private ArrayList<String> mAllContentNameList;
	
	
	private ArrayList<Pair<String, ArrayList<HashMap<String, String>>>> mAllContentArrayList;
	
	private FileSystem mFilesystem;
	private Parser mParser;
	private Context mCtx;
	private static final String mTempFile = "defaultSaveFile.spf";
	private int mCurrentSprite;
	private int mIdCounter;
	private static String STAGE;

	
	public ArrayList<HashMap<String, String>> getCurrentSpriteList(){
		return mCurrentSpriteList;
	}
	
	public ArrayList<String> getContentGalleryList(){
		return mContentGalleryList;
	}
	
	public void resetContent(){
		mCurrentSpriteList = null; //TODO @niko, warum hast du das auskommentiert? weil dan geht testClear nicht
		mAllContentArrayList.clear();
		mCurrentSprite = 0;
		mIdCounter = 0;
		mContentGalleryList.clear();
		}
	
	public void addSprite(Pair<String, ArrayList<HashMap<String, String>>> sprite)
	{
		mAllContentArrayList.add(sprite);
		mCurrentSprite = mAllContentArrayList.size()-1;
		mAllContentNameList.add(sprite.first);
		switchSprite(mCurrentSprite);
	}
	
	public void switchSprite(int position){
		mCurrentSpriteList = mAllContentArrayList.get(position).second;
		mCurrentSprite = position;
		loadContentGalleryList();
		setChanged();
		notifyObservers();
	}
	
	private void loadContentGalleryList(){
		mContentGalleryList.clear();
		for(int i = 0; i < mCurrentSpriteList.size(); i++){
			String type = mCurrentSpriteList.get(i).get(BrickDefine.BRICK_TYPE);
			
			if( type.equals(String.valueOf(BrickDefine.SET_BACKGROUND)) 
					|| type.equals(String.valueOf(BrickDefine.SET_COSTUME))){
			
				mContentGalleryList.add(mCurrentSpriteList.get(i).get(BrickDefine.BRICK_VALUE_1));
			}
		}
	}
	

	public void removeBrick(int position){
		String type = mCurrentSpriteList.get(position).get(BrickDefine.BRICK_TYPE);
		if( type.equals(String.valueOf(BrickDefine.SET_BACKGROUND)) 
				|| type.equals(String.valueOf(BrickDefine.SET_COSTUME))){
			
			mContentGalleryList.remove(mCurrentSpriteList.get(position).get(BrickDefine.BRICK_VALUE_1));
		}
		mCurrentSpriteList.remove(position);
		setChanged();
		notifyObservers();
	}
	
	public void addBrick(HashMap<String, String> map){
		map.put(BrickDefine.BRICK_ID, ((Integer)mIdCounter).toString());
		mIdCounter++;
		mCurrentSpriteList.add(map);
		
		setChanged();
		notifyObservers(mCurrentSpriteList.size()-1);
	}
	
	public ContentManager(Context context){
		mCtx = context;
		STAGE = mCtx.getString(R.string.stage);
		mCurrentSpriteList = null;
		mAllContentArrayList = new ArrayList<Pair<String, ArrayList<HashMap<String, String>>>>();
		
		mFilesystem = new FileSystem();
		mParser = new Parser();
		
		mIdCounter = 0;	
		mContentGalleryList = new ArrayList<String>();
		
		mAllContentNameList = new ArrayList<String>();
		
		resetContent();
		setDefaultStage();
	}
	
	
	public void setDefaultStage(){ //TODO komischer name!
		mCurrentSpriteList = new ArrayList<HashMap<String,String>>();
		mCurrentSprite = 0;

		mAllContentArrayList.add(new Pair<String, ArrayList<HashMap<String, String>>>(mCtx.getString(R.string.stage), mCurrentSpriteList));
	}

	private void setmAllContentArrayList(
			ArrayList<Pair<String, ArrayList<HashMap<String, String>>>> list) {
		mAllContentArrayList = list; 
	}
	
	public String getCurrentSpriteName(){
		return mAllContentArrayList.get(mCurrentSprite).first;
	}
	
	public Integer getCurrentSpritePosition(){
		return mCurrentSprite;
	}
	
	public void setObserver(Observer observer)
	{
		addObserver(observer);
	}
	
	public void loadAllContentNameList(){
		mAllContentNameList.clear();
		for (int i=0; i<mAllContentArrayList.size(); i++)
    		mAllContentNameList.add(mAllContentArrayList.get(i).first);
	}
	
    public ArrayList<String> getAllContentNameList(){
    	return mAllContentNameList;
    }
    
    public ArrayList<Pair<String, ArrayList<HashMap<String, String>>>> getAllContentList(){
    	return mAllContentArrayList;
    }
    
	/**
	 * test method
	 */
    public int getIdCounter()
    {
    	return mIdCounter;
    }
    
    public boolean moveBrickUpInList(int position){
    	if(position > 0 && position < mCurrentSpriteList.size()){
    		HashMap<String, String> map = mCurrentSpriteList.get(position);
    		mCurrentSpriteList.remove(position);
    		mCurrentSpriteList.add(position-1, map);
    		setChanged();
    		notifyObservers(position-1);
    		return true;
    	}
    	return false;
    }
    
    public boolean moveBrickDownInList(int position){
    	if(position < mCurrentSpriteList.size()-1 && position >= 0){
    		HashMap<String, String> map = mCurrentSpriteList.get(position);
    		mCurrentSpriteList.remove(position);
    		mCurrentSpriteList.add(position+1, map);
    		setChanged();
    		notifyObservers(position+1);
    		return true;
    	}
    	return false;
    }
    
    
	///////////////////////////////
	/**
	 * load content into data structure
	 */
	public void loadContent(){
		loadContent(mTempFile);
	}
	/**
	 * load content into data structure
	 */
	public void loadContent(String fileName){
		
		resetContent();
		
		FileInputStream scratch = mFilesystem.createOrOpenFileInput
			(Utils.concatPaths(ConstructionSiteActivity.ROOT, fileName), mCtx);
			
		try {
			if(scratch != null && scratch.available() > 0){
				setmAllContentArrayList(mParser.parse(scratch, mCtx));
				mCurrentSpriteList = mAllContentArrayList.get(0).second;
				loadContentGalleryList();
			    mIdCounter = getHighestId();
			    mCurrentSprite = 0;
	
			    scratch.close();
			}

		} catch (IOException e) {
		}
		if(mAllContentArrayList.size() == 0)
		{
			setDefaultStage();
		}
		
		loadAllContentNameList();
		
	    setChanged();
	    notifyObservers();
	}
	


	private int getHighestId() {
		ArrayList<Pair<String, ArrayList<HashMap<String, String>>>> spriteList;
        spriteList = (ArrayList<Pair<String, ArrayList<HashMap<String, String>>>>) mAllContentArrayList.clone();
        int highestId = 0;
		for(int i=0; i<mAllContentArrayList.size(); i++){
			ArrayList<HashMap<String, String>> sprite = spriteList.get(i).second;
			for(int j=0; j<sprite.size(); j++){
				HashMap<String, String> brickList = sprite.get(j);
				String stringId =  brickList.get(BrickDefine.BRICK_ID);
				if(brickList.size()>0 && !(brickList.get(BrickDefine.BRICK_ID).equals(""))){
					int tempId = Integer.valueOf(brickList.get(BrickDefine.BRICK_ID).toString()).intValue();
					boolean test = (highestId<tempId);
					if(test){
						highestId = tempId;
					}		
				}
			}
		}
		return (highestId+1); // ID immer aktuellste freie
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
		//((Activity)mCtx).setTitle(title.replace(ConstructionSiteActivity.DEFAULT_FILE_ENDING, "").replace("/", ""));
		//TODO: setTitle-> ClassCastException Testing
		ArrayList< Pair<String, ArrayList<HashMap<String, String>>>> spriteBrickList = new ArrayList< Pair<String, ArrayList<HashMap<String, String>>>>();
		for(int i=0; i<mAllContentArrayList.size(); i++){
			spriteBrickList.add(mAllContentArrayList.get(i));
		}
		
		FileOutputStream fd = mFilesystem.createOrOpenFileOutput(Utils.concatPaths(ConstructionSiteActivity.ROOT, file), mCtx);
		DataOutputStream ps = new DataOutputStream(fd);
		
		String xml = mParser.toXml(spriteBrickList, mCtx);
		try {
			ps.write(xml.getBytes());
			ps.close();
			fd.close();
		} catch (IOException e) {
			Log.e("Contentmanager", "ERROR saving file " + e.getMessage());
			e.printStackTrace();
		}	
		
		Log.d("Contentmanager", "Save file!");
   	}
	
    

}