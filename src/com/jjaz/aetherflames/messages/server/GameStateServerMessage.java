package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;
import com.jjaz.aetherflames.messages.client.GameStateClientMessage;

public class GameStateServerMessage extends ServerMessage implements AetherFlamesConstants {
	
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
	
	/**
	 * C'tor
	 */
	public GameStateServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param frameNum The frame number of the message
	 */
	public GameStateServerMessage(final int frameNum,
			final int ShipID,
			final int Health,
			final int Energy,
			final boolean ShieldActive,
			final float Orientation,
			final float AngularVelocity,
			final float ShipPosX, final float ShipPosY,
			final float ShipVelocityX, final float ShipVelocityY) {
		this.mFrameNum = frameNum;
		this.mShipID = ShipID;
		this.mHealth = Health;
		this.mEnergy = Energy;
		this.mShieldActive = ShieldActive;
		this.mOrientation = Orientation;
		this.mAngularVelocity = AngularVelocity;
		this.mShipPosX = ShipPosX;
		this.mShipPosY = ShipPosY;
		this.mShipVelocityX = ShipVelocityX;
		this.mShipVelocityY = ShipVelocityY;
	}
	
	/**
	 * Setter.
	 * 
	 * @param sID Ship id.
	 * @param health Ship health.
	 * @param energy Ship energy.
	 * @param shieldActive Ship shield on.
	 * @param orientation Ship orientation (angle 0-360).
	 * @param posX Ship initial position x component.
	 * @param posY Ship initial position y component.
	 * @param velX Ship velocity x component.
	 * @param velY Ship velocity y component.
	 */	
	public void setShipState(final int sID, final int health,
							 final int energy, final boolean shieldActive,
			   			     final float orientation, final float omega,
			   			     final float posX, final float posY,
			   			  	 final float velX, final float velY) {
		this.mShipID = sID;
		this.mHealth = health;
		this.mEnergy = energy;
		this.mShieldActive = shieldActive;
		this.mOrientation = orientation;
		this.mAngularVelocity = omega;
		this.mShipPosX = posX;
		this.mShipPosY = posY;		
		this.mShipVelocityX = velX;
		this.mShipVelocityY = velY;
	}
	
	public void setFromClientMessage(GameStateClientMessage message) {
		this.mFrameNum = message.mFrameNum;
		this.mShipID = message.mShipID;
		this.mHealth = message.mHealth;
		this.mEnergy = message.mEnergy;
		this.mShieldActive = message.mShieldActive;
		this.mOrientation = message.mOrientation;
		this.mAngularVelocity = message.mAngularVelocity;
		this.mShipPosX = message.mShipPosX;
		this.mShipPosY = message.mShipPosY;		
		this.mShipVelocityX = message.mShipVelocityX;
		this.mShipVelocityY = message.mShipVelocityY;
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

		/*this.mBulletID = pDataInputStream.readInt();
		this.mBulletType = pDataInputStream.readInt();
		this.mBulletAngle = pDataInputStream.readFloat();
		this.mBulletPosX = pDataInputStream.readFloat();
		this.mBulletPosY = pDataInputStream.readFloat();
		this.mBulletVelocityX = pDataInputStream.readFloat();
		this.mBulletVelocityY = pDataInputStream.readFloat();*/
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
		
		/*pDataOutputStream.writeInt(this.mBulletID);
		pDataOutputStream.writeInt(this.mBulletType);
		pDataOutputStream.writeFloat(this.mBulletAngle);
		pDataOutputStream.writeFloat(this.mBulletPosX);
		pDataOutputStream.writeFloat(this.mBulletPosY);
		pDataOutputStream.writeFloat(this.mBulletVelocityX);
		pDataOutputStream.writeFloat(this.mBulletVelocityY);*/
	}
}
