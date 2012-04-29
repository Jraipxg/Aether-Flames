package com.jjaz.aetherflames.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class HitHealthPackClientMessage extends ClientMessage implements AetherFlamesConstants {
	
	public int mShipID;
	public int mHealthPackID;
	
	/**
	 * C'tor
	 */
	public HitHealthPackClientMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param sID Ship id.
	 * @param hID Health pack id.
	 */
	public HitHealthPackClientMessage(final int sID, final int hID) {
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
		return FLAG_MESSAGE_CLIENT_HIT_HEALTH_PACK;
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
