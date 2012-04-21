package com.jjaz.aetherflames;

public class Nyannon extends ProjectileWeapon
{
	public Nyannon()
	{
		COST = 500;
		COOLDOWN = 500;//in ms
		DAMAGE = 500; 
		BLAST_RADIUS = 2.5f;//in m
		LAUNCH_VELOCITY = 2.5f;
		
		BULLET_SIZE = 100;
		BULLET_DENSITY = 0.5f;
		
		texture = AetherFlamesActivity.mNyanTextureRegion;
		name = "NyanCat";
		type = NYANNON;
	}
}
