package com.jjaz.aetherflames;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class HealthCrate
{
	public static final int HEALING_AMOUNT = 500;
	public static final int DROP_RATE = 20000; //ms
	public static final int DELAY = 100;
	
	private static final int HEALTH_SIZE = 40;
	
	public static Body spawn(int id, float x, float y)
	{
		final Sprite box = new Sprite(-HEALTH_SIZE, HEALTH_SIZE, HEALTH_SIZE, HEALTH_SIZE, AetherFlamesActivity.mHealthCrateTextureRegion, AetherFlamesActivity.mVertexBufferObjectManager);

		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.5f);
		final Body boxBody = PhysicsFactory.createBoxBody(AetherFlamesActivity.mPhysicsWorld, box, BodyType.DynamicBody, boxFixtureDef);
		boxBody.setTransform(x, y, 0);
		boxBody.setLinearDamping(10);
		boxBody.setAngularDamping(10);
		
		boxBody.setUserData("health" + " " + id);

		AetherFlamesActivity.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box, boxBody, true, true));

		AetherFlamesActivity.mScene.attachChild(box);
		
		return boxBody;
	}
}
