package com.mark.breakout.game.entity.tiles;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.graphics.Rect;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mark.breakout.activity.BreakOutBaseActivity;
import com.mark.breakout.game.Information;
import com.mark.breakout.scene.GameScene;

public abstract class BaseTile extends AnimatedSprite  {
	public abstract FallingTile createFallingTile(float childTileXpos, float childTileYpos, 
			float childTileWidthToUse, float childTileHeightToUse, 
			VertexBufferObjectManager vertexBufferObjectManager);
	public abstract int getTileHitPoints();
	
	protected int hitsLeft;
	protected Body baseBody;
	private float childTileXpos = 0;
	private int childTileNumber = 0;
	protected PhysicsWorld mPhysicsWorld;
	protected BreakOutBaseActivity breakOutBaseActivity;
	
	public BaseTile(float pX, float pY, float pWidth, float pHeight,
			BreakOutBaseActivity baseActivity,
			ITiledTextureRegion pTiledTextureRegion, 
			int collisionLives, PhysicsWorld mPhysicsWorld) 
	{
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, baseActivity.getVertexBufferObjectManager());
		hitsLeft = collisionLives;
		this.breakOutBaseActivity = baseActivity;
		this.baseBody = PhysicsFactory.createBoxBody(
				mPhysicsWorld, 
				this, 
				BodyType.KinematicBody, 
				PhysicsFactory.createFixtureDef(0f, 1f, 0f));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, this.baseBody, true, true));
		this.baseBody.setUserData(this);
		this.mPhysicsWorld = mPhysicsWorld;
		
	}

	public void performTileRemovalFromCollision() {
		if ( this.getParent() instanceof GameScene)
		{
			GameScene gs = (GameScene) this.getParent();
			gs.performSphereAndTileCollisionUpdate(getTileHitPoints());
			this.collapseTileAnimation(this.getX(), this.getY(), gs, BreakOutBaseActivity.getSharedInstance());
			this.clearEntityModifiers();
	        this.clearUpdateHandlers();
	        this.setVisible(false);
	        baseBody.setActive(false);
	        this.detachSelf();
		}
	}
	
	public void performTileCollision() {
		hitsLeft--;
		if ( hitsLeft <= 0)
		{
			this.performTileRemovalFromCollision();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void collapseTileAnimation(final float x, final float y, 
			final GameScene scene, final BreakOutBaseActivity breakOutBaseActivity)
	{
		final int numberOfChildTiles = 5;
		final float childTileGap = 0.8f;	
		final float childTilefullTileWidth = getWidth() / numberOfChildTiles;
		final float childTileWidthWithGap = childTilefullTileWidth * childTileGap;
		childTileXpos = x;
		for ( int i = 0; i < numberOfChildTiles; i++)
		{
			childTileNumber++;
	    	float childTileWidthToUse = childTileWidthWithGap;
	    	if ( childTileNumber == numberOfChildTiles)
	    	{
	    		childTileWidthToUse = childTilefullTileWidth;
	    	}
	    	Log.d("BaseTile", "Creating new falling tile sprite");
	        final FallingTile fallingTile = createFallingTile(
	        		childTileXpos, y, childTileWidthToUse, getHeight(), 
	        		breakOutBaseActivity.getVertexBufferObjectManager());
	        childTileXpos += childTilefullTileWidth;
	        fallingTile.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {
					
				}
				
				@Override
				public void onUpdate(float pSecondsElapsed) {
					// 
					if ( fallingTile.collidesWith(scene.getPaddle()))
					{
						Log.d("BaseTile", "Falling Tile Collided with Paddle");
						// remove the modifer handler
						fallingTile.clearEntityModifiers();
						fallingTile.clearUpdateHandlers();
						fallingTile.setVisible(false);
						// we have a collision
						scene.givePlayerPoints(fallingTile.getTileHitPoints());
						scene.getListOfDirtyEntities().add(fallingTile);
					}
				}
			});
	        fallingTile.registerEntityModifier(
        		new MoveYModifier(fallingTile.getFallingSpeed(), y, breakOutBaseActivity.mCamera.getHeight() + fallingTile.getHeight())
        		{
        			@Override
        			protected void onModifierFinished(IEntity pItem) {
        				Log.d("BaseTile", "Removing fallen tile from Y Modifier");
        				// remove the tile
        				scene.getListOfDirtyEntities().add(pItem);
        			}
        		}
	        );
	        Log.d("BaseTile", "Attaching new falling tile sprite to scene");
	        scene.attachChild(fallingTile);

		}
		
//		IEntityFactory recFact = new IEntityFactory() {
//		    @Override
//		    public BaseTile create(float pX, float pY) {
//		    	childTileNumber++;
//		    	float childTileWidthToUse = childTileWidthWithGap;
//		    	if ( childTileNumber == numberOfChildTiles)
//		    	{
//		    		childTileWidthToUse = childTilefullTileWidth;
//		    	}
//		        BaseTile childTile = createChildTile(childTileXpos, y, childTileWidthToUse, getHeight(), 
//		        		breakOutBaseActivity.getVertexBufferObjectManager());
//		        childTileXpos += childTilefullTileWidth;
////		        rect.setColor(Color.RED);
//		        return childTile;
//		    }
//		};
//		
//		final ParticleSystem particleSystem = new ParticleSystem(
//				recFact, particleEmitter, 500, 500, numberOfChildTiles);
//		
//		particleSystem.addParticleInitializer(new VelocityParticleInitializer<IEntity>(0, 100));
//		
//		scene.attachChild(particleSystem);
//		scene.registerUpdateHandler(new TimerHandler(10, new ITimerCallback() {
//		    @Override
//		    public void onTimePassed(final TimerHandler pTimerHandler) {
//		        particleSystem.detachSelf();
//		        scene.sortChildren();
//		        scene.unregisterUpdateHandler(pTimerHandler);
//		    }
//		}));
		
	}
	
	
//	private void createTileExplosion(final float posX, 
//			final float posY, 
//			final IEntity target,
//			final BreakOutBaseActivity breakOutBaseActivity) 
//	{
//		int mNumPart = 15;
//		int mTimePart = 2;
//
//		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,posY);
//		
//		IEntityFactory recFact = new IEntityFactory() {
//		    @Override
//		    public Rectangle create(float pX, float pY) {
//		        Rectangle rect = new Rectangle(posX, posY, 10, 10, 
//		        		breakOutBaseActivity.getVertexBufferObjectManager());
//		        rect.setColor(Color.GREEN);
//		        return rect;
//		    }
//		};
//		final ParticleSystem particleSystem = new ParticleSystem(
//				recFact, particleEmitter, 500, 500, mNumPart);
//		
//		particleSystem.addParticleInitializer(new VelocityParticleInitializer(-50, 50, -50, 50));
//
//		particleSystem.addParticleModifier(new AlphaParticleModifier(0,0.6f * mTimePart, 1, 0));
//		particleSystem.addParticleModifier(new RotationParticleModifier(0, mTimePart, 0, 360));
//
//		target.attachChild(particleSystem);
//		target.registerUpdateHandler(new TimerHandler(mTimePart, new ITimerCallback() {
//		    @Override
//		    public void onTimePassed(final TimerHandler pTimerHandler) {
//		        particleSystem.detachSelf();
//		        target.sortChildren();
//		        target.unregisterUpdateHandler(pTimerHandler);
//		    }
//		}));
//	}

}
