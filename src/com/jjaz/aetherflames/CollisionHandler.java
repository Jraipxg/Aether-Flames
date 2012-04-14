package com.jjaz.aetherflames;

import org.andengine.engine.Engine;
import org.andengine.engine.Engine.EngineLock;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHandler implements ContactListener
{
	
	
	private PhysicsWorld mPhysicsWorld;
	private Scene mScene;
	private Engine mEngine;

	public CollisionHandler(PhysicsWorld world, Scene scene, Engine engine)
	{
		mPhysicsWorld = world;
		mScene = scene;
		mEngine = engine;
	}
	
	public void destroyBody(Body body)
	{
		body.setUserData("delete");
	}
	
	@Override
	public void beginContact(Contact contact)
	{
		final Body BodyA = contact.getFixtureA().getBody();
		final Body BodyB = contact.getFixtureB().getBody();

		if(BodyA.getUserData() == "bullet")
		{
			destroyBody(BodyA);
		}
		
		if(BodyB.getUserData() == "bullet")
		{
			destroyBody(BodyB);
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// TODO Auto-generated method stub

	}

}
