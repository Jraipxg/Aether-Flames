package com.jjaz.aetherflames;

public interface AetherFlamesConstants {

	/* Bullet Types */
	public static final int PLASMA_BLASTER = 0;
//	public static final int MINE = PLASMA_BLASTER + 1;
	
	/* General networking constants */
	public static final int SERVER_PORT = 4444;
	public static final short PROTOCOL_VERSION = 1;
	
	/* Server --> Client */
	public static final short FLAG_MESSAGE_SERVER_NEW_BULLET = 1;
	public static final short FLAG_MESSAGE_SERVER_SHIP_UPDATE = FLAG_MESSAGE_SERVER_NEW_BULLET + 1;
	public static final short FLAG_MESSAGE_SERVER_COLLISION = FLAG_MESSAGE_SERVER_SHIP_UPDATE + 1;
	public static final short FLAG_MESSAGE_SERVER_GAME_START = FLAG_MESSAGE_SERVER_COLLISION + 1;
	public static final short FLAG_MESSAGE_SERVER_GAME_END = FLAG_MESSAGE_SERVER_GAME_START + 1;
	public static final short FLAG_MESSAGE_SERVER_DONE = FLAG_MESSAGE_SERVER_GAME_END + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_CLOSE = Short.MIN_VALUE;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED = FLAG_MESSAGE_SERVER_CONNECTION_CLOSE + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH = FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_PONG = FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH + 1;

	
	/* Client --> Server */
	public static final short FLAG_MESSAGE_CLIENT_NEW_BULLET = 1;
	public static final short FLAG_MESSAGE_CLIENT_SHIP_UPDATE = FLAG_MESSAGE_CLIENT_NEW_BULLET + 1;
	public static final short FLAG_MESSAGE_CLIENT_COLLISION = FLAG_MESSAGE_CLIENT_SHIP_UPDATE + 1;
	public static final short FLAG_MESSAGE_CLIENT_DONE = FLAG_MESSAGE_CLIENT_COLLISION + 1;
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE = Short.MIN_VALUE;
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH = FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE + 1;
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_PING = FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH + 1;
}