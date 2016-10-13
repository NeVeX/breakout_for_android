package com.mark.breakout.game.entity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mark.breakout.activity.BreakOutBaseActivity;

public class Sphere extends AnimatedSprite  {
//	private final PhysicsHandler physicsHandler;
	private BreakOutBaseActivity breakOutBaseActivity;
	final public Body body;
	private Camera gameCamera;
	private static final float VELOCITY = 100.0f;
	
	public Sphere(BreakOutBaseActivity baseActivity, 
			float pX, float pY, float pWidth, float pHeight,
			PhysicsWorld mPhysicsWorld) {
		super(pX, pY, pWidth, pHeight, baseActivity.sphereTexture,
				baseActivity.getVertexBufferObjectManager());
//		this.physicsHandler = new PhysicsHandler(this);
//		this.registerUpdateHandler(this.physicsHandler);
//		this.physicsHandler.setVelocity(VELOCITY);
//		breakOutBaseActivity = baseActivity;
//		gameCamera = breakOutBaseActivity.mCamera;
		body = PhysicsFactory.createCircleBody(
				mPhysicsWorld, 
				this, 
				BodyType.DynamicBody, 
				PhysicsFactory.createFixtureDef(0f, 1f, 0f));
		body.setUserData(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
//		this.body.setLinearVelocity(1, -1);
		this.body.setLinearVelocity(10, 10);
//		this.body.setAngularVelocity(0);
		
	}

	public void performPaddleCollisionLogic(Paddle paddle)
	{
		float paddleCenterXInPixels = (paddle.body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		float sphereCenterXInPixels = (this.body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		Log.d("SPHERE", "Sphere X [Pixels] : "+sphereCenterXInPixels + ". Paddle X [Pixels] :"+paddleCenterXInPixels);
		
		
		// determine if the sphere center is before the paddle center x
		boolean moveSphereLeft = false;
		if ( sphereCenterXInPixels <= paddleCenterXInPixels ) { 
			moveSphereLeft = true; 
		}
		
		float currentXVelocity = Math.abs(this.body.getLinearVelocity().x);
		if ( moveSphereLeft ) { 
			currentXVelocity *= -1; 
			Log.d("SPHERE", "Moving X co-ord in left direction");
		}
		else
		{
			Log.d("SPHERE", "Moving X co-ord in right direction");
		}
		this.body.setLinearVelocity(
				currentXVelocity,
				this.body.getLinearVelocity().y);
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		
		super.onManagedUpdate(pSecondsElapsed);
//		if(this.mX < 0) {
//			this.physicsHandler.setVelocityX(VELOCITY);
//		} else if(this.mX + this.getWidth() > gameCamera.getWidth()) {
//			this.physicsHandler.setVelocityX(-VELOCITY);
//		}
//
//		if(this.mY < 0) {
//			this.physicsHandler.setVelocityY(VELOCITY);
//		} else if(this.mY + this.getHeight() > gameCamera.getHeight()) {
//			this.physicsHandler.setVelocityY(-VELOCITY);
//		}
//
//		super.onManagedUpdate(pSecondsElapsed);
	}
}
