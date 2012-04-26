package com.jjaz.aetherflames;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class ProjectileWeapon implements AetherFlamesConstants
{
	protected int COST;
	protected int COOLDOWN;//in ms
	protected int DAMAGE;
	protected float BLAST_RADIUS;//in m

	protected int BULLET_SIZE;
	protected float BULLET_DENSITY;
	protected float LAUNCH_VELOCITY;
	
	protected TiledTextureRegion texture;
	protected ITextureRegion explosion;
	protected String name;
	protected int type;
	
	protected Sprite weaponSelectionSprite;
	
	public int getType() {
		return type;
	}
	
	public Body fire(int id, Vector2 position, Vector2 initialVelocity, float angle)
	{
		AnimatedSprite bullet = new AnimatedSprite(-BULLET_SIZE, -BULLET_SIZE, BULLET_SIZE, BULLET_SIZE, texture, AetherFlamesActivity.mVertexBufferObjectManager);
		bullet.animate(70);
		
		FixtureDef bulletFixtureDef = PhysicsFactory.createFixtureDef(BULLET_DENSITY, 0.5f, 0.0f);
		Body bulletBody = PhysicsFactory.createCircleBody(AetherFlamesActivity.mPhysicsWorld, bullet, BodyType.DynamicBody, bulletFixtureDef);
		
		Vector2 barrelVelocity = initialVelocity.cpy();
		barrelVelocity.x += -(float)(LAUNCH_VELOCITY*Math.sin(angle));
		barrelVelocity.y += (float)(LAUNCH_VELOCITY*Math.cos(angle));
		bulletBody.setLinearVelocity(barrelVelocity);
		
		float launchAngle = (float) Math.atan2(barrelVelocity.y, barrelVelocity.x);
		bulletBody.setTransform(position.x, position.y, launchAngle);
		
		if(launchAngle > Math.PI/2 || (launchAngle < -Math.PI/2 && launchAngle > -Math.PI)) //fix graphic inversion
		{
			bullet.setScale(1, -1);
		}
		bullet.setRotation(MathUtils.radToDeg(launchAngle));

		bulletBody.setUserData("projectile " + name  + " " + DAMAGE  + " " + BLAST_RADIUS + " " + id);
		
		AetherFlamesActivity.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet, bulletBody, true, true));
		AetherFlamesActivity.mScene.attachChild(bullet);
		
		return bulletBody;
	}
}
