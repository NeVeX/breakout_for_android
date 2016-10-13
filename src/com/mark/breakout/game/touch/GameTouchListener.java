package com.mark.breakout.game.touch;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.ITouchEventCallback;

import com.mark.breakout.activity.BreakOutBaseActivity;
import com.mark.breakout.debug.LogConstants;
import com.mark.breakout.scene.GameScene;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class GameTouchListener implements IOnSceneTouchListener {
	private GameScene gameScene;
	private float previousX = 0.0f;
	
	public GameTouchListener(GameScene gameScene) {
	    this.gameScene = gameScene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		synchronized (this) {
		    switch (pSceneTouchEvent.getAction()) {
		        case TouchEvent.ACTION_MOVE:
		        	this.calculateMoveXPositionForMotion(pSceneTouchEvent.getX());
		        	break;
		        case TouchEvent.ACTION_UP:
		        	previousX = 0;
		        	break;
		        	
		        	
		        default:
		            break;
		    }
		}
		return false;
	}
	
	private void calculateMoveXPositionForMotion(final float newX)
	{
		Log.d(LogConstants.PADDLE_TOUCH, "Raw Touch Move X : "+newX);
		
    	float moveX = newX - previousX;
    	if ( previousX != 0)
		{
			this.gameScene.setHumanTouchMoveX(moveX);
		}
    	previousX = newX;
    	Log.d(LogConstants.PADDLE_TOUCH, "Moving Paddle by X amount :"+moveX);
    	
	}
	
}
