package com.mark.breakout.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mark.breakout.R;
import com.mark.breakout.activity.BreakOutBaseActivity;
import com.mark.breakout.game.BreakOutGameLoop;
import com.mark.breakout.game.Information;
import com.mark.breakout.game.entity.CollisionListener;
import com.mark.breakout.game.entity.EntitySettings;
import com.mark.breakout.game.entity.Paddle;
import com.mark.breakout.game.entity.Sphere;
import com.mark.breakout.game.entity.tiles.BaseTile;
import com.mark.breakout.game.entity.tiles.NormalTile;
import com.mark.breakout.game.level.LevelManager;
import com.mark.breakout.game.touch.GameTouchListener;

public class GameScene extends Scene {
	private boolean inErrorState = false;
	public BreakOutBaseActivity breakOutBaseActivity;
	private int playerScore = 0, previousPlayerScore = 0;
	private int levelBlocksRemaining;
	private Text scoreText;
	private Paddle paddle;
	public Paddle getPaddle() {
		return paddle;
	}

	private Rectangle pauseRect;
	private Sphere sphere;
	private Camera mCamera;
	public PhysicsWorld mPhysicsWorld;
	private float humanTouchMoveX;
	private List<BaseTile> tileList;
	private List<IEntity> listOfDirtyEntities = new ArrayList<IEntity>();
	public List<IEntity> getListOfDirtyEntities() {
		return listOfDirtyEntities;
	}

	public void setListOfDirtyEntities(List<IEntity> listOfDirtyEntities) {
		this.listOfDirtyEntities = listOfDirtyEntities;
	}

	private int currentLevel = 0;
	private LevelManager levelManager;
	
	private IOnAreaTouchListener touchAreaListener = new IOnAreaTouchListener() {

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				ITouchArea pTouchArea, float pTouchAreaLocalX,
				float pTouchAreaLocalY) {
			if ( pTouchArea == pauseRect)
			{	
				Log.d("GameScene", "Pausing the game because pause button touched.");
				// pause the game
				pauseGame();
				return true;
			}
			return false;
		}
	};
	
	public void resumeGame() 
	{
		// resume play
		this.clearChildScene();
	}
	
	public void pauseGame()
	{
		// pause game
		// carefully set the modal options
		this.setChildScene(PauseScene.getSharedInstance(this), 
				false, true, true);
	}
	
	public GameScene(int levelToLoad) {
		this.currentLevel = levelToLoad;
		breakOutBaseActivity = BreakOutBaseActivity.getSharedInstance();
	    setBackground(new Background(Color.CYAN));
	    mCamera = BreakOutBaseActivity.getSharedInstance().mCamera;
	    registerUpdateHandler(new BreakOutGameLoop(this));
	    
	    this.setOnSceneTouchListener(new GameTouchListener(this));
	    
	    this.setOnAreaTouchListener(touchAreaListener);
	    
	    levelManager = new LevelManager(breakOutBaseActivity.getAssets());
	    this.createPhysics();
	    createAndAttachEntities();
	    breakOutBaseActivity.setCurrentScene(this);
	}

	private void createPhysics() {
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		final VertexBufferObjectManager vertexBufferObjectManager = breakOutBaseActivity.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, mCamera.getHeight() - 2, mCamera.getWidth(), 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, mCamera.getWidth(), 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, mCamera.getHeight(), vertexBufferObjectManager);
		final Rectangle right = new Rectangle(mCamera.getWidth() - 2, 0, 2, mCamera.getHeight(), vertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 1f, 0f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.attachChild(ground);
		this.attachChild(roof);
		this.attachChild(left);
		this.attachChild(right);

		this.registerUpdateHandler(this.mPhysicsWorld);
		this.mPhysicsWorld.setContactListener(new CollisionListener());
	}

	private void createAndAttachEntities() {
		// the paddle
		paddle = new Paddle(breakOutBaseActivity, 
				mCamera.getWidth() / 2 - EntitySettings.PADDLE_WIDTH / 2,
        		mCamera.getHeight() - EntitySettings.PADDLE_HEIGHT - 10, 
        		EntitySettings.PADDLE_WIDTH, EntitySettings.PADDLE_HEIGHT,
	    		this.mPhysicsWorld);
	    attachChild(paddle);
		// the sphere - add to top of the paddle
	    sphere = new Sphere(breakOutBaseActivity, 
//	    		paddle.getX() + paddle.getWidth() / 2, 
//	    		paddle.getY(), 
	    		0, 
	    		mCamera.getHeight() / 2,
	    		EntitySettings.SPHERE_WIDTH, EntitySettings.SPHERE_WIDTH,
	    		this.mPhysicsWorld
	    		);
	    attachChild(sphere);
	    createTiles(++this.currentLevel);
	    createPauseButton();
	    createScoreEntity();
	}
	
	private void createScoreEntity() {
		scoreText = new Text(
				0, 
				0, 
				breakOutBaseActivity.scoreFont, 
				Integer.toString(0),
				200,
				breakOutBaseActivity.getVertexBufferObjectManager());
		this.attachChild(scoreText);
	}

	public void updatePlayerScore() 
	{
		if ( this.playerScore != this.previousPlayerScore)
		{
			scoreText.setText(Integer.toString(this.playerScore));
			scoreText.setPosition(mCamera.getWidth() - scoreText.getWidth(), 5);
			Log.d("GameScene", "Set Score Location: "+scoreText.getX() +", "+scoreText.getY());
			scoreText.setColor(Color.PINK);
			this.previousPlayerScore = this.playerScore;
		}
	}

	private void createPauseButton() 
	{
		pauseRect = new Rectangle(5, 5, 15, 15, breakOutBaseActivity.getVertexBufferObjectManager());
		pauseRect.setColor(Color.PINK);
		this.registerTouchArea(pauseRect);
		attachChild(pauseRect);
		
	}

	private void handleFatalError()
	{
		breakOutBaseActivity.showGenericErrorDialogAndQuit();
	}
	
	private void createTiles(int nextLevel)
	{
		tileList = levelManager.loadLevel(nextLevel, this);
		if ( tileList == null || tileList.size() < 1)
		{
			this.handleFatalError();
			return;
		}
		levelBlocksRemaining = levelManager.getLevelTiles().size();
	}

	public void setHumanTouchMoveX(float moveX) {
		if ( humanTouchMoveX == 0 )
		{
			humanTouchMoveX = moveX;
		}
	}
	
	public void movePaddle()
	{
		paddle.movePaddle(humanTouchMoveX);
		humanTouchMoveX = 0;
	}

	public void removeDirtyEntites() {
		synchronized (this.listOfDirtyEntities) {
			for( IEntity e : this.listOfDirtyEntities)
			{
				this.detachChild(e);
			}
			this.listOfDirtyEntities.clear();
		}
	}

	public void givePlayerPoints(int givenScore) {
		Log.d("BaseTile", "Updating Player Score. Current Score : "+this.scoreText.getText());
		this.playerScore += givenScore;
		Log.d("BaseTile", "New Player Score : "+this.scoreText.getText());
	}

	public void performSphereAndTileCollisionUpdate(int pointsToGivePlayer) {
		this.givePlayerPoints(pointsToGivePlayer);
		this.levelBlocksRemaining--;
	}

	public void checkIfLevelShouldEnd() {
		if ( this.levelBlocksRemaining <= 0)
		{
			// this level is complete
			breakOutBaseActivity.setCurrentScene(new GameScene(this.currentLevel++));
		}
		
	}

	
	

	
}
