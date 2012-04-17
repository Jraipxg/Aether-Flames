package com.jjaz.aetherflames;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;

import com.badlogic.gdx.math.Vector2;

public class ShipMovementControlListener implements IOnScreenControlListener, IAnalogOnScreenControlListener
{
	private Ship mShip;
	private ClientGameManager mClientGameManager;
	
	public ShipMovementControlListener(Ship ship, ClientGameManager pCGM)
	{
		mShip = ship;
		mClientGameManager = pCGM;
	}
	
	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) 
	{
		//these were for 
		//mShip.fireThrusters(pValueY);
		//mShip.turn(pValueX);
		
		if(pValueX != 0 || pValueY != 0)
		{
			//mShip.turnInstantAndThrust(new Vector2(pValueX, pValueY));
			mClientGameManager.queueTurnInstantAndThrustEvent(new Vector2(pValueX, pValueY));
		}
	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl)
	{
		// TODO Auto-generated method stub
		
	}
}
