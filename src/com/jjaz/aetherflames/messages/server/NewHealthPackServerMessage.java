package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class NewHealthPackServerMessage extends ServerMessage implements AetherFlamesConstants {

	public int mID;
	public float mPosX;
	public float mPosY;
	
	/**
	 * C'tor
	 */
	public NewHealthPackServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param id The id of the health pack
	 * @param posX The X position of the new health pack
	 * @param posY The Y position of the new health pack
	 */
	public NewHealthPackServerMessage(final int id, final float posX, final float posY) {
		this.mID = id;
		this.mPosX = posX;
		this.mPosY = posY;
	}
	
	/**
	 * Setter.
	 * 
	 * @param id Health pack id.
	 * @param posX Health pack initial position x component.
	 * @param posY Health pack initial position y component.
	 */	
	public void setNewHealthPack (final int id, final float posX, final float posY) {
		this.mID = id;
		this.mPosX = posX;
		this.mPosY = posY;
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_NEW_HEALTH_PACK;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mID = pDataInputStream.readInt();
		this.mPosX = pDataInputStream.readFloat();
		this.mPosY = pDataInputStream.readFloat();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mID);
		pDataOutputStream.writeFloat(this.mPosX);
		pDataOutputStream.writeFloat(this.mPosY);
	}	
}
