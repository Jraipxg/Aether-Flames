package com.jjaz.aetherflames;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;

public class ShipMovementControlListener implements IOnScreenControlListener
{
	private Ship mShip;
	
	public ShipMovementControlListener(Ship ship)
	{
		mShip = ship;
	}
	
	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) 
	{
		mShip.fireThrusters(pValueY);
		
		mShip.turn(pValueX);
	}
}
