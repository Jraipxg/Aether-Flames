package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.messages.AetherFlamesConstants;

public class GameStartServerMessage extends ServerMessage implements AetherFlamesConstants {

	public boolean mStart;
	
	/**
	 * C'tor
	 */
	public GameStartServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param start True to start game.
	 */
	public GameStartServerMessage(final boolean start) {
		this.mStart = start;
	}
	
	/**
	 * Setter.
	 * 
	 * @param start True to start game.
	 */	
	public void setNewBullet(final boolean start) {
		this.mStart = start;	
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_GAME_START;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mStart = pDataInputStream.readBoolean();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeBoolean(this.mStart);
	}
	
}
