package com.mark.breakout.scene;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

import com.mark.breakout.R;
import com.mark.breakout.activity.BreakOutBaseActivity;

public class SplashScene extends Scene {

	private BreakOutBaseActivity breakOutActivity;

	public SplashScene() {
		super();
		setBackground(new Background(Color.WHITE));
		breakOutActivity = BreakOutBaseActivity.getSharedInstance();
		Text titleOne = new Text(
				0, 0, 
				breakOutActivity.titleFont, 
				breakOutActivity.getString(R.string.splash_title_name_one), 
				breakOutActivity.getVertexBufferObjectManager());
		Text titleTwo = new Text(
				0, 0, 
				breakOutActivity.titleFont, 
				breakOutActivity.getString(R.string.splash_title_name_two), 
				breakOutActivity.getVertexBufferObjectManager());
		
		// define the text movement
		titleOne.setPosition(
				-titleOne.getWidth(), 
				breakOutActivity.mCamera.getHeight() / 2);
		titleTwo.setPosition(
				breakOutActivity.mCamera.getWidth(), 
				breakOutActivity.mCamera.getHeight() / 2);

		attachChild(titleOne);
		attachChild(titleTwo);

		titleOne.registerEntityModifier(
				new MoveXModifier(
						1, 
						titleOne.getX(), 
						breakOutActivity.mCamera.getWidth() / 2 - titleOne.getWidth()));
		titleTwo.registerEntityModifier(
				new MoveXModifier(
						1, 
						titleTwo.getX(), 
						breakOutActivity.mCamera.getWidth() / 2));
		
		loadResources();
		
	}
	
	private void loadResources()
	{
		DelayModifier dMod = new DelayModifier(2){
		    @Override
		    protected void onModifierFinished(IEntity pItem) {
		    	breakOutActivity.setCurrentScene(new MainMenuScene());
		    }
		};
		registerEntityModifier(dMod);
	}
	

	
}
