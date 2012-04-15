package com.jjaz.aetherflames;

import java.util.Map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHandler implements ContactListener
{
	public void destroyBody(Body body)
	{
		body.setUserData("delete");
	}
	
	void explode(Body body, int damage, float blastRadius)
	{
		for (Map.Entry<Integer,Ship> shipEntry : AetherFlamesActivity.ships.entrySet()) 
		{
			Ship ship = shipEntry.getValue();
			if(ship.isWithinRange(body.getWorldCenter(), blastRadius))
			{
				ship.damage(damage);
			}
		}
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
				explode(bodyA, damage, blastRadius);
			}
		}
		
		if(tokensB != null)
		{
			if(tokensB[0].equals("projectile"))
			{
				int damage = Integer.parseInt(tokensB[2]);
				float blastRadius = Float.parseFloat(tokensB[3]);
				explode(bodyB, damage, blastRadius);
			}
		}
		
		if(tokensA != null && tokensB != null)
		{
			if(tokensA[0].equals("ship") && tokensB[0].equals("health"))
			{
				AetherFlamesActivity.ships.get(Integer.parseInt(tokensA[1])).heal(HealthCrate.HEALING_AMOUNT);
				destroyBody(bodyB);
			}
			if(tokensA[0].equals("health") && tokensB[0].equals("ship"))
			{
				AetherFlamesActivity.ships.get(Integer.parseInt(tokensB[1])).heal(HealthCrate.HEALING_AMOUNT);
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
