package com.jjaz.aetherflames;

public class PlasmaBlaster extends ProjectileWeapon
{	
	public PlasmaBlaster()
	{
		COST = 50;
		COOLDOWN = 125;//in ms
		DAMAGE = 100; 
		BLAST_RADIUS = 0.001f;//in m
		LAUNCH_VELOCITY = 10;
		
		BULLET_SIZE = 10;
		
		texture = AetherFlamesActivity.mPlasmaBlastTextureRegion;
		name = "PlasmaBall";
		type = PLASMA_BLASTER;
	}
}
