package com.jjaz.aetherflames.messages.phone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;
import org.andengine.util.debug.Debug;

import com.jjaz.aetherflames.AetherFlamesConstants;
import com.jjaz.aetherflames.GameServer;

public class CurrentPlayerCountPhoneMessage extends ClientMessage implements AetherFlamesConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public GameServer mServer;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public CurrentPlayerCountPhoneMessage() {
		// intentionally empty
	}
	
	public CurrentPlayerCountPhoneMessage(final GameServer pServer)
	{
		mServer = pServer;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setServer(final GameServer pServer) {
		mServer = pServer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_PHONE_CURRENT_PLAYER_COUNT;
	}

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		ObjectInputStream reader = new ObjectInputStream(pDataInputStream);
		try {
			this.mServer = (GameServer) reader.readObject();
		} catch (ClassNotFoundException e) {
			Debug.e(e);
		}
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		ObjectOutputStream writer = new ObjectOutputStream(pDataOutputStream);
		writer.writeObject(this.mServer);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
