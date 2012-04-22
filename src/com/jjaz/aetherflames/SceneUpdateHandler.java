package com.jjaz.aetherflames;

import java.util.Map;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.util.HorizontalAlign;

import com.badlogic.gdx.physics.box2d.Body;

public class SceneUpdateHandler implements IUpdateHandler
{
	final long timeBetweenHealthDrops = 1000; //ms
	
	long timeOfLastHealthDrop;
	
	public SceneUpdateHandler()
	{
		timeOfLastHealthDrop = 0;
	}
	
	boolean deleted(PhysicsConnector pc)
	{
		Body body = pc.getBody();
		if(body == null)
		{
			return false;
		}
		Object userData = body.getUserData();
		if(userData == null)
		{
			return false;
		}
		if(userData.equals("delete"))
		{
			IEntity shape = pc.getShape();

			AetherFlamesActivity.mPhysicsWorld.unregisterPhysicsConnector(pc);
			AetherFlamesActivity.mPhysicsWorld.destroyBody(body);
			AetherFlamesActivity.mScene.detachChild(shape);
			
			return true;
		}
		return false;
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		AetherFlamesActivity.mPhysicsWorld.onUpdate(pSecondsElapsed);
		PhysicsConnectorManager pcm = AetherFlamesActivity.mPhysicsWorld.getPhysicsConnectorManager();
		for(int i = 0; i < pcm.size(); i++)
		{
			PhysicsConnector pc = pcm.get(i);
			if(deleted(pc))
			{ 
				continue;
			}
		}
		
		for (Map.Entry<Integer,Ship> shipEntry : AetherFlamesActivity.ships.entrySet()) 
		{
			Ship ship = shipEntry.getValue();
			ship.updateStatusBars();
			ship.regen();
		}

		long timeSinceLastHealthDrop = System.currentTimeMillis() - timeOfLastHealthDrop;
		if(timeSinceLastHealthDrop > HealthCrate.DROP_RATE)
		{
			float spawnX = (float)(Math.random()*AetherFlamesActivity.WORLD_WIDTH*0.8 + AetherFlamesActivity.WORLD_WIDTH*0.1f);
			float spawnY = (float)(Math.random()*AetherFlamesActivity.WORLD_HEIGHT*0.8 + AetherFlamesActivity.WORLD_HEIGHT*0.1f);
			HealthCrate.spawn(spawnX, spawnY);
			timeOfLastHealthDrop = System.currentTimeMillis();
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

}
