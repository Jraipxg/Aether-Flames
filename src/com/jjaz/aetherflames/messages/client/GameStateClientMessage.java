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
	public GameStateClientMessage(final int frameNum,
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
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_CLIENT_GAME_STATE;
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
	}
}
