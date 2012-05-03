package com.jjaz.aetherflames;

import java.util.ArrayList;
import java.util.Map;

import org.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHandler implements ContactListener
{
	protected static ArrayList<Sprite> explosionSpriteList;
	
	public CollisionHandler()
	{
		explosionSpriteList = new ArrayList<Sprite>();
	}
	
	public void destroyBody(Body body)
	{
		body.setUserData("delete");
	}
	
	public static void drawExplosion(Vector2 explosionPoint, float size)
	{
		float explosionSpriteRadius = (size==0.0f)? 25.0f : size*AetherFlamesActivity.WORLD_TO_CAMERA;
		Sprite explosionSprite = new Sprite(explosionPoint.x - explosionSpriteRadius/2, explosionPoint.y - explosionSpriteRadius/2, explosionSpriteRadius, explosionSpriteRadius, AetherFlamesActivity.mExplosionTextureRegion, AetherFlamesActivity.mVertexBufferObjectManager);
		float explosionAngle = (float)(Math.random()*Math.PI*2);
		explosionSpriteList.add(explosionSprite);
		AetherFlamesActivity.mScene.attachChild(explosionSprite);
		explosionSprite.setRotation(explosionAngle);
	}
	
	void explode(Body body, int damage, float blastRadius, int bulletID)
	{
		//draw explosion image
		CollisionHandler.drawExplosion(body.getWorldCenter().cpy().mul(AetherFlamesActivity.WORLD_TO_CAMERA), blastRadius);
		
		//deal damage if the ship is my ship
		for (Map.Entry<Integer,Ship> shipEntry : AetherFlamesActivity.ships.entrySet())
		{
			Ship ship = shipEntry.getValue();
			if(AetherFlamesActivity.mGameStarted && ship.id == AetherFlamesActivity.myShipColor && ship.isWithinRange(body.getWorldCenter(), blastRadius))
			{
				synchronized (AetherFlamesActivity.mPhysicsWorld) {
					ship.damage(damage);
				}
				AetherFlamesActivity.mPhysicsWorld.registerCollision(bulletID, ship.id);
			}
		}
		
		//remove the body
		destroyBody(body);
	}
	
	@Override
	public void beginContact(Contact contact)
	{
		final Body bodyA = contact.getFixtureA().getBody();
		String [] tokensA = null;
		if(bodyA.getUserData() instanceof String)
		{
			String userDataA = (String) bodyA.getUserData();
			tokensA = userDataA.split(" ");
		}
		
		final Body bodyB = contact.getFixtureB().getBody();
		String [] tokensB = null;
		if(bodyB.getUserData() instanceof String)
		{
			String userDataB = (String) bodyB.getUserData();
			tokensB = userDataB.split(" ");
		}
		

		if(tokensA != null)
		{
			if(tokensA[0].equals("projectile"))
			{
				int damage = Integer.parseInt(tokensA[2]);
				float blastRadius = Float.parseFloat(tokensA[3]);
				int id = Integer.parseInt(tokensA[4]);
				explode(bodyA, damage, blastRadius, id);
			}
		}
		
		if(tokensB != null)
		{
			if(tokensB[0].equals("projectile"))
			{
				int damage = Integer.parseInt(tokensB[2]);
				float blastRadius = Float.parseFloat(tokensB[3]);
				int id = Integer.parseInt(tokensB[4]);
				explode(bodyB, damage, blastRadius, id);
			}
		}
		
		if(tokensA != null && tokensB != null)
		{
			if(tokensA[0].equals("ship") && tokensB[0].equals("health"))
			{
				int hID = Integer.parseInt(tokensB[1]);
				int sID = Integer.parseInt(tokensA[1]);
				AetherFlamesActivity.ships.get(sID).heal(HealthCrate.HEALING_AMOUNT);
				AetherFlamesActivity.mPhysicsWorld.registerHealthPackHit(hID, sID);
				destroyBody(bodyB);
			}
			if(tokensA[0].equals("health") && tokensB[0].equals("ship"))
			{
				int hID = Integer.parseInt(tokensA[1]);
				int sID = Integer.parseInt(tokensB[1]);
				AetherFlamesActivity.ships.get(sID).heal(HealthCrate.HEALING_AMOUNT);
				AetherFlamesActivity.mPhysicsWorld.registerHealthPackHit(hID, sID);
				destroyBody(bodyA);
			}
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		// empty
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		// empty
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// empty
	}

}
