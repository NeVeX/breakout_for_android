package com.mark.breakout.activity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mark.breakout.game.Information;
import com.mark.breakout.scene.PauseScene;
import com.mark.breakout.scene.SplashScene;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;

public class BreakOutBaseActivity extends SimpleBaseGameActivity {

	static final int CAMERA_WIDTH = 480;
	static final int CAMERA_HEIGHT = 800;

	public Font titleFont;
	public Font scoreFont;
	public Camera mCamera;
	
//	public static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
	
	//A reference to the current scene
	public Scene mCurrentScene;
	public static BreakOutBaseActivity instance;
	
	// textures
	private BitmapTextureAtlas mBitmapTextureAtlas;
	public TiledTextureRegion sphereTexture;
	public TiledTextureRegion paddleTexture;
	public TiledTextureRegion tileTexture;
	
	public static BreakOutBaseActivity getSharedInstance() {
	    return instance;
	}

	// to change the current main scene
	public void setCurrentScene(Scene scene) {
	    mCurrentScene = scene;
	    getEngine().setScene(mCurrentScene);
	}
	
//	public void pauseGame()
//	{
//		this.setCurrentScene(new PauseScene(this.mCurrentScene));
//		this.mEngine.stop();
//	}
	
//	public void unPauseGame(Scene sceneToResume)
//	{
//		if ( sceneToResume != null )
//		{
//			this.setCurrentScene(sceneToResume);
//			this.mEngine.start();
//		}
//	}
	
	public void showGenericErrorDialogAndQuit()
	{
//		this.pauseGame();
		this.mEngine.stop();
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(BreakOutBaseActivity.this);
				builder.setMessage("An unexpected error occured in the app. Blast! \n Application will shutdown now.");
				builder.setNeutralButton("Blah!", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// shut down
						BreakOutBaseActivity.this.finish();
					}
				}
				);
				builder.show();
			}
		});
	}
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		instance = this;
	    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
//	    this.debugInformation();
	    return new EngineOptions(true, 
	    		ScreenOrientation.PORTRAIT_SENSOR, 
	    		new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), 
	    		mCamera);
	}
	
	private void debugInformation() {
//		final Display display = getWindowManager().getDefaultDisplay();
//	    Information.PHONE_SCREEN_WIDTH = display.getWidth();
//	    Information.PHONE_SCREEN_HEIGHT = display.getHeight();
//	 
//	    String deb = String.format("Phone Screen Resolution: %d / %d",Information.PHONE_SCREEN_WIDTH,Information.PHONE_SCREEN_HEIGHT);
//	    Log.d("Information", deb);
	}

	@Override
	protected void onCreateResources() {
		titleFont = FontFactory.create(
				this.getFontManager(),
				this.getTextureManager(), 
				256, 256, 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 
				32);
		scoreFont = FontFactory.create(
				this.getFontManager(),
				this.getTextureManager(), 
				(int) mCamera.getWidth(), (int) mCamera.getHeight(), 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 
				32);
		scoreFont.load();
		titleFont.load();
	    loadTextures();
		
	}
	
	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new SplashScene();
		return mCurrentScene;	    
	}
	

	private void loadTextures()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 
				128, 128, TextureOptions.BILINEAR);
		this.sphereTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.mBitmapTextureAtlas, 
				this, 
				"ball.png", 
				0, 0, 1, 1);
		this.paddleTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.mBitmapTextureAtlas, 
				this, 
				"paddle.png", 
				32, 0, 1, 1);
		this.tileTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.mBitmapTextureAtlas, 
				this, 
				"tile.png", 
				64, 0, 1, 1);
		this.mBitmapTextureAtlas.load();
	}
	
	
}
