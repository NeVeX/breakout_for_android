package com.mark.breakout.game.entity;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mark.breakout.activity.BreakOutBaseActivity;
import com.mark.breakout.debug.LogConstants;

public class Paddle extends AnimatedSprite {

//	private Rectangle sprite;
//	private static Paddle instance;
	final public Body body;
	private Camera mCamera;
	private PhysicsWorld mPhysicsWorld;
//	public static Paddle getSharedInstance()
//	{
//		if ( instance == null )
//		{
//			instance = new Paddle();
//		}
//		return instance;
//	}
	
	public Paddle(BreakOutBaseActivity baseActivity, 
			float pX, float pY, float pWidth, float pHeight,
			PhysicsWorld mPhysicsWorld) {
		super(pX, pY, pWidth, pHeight, baseActivity.paddleTexture,
				baseActivity.getVertexBufferObjectManager());
		this.mPhysicsWorld = mPhysicsWorld;
        mCamera = BreakOutBaseActivity.getSharedInstance().mCamera;
//        sprite.setPosition(
//        		mCamera.getWidth() / 2 - sprite.getWidth() / 2,
//        		mCamera.getHeight() - sprite.getHeight() - 10);
        
        body = PhysicsFactory.createBoxBody(
				mPhysicsWorld, 
				this, 
				BodyType.KinematicBody, 
				PhysicsFactory.createFixtureDef(0f, 1f, 0f));
        body.setUserData(this);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
        
	}
	
//	public Rectangle getSprite() {
//		return sprite;
//	}
	

	public void movePaddle(float newPaddleXInPixels)
	{
		// check if we are actually going to move the paddle
		if ( newPaddleXInPixels == 0) { return; }
//		float newPaddleXInPixels = newPaddleXInPixels * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		int leftBoundary = 0 + (int) this.getWidth() / 2;
        int rightBoundary = (int) (mCamera.getWidth() - (int) this.getWidth() / 2 );
        float newXInPixels;

        float currentXPositionInPixels = this.body.getPosition().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        Log.d(LogConstants.PADDLE, "Current X Position in Pixels : " + currentXPositionInPixels + ". New X length in Pixels : " + newPaddleXInPixels);
        Log.d(LogConstants.PADDLE, "Camera Width : "+mCamera.getWidth());
        Log.d(LogConstants.PADDLE, "Sprite Width : "+this.getWidth());
        newXInPixels = currentXPositionInPixels + newPaddleXInPixels;

        // Double Check That New X,Y Coordinates are within Limits
        if (newXInPixels < leftBoundary)
        {
        	newXInPixels = leftBoundary;
        }
        else if (newXInPixels > rightBoundary)
        {
        	newXInPixels = rightBoundary;
        }

        Log.d(LogConstants.PADDLE, "Paddle current X position : " +this.body.getPosition().x +" + with Y : " +this.body.getPosition().y);
        Log.d(LogConstants.PADDLE, "Moving Paddle to new X position : " +newXInPixels / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT +" + with Y : " +this.body.getPosition().y);
        this.body.setTransform(
        		newXInPixels / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
        		this.body.getPosition().y, 
        		0);
	}
	
	
}
