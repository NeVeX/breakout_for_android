package com.mark.breakout.scene;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.util.color.Color;

import com.mark.breakout.R;
import com.mark.breakout.activity.BreakOutBaseActivity;

public class PauseScene extends MenuScene implements IOnMenuItemClickListener {
	private BreakOutBaseActivity breakOutActivity;
	final private int MENU_RESUME = 0;
	private GameScene resumeScene;
	
	private static PauseScene pauseScene;
	
	public static PauseScene getSharedInstance(GameScene resumeScene)
	{
		if ( pauseScene == null )
		{ 
			pauseScene = new PauseScene();
		}
		pauseScene.resumeScene = resumeScene;
		return pauseScene;
	}
	
	
	private PauseScene() {
		super(BreakOutBaseActivity.getSharedInstance().mCamera);
		setBackground(new Background(Color.WHITE));
		breakOutActivity = BreakOutBaseActivity.getSharedInstance();
		IMenuItem resumeButton = new TextMenuItem(
				MENU_RESUME, 
				breakOutActivity.titleFont, 
				breakOutActivity.getString(R.string.pause_menu_resume_button_text), 
				breakOutActivity.getVertexBufferObjectManager());
		resumeButton.setPosition(
				mCamera.getWidth() / 2 - resumeButton.getWidth() / 2, 
				mCamera.getHeight() / 2 - resumeButton.getHeight() / 2);
		addMenuItem(resumeButton);
		
		setOnMenuItemClickListener(this);
		this.registerTouchArea(resumeButton);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
	        case MENU_RESUME:
	        	this.resumeScene.resumeGame();
	            return true;
	        default:
	            break;
	    }
	    return false;
	}

}
