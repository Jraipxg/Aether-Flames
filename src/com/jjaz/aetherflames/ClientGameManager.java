package com.jjaz.aetherflames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import com.jjaz.aetherflames.messages.AetherFlamesConstants;
import com.jjaz.aetherflames.messages.client.ShipUpdateClientMessage;
import com.jjaz.aetherflames.messages.server.CollisionServerMessage;
import com.jjaz.aetherflames.messages.server.NewBulletServerMessage;
import com.jjaz.aetherflames.messages.server.ShipUpdateServerMessage;
import com.jjaz.aetherflames.physics.DistributedFixedStepPhysicsWorld;

public class ClientGameManager implements AetherFlamesConstants {

	// Update queue
	private ArrayList<ClientMessage> updateQueue;

	// Game Data
	private DistributedFixedStepPhysicsWorld physicsWorld;
	private ArrayList<Body> ships;
	private ArrayList<Integer> shipHealths;
	private Map<Integer, Body> bullets;
	private int nextBulletID;
	private int myID;
	private int frameCount;

	// Networking variables
	private ServerConnector<SocketConnection> serverConnector;
	private MessagePool<IMessage> messagePool;

	// Constructors
	/**
	 * Default constructor
	 */
	public ClientGameManager() {
		this.updateQueue = new ArrayList<ClientMessage>();
		this.ships = new ArrayList<Body>();
		this.shipHealths = new ArrayList<Integer>();
		this.bullets = new HashMap<Integer, Body>();
		this.nextBulletID = myID * 1000000;
		this.frameCount = 0;
	}

	/**
	 * Constructor
	 * 
	 * @param w The game's distributed physics world.
	 * @param id This player's game ID.
	 * @param connector Connection to the game server.
	 * @param pool Message pool
	 */
	public ClientGameManager(DistributedFixedStepPhysicsWorld w, int id, ServerConnector<SocketConnection> connector, MessagePool<IMessage> pool) {
		this();
		this.physicsWorld = w;
		this.myID = id;
		this.serverConnector = connector;
		this.messagePool = pool;
	}

	// Methods
	/**
	 * Creates a new set of players
	 * 
	 * @param numPlayers NUmber of players in the game
	 */
	public void startGame(int numPlayers) {

	}

	/**
	 * Queues a force application message for the local player's ship
	 * 
	 * @param force The force to be applied
	 */
	public synchronized void applyForce(Vector2 force) {
		ShipUpdateClientMessage message = (ShipUpdateClientMessage)this.messagePool.obtainMessage(FLAG_MESSAGE_CLIENT_SHIP_UPDATE);
		Body myShip = this.ships.get(myID);

		// get the current state of the ship
		float angle = myShip.getAngle();
		float omega = myShip.getAngularVelocity();
		Vector2 center = myShip.getWorldCenter();

		// queue the new message
		message.setUpdateMessage(this.myID, this.shipHealths.get(myID), angle, omega, force.x, force.y, center.x, center.y);
		this.updateQueue.add(message);
	}

	/**
	 * Queues a change-of-angular-velocity message for the local player's ship
	 * 
	 * @param omega The new angular velocity
	 */
	public synchronized void setAngularVelocity(float omega) {
		ShipUpdateClientMessage message = (ShipUpdateClientMessage)this.messagePool.obtainMessage(FLAG_MESSAGE_CLIENT_SHIP_UPDATE);
		Body myShip = this.ships.get(myID);

		// get the current state of the ship
		float angle = myShip.getAngle();
		Vector2 center = myShip.getWorldCenter();

		// queue the new message
		message.setUpdateMessage(this.myID, this.shipHealths.get(myID), angle, omega, 0, 0, center.x, center.y);
		this.updateQueue.add(message);
	}

	/**
	 * Queues a create bullet message
	 * 
	 * @param velocity The velocity of the bullet
	 * @param center The starting coordinate of the bullet
	 */
	public synchronized void createNewBullet(Vector2 velocity, Vector2 center){}

	/**
	 * Sends all updates since the last frame to the game server
	 */
	public synchronized void sendUpdates() {
		// ADD IN CLIENT END GAME MESSAGE
		try {
			for (ClientMessage message : this.updateQueue) {
				this.serverConnector.sendClientMessage(message);
				this.messagePool.recycleMessage(message);
			}
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	/**
	 * Handles a ship update message from the server
	 * 
	 * @param message The message to handle
	 */
	public void handleShipUpdateMessage(ShipUpdateServerMessage message) {
		int id = message.mShipID;
		Body shipBody = this.ships.get(id);

		// get the message parameters
		float angle = message.mOrientation;
		float omega = message.mAngularVelocity;
		Vector2 force = new Vector2(message.mVectorX, message.mVectorY);
		Vector2 point = new Vector2(message.mPosX, message.mPosY);

		Vector2 myCenter = shipBody.getWorldCenter();
		//if (point.x != myCenter.x && point.y != myCenter.y) {
		//	shipBody.
		//}

		// apply the updates
		shipBody.applyForce(force, myCenter);
		shipBody.setAngularVelocity(omega);
	}

	/**
	 * Handles a new bullet message from the server
	 * 
	 * @param message The message to handle
	 */
	public void handleNewBulletMessage(NewBulletServerMessage message) {
		int id = message.mShipID;
		Body shipBody = this.ships.get(id);

		// CREATE BULLET AND PUT IN LIST
	}

	/**
	 * Handles a new collision message from the server
	 * 
	 * @param message The message to handle
	 */
	public void handleCollisionMessage(CollisionServerMessage message) {
		int shipID = message.mShipID;
		int bulletID = message.mBulletID;

		int newHealth = this.shipHealths.get(shipID) - 10;
		this.shipHealths.set(shipID, newHealth);

		// destroy the ship if its dead
		if (newHealth <= 0) {
			this.physicsWorld.destroyBody(this.ships.get(shipID));
		}

		// remove the bullet from the world
		this.physicsWorld.destroyBody(this.bullets.get(bulletID));
	}

	/**
	 * Makes all changes from the previous frame and sets up the next frame
	 */
	public void nextFrame() {
		this.physicsWorld.step();
		this.frameCount++;
	}

	// Setters
	public void setPhysicsWorld(DistributedFixedStepPhysicsWorld w) {
		this.physicsWorld = w;
	}

	public void setID(int id) {
		this.myID = id;
	}

	public void setServerConnector(ServerConnector<SocketConnection> connector) {
		this.serverConnector = connector;
	}

	public void setMessagePool(MessagePool<IMessage> pool) {
		this.messagePool = pool;
	}

	// Getters
	public DistributedFixedStepPhysicsWorld gePhysicsWorld() {
		return this.physicsWorld;
	}

	public int getID() {
		return this.myID;
	}

	public int getFrameCount() {
		return this.frameCount;
	}
}