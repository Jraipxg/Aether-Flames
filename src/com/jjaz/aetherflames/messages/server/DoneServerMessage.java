package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class DoneServerMessage extends ServerMessage implements AetherFlamesConstants {
	
	public boolean mDone;
	
	/**
	 * C'tor
	 */
	public DoneServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param done Filler param.
	 */
	public DoneServerMessage(final boolean done) {
		this.mDone = done;
	}
	
	/**
	 * Setter.
	 * 
	 * @param done Filler param.
	 */	
	public void setDone(final boolean done) {
		this.mDone = done;
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_DONE;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mDone = pDataInputStream.readBoolean();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeBoolean(this.mDone);
	}
	
	
}
