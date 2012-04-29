package com.jjaz.aetherflames;

import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.util.debug.Debug;

import com.jjaz.aetherflames.AetherFlamesConstants;
import com.jjaz.aetherflames.messages.phone.*;
import com.jjaz.aetherflames.messages.matchmaker.*;

public class MatchmakerClient implements AetherFlamesConstants {

	// Networking variables
	private ServerConnector<SocketConnection> mMatchmakerConnector;
	private MessagePool<IMessage> mMessagePool;
	boolean matchmakerFound;
	
	public MatchmakerClient() {
		matchmakerFound = false;
		this.mMessagePool = new MessagePool<IMessage>();
		
		initMessagePool();
	}
	
	/**
	 * Initializes the message pool
	 */
	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_PHONE_CONNECTION_CLOSE, ConnectionClosePhoneMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_PHONE_CONNECTION_ESTABLISH, ConnectionEstablishPhoneMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_PHONE_CURRENT_PLAYER_COUNT, CurrentPlayerCountPhoneMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_PHONE_GET_FIRST_SERVER, GetFirstServerPhoneMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_PHONE_GET_SERVER_LIST, GetServerListPhoneMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_PHONE_START_SERVER, StartServerPhoneMessage.class);
	}
	

	/**
	 * Registers all matchmaker messages to be handled by this client
	 */
	private void registerMessageHandlers() {
		try {
			/*this.mMatchmakerConnector.registerServerMessage(FLAG_MESSAGE_MATCHMAKER_CONNECTION_CLOSE, ConnectionCloseMatchmakerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ConnectionCloseMatchmakerMessage gameStateMessage = (ConnectionCloseMatchmakerMessage)pServerMessage;
					DistributedFixedStepPhysicsWorld.this.handleGameStateMessage(gameStateMessage);
				}
			});*/
			this.mMatchmakerConnector.registerServerMessage(FLAG_MESSAGE_MATCHMAKER_CONNECTION_ESTABLISH, ConnectionEstablishMatchmakerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					MatchmakerClient.this.matchmakerFound = true; // connection established
				}
			});
			this.mMatchmakerConnector.registerServerMessage(FLAG_MESSAGE_MATCHMAKER_FREE_SERVER, FreeServerMatchmakerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final FreeServerMatchmakerMessage freeServerMessage = (FreeServerMatchmakerMessage)pServerMessage;
					MatchmakerClient.this.handleFreeServerMessage(freeServerMessage);
				}
			});
			this.mMatchmakerConnector.registerServerMessage(FLAG_MESSAGE_MATCHMAKER_SERVER_LIST, ServerListMatchmakerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ServerListMatchmakerMessage serverListMessage = (ServerListMatchmakerMessage)pServerMessage;
					MatchmakerClient.this.handleServerListMessage(serverListMessage);
				}
			});

		} catch (final Throwable t) {
			Debug.e(t);
		}
	}

	/**
	 * Handle free server messages. This message contains a single server name/IP that
	 * the client can try to connect to.
	 * 
	 * @param message The message to handle
	 */
	public void handleFreeServerMessage(FreeServerMatchmakerMessage message) {

	}

	/**
	 * Handle server list messages. This message contains a serialized list of
	 * servers.
	 * 
	 * @param message The message to handle
	 */
	public void handleServerListMessage(ServerListMatchmakerMessage message) {

	}
	
	/**
	 * Should return a single server, however that is structured.
	 */
	public void requestSingleServer() {
		GetFirstServerPhoneMessage message = (GetFirstServerPhoneMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_PHONE_GET_FIRST_SERVER);
		
		// send the message
		try {
			this.mMatchmakerConnector.sendClientMessage(message);
			this.mMessagePool.recycleMessage(message);
		} catch (IOException e) {
			Debug.e(e);
		}
		
		// SHOULD WAIT HERE FOR THE RESPONSE AND RETURN THAT SERVER
	}
	
	/**
	 * Should return a server list, however that is structured.
	 */
	public void requestServerList() {
		GetServerListPhoneMessage message = (GetServerListPhoneMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_PHONE_GET_SERVER_LIST);
		
		// send the message
		try {
			this.mMatchmakerConnector.sendClientMessage(message);
			this.mMessagePool.recycleMessage(message);
		} catch (IOException e) {
			Debug.e(e);
		}
		
		// SHOULD WAIT HERE FOR THE RESPONSE AND RETURN THAT LIST
	}
	
	/**
	 * Inform the matchmaker that this phone is a server.
	 */
	public void startServer() {
		StartServerPhoneMessage message = (StartServerPhoneMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_PHONE_START_SERVER);
		
		// send the message
		try {
			this.mMatchmakerConnector.sendClientMessage(message);
			this.mMessagePool.recycleMessage(message);
		} catch (IOException e) {
			Debug.e(e);
		}
	}	
	
	/**
	 * Inform the matchmaker of the number of players currently on this server.
	 * Should be called every time a player connects or disconnects if the game
	 * has not yet started.
	 * 
	 * @param numPlayers Current number of players on the server.
	 */
	public void setPlayerCount(short numPlayers) {
		CurrentPlayerCountPhoneMessage message = (CurrentPlayerCountPhoneMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_PHONE_CURRENT_PLAYER_COUNT);
		
		// TODO: CREATE A GAMESERVER OBJECT AND SET THE NUMBER OF PLAYERS IN IT AND ADD IT TO THE MESSAGE
		
		// send the message
		try {
			this.mMatchmakerConnector.sendClientMessage(message);
			this.mMessagePool.recycleMessage(message);
		} catch (IOException e) {
			Debug.e(e);
		}
	}	
	
	/**
	 * Close the connection on starting a game.
	 */
	public void startGame() {
		ConnectionClosePhoneMessage message = (ConnectionClosePhoneMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_PHONE_CONNECTION_CLOSE);
		
		// send the message
		try {
			this.mMatchmakerConnector.sendClientMessage(message);
			this.mMessagePool.recycleMessage(message);
			this.mMatchmakerConnector.terminate(); // don't need this forever
		} catch (IOException e) {
			Debug.e(e);
		}
	}
	
	public void setServerConnector(ServerConnector<SocketConnection> connector) {
		this.mMatchmakerConnector = connector;
		
		registerMessageHandlers();
	}
	
}
