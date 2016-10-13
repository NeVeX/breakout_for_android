package com.mark.breakout.game.entity.tiles;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mark.breakout.activity.BreakOutBaseActivity;

public class NormalTile extends BaseTile {
	
	public NormalTile(BreakOutBaseActivity baseActivity, 
			float pX, float pY, float pWidth, float pHeight,
			PhysicsWorld mPhysicsWorld) {
		super(pX, pY, pWidth, pHeight, 
				baseActivity,
				baseActivity.tileTexture, 
				1,
				mPhysicsWorld);
	}

	
	/**
	 * Create a falling tile for the removal effect
	 */
	@Override
	public FallingTile createFallingTile(float childTileXpos, float childTileYpos,
			float childTileWidthToUse, float childTileHeightToUse,
			VertexBufferObjectManager vertexBufferObjectManager) {
		
		return new FallingTile(childTileXpos, childTileYpos, 
				childTileWidthToUse, childTileHeightToUse, 
				TileType.NORMAL_FALLING_TILE_HIT_POINTS,
				TileType.NORMAL_FALLING_TILE_SPEED_IN_SECS,
				getTiledTextureRegion(),
				vertexBufferObjectManager, mPhysicsWorld);
	}


	@Override
	public int getTileHitPoints() {
		return TileType.NORMAL_TILE_HIT_POINTS;
	}


}
