package com.jjaz.aetherflames;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class ProjectileWeapon
{
	protected int COST;
	protected int COOLDOWN;//in ms
	protected int DAMAGE;
	protected float BLAST_RADIUS;//in m
	
	protected int BULLET_SIZE;
	protected int LAUNCH_VELOCITY;
	
	protected ITextureRegion texture;
	protected String name;
	
	public void fire(Vector2 position, Vector2 initialVelocity, float angle)
	{
		Sprite bullet = new Sprite(-BULLET_SIZE, -BULLET_SIZE, BULLET_SIZE, BULLET_SIZE, texture, AetherFlamesActivity.mVertexBufferObjectManager);

		FixtureDef bulletFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.0f);
		Body bulletBody = PhysicsFactory.createCircleBody(AetherFlamesActivity.mPhysicsWorld, bullet, BodyType.DynamicBody, bulletFixtureDef);
		bulletBody.setTransform(position.x, position.y, 0);

		Vector2 barrelVelocity = initialVelocity.cpy();
		barrelVelocity.x += -(float)(LAUNCH_VELOCITY*Math.sin(angle));
		barrelVelocity.y += (float)(LAUNCH_VELOCITY*Math.cos(angle));
		bulletBody.setLinearVelocity(barrelVelocity);
		
		bulletBody.setUserData("projectile " + name  + " " + DAMAGE  + " " + BLAST_RADIUS);
		
		AetherFlamesActivity.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet, bulletBody, true, true));
		AetherFlamesActivity.mScene.attachChild(bullet);
	}
}
