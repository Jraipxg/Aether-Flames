package com.jjaz.aetherflames.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;


public class DoneClientMessage extends ClientMessage implements AetherFlamesConstants {
	
	public boolean mDone;
	
	/**
	 * C'tor
	 */
	public DoneClientMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param done Filler param.
	 */
	public DoneClientMessage(final boolean done) {
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
		return FLAG_MESSAGE_CLIENT_DONE;
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
