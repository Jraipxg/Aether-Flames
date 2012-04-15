package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class CollisionServerMessage extends ServerMessage implements AetherFlamesConstants {
	
	public int mShipID;
	public int mBulletID;
	
	/**
	 * C'tor
	 */
	public CollisionServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param sID Ship id.
	 * @param bID Bullet id.
	 */
	public CollisionServerMessage(final int sID, final int bID) {
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
		return FLAG_MESSAGE_SERVER_COLLISION;
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
