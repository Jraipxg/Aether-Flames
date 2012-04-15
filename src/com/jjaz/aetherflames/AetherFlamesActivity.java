package com.jjaz.aetherflames;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class AetherFlamesActivity extends SimpleBaseGameActivity 
{
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int CAMERA_WIDTH = 960;
	protected static final int CAMERA_HEIGHT = 540;
	
	protected static final float WORLD_WIDTH = 30.0f;
	protected static final float WORLD_HEIGHT = 16.875f;//WORLD_WIDTH*(CAMERA_HEIGHT/CAMERA_WIDTH);

	protected static final float WORLD_TO_CAMERA = 32;
	protected static final float CAMERA_TO_WORLD = 0.03125f;

	protected static final float SHIP_START_PADDING = 1.25f;
	protected static final float HEALTH_CRATE_START_PADDING = 3f;
	

	// ===========================================================
	// Fields
	// ===========================================================

	protected static Camera mCamera;
	protected static Scene mScene;
	protected static Engine mGameEngine;
	protected static PhysicsWorld mPhysicsWorld;
	protected static VertexBufferObjectManager mVertexBufferObjectManager;
	protected static Font mFont;

	protected static BitmapTextureAtlas mSpaceTexture;
	protected static ITextureRegion mSpaceTextureRegion;

	protected static BitmapTextureAtlas mShipTextures;
	protected static TiledTextureRegion mShipTextureRegion;

	protected static BitmapTextureAtlas mHealthCrateTexture;
	protected static ITextureRegion mHealthCrateTextureRegion;

	protected static BitmapTextureAtlas mPlasmaBlastTexture; 
	protected static ITextureRegion mPlasmaBlastTextureRegion; //TODO: change this to an animated sprite

	protected static BitmapTextureAtlas mControlStickTexture;
	protected static ITextureRegion mControlStickBaseTextureRegion;
	protected static ITextureRegion mControlStickKnobTextureRegion;
	
	protected static BitmapTextureAtlas mButtonsTexture;
	protected static ITextureRegion mButtonsBaseTextureRegion;
	protected static ITextureRegion mButtonsKnobTextureRegion;
	
	private CollisionHandler mCollisionHandler;
	private SceneUpdateHandler mSceneUpdateHandler;
	
	private DigitalOnScreenControl mControlStick;
	private DigitalOnScreenControl mButtons;
	
	public static Map<Integer, Ship> ships;
	
	public static int myShipColor;
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		AetherFlamesActivity.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), AetherFlamesActivity.mCamera);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		return engineOptions;
	}

	@Override
	public void onCreateResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		AetherFlamesActivity.mSpaceTexture = new BitmapTextureAtlas(this.getTextureManager(), 960*2, 540, TextureOptions.BILINEAR);
		AetherFlamesActivity.mSpaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mSpaceTexture, this, "spaceBackground.png", 0, 0);
		AetherFlamesActivity.mSpaceTexture.load();
		
		AetherFlamesActivity.mShipTextures = new BitmapTextureAtlas(this.getTextureManager(), 512, 64, TextureOptions.BILINEAR);
		AetherFlamesActivity.mShipTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(AetherFlamesActivity.mShipTextures, this, "ships.png", 0, 0, 8, 1);
		AetherFlamesActivity.mShipTextures.load();
		
		AetherFlamesActivity.mControlStickTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		AetherFlamesActivity.mControlStickBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mControlStickTexture, this, "D-Pad.png", 0, 0);
		AetherFlamesActivity.mControlStickKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mControlStickTexture, this, "Empty.png", 128, 0);
		AetherFlamesActivity.mControlStickTexture.load();
		
		AetherFlamesActivity.mButtonsTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		AetherFlamesActivity.mButtonsBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mButtonsTexture, this, "Buttons.png", 0, 0);
		AetherFlamesActivity.mButtonsKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mButtonsTexture, this, "Empty.png", 128, 0);
		AetherFlamesActivity.mButtonsTexture.load();

		AetherFlamesActivity.mHealthCrateTexture = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
		AetherFlamesActivity.mHealthCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mHealthCrateTexture, this, "healthCrate.png", 0, 0);
		AetherFlamesActivity.mHealthCrateTexture.load();
		
		AetherFlamesActivity.mPlasmaBlastTexture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		AetherFlamesActivity.mPlasmaBlastTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(AetherFlamesActivity.mPlasmaBlastTexture, this, "Bullet.png", 0, 0);
		AetherFlamesActivity.mPlasmaBlastTexture.load();

		AetherFlamesActivity.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, 30, true, Color.WHITE);
		AetherFlamesActivity.mFont.load();
	}

	@Override
	public Scene onCreateScene()
	{
		AetherFlamesActivity.mGameEngine = this.mEngine;
		AetherFlamesActivity.mGameEngine.registerUpdateHandler(new FPSLogger());

		AetherFlamesActivity.mScene = new Scene();
		AetherFlamesActivity.mScene.setBackground(new Background(0, 0, 0));
		
		AetherFlamesActivity.mVertexBufferObjectManager = this.getVertexBufferObjectManager();
		AetherFlamesActivity.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);

		this.mCollisionHandler = new CollisionHandler();
		this.mSceneUpdateHandler = new SceneUpdateHandler();
		AetherFlamesActivity.mPhysicsWorld.setContactListener(mCollisionHandler);
		
		ships = new ConcurrentHashMap<Integer,Ship>();
		
		this.initBattlefield();
		this.initShips();
		this.initHealthCrates();
		this.initOnScreenControls();
		
		AetherFlamesActivity.mScene.registerUpdateHandler(this.mSceneUpdateHandler);
		
		return AetherFlamesActivity.mScene;
	}

	@Override
	public void onGameCreated()
	{
		
	}
	
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		
	}
	
	protected Dialog onCreateDialog(final int pID) {
		return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Player " + pID + " Wins!")
				.setCancelable(false)
				.setPositiveButton("Ok", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						//final Intent intent = new Intent(AetherFlamesActivity.this, BluetoothListDevicesActivity.class);
						//AetherFlamesActivity.this.startActivityForResult(intent, REQUESTCODE_BLUETOOTH_CONNECT);
					}
				})
				/*.setNeutralButton("Server", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						//AetherFlamesActivity.this.toast("You can add and move sprites, which are only shown on the clients.");
						//AetherFlamesActivity.this.initServer();
						//AetherFlamesActivity.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.setNegativeButton("Both", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						//AetherFlamesActivity.this.toast("You can add sprites and move them, by dragging them.");
						//AetherFlamesActivity.this.initServerAndClient();
						//AetherFlamesActivity.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})*/
				.create();
	}
	
	private void initOnScreenControls()
	{		
		this.mControlStick = new DigitalOnScreenControl(0, CAMERA_HEIGHT - AetherFlamesActivity.mControlStickBaseTextureRegion.getHeight(), AetherFlamesActivity.mCamera, AetherFlamesActivity.mControlStickBaseTextureRegion, AetherFlamesActivity.mControlStickKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new ShipMovementControlListener(ships.get(AetherFlamesActivity.myShipColor)));
		this.mControlStick.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mControlStick.getControlBase().setAlpha(0.5f);
		this.mControlStick.getControlBase().setScaleCenter(0, 128);
		this.mControlStick.getControlBase().setScale(1.5f);
		this.mControlStick.getControlKnob().setScale(1.5f);
		this.mControlStick.refreshControlKnobPosition();

		AetherFlamesActivity.mScene.setChildScene(this.mControlStick);
		
		this.mButtons = new DigitalOnScreenControl(CAMERA_WIDTH - AetherFlamesActivity.mButtonsBaseTextureRegion.getWidth(), CAMERA_HEIGHT - AetherFlamesActivity.mButtonsBaseTextureRegion.getHeight(), AetherFlamesActivity.mCamera, AetherFlamesActivity.mButtonsBaseTextureRegion, AetherFlamesActivity.mButtonsKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new ButtonControlListener(ships.get(AetherFlamesActivity.myShipColor)));
		this.mButtons.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mButtons.getControlBase().setAlpha(0.5f);
		this.mButtons.getControlBase().setScaleCenter(128, 128);
		this.mButtons.getControlBase().setScale(1.5f);
		this.mButtons.getControlKnob().setScale(1.5f);
		this.mButtons.refreshControlKnobPosition();

		this.mControlStick.setChildScene(this.mButtons);
	}

	private void initShips()
	{		
		AetherFlamesActivity.myShipColor = Ship.BLUE_SHIP; //TODO: set this on game configuration using network

		ships.put(Ship.WHITE_SHIP, new Ship(SHIP_START_PADDING, SHIP_START_PADDING, -45.0f, Ship.WHITE_SHIP));
		ships.put(Ship.RED_SHIP, new Ship(WORLD_WIDTH/2, SHIP_START_PADDING, 0.0f, Ship.RED_SHIP));
		ships.put(Ship.ORANGE_SHIP, new Ship(WORLD_WIDTH - SHIP_START_PADDING, SHIP_START_PADDING, 45.0f, Ship.ORANGE_SHIP));
		
		ships.put(Ship.YELLOW_SHIP, new Ship(SHIP_START_PADDING, WORLD_HEIGHT/2, -90.0f, Ship.YELLOW_SHIP));
		ships.put(Ship.GREEN_SHIP, new Ship(WORLD_WIDTH - SHIP_START_PADDING, WORLD_HEIGHT/2, 90.0f, Ship.GREEN_SHIP));
		
		ships.put(Ship.BLUE_SHIP, new Ship(SHIP_START_PADDING, WORLD_HEIGHT - SHIP_START_PADDING, -135.0f, Ship.BLUE_SHIP));
		ships.put(Ship.PURPLE_SHIP, new Ship(WORLD_WIDTH/2, WORLD_HEIGHT - SHIP_START_PADDING, 180.0f, Ship.PURPLE_SHIP));
		ships.put(Ship.BLACK_SHIP, new Ship(WORLD_WIDTH - SHIP_START_PADDING, WORLD_HEIGHT - SHIP_START_PADDING, 135.0f, Ship.BLACK_SHIP));
	}

	private void initHealthCrates()
	{
		HealthCrate.spawn(WORLD_WIDTH/2 + HEALTH_CRATE_START_PADDING, WORLD_HEIGHT/2);
		HealthCrate.spawn(WORLD_WIDTH/2 - HEALTH_CRATE_START_PADDING, WORLD_HEIGHT/2);
		HealthCrate.spawn(WORLD_WIDTH/2, WORLD_HEIGHT/2 + HEALTH_CRATE_START_PADDING);
		HealthCrate.spawn(WORLD_WIDTH/2, WORLD_HEIGHT/2 - HEALTH_CRATE_START_PADDING);
	}

	private void initBattlefield()
	{
		//space background
		final Sprite background = new Sprite(0, 0, 960, 540, AetherFlamesActivity.mSpaceTextureRegion, this.getVertexBufferObjectManager());
		AetherFlamesActivity.mScene.attachChild(background);

		//battlefield boundaries
		final Rectangle bottomOuter = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, mVertexBufferObjectManager);
		final Rectangle topOuter = new Rectangle(0, 0, CAMERA_WIDTH, 2, mVertexBufferObjectManager);
		final Rectangle leftOuter = new Rectangle(0, 0, 2, CAMERA_HEIGHT, mVertexBufferObjectManager);
		final Rectangle rightOuter = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, mVertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(AetherFlamesActivity.mPhysicsWorld, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(AetherFlamesActivity.mPhysicsWorld, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(AetherFlamesActivity.mPhysicsWorld, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(AetherFlamesActivity.mPhysicsWorld, rightOuter, BodyType.StaticBody, wallFixtureDef);
	}
}
