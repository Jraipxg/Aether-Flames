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

	boolean leftAlreadyPressed;
	boolean rightAlreadyPressed;
	@Override
	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) 
	{
		if (mShip != null && mShip.getHealth() > 0) {
			if(pValueX < 0) //left button
			{
				if(!leftAlreadyPressed)
				{
					mShip.previousWeapon();
				}
				leftAlreadyPressed = true;
			}
			else
			{
				leftAlreadyPressed = false;
			}

			if(pValueX > 0) //right button
			{
				if(!rightAlreadyPressed)
				{
					mShip.nextWeapon();
				}
				rightAlreadyPressed = true;
			}
			else
			{
				rightAlreadyPressed = false;
			}

			if(pValueY < 0) //up button
			{
				mShip.activateShields();
			}
			else
			{
				mShip.deactivateShields();
			}

			if(pValueY > 0) //down button
			{
				mShip.fireWeapon();
			}
		}
	}
}
