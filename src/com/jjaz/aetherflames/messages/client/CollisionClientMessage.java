package com.jjaz.aetherflames.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class CollisionClientMessage extends ClientMessage implements AetherFlamesConstants {
	
	public int mShipID;
	public int mBulletID;
	
	/**
	 * C'tor
	 */
	public CollisionClientMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param sID Ship id.
	 * @param bID Bullet id.
	 */
	public CollisionClientMessage(final int sID, final int bID) {
		this.mShipID = sID;
		this.mBulletID = bID;
	}
	
	/**
	 * Setter.
	 * 
	 * @param sID Ship id.
	 * @param bID Bullet id.
	 */	
	public void setCollision(final int sID, final int bID) {
		this.mShipID = sID;
		this.mBulletID = bID;		
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_CLIENT_COLLISION;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mShipID = pDataInputStream.readInt();
		this.mBulletID = pDataInputStream.readInt();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mShipID);
		pDataOutputStream.writeInt(this.mBulletID);
	}
	
	
}
