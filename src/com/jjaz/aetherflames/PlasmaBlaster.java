package com.jjaz.aetherflames;

public class PlasmaBlaster extends ProjectileWeapon
{	
	public PlasmaBlaster()
	{
		COST = 50;
		COOLDOWN = 125;//in ms
		DAMAGE = 100; 
		BLAST_RADIUS = 0.0f;//in m
		LAUNCH_VELOCITY = 10;
		
		BULLET_SIZE = 10;
		BULLET_DENSITY = 0.1f;
		texture = AetherFlamesActivity.mPlasmaSphereTextureRegion;
		name = "PlasmaSphere";
		type = PLASMA_BLASTER;
	}
}
