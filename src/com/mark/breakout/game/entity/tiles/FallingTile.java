package com.mark.breakout.game.entity.tiles;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FallingTile extends AnimatedSprite {
	protected Body baseBody;
	private int tileHitPoints;
	private int fallingSpeedInSeconds;

	public FallingTile(
			float childTileXpos, float childTileYpos,
			float childTileWidthToUse, float childTileHeightToUse,
			int hitPoints, int fallingSpeedInSeconds,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager,
			PhysicsWorld mPhysicsWorld) {
		
		super(childTileXpos, childTileYpos,
				childTileWidthToUse, childTileHeightToUse,
				pTiledTextureRegion,
				vertexBufferObjectManager);
		tileHitPoints = hitPoints;
		this.fallingSpeedInSeconds = fallingSpeedInSeconds;
//		this.baseBody = PhysicsFactory.createBoxBody(
//				mPhysicsWorld, 
//				this, 
//				BodyType.KinematicBody, 
//				PhysicsFactory.createFixtureDef(0f, 1f, 0f));
//		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, this.baseBody, true, true));
		
	}
	
	public int getTileHitPoints() {
		return tileHitPoints;
	}

	public float getFallingSpeed() {
		return this.fallingSpeedInSeconds;
	}
	
}
