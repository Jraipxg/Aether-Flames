package com.jjaz.aetherflames;

import org.andengine.entity.sprite.Sprite;

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
		weaponSelectionSprite = new Sprite(0, 0, AetherFlamesActivity.WEAPON_SELECTION_BOX_SIZE, AetherFlamesActivity.WEAPON_SELECTION_BOX_SIZE, AetherFlamesActivity.mNyannonWeaponTextureRegion, AetherFlamesActivity.mVertexBufferObjectManager);
		weaponSelectionSprite.setAlpha(0.5f);
		weaponSelectionSprite.setVisible(false);
		AetherFlamesActivity.mWeaponSelection.attachChild(weaponSelectionSprite);
		explosion = AetherFlamesActivity.mExplosionTextureRegion;
		
		name = "NyanCat";
		type = NYANNON;
	}
}
