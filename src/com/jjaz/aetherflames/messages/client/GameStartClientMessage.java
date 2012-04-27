package com.jjaz.aetherflames.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class GameStartClientMessage extends ClientMessage implements AetherFlamesConstants {

	public boolean mStart;
	
	/**
	 * C'tor
	 */
	public GameStartClientMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param start True to start game.
	 */
	public GameStartClientMessage(final boolean start) {
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
		return FLAG_MESSAGE_CLIENT_GAME_START;
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
