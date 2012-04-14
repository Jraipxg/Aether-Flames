package com.jjaz.aetherflames;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;

public class SceneUpdateHandler implements IUpdateHandler
{
	private PhysicsWorld mPhysicsWorld;
	private Scene mScene;
	private Engine mEngine;
	
	public SceneUpdateHandler(PhysicsWorld world, Scene scene, Engine engine)
	{
		mPhysicsWorld = world;
		mScene = scene;
		mEngine = engine;
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		this.mPhysicsWorld.onUpdate(pSecondsElapsed);
		PhysicsConnectorManager pcm = this.mPhysicsWorld.getPhysicsConnectorManager();
		for(int i = 0; i < pcm.size(); i++)
		{
			Body body = pcm.get(i).getBody();
			if(body == null)
			{
				continue;
			}
			Object userData = body.getUserData();
			if(userData == null)
			{
				continue;
			}
			if(userData.equals("delete"))
			{
				//final EngineLock engineLock = AetherFlamesActivity.this.mEngine.getEngineLock();
				//engineLock.lock();
				PhysicsConnector bulletPhysicsConnector = pcm.get(i);
				IEntity shape = bulletPhysicsConnector.getShape();

				this.mPhysicsWorld.unregisterPhysicsConnector(bulletPhysicsConnector);
				this.mPhysicsWorld.destroyBody(body);

				//this.mScene.unregisterTouchArea(BodyA);
				this.mScene.detachChild(shape);
				
				//System.gc();
				//engineLock.unlock();
			}
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

}
