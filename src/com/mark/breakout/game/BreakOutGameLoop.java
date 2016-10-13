package com.mark.breakout.game;

import org.andengine.engine.handler.IUpdateHandler;

import com.mark.breakout.scene.GameScene;

public class BreakOutGameLoop implements IUpdateHandler {

	private GameScene breakoutGameScene;
	public BreakOutGameLoop(GameScene breakoutGameScene)
	{
		this.breakoutGameScene = breakoutGameScene;
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// move ship if necessary
		breakoutGameScene.movePaddle();
		breakoutGameScene.removeDirtyEntites();
		breakoutGameScene.updatePlayerScore();
		breakoutGameScene.checkIfLevelShouldEnd();
	}

	@Override
	public void reset() {
	}

}
