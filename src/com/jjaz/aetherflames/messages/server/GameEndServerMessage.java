package com.jjaz.aetherflames.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import com.jjaz.aetherflames.AetherFlamesConstants;

public class GameEndServerMessage extends ServerMessage implements AetherFlamesConstants {

	public boolean mEnd;
	public int mWinner;
	
	/**
	 * C'tor
	 */
	public GameEndServerMessage() {
		// intentionally empty
	}
	
	/**
	 * C'tor
	 * 
	 * @param end True to end game.
	 * @param winner Ship ID of winner.
	 */
	public GameEndServerMessage(final boolean end, final int winner) {
		this.mEnd = end;
		this.mWinner = winner;
	}
	
	/**
	 * Setter.
	 * 
	 * @param end True to end game.
	 * @param winner Ship ID of winner.
	 */	
	public void setNewBullet(final boolean end, final int winner) {
		this.mEnd = end;
		this.mWinner = winner;
	}
	
	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_GAME_END;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mEnd = pDataInputStream.readBoolean();
		this.mWinner = pDataInputStream.readInt();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeBoolean(this.mEnd);
		pDataOutputStream.writeInt(this.mWinner);
	}
	
}
