package com.jjaz.aetherflames.physics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jjaz.aetherflames.AetherFlamesActivity;
import com.jjaz.aetherflames.AetherFlamesConstants;
import com.jjaz.aetherflames.ProjectileWeapon;
import com.jjaz.aetherflames.Ship;
import com.jjaz.aetherflames.messages.client.GameStateClientMessage;
import com.jjaz.aetherflames.messages.client.ShipUpdateClientMessage;
import com.jjaz.aetherflames.messages.server.GameStateServerMessage;

public class DistributedFixedStepPhysicsWorld extends FixedStepPhysicsWorld implements AetherFlamesConstants {
	
	// Game state variables
	private int mID;
	private Map<Integer, Ship> mShips;
	private ArrayList<Bullet> mBullets;
	private int mNextBulletID;
	
	// Frame rate variables
	private int mMaximumStepsPerUpdate;
	private final float mTimeStep;
	private float mSecondsElapsedAccumulator;
	private int mFrameNum;
	
	// Networking variables
	private ServerConnector<SocketConnection> mServerConnector;
	private MessagePool<IMessage> mMessagePool;
	
	public DistributedFixedStepPhysicsWorld(final int pStepsPerSecond, final Vector2 pGravity, final boolean pAllowSleep, final int pVelocityIterations, final int pPositionIterations) {
		super(pStepsPerSecond, pGravity, pAllowSleep, pVelocityIterations, pPositionIterations);
		this.mBullets = new ArrayList<Bullet>();
		this.mNextBulletID = 0;
		this.mMaximumStepsPerUpdate = 10;
		this.mTimeStep = 1.0f / pStepsPerSecond;
		this.mSecondsElapsedAccumulator = 0;
		this.mFrameNum = 0;
		this.mMessagePool = new MessagePool<IMessage>();
		
		initMessagePool();
	}
	
	/**
	 * Initializes the message pool
	 */
	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_GAME_STATE, GameStateClientMessage.class);
	}
	

	/**
	 * Registers all gameplay message types to be handled by the designated methods in the ClientGameManager class
	 */
	private void registerMessageHandlers() {
		try {
			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_GAME_STATE, GameStateServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final GameStateServerMessage gameStateMessage = (GameStateServerMessage)pServerMessage;
					DistributedFixedStepPhysicsWorld.this.handleGameStateMessage(gameStateMessage);
				}
			});

		} catch (final Throwable t) {
			Debug.e(t);
		}
	}
	
	/**
	 * Reports the game state to the server
	 */
	private void reportState() {
		Bullet latestBullet;
		
		if (this.mBullets.size() != 0) {
			latestBullet = this.mBullets.get(0);
			this.mBullets.remove(0);
		} else {
			// set the fields of the bullet object to empty if there was no bullet
			latestBullet = new Bullet();
			latestBullet.id = FIELD_EMPTY;
			latestBullet.type = FIELD_EMPTY;
			latestBullet.angle = FIELD_EMPTY;
			latestBullet.position = new Vector2(FIELD_EMPTY, FIELD_EMPTY);
			latestBullet.velocity = new Vector2(FIELD_EMPTY, FIELD_EMPTY);
		}
		
		Ship ship = this.mShips.get(this.mID);
		
		// set the message fields and send the message if the ship is still alive
		if (ship != null) {
			GameStateClientMessage message = (GameStateClientMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_GAME_STATE);
			message.setFrameNumber(this.mFrameNum);
			message.setShipState(this.mShips.get(this.mID));
			message.setBulletState(latestBullet.id, latestBullet.type, latestBullet.angle, latestBullet.position.x, latestBullet.position.y, latestBullet.velocity.x, latestBullet.velocity.y);
			
			// send the message
			try {
				this.mServerConnector.sendClientMessage(message);
				this.mMessagePool.recycleMessage(message);
			} catch (IOException e) {
				Debug.e(e);
			}
		}
	}
	
	/**
	 * Update the ship and estimate the new position based on previous values
	 * 
	 * @param ship The ship to update
	 * @param messageFrame The frame at which the message was sent
	 * @param currentFrame The current frame on the local device
	 * @param angle New angle
	 * @param angularVelocity New angularVelocity
	 * @param position New position
	 * @param velocity New velocity
	 */
	private void updateShipBody(Ship ship, int messageFrame, int currentFrame, float angle, float angularVelocity, Vector2 position, Vector2 velocity) {
		int frameDiff = currentFrame - messageFrame;
		float framePeriod = 1.0f / FRAMES_PER_SECOND;
		float dt = frameDiff * framePeriod;
		
		// estimate the current position and angle
		float curAngle = angle + angularVelocity * dt;
		Vector2 curPos = position.add(velocity.mul(dt));
		
		ship.setAngle(curAngle);
		//ship.setAngularVelocity(angularVelocity);
		ship.setPosition(curPos);
		ship.setVelocity(velocity);
		
	}
	
	/**
	 * Update the ship and estimate the new position based on previous values
	 * 
	 * @param messageFrame The frame at which the message was sent
	 * @param currentFrame The current frame on the local device
	 * @param weapons The weapons of the current ship
	 * @param type Bullet type
	 * @param angle Angle upon creation
	 * @param position Position upon creation
	 * @param velocity Velocity upon creation
	 */
	private void createNewBullet(int messageFrame, int currentFrame, ArrayList<ProjectileWeapon> weapons, int type, float angle, Vector2 position, Vector2 velocity) {
		int frameDiff = currentFrame - messageFrame;
		float framePeriod = 1.0f / FRAMES_PER_SECOND;
		float dt = frameDiff * framePeriod;
		
		int i = 0;
		ProjectileWeapon weapon;
		while (i < weapons.size() && weapons.get(i).getType() != type) {
			i++;
		}
		
		// fire the weapon if it exists
		if (i < weapons.size()) {
			weapon = weapons.get(i);
			Vector2 curPos = position.add(velocity.mul(dt));
			weapon.fire(curPos, velocity, angle);
		}
	}
	
	/**
	 * Handle game update messages
	 * 
	 * @param message The message to handle
	 */
	public void handleGameStateMessage(GameStateServerMessage message) {
		int messageFrame = message.mFrameNum;
		int currentFrame = this.mFrameNum;
		
		if (currentFrame - messageFrame > MAX_GAME_STATE_DELAY) {
			return;
		}
		
		int shipID = message.mShipID;
		Ship ship = this.mShips.get(shipID);
		
		// update the ship if it still exists
		if (ship != null) {
			// update physical ship parameters
			float angle = message.mOrientation;
			float angularVelocity = message.mAngularVelocity;
			Vector2 position = new Vector2(message.mShipPosX, message.mShipPosY);
			Vector2 velocity = new Vector2(message.mShipVelocityX, message.mShipVelocityY);
			updateShipBody(ship, messageFrame, currentFrame, angle, angularVelocity, position, velocity);
		
			// set nonphysical ship parameters
			ship.setHealth(message.mHealth);
			ship.setEnergy(message.mEnergy);
			if (message.mShieldActive) {
				ship.activateShields();
			} else {
				ship.deactivateShields();
			}
			
			// create a new bullet if there is one
			int bulletID = message.mBulletID;
			if (bulletID != FIELD_EMPTY) {
				int type = message.mBulletType;
				angle = message.mBulletAngle;
				position = new Vector2(message.mBulletPosX, message.mBulletPosY);
				velocity = new Vector2(message.mBulletVelocityX, message.mBulletVelocityY);
				createNewBullet(messageFrame, currentFrame, ship.getAvailableWeapons(), type, angle, position, velocity);
			}
		}
	}
	
	/**
	 * Registers a new bullet to be sent to other users
	 */
	public void registerBullet(int type, Vector2 position, Vector2 velocity, float angle) {
		Bullet b = new Bullet();
		b.id = this.mNextBulletID;
		b.type = type;
		b.position = position;
		b.velocity = velocity;
		b.angle = angle;
		
		if (this.mBullets.size() < MAX_BULLETS_PER_FRAME) {
			mBullets.add(b);
			this.mNextBulletID++;
		}
	}
	
	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mRunnableHandler.onUpdate(pSecondsElapsed);
		this.mSecondsElapsedAccumulator += pSecondsElapsed;

		final int velocityIterations = this.mVelocityIterations;
		final int positionIterations = this.mPositionIterations;

		final World world = this.mWorld;
		final float stepLength = this.mTimeStep;
		
		int stepsAllowed = this.mMaximumStepsPerUpdate;
		
		while(this.mSecondsElapsedAccumulator >= stepLength && stepsAllowed > 0) {
			world.step(stepLength, velocityIterations, positionIterations);
			this.mSecondsElapsedAccumulator -= stepLength;
			stepsAllowed--;
			
			if ((this.mFrameNum % FRAMES_PER_UPDATE) == 0) {
				this.reportState();
			}
			
			this.mFrameNum++;
		}
		
		this.mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	}
	
	/**
	 * Removes the given ship from the list
	 * 
	 * @param id The id of the ship to remove
	 */
	public void removeShip(int id) {
		this.mShips.remove(id);
	}
	
	// Getters
	public int getID() {
		return this.mID;
	}
	
	public int frameNum() {
		return this.mFrameNum;
	}
	
	// Setters
	public void setID(int id) {
		this.mID = id;
		this.mNextBulletID = this.mID * 1000000; // makes sure there are enough IDs
	}
	
	public void setShips(Map<Integer, Ship> m) {
		this.mShips = m;
	}
	
	public void setServerConnector(ServerConnector<SocketConnection> connector) {
		this.mServerConnector = connector;
		
		registerMessageHandlers();
	}
	
	private class Bullet {
		public int id;
		public int type;
		public Vector2 position;
		public Vector2 velocity;
		public float angle;
	}

}
