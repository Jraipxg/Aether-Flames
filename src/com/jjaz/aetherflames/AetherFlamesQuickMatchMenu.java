package com.jjaz.aetherflames;

import java.util.ArrayList;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

public class AetherFlamesQuickMatchMenu extends MenuScene implements IOnMenuItemClickListener
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
	
	public AetherFlamesQuickMatchMenu()
	{
		super(AetherFlamesActivity.mCamera);
		
		//this.setPosition(10, AetherFlamesActivity.CAMERA_HEIGHT*0.2f);

		final IMenuItem anyMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_ANY_PLAYER, AetherFlamesActivity.mFontSmall, "Any", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		anyMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		anyMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(anyMenuItem);

		final IMenuItem oneMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_ONE_PLAYER, AetherFlamesActivity.mFontSmall, "One", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		oneMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		oneMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(oneMenuItem);

		final IMenuItem twoMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_TWO_PLAYER, AetherFlamesActivity.mFontSmall, "Two", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		twoMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		twoMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(twoMenuItem);

		final IMenuItem threeMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_THREE_PLAYER, AetherFlamesActivity.mFontSmall, "Three", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		threeMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		threeMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(threeMenuItem);

		final IMenuItem fourMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_FOUR_PLAYER, AetherFlamesActivity.mFontSmall, "Four", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		fourMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		fourMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(fourMenuItem);

		/*final IMenuItem fiveMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_FIVE_PLAYER, AetherFlamesActivity.mFontSmall, "Five Players", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		fiveMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		fiveMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(fiveMenuItem);

		final IMenuItem sixMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SIX_PLAYER, AetherFlamesActivity.mFontSmall, "Six Players", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		sixMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		sixMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(sixMenuItem);

		final IMenuItem sevenMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SEVEN_PLAYER, AetherFlamesActivity.mFontSmall, "Seven Players", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		sevenMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		sevenMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(sevenMenuItem);

		final IMenuItem eightMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_EIGHT_PLAYER, AetherFlamesActivity.mFontSmall, "Eight Players", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		eightMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		eightMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(eightMenuItem);*/

		final IMenuItem backMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_BACK, AetherFlamesActivity.mFontSmall, "Back", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		backMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		backMenuItem.setAlpha(AetherFlamesMainMenu.MENU_ALPHA);
		this.addMenuItem(backMenuItem);
		
		//this.setMenuAnimator(new SlideMenuAnimator());
		this.buildAnimations();
		this.setBackgroundEnabled(false);
		this.setOnMenuItemClickListener(this);
		
		this.setAlpha(0.5f);
	}	

	void searchForGame(int numPlayersDesired)
	{
		AetherFlamesActivity.mScene.clearChildScene();
		if(numPlayersDesired == 0) //debug
		{

			ArrayList<Integer> colors = new ArrayList<Integer>();
			colors.add(Ship.WHITE_SHIP);
			colors.add(Ship.RED_SHIP);
			colors.add(Ship.ORANGE_SHIP);
			colors.add(Ship.YELLOW_SHIP);
			colors.add(Ship.GREEN_SHIP);
			colors.add(Ship.BLUE_SHIP);
			colors.add(Ship.PURPLE_SHIP);
			colors.add(Ship.BLACK_SHIP);
			
			AetherFlamesActivity.createGame(colors, AetherFlamesActivity.myShipColor);
			AetherFlamesActivity.afa.startGame();
			AetherFlamesMainMenu.revertMenu();
			return;
		}
		GameServer gs = MatchmakerClient.requestSingleServer(numPlayersDesired);
		AetherFlamesActivity.afa.initClient(gs);
		AetherFlamesMainMenu.revertMenu();
	}
	
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY)
	{
		int id = pMenuItem.getID();
		if(id <= 8)
		{
			searchForGame(id);
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
