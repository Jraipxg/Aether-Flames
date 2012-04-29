package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class HitHealthPackServerMessage extends ServerMessage implements AetherFlamesConstants {
	
	public int mShipID;
	public int mHealthPackID;
	
	/**
	 * C'tor
	 */
	public HitHealthPackServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param sID Ship id.
	 * @param hID Health pack id.
	 */
	public HitHealthPackServerMessage(final int sID, final int hID) {
		this.mShipID = sID;
		this.mHealthPackID = hID;
	}
	
	/**
	 * Setter.
	 * 
	 * @param sID Ship id.
	 * @param hID Health pack id.
	 */	
	public void setHitHealthPack(final int sID, final int hID) {
		this.mShipID = sID;
		this.mHealthPackID = hID;		
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_HIT_HEALTH_PACK;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mShipID = pDataInputStream.readInt();
		this.mHealthPackID = pDataInputStream.readInt();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mShipID);
		pDataOutputStream.writeInt(this.mHealthPackID);
	}

}
