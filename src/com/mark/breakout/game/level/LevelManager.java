package com.mark.breakout.game.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.util.Log;

import com.mark.breakout.game.entity.tiles.BaseTile;
import com.mark.breakout.game.entity.tiles.NormalTile;
import com.mark.breakout.game.entity.tiles.TileType;
import com.mark.breakout.scene.GameScene;

public class LevelManager {
	private List<BaseTile> levelTiles = new ArrayList<BaseTile>();
	public List<BaseTile> getLevelTiles() {
		return levelTiles;
	}

	AssetManager assetManager;
	
	public LevelManager(AssetManager assetManager)
	{
		this.assetManager = assetManager;
	}
	
	public List<BaseTile> loadLevel(int levelNumber, GameScene gs)
	{
		List<LevelInputData> levelData = this.readLevelDataFromFile(levelNumber);
		for (LevelInputData l : levelData )
		{
			BaseTile bss = null;
			if ( l.getTileType().equals(TileType.NORMAL_TILE))
			{
				bss = new NormalTile(
						gs.breakOutBaseActivity, 
						l.getStartX(), 
						l.getStartY(), 
						TileType.NORMAL_TILE_WIDTH, 
						TileType.NORMAL_TILE_HEIGHT, 
						gs.mPhysicsWorld);
			}
			
			if ( bss != null )
			{
				gs.attachChild(bss);
				levelTiles.add(bss);
			}
		}
		return levelTiles;
	}
	
	private List<LevelInputData> readLevelDataFromFile(int levelNumber)
	{
		String levelFile = "levels/level_"+levelNumber+".csv";
		List<LevelInputData> returnList = new ArrayList<LevelInputData>();
		InputStream is = null;
		try {
			is = this.assetManager.open(levelFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ( (line = br.readLine()) != null )
			{
				String[] listEntries = line.split(",");
				if ( listEntries.length != 4 ) 
				{ 
					Log.w("LevelManager", "Not reading line because split size not as expected.");
					continue;
				}
				LevelInputData ld = new LevelInputData();
				ld.setTileType(listEntries[0]);
				ld.setRowNumber(Integer.parseInt(listEntries[1]));
				ld.setStartX(Integer.parseInt(listEntries[2]));
				ld.setStartY(Integer.parseInt(listEntries[3]));
				returnList.add(ld);
			}
		} catch (IOException e) {
			Log.e("LevelManager", "Could not load file ["+levelFile+"]. \n Exception is "+e);
		}
		finally {
			if (is != null ) { try {
				is.close();
			} catch (IOException e) {
				Log.e("LevelManager", "Could not close InputStream. \n Exception is "+e);
			} }
		}
		return returnList;
	}
	
}
