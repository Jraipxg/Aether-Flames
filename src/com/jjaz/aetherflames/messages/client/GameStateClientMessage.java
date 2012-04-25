package com.jjaz.aetherflames.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;
import com.jjaz.aetherflames.Ship;

public class GameStateClientMessage extends ClientMessage implements AetherFlamesConstants {
	
	public int mFrameNum;
	
	// ship data
	public int mShipID;
	public int mHealth;
	public int mEnergy;
	public boolean mShieldActive;
	public float mOrientation;
	public float mAngularVelocity;
	public float mShipPosX, mShipPosY;
	public float mShipVelocityX, mShipVelocityY;
	
	// bullet data
	public int mBulletID;
	public int mBulletType;
	public float mBulletAngle;
	public float mBulletPosX, mBulletPosY;
	public float mBulletVelocityX, mBulletVelocityY;
	
	/**
	 * C'tor
	 */
	public GameStateClientMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param frameNum The frame number of the message
	 */
	public GameStateClientMessage(final int frameNum) {
		this.mFrameNum = frameNum;
	}
	
	public void setFrameNumber(int num) {
		this.mFrameNum = num;
	}
	
	/**
	 * Setter.
	 * 
	 * @param ship The ship to report the state of
	 */	
	public void setShipState(final Ship s) {
		this.mShipID = s.id;
		this.mHealth = s.getHealth();
		this.mEnergy = s.getEnergy();
		this.mShieldActive = s.shieldActivated();
		this.mOrientation = s.getAngle();
		this.mAngularVelocity = s.getAngularVelocity();
		this.mShipPosX = s.getPosition().x;
		this.mShipPosY = s.getPosition().y;		
		this.mShipVelocityX = s.getVelocity().x;
		this.mShipVelocityY = s.getVelocity().y;
	}
	
	/**
	 * Setter.
	 * 
	 * @param bID Bullet id.
	 * @param posX Bullet initial position x component.
	 * @param posY Bullet initial position y component.
	 * @param velX Bullet velocity x component.
	 * @param velY Bullet velocity y component.
	 */	
	public void setBulletState(final int bID, final int type, final float angle,
			   			       final float posX, final float posY,
			   			  	   final float velX, final float velY) {
		this.mBulletID = bID;
		this.mBulletType = type;
		this.mBulletAngle = angle;
		this.mBulletPosX = posX;
		this.mBulletPosY = posY;		
		this.mBulletVelocityX = velX;
		this.mBulletVelocityY = velY;
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_GAME_STATE;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mFrameNum = pDataInputStream.readInt();
		
		this.mShipID = pDataInputStream.readInt();
		this.mHealth = pDataInputStream.readInt();
		this.mEnergy = pDataInputStream.readInt();
		this.mShieldActive = pDataInputStream.readBoolean();
		this.mOrientation = pDataInputStream.readFloat();
		this.mAngularVelocity = pDataInputStream.readFloat();
		this.mShipPosX = pDataInputStream.readFloat();
		this.mShipPosY = pDataInputStream.readFloat();
		this.mShipVelocityX = pDataInputStream.readFloat();
		this.mShipVelocityY = pDataInputStream.readFloat();

		this.mBulletID = pDataInputStream.readInt();
		this.mBulletType = pDataInputStream.readInt();
		this.mBulletAngle = pDataInputStream.readFloat();
		this.mBulletPosX = pDataInputStream.readFloat();
		this.mBulletPosY = pDataInputStream.readFloat();
		this.mBulletVelocityX = pDataInputStream.readFloat();
		this.mBulletVelocityY = pDataInputStream.readFloat();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mFrameNum);
		
		pDataOutputStream.writeInt(this.mShipID);
		pDataOutputStream.writeInt(this.mHealth);
		pDataOutputStream.writeInt(this.mEnergy);
		pDataOutputStream.writeBoolean(this.mShieldActive);
		pDataOutputStream.writeFloat(this.mOrientation);
		pDataOutputStream.writeFloat(this.mAngularVelocity);
		pDataOutputStream.writeFloat(this.mShipPosX);
		pDataOutputStream.writeFloat(this.mShipPosY);
		pDataOutputStream.writeFloat(this.mShipVelocityX);
		pDataOutputStream.writeFloat(this.mShipVelocityY);
		
		pDataOutputStream.writeInt(this.mBulletID);
		pDataOutputStream.writeInt(this.mBulletType);
		pDataOutputStream.writeFloat(this.mBulletAngle);
		pDataOutputStream.writeFloat(this.mBulletPosX);
		pDataOutputStream.writeFloat(this.mBulletPosY);
		pDataOutputStream.writeFloat(this.mBulletVelocityX);
		pDataOutputStream.writeFloat(this.mBulletVelocityY);
	}
}
