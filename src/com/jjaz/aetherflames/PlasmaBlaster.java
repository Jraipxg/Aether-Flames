package com.jjaz.aetherflames;

import org.andengine.entity.sprite.Sprite;

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
		weaponSelectionSprite = new Sprite(0, 0, AetherFlamesActivity.WEAPON_SELECTION_BOX_SIZE, AetherFlamesActivity.WEAPON_SELECTION_BOX_SIZE, AetherFlamesActivity.mPlasmaBlasterWeaponTextureRegion, AetherFlamesActivity.mVertexBufferObjectManager);
		weaponSelectionSprite.setAlpha(0.5f);
		weaponSelectionSprite.setVisible(false);
		AetherFlamesActivity.mWeaponSelection.attachChild(weaponSelectionSprite);
		explosion = AetherFlamesActivity.mExplosionTextureRegion;
		
		name = "PlasmaSphere";
		type = PLASMA_BLASTER;
	}
}
