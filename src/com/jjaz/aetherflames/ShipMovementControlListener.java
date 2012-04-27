package com.jjaz.aetherflames;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;

import com.badlogic.gdx.math.Vector2;

public class ShipMovementControlListener implements IOnScreenControlListener, IAnalogOnScreenControlListener
{
	private Ship mShip;
	
	public ShipMovementControlListener(Ship ship)
	{
		mShip = ship;
	}
	
	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) 
	{
		//these were for 
		//mShip.fireThrusters(pValueY);
		//mShip.turn(pValueX);
		
		if(AetherFlamesActivity.mGameStarted && mShip != null && mShip.getHealth() > 0 && (pValueX != 0 || pValueY != 0))
		{
			mShip.turnInstantAndThrust(new Vector2(pValueX, pValueY));
		}
	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl)
	{
		// TODO Auto-generated method stub
		
	}
}
