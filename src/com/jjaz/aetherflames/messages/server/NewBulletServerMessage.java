package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class NewBulletServerMessage extends ServerMessage implements AetherFlamesConstants {

	public int mShipID;
	public int mBulletID;
	public int mBulletType;
	public float mVectorX, mVectorY;
	public float mPosX, mPosY;
	public float mAngle;
	
	/**
	 * C'tor
	 */
	public NewBulletServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param sID Ship id.
	 * @param bID Bullet id.
	 * @param vecX Bullet vector x component.
	 * @param vecY Bullet vector y component.
	 * @param posX Bullet initial position x component.
	 * @param posY Bullet initial position y component.
	 */
	public NewBulletServerMessage(final int sID, final int bID, final int type,
								  final float vecX, final float vecY,
								  final float posX, final float posY, final float angle) {
		this.mShipID = sID;
		this.mBulletID = bID;
		this.mBulletType = type;
		this.mVectorX = vecX;
		this.mVectorY = vecY;
		this.mPosX = posX;
		this.mPosY = posY;
		this.mAngle = angle;
	}
	
	/**
	 * Setter.
	 * 
	 * @param sID Ship id.
	 * @param bID Bullet id.
	 * @param vecX Bullet vector x component.
	 * @param vecY Bullet vector y component.
	 * @param posX Bullet initial position x component.
	 * @param posY Bullet initial position y component.
	 */	
	public void setNewBullet(final int sID, final int bID, final int type,
			  				 final float vecX, final float vecY,
			  				 final float posX, final float posY, final float angle) {
		this.mShipID = sID;
		this.mBulletID = bID;
		this.mBulletType = type;
		this.mVectorX = vecX;
		this.mVectorY = vecY;
		this.mPosX = posX;
		this.mPosY = posY;		
		this.mAngle = angle;
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_NEW_BULLET;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mShipID = pDataInputStream.readInt();
		this.mBulletID = pDataInputStream.readInt();
		this.mBulletType = pDataInputStream.readInt();
		this.mVectorX = pDataInputStream.readFloat();
		this.mVectorY = pDataInputStream.readFloat();
		this.mPosX = pDataInputStream.readFloat();
		this.mPosY = pDataInputStream.readFloat();
		this.mAngle = pDataInputStream.readFloat();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mShipID);
		pDataOutputStream.writeInt(this.mBulletID);
		pDataOutputStream.writeInt(this.mBulletType);
		pDataOutputStream.writeFloat(this.mVectorX);
		pDataOutputStream.writeFloat(this.mVectorY);
		pDataOutputStream.writeFloat(this.mPosX);
		pDataOutputStream.writeFloat(this.mPosY);
		pDataOutputStream.writeFloat(this.mAngle);
	}
	
	
}
