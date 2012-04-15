package com.jjaz.aetherflames;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Mine
{
	protected int COST;
	protected int COOLDOWN;//in ms
	protected int DAMAGE;
	protected float BLAST_RADIUS;//in m
	
	protected int MINE_SIZE;//in m, this is also the trigger radius
	
	protected ITextureRegion texture;
	protected String name;
	
	public void launch(Vector2 position)
	{
		Sprite mine = new Sprite(-MINE_SIZE, -MINE_SIZE, MINE_SIZE, MINE_SIZE, texture, AetherFlamesActivity.mVertexBufferObjectManager);

		FixtureDef bulletFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.0f);
		Body bulletBody = PhysicsFactory.createCircleBody(AetherFlamesActivity.mPhysicsWorld, mine, BodyType.DynamicBody, bulletFixtureDef);
		bulletBody.setTransform(position.x, position.y, 0);
		
		bulletBody.setUserData("mine " + name  + " " + DAMAGE + " " + BLAST_RADIUS);
		
		AetherFlamesActivity.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mine, bulletBody, true, true));
		AetherFlamesActivity.mScene.attachChild(mine);
	}
}
