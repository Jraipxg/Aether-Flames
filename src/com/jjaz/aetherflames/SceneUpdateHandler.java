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
	protected static boolean purgeGame = false;
	
	final long timeBetweenExplosionCleanup = 350;//ms

	long timeOfLastHealthDrop;
	long timeOfLastExplosionCleanup;
	
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
			destroy(pc);
			
			return true;
		}
		return false;
	}
	
	void destroy(PhysicsConnector pc)
	{
		Body body = pc.getBody();
		IEntity shape = pc.getShape();

		AetherFlamesActivity.mPhysicsWorld.unregisterPhysicsConnector(pc);
		AetherFlamesActivity.mPhysicsWorld.destroyBody(body);
		AetherFlamesActivity.mScene.detachChild(shape);
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		PhysicsConnectorManager pcm = AetherFlamesActivity.mPhysicsWorld.getPhysicsConnectorManager();
		if(purgeGame)
		{
			for (Map.Entry<Integer,Ship> shipEntry : AetherFlamesActivity.ships.entrySet()) 
			{
				shipEntry.getValue().destroyShip(false);
			}
			
			while(pcm.size() > 0)
			{
				destroy(pcm.get(0));
			}

			purgeGame = false;
			AetherFlamesActivity.mScene.unregisterUpdateHandler(this);
			return;
		}
		
		AetherFlamesActivity.mPhysicsWorld.onUpdate(pSecondsElapsed);
		
		for(int i = 0; i < pcm.size(); i++)
		{
			PhysicsConnector pc = pcm.get(i);
			if(deleted(pc))
			{
				i--;
			}
		}
		
		for (Map.Entry<Integer,Ship> shipEntry : AetherFlamesActivity.ships.entrySet()) 
		{
			Ship ship = shipEntry.getValue();
			ship.updateStatusBars();
			ship.regen();
		}
		
		if(AetherFlamesActivity.ships.size() == 1)
		{
			Ship winner = AetherFlamesActivity.ships.entrySet().iterator().next().getValue();
			final Text winText = new Text(AetherFlamesActivity.CAMERA_WIDTH/2, AetherFlamesActivity.CAMERA_HEIGHT/2, AetherFlamesActivity.mFont, "Player " + winner.id + " wins!", new TextOptions(HorizontalAlign.CENTER), AetherFlamesActivity.mVertexBufferObjectManager);
			float textHeight = winText.getHeight();
			float textWidth = winText.getWidth();
			winText.setY(AetherFlamesActivity.CAMERA_HEIGHT/2 - textHeight/2);
			winText.setX(AetherFlamesActivity.CAMERA_WIDTH/2 - textWidth/2);
			AetherFlamesActivity.mScene.attachChild(winText);
			AetherFlamesActivity.mGameEngine.stop();
		}

		/*long timeSinceLastHealthDrop = System.currentTimeMillis() - timeOfLastHealthDrop;
		if(timeSinceLastHealthDrop > HealthCrate.DROP_RATE)
		{
			float spawnX = (float)(Math.random()*AetherFlamesActivity.WORLD_WIDTH*0.8 + AetherFlamesActivity.WORLD_WIDTH*0.1f);
			float spawnY = (float)(Math.random()*AetherFlamesActivity.WORLD_HEIGHT*0.8 + AetherFlamesActivity.WORLD_HEIGHT*0.1f);
			HealthCrate.spawn(spawnX, spawnY);
			timeOfLastHealthDrop = System.currentTimeMillis();
		}*/
		
		long timeSinceLastExplosionCleanup = System.currentTimeMillis() - timeOfLastExplosionCleanup;
		if(timeSinceLastExplosionCleanup > timeBetweenExplosionCleanup)
		{
			for(int i = 0; i < CollisionHandler.explosionSpriteList.size(); i++)
			{
				AetherFlamesActivity.mScene.detachChild(CollisionHandler.explosionSpriteList.get(i));
			}
			CollisionHandler.explosionSpriteList.clear();
			timeOfLastExplosionCleanup = System.currentTimeMillis();
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

}
