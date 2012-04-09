package com.jjaz.aetherflames;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class AetherFlamesActivity extends SimpleBaseGameActivity implements OnClickListener 
{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int HEALTH_SIZE = 40;
	private static final int SHIP_SIZE = 40;

	private static final int CAMERA_WIDTH = 960;
	private static final int CAMERA_HEIGHT = 540;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BitmapTextureAtlas mVehiclesTexture;
	private TiledTextureRegion mVehiclesTextureRegion;

	private BitmapTextureAtlas mSpaceTexture;
	private ITextureRegion mSpaceTextureRegion;

	private BitmapTextureAtlas mHealthCrateTexture;
	private ITextureRegion mHealthCrateTextureRegion;

	private BitmapTextureAtlas mControlStickTexture;
	private ITextureRegion mControlStickBaseTextureRegion;
	private ITextureRegion mControlStickKnobTextureRegion;
	
	private BitmapTextureAtlas mButtonsTexture;
	private ITextureRegion mButtonsBaseTextureRegion;
	private ITextureRegion mButtonsKnobTextureRegion;
	
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFace1TextureRegion;
	private ITextureRegion mFace2TextureRegion;
	private ITextureRegion mFace3TextureRegion;

	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;

	private Body mShipBody;
	private TiledSprite mShip;
	private DigitalOnScreenControl mControlStick;
	private DigitalOnScreenControl mButtons;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions()
	{
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public void onCreateResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mVehiclesTexture = new BitmapTextureAtlas(this.getTextureManager(), 60, 30, TextureOptions.BILINEAR);
		this.mVehiclesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mVehiclesTexture, this, "ship.png", 0, 0, 1, 1);
		this.mVehiclesTexture.load();

		this.mSpaceTexture = new BitmapTextureAtlas(this.getTextureManager(), 960*2, 540, TextureOptions.BILINEAR);
		this.mSpaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mSpaceTexture, this, "spaceBackground.png", 0, 0);
		this.mSpaceTexture.load();

		this.mControlStickTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mControlStickBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mControlStickTexture, this, "D-Pad.png", 0, 0);
		this.mControlStickKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mControlStickTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mControlStickTexture.load();
		
		this.mButtonsTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mButtonsBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonsTexture, this, "D-Pad.png", 0, 0);
		this.mButtonsKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mButtonsTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mControlStickTexture.load();

		this.mHealthCrateTexture = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
		this.mHealthCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mHealthCrateTexture, this, "healthCrate.png", 0, 0);
		this.mHealthCrateTexture.load();
		

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512);
		this.mFace1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png");
		this.mFace2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png");
		this.mFace3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_hexagon_tiled.png");
	}

	@Override
	public Scene onCreateScene()
	{
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0, 0, 0));

		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);

		this.initSpace();
		this.initBorders();
		this.initShip();
		this.initObstacles();
		this.initOnScreenControls();

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);

		return this.mScene;
	}

	@Override
	public void onGameCreated()
	{

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initOnScreenControls()
	{
		/*final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(),
				new IAnalogOnScreenControlListener()
				{
					@Override
					public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY)
					{
						final Body shipBody = AetherFlamesActivity.this.mShipBody;
						
						float shipAngle = shipBody.getAngle();
						float thrustMagnitude = pValueY * 5;
						float thrustX = (float)(thrustMagnitude*Math.sin(shipAngle));
						float thrustY = -(float)(thrustMagnitude*Math.cos(shipAngle));
						final Vector2 force = Vector2Pool.obtain(thrustX, thrustY);//(float)(thrustMagnitude*Math.cos(shipBody.getAngle())
						final Vector2 point = shipBody.getWorldCenter();
						if(Math.abs(force.y) > 0.03)
						{
							//shipBody.setLinearVelocity(velocity);
							shipBody.applyForce(force, point);
						}
						Vector2Pool.recycle(force);
						
						if(pValueX != 0)
						{
							shipBody.setAngularVelocity(pValueX);
							//shipBody.applyTorque(pValueX);
						}
						
						AetherFlamesActivity.this.mShip.setRotation(MathUtils.radToDeg(shipBody.getAngle()));
					}

					@Override
					public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl)
					{
					}
				});
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(1.3f);
		analogOnScreenControl.getControlKnob().setScale(1.3f);
		analogOnScreenControl.refreshControlKnobPosition();

		this.mScene.setChildScene(analogOnScreenControl);*/
		
		this.mControlStick = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mControlStickBaseTextureRegion.getHeight(), this.mCamera, this.mControlStickBaseTextureRegion, this.mControlStickKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				final Body shipBody = AetherFlamesActivity.this.mShipBody;
				
				float shipAngle = shipBody.getAngle();
				float thrustMagnitude = pValueY * 5;
				float thrustX = (float)(thrustMagnitude*Math.sin(shipAngle));
				float thrustY = -(float)(thrustMagnitude*Math.cos(shipAngle));
				final Vector2 force = Vector2Pool.obtain(thrustX, thrustY);//(float)(thrustMagnitude*Math.cos(shipBody.getAngle())
				final Vector2 point = shipBody.getWorldCenter();
				if(Math.abs(force.y) > 0.03)
				{
					//shipBody.setLinearVelocity(velocity);
					shipBody.applyForce(force, point);
				}
				Vector2Pool.recycle(force);
				
				if(pValueX != 0)
				{
					shipBody.setAngularVelocity(pValueX);
					//shipBody.applyTorque(pValueX);
				}
				
				AetherFlamesActivity.this.mShip.setRotation(MathUtils.radToDeg(shipBody.getAngle()));
			}
		});
		this.mControlStick.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mControlStick.getControlBase().setAlpha(0.5f);
		this.mControlStick.getControlBase().setScaleCenter(0, 128);
		this.mControlStick.getControlBase().setScale(1.25f);
		this.mControlStick.getControlKnob().setScale(1.25f);
		this.mControlStick.refreshControlKnobPosition();

		this.mScene.setChildScene(this.mControlStick);
		
		final Sprite face = new ButtonSprite(0, 0, this.mFace1TextureRegion, this.mFace2TextureRegion, this.mFace3TextureRegion, this.getVertexBufferObjectManager(), this);
		this.mScene.registerTouchArea(face);
		this.mScene.attachChild(face);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);
	}

	private void initShip()
	{
		this.mShip = new TiledSprite(20, 20, SHIP_SIZE, SHIP_SIZE, this.mVehiclesTextureRegion, this.getVertexBufferObjectManager());
		this.mShip.setCurrentTileIndex(0);

		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		this.mShipBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, this.mShip, BodyType.DynamicBody, carFixtureDef);
		this.mShipBody.getFixtureList().get(0).setFriction(0);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mShip, this.mShipBody, true, false));

		this.mScene.attachChild(this.mShip);
	}

	private void initObstacles()
	{
		this.addObstacle(CAMERA_WIDTH / 2 + 50, CAMERA_HEIGHT / 2);
		this.addObstacle(CAMERA_WIDTH / 2 - 50, CAMERA_HEIGHT / 2);
		this.addObstacle(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2 + 50);
		this.addObstacle(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2 - 50);
	}

	private void addObstacle(final float pX, final float pY)
	{
		final Sprite box = new Sprite(pX, pY, HEALTH_SIZE, HEALTH_SIZE, this.mHealthCrateTextureRegion, this.getVertexBufferObjectManager());

		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.5f);
		final Body boxBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, box, BodyType.DynamicBody, boxFixtureDef);
		boxBody.setLinearDamping(10);
		boxBody.setAngularDamping(10);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box, boxBody, true, true));

		this.mScene.attachChild(box);
	}

	private void initSpace()
	{
		final Sprite background = new Sprite(0, 0, 960, 540, this.mSpaceTextureRegion, this.getVertexBufferObjectManager());
		this.mScene.attachChild(background);
	}

	private void initBorders()
	{
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

		final Rectangle bottomOuter = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle topOuter = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle leftOuter = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle rightOuter = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, rightOuter, BodyType.StaticBody, wallFixtureDef);

		/*this.mScene.attachChild(bottomOuter);
		this.mScene.attachChild(topOuter);
		this.mScene.attachChild(leftOuter);
		this.mScene.attachChild(rightOuter);*/
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(AetherFlamesActivity.this, "Clicked", Toast.LENGTH_LONG).show();
			}
		});
		
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
