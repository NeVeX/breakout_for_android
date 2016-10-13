package com.mark.breakout.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.util.color.Color;

import com.mark.breakout.R;
import com.mark.breakout.activity.BreakOutBaseActivity;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {

	private BreakOutBaseActivity breakOutActivity;
	final private int MENU_START = 0;

	public MainMenuScene()
	{
		super(BreakOutBaseActivity.getSharedInstance().mCamera);
		breakOutActivity = BreakOutBaseActivity.getSharedInstance();

		setBackground(new Background(Color.BLUE));
		IMenuItem startButton = new TextMenuItem(
				MENU_START, 
				breakOutActivity.titleFont, 
				breakOutActivity.getString(R.string.main_menu_start_button_text), 
				breakOutActivity.getVertexBufferObjectManager());
		startButton.setPosition(
				mCamera.getWidth() / 2 - startButton.getWidth() / 2, 
				mCamera.getHeight() / 2 - startButton.getHeight() / 2);
		addMenuItem(startButton);

		setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
	        case MENU_START:
	        	breakOutActivity.setCurrentScene(new GameScene(0));
	            return true;
	        default:
	            break;
	    }
	    return false;
	}
	
	
}
