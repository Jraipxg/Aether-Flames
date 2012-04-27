package com.jjaz.aetherflames;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.handler.IUpdateHandler;
import com.jjaz.aetherflames.AetherFlamesConstants;
import com.jjaz.aetherflames.messages.client.ConnectionCloseClientMessage;
import com.jjaz.aetherflames.messages.client.ConnectionEstablishClientMessage;
import com.jjaz.aetherflames.messages.client.GameStateClientMessage;
import com.jjaz.aetherflames.messages.client.NewBulletClientMessage;
import com.jjaz.aetherflames.messages.client.ShipUpdateClientMessage;
import com.jjaz.aetherflames.messages.client.CollisionClientMessage;
import com.jjaz.aetherflames.messages.client.DoneClientMessage;
import com.jjaz.aetherflames.messages.server.GameStateServerMessage;
import com.jjaz.aetherflames.messages.server.CollisionServerMessage;
import com.jjaz.aetherflames.messages.server.GameEndServerMessage;
import com.jjaz.aetherflames.messages.server.GameStartServerMessage;
import com.jjaz.aetherflames.messages.server.NewBulletServerMessage;
import com.jjaz.aetherflames.messages.server.ShipUpdateServerMessage;
import com.jjaz.aetherflames.messages.server.DoneServerMessage;
import com.jjaz.aetherflames.messages.server.ConnectionEstablishedServerMessage;
import com.jjaz.aetherflames.messages.server.ConnectionRejectedProtocolMissmatchServerMessage;
import com.jjaz.aetherflames.messages.server.ConnectionRejectedGameStartedServerMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.SocketServer.ISocketServerListener.DefaultSocketServerListener;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.util.debug.Debug;

public class AetherFlamesServer extends
		SocketServer<SocketConnectionClientConnector> implements
		AetherFlamesConstants, IUpdateHandler {


	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
	boolean gameStarted;
	LinkedList<ServerMessage> messages;
	HashSet<ClientConnector<SocketConnection>> connectedPlayers;
	int numDone; // needs to reach number of players
	int numPlayers; // current player count
	final int NUM_PLAYERS = 1;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public AetherFlamesServer(final ISocketConnectionClientConnectorListener pSocketConnectionClientConnectorListener) {
		super(SERVER_PORT, pSocketConnectionClientConnectorListener, new DefaultSocketServerListener<SocketConnectionClientConnector>());

		this.initMessagePool();
		messages = new LinkedList<ServerMessage>();
		connectedPlayers = new HashSet<ClientConnector<SocketConnection>>();
		
		numDone = 0;
		gameStarted = false;

	}		

	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ShipUpdateServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ShipUpdateServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_NEW_BULLET, NewBulletServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_SHIP_UPDATE, ShipUpdateServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_COLLISION, CollisionServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_GAME_START, GameStartServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_GAME_END, GameEndServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_DONE, DoneServerMessage.class);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
		final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_GAME_STATE, NewBulletClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				synchronized (AetherFlamesServer.this) {
					final GameStateClientMessage gameStateClientMessage = (GameStateClientMessage)pClientMessage;
					final GameStateServerMessage gameStateServerMessage = (GameStateServerMessage)AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_GAME_STATE);
					gameStateServerMessage.setFromClientMessage(gameStateClientMessage); // copy the data to the server message
					AetherFlamesServer.this.sendBroadcastServerMessage(gameStateServerMessage); // broadcast
					AetherFlamesServer.this.mMessagePool.recycleMessage(gameStateServerMessage);
				}
			}
		});
		
		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_NEW_BULLET, NewBulletClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				synchronized (AetherFlamesServer.this) {
					final NewBulletClientMessage newBulletClientMessage = (NewBulletClientMessage)pClientMessage;
					final NewBulletServerMessage newBulletServerMessage = (NewBulletServerMessage)AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_NEW_BULLET);
					newBulletServerMessage.setNewBullet(newBulletClientMessage.mShipID, newBulletClientMessage.mBulletID, newBulletClientMessage.mBulletType,
														newBulletClientMessage.mVelocityX, newBulletClientMessage.mVelocityY,
														newBulletClientMessage.mPosX, newBulletClientMessage.mPosY, newBulletClientMessage.mAngle);
					AetherFlamesServer.this.sendBroadcastServerMessage(newBulletServerMessage); // broadcast
					AetherFlamesServer.this.mMessagePool.recycleMessage(newBulletServerMessage);
				}
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_SHIP_UPDATE, ShipUpdateClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				synchronized (AetherFlamesServer.this) {
					final ShipUpdateClientMessage shipUpdateClientMessage = (ShipUpdateClientMessage)pClientMessage;
					final ShipUpdateServerMessage shipUpdateServerMessage = (ShipUpdateServerMessage)AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_SHIP_UPDATE);
					shipUpdateServerMessage.setShipUpdate(shipUpdateClientMessage.mShipID, shipUpdateClientMessage.mHealth,
														shipUpdateClientMessage.mOrientation, shipUpdateClientMessage.mAngularVelocity,
														shipUpdateClientMessage.mVectorX, shipUpdateClientMessage.mVectorY,
														shipUpdateClientMessage.mPosX, shipUpdateClientMessage.mPosY);
					messages.addLast(shipUpdateServerMessage);
				}
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_COLLISION, CollisionClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {

				synchronized (AetherFlamesServer.this) {
					final CollisionClientMessage collisionClientMessage = (CollisionClientMessage)pClientMessage;
					final CollisionServerMessage collisionServerMessage = (CollisionServerMessage)AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_COLLISION);
					collisionServerMessage.setCollision(collisionClientMessage.mShipID, collisionClientMessage.mBulletID);
					AetherFlamesServer.this.sendBroadcastServerMessage(collisionServerMessage); // broadcast
					AetherFlamesServer.this.mMessagePool.recycleMessage(collisionServerMessage);
				}
			}
		});
		
		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, ConnectionCloseClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				connectedPlayers.remove(pClientConnector); // remove this player (if it exists) from the set of players
				pClientConnector.terminate();
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, ConnectionEstablishClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				synchronized(AetherFlamesServer.this) {
					final ConnectionEstablishClientMessage connectionEstablishClientMessage = (ConnectionEstablishClientMessage) pClientMessage;
					if(connectionEstablishClientMessage.getProtocolVersion() == PROTOCOL_VERSION) {
						if (gameStarted == false && connectedPlayers.size() < 4) {
							final ConnectionEstablishedServerMessage connectionEstablishedServerMessage = (ConnectionEstablishedServerMessage) AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED);
							try {
								pClientConnector.sendServerMessage(connectionEstablishedServerMessage);
							} catch (IOException e) {
								Debug.e(e);
							}
							connectedPlayers.add(pClientConnector); // add this client to our known clients
							AetherFlamesServer.this.mMessagePool.recycleMessage(connectionEstablishedServerMessage);
							// if we reached the player count, start the game already!
							if (connectedPlayers.size() == NUM_PLAYERS) {
								final GameStartServerMessage gameStartServerMessage = (GameStartServerMessage) AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_GAME_START);
								AetherFlamesServer.this.sendBroadcastServerMessage(gameStartServerMessage);
								AetherFlamesServer.this.mMessagePool.recycleMessage(gameStartServerMessage);
								gameStarted = true; // game has begun, no more players!
							}
						} else { // rejected - game started or full
							final ConnectionRejectedGameStartedServerMessage connectionRejectedGameStartedServerMessage = (ConnectionRejectedGameStartedServerMessage) AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_GAME_STARTED);
							try {
								pClientConnector.sendServerMessage(connectionRejectedGameStartedServerMessage);
							} catch (IOException e) {
								Debug.e(e);
							}
							AetherFlamesServer.this.mMessagePool.recycleMessage(connectionRejectedGameStartedServerMessage);
						}
					} else {
						final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage) AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH);
						connectionRejectedProtocolMissmatchServerMessage.setProtocolVersion(PROTOCOL_VERSION);
						try {
							pClientConnector.sendServerMessage(connectionRejectedProtocolMissmatchServerMessage);
						} catch (IOException e) {
							Debug.e(e);
						}
						AetherFlamesServer.this.mMessagePool.recycleMessage(connectionRejectedProtocolMissmatchServerMessage);
					}
				}
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_DONE, DoneClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				// can use this to send out all the client messages
				synchronized (AetherFlamesServer.this) {
					numDone += 1;
					if (numDone == NUM_PLAYERS) {
						messages.addLast((DoneServerMessage)AetherFlamesServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_DONE));
						try {
							Iterator<ServerMessage> iterator = messages.iterator();
							while (iterator.hasNext()) {
								ServerMessage message = iterator.next();
								AetherFlamesServer.this.sendBroadcastServerMessage(message);
								AetherFlamesServer.this.mMessagePool.recycleMessage(message);
							}
						} catch (IOException e) {
							Debug.e(e);
						}
						messages.clear();
						numDone = 0; // reset
					}
				}

			}
		});

		return clientConnector;
	}

}
