package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.messages.AetherFlamesConstants;

public class ShipUpdateServerMessage extends ServerMessage implements AetherFlamesConstants {
	
	public int mShipID;
	public int mHealth;
	public float mOrientation;
	public float mVectorX, mVectorY;
	public float mPosX, mPosY;
	
	/**
	 * C'tor
	 */
	public ShipUpdateServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param sID Ship id.
	 * @param health Ship health.
	 * @param orientation Ship orientation (angle 0-360).
	 * @param vecX Ship vector x component.
	 * @param vecY Ship vector y component.
	 * @param posX Ship initial position x component.
	 * @param posY Ship initial position y component.
	 */
	public ShipUpdateServerMessage(final int sID, final int health,
								   final float orientation,
								   final float vecX, final float vecY,
								   final float posX, final float posY) {
		this.mShipID = sID;
		this.mHealth = health;
		this.mOrientation = orientation;
		this.mVectorX = vecX;
		this.mVectorY = vecY;
		this.mPosX = posX;
		this.mPosY = posY;
	}
	
	/**
	 * Setter.
	 * 
	 * @param sID Ship id.
	 * @param health Ship health.
	 * @param orientation Ship orientation (angle 0-360).
	 * @param vecX Ship vector x component.
	 * @param vecY Ship vector y component.
	 * @param posX Ship initial position x component.
	 * @param posY Ship initial position y component.
	 */	
	public void setNewBullet(final int sID, final int health,
			   				 final float orientation,
			   				 final float vecX, final float vecY,
			   				 final float posX, final float posY) {
		this.mShipID = sID;
		this.mHealth = health;
		this.mOrientation = orientation;
		this.mVectorX = vecX;
		this.mVectorY = vecY;
		this.mPosX = posX;
		this.mPosY = posY;		
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_SHIP_UPDATE;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mShipID = pDataInputStream.readInt();
		this.mHealth = pDataInputStream.readInt();
		this.mOrientation = pDataInputStream.readFloat();
		this.mVectorX = pDataInputStream.readFloat();
		this.mVectorY = pDataInputStream.readFloat();
		this.mPosX = pDataInputStream.readFloat();
		this.mPosY = pDataInputStream.readFloat();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mShipID);
		pDataOutputStream.writeInt(this.mHealth);
		pDataOutputStream.writeFloat(this.mOrientation);
		pDataOutputStream.writeFloat(this.mVectorX);
		pDataOutputStream.writeFloat(this.mVectorY);
		pDataOutputStream.writeFloat(this.mPosX);
		pDataOutputStream.writeFloat(this.mPosY);
	}
	
	
	
}
