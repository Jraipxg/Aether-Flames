package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:23:25 - 21.05.2011
 */
public class ConnectionEstablishedServerMessage extends ServerMessage implements AetherFlamesConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public short mPlayerNum;
	public short mMaxPlayers;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionEstablishedServerMessage() {

	}
	
	public ConnectionEstablishedServerMessage(short pPlayerNum, short pMaxPlayers) {
		this.mPlayerNum = pPlayerNum;
		this.mMaxPlayers = pMaxPlayers;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setPlayers(final short playerNum, final short maxPlayers) {
		this.mPlayerNum = playerNum;
		this.mMaxPlayers = maxPlayers;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED;
	}

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		this.mPlayerNum = pDataInputStream.readShort();
		this.mMaxPlayers = pDataInputStream.readShort();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeShort(this.mPlayerNum);
		pDataOutputStream.writeShort(this.mMaxPlayers);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
