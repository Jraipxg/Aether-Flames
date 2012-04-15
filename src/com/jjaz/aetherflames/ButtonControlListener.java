package com.jjaz.aetherflames;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;

public class ButtonControlListener implements IOnScreenControlListener
{
	private Ship mShip;
	
	public ButtonControlListener(Ship ship)
	{
		mShip = ship;
	}
	
	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) 
	{
		if(pValueX < 0) //left button
		{
			mShip.fireWeapon();
		}
		if(pValueX > 0) //right button
		{
			mShip.fireWeapon();
		}
		if(pValueY < 0) //up button
		{
			mShip.fireWeapon();
		}
		if(pValueY > 0) //down button
		{
			mShip.fireWeapon();
		}
	}
}
