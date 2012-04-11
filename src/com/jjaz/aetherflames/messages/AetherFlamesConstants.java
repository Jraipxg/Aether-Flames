package com.jjaz.aetherflames.messages;

public interface AetherFlamesConstants {

	/* Server --> Client */
	public static final short FLAG_MESSAGE_SERVER_NEW_BULLET = 1;
	public static final short FLAG_MESSAGE_SERVER_SHIP_UPDATE = FLAG_MESSAGE_SERVER_NEW_BULLET + 1;
	public static final short FLAG_MESSAGE_SERVER_COLLISION = FLAG_MESSAGE_SERVER_SHIP_UPDATE + 1;
	public static final short FLAG_MESSAGE_SERVER_GAME_START = FLAG_MESSAGE_SERVER_COLLISION + 1;
	public static final short FLAG_MESSAGE_SERVER_GAME_END = FLAG_MESSAGE_SERVER_GAME_START + 1;

	/* Client --> Server */
	public static final short FLAG_MESSAGE_CLIENT_NEW_BULLET = 1;
	public static final short FLAG_MESSAGE_CLIENT_SHIP_UPDATE = FLAG_MESSAGE_CLIENT_NEW_BULLET + 1;
	public static final short FLAG_MESSAGE_CLIENT_COLLISION = FLAG_MESSAGE_CLIENT_SHIP_UPDATE + 1;
}