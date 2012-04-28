package com.jjaz.aetherflames.messages.matchmaker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class FreeServerMatchmakerMessage extends ServerMessage implements AetherFlamesConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private String mServerName;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public FreeServerMatchmakerMessage() {

	}

	public FreeServerMatchmakerMessage(final String pServerName) {
		this.mServerName = pServerName;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getProtocolVersion() {
		return this.mServerName;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_MATCHMAKER_FREE_SERVER;
	}

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		this.mServerName = pDataInputStream.readLine();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeChars(this.mServerName);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
