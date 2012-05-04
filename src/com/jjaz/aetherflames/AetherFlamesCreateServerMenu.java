package com.jjaz.aetherflames;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

public class AetherFlamesCreateServerMenu extends MenuScene implements IOnMenuItemClickListener
{
	protected static final int MENU_ANY_PLAYER = 0;
	protected static final int MENU_ONE_PLAYER = 1;
	protected static final int MENU_TWO_PLAYER = 2;
	protected static final int MENU_THREE_PLAYER = 3;
	protected static final int MENU_FOUR_PLAYER = 4;
	protected static final int MENU_FIVE_PLAYER = 5;
	protected static final int MENU_SIX_PLAYER = 6;
	protected static final int MENU_SEVEN_PLAYER = 7;
	protected static final int MENU_EIGHT_PLAYER = 8;

	protected static final int MENU_BACK = 9;
	protected static Text winText2;
	
	public AetherFlamesCreateServerMenu()
	{
		super(AetherFlamesActivity.mCamera);
		
		Color normalColor = new Color(1, 1, 1);
		Color rolloverColor = new Color(0.94f, 0.64f, 0.24f);

		/*final IMenuItem anyMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_ANY_PLAYER, AetherFlamesActivity.mFontSmall, "Any", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		anyMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(anyMenuItem);

		final IMenuItem oneMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_ONE_PLAYER, AetherFlamesActivity.mFontSmall, "One", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		oneMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(oneMenuItem);*/

		final IMenuItem twoMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_TWO_PLAYER, AetherFlamesActivity.mFontSmall, "Two", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		twoMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(twoMenuItem);

		final IMenuItem threeMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_THREE_PLAYER, AetherFlamesActivity.mFontSmall, "Three", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		threeMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(threeMenuItem);

		final IMenuItem fourMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_FOUR_PLAYER, AetherFlamesActivity.mFontSmall, "Four", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		fourMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(fourMenuItem);

		final IMenuItem fiveMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_FIVE_PLAYER, AetherFlamesActivity.mFontSmall, "Five", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		fiveMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(fiveMenuItem);

		final IMenuItem sixMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SIX_PLAYER, AetherFlamesActivity.mFontSmall, "Six", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		sixMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(sixMenuItem);

		/*final IMenuItem sevenMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SEVEN_PLAYER, AetherFlamesActivity.mFontSmall, "Seven", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		sevenMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(sevenMenuItem);

		final IMenuItem eightMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_EIGHT_PLAYER, AetherFlamesActivity.mFontSmall, "Eight", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		eightMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(eightMenuItem);*/

		final IMenuItem backMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_BACK, AetherFlamesActivity.mFontSmall, "Back", AetherFlamesActivity.mVertexBufferObjectManager), rolloverColor, normalColor);
		backMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.addMenuItem(backMenuItem);
		
		//this.setMenuAnimator(new SlideMenuAnimator());
		this.buildAnimations();
		this.setBackgroundEnabled(false);
		this.setOnMenuItemClickListener(this);
		
		this.setAlpha(0.5f);
	}	

	void becomeServer(int numPlayersDesired)
	{
		AetherFlamesActivity.mScene.clearChildScene();
		GameServer gs = null;
		try
		{
			gs = new GameServer(WifiUtils.getWifiIPv4Address(AetherFlamesActivity.afa), "???", numPlayersDesired);
			gs.setNumPlayers(1);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		AetherFlamesActivity.afa.initServerAndClient(gs);
		MatchmakerClient.startServer(gs);
		//AetherFlamesMainMenu.revertMenu();
	}
	
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY)
	{
		int id = pMenuItem.getID();
		if(id <= 8)
		{
			becomeServer(id);
			return true;
		}
		else if(id == MENU_BACK)
		{
			this.back();
			return true;
		}
		else
		{
			return false;
		}
	}
}
