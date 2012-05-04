package com.jjaz.aetherflames;

import java.net.UnknownHostException;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.util.color.Color;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.opengl.GLES20;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AetherFlamesMainMenu extends MenuScene implements IOnMenuItemClickListener
{
	protected static final float MENU_ALPHA = 1.0f;
	protected static final Color mNormalColor = new Color(0.75f, 0.75f, 0.75f);
	protected static final Color mRolloverColor = new Color(0.94f, 0.64f, 0.24f);
	
	protected static final int MENU_QUICK_MATCH = 0;
	protected static final int MENU_SERVER_LIST = 1;
	protected static final int MENU_CREATE_SERVER = 2;
	protected static final int MENU_MANUAL_JOIN = 3;
	protected static final int MENU_QUIT = 4;
	
	protected static Sprite title;
	protected static MenuScene quickMatchMenu;
	protected static MenuScene serverListMenu;
	protected static MenuScene createServerMenu;

	public AetherFlamesMainMenu()
	{
		super(AetherFlamesActivity.mCamera);
		
		title = new Sprite(0, 0, 960, 540, AetherFlamesActivity.mTitleTextureRegion, AetherFlamesActivity.mVertexBufferObjectManager);
		AetherFlamesActivity.mScene.attachChild(title);
		
		this.setPosition(10, AetherFlamesActivity.CAMERA_HEIGHT*0.16f);

		final IMenuItem quickMatchMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUICK_MATCH, AetherFlamesActivity.mFont, "Quick Match", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		quickMatchMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		quickMatchMenuItem.setAlpha(MENU_ALPHA);
		this.addMenuItem(quickMatchMenuItem);

		/*final IMenuItem serverListMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SERVER_LIST, AetherFlamesActivity.mFont, "Servers", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		serverListMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		serverListMenuItem.setAlpha(MENU_ALPHA);
		this.addMenuItem(serverListMenuItem);*/

		final IMenuItem createServerMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_CREATE_SERVER, AetherFlamesActivity.mFont, "Create Server", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		createServerMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		createServerMenuItem.setAlpha(MENU_ALPHA);
		this.addMenuItem(createServerMenuItem);

		final IMenuItem manualJoinMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_MANUAL_JOIN, AetherFlamesActivity.mFont, "Manual Game Join", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		manualJoinMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		manualJoinMenuItem.setAlpha(MENU_ALPHA);
		this.addMenuItem(manualJoinMenuItem);

		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, AetherFlamesActivity.mFont, "Quit", AetherFlamesActivity.mVertexBufferObjectManager), AetherFlamesMainMenu.mRolloverColor, AetherFlamesMainMenu.mNormalColor);
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		quitMenuItem.setAlpha(MENU_ALPHA);
		this.addMenuItem(quitMenuItem);
		
		SlideMenuAnimator animator = new SlideMenuAnimator();
		this.setMenuAnimator(animator);
		
		this.buildAnimations();
		this.setBackgroundEnabled(false);
		this.setOnMenuItemClickListener(this);
		
		this.setAlpha(0.5f);
	}
	
	private void handleQuickMatch()
	{
		if(quickMatchMenu == null)
		{
			quickMatchMenu = new AetherFlamesQuickMatchMenu();
		} 
		quickMatchMenu.setPosition(10, AetherFlamesActivity.CAMERA_HEIGHT*0.2f);
		this.setChildSceneModal(AetherFlamesMainMenu.quickMatchMenu);
	}
	
	private void handleServerList()
	{
		//go to server list menu
		if(serverListMenu == null)
		{
			serverListMenu = new AetherFlamesServerListMenu();
		} 
		serverListMenu.setPosition(10, AetherFlamesActivity.CAMERA_HEIGHT*0.2f);
		this.setChildSceneModal(AetherFlamesMainMenu.serverListMenu);
	}
	
	private void handleCreateServer()
	{
		if(createServerMenu == null)
		{
			createServerMenu = new AetherFlamesCreateServerMenu();
		} 
		createServerMenu.setPosition(10, AetherFlamesActivity.CAMERA_HEIGHT*0.2f);
		this.setChildSceneModal(AetherFlamesMainMenu.createServerMenu);
	}
	
	public void showIPInput() {
		AetherFlamesActivity.afa.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(AetherFlamesActivity.afa);

				alert.setTitle("Manual Connection");
				alert.setMessage("Enter Server IP Address");

				final EditText editText = new EditText(AetherFlamesActivity.afa);
				editText.setTextSize(20f);
				editText.setText("");
				editText.setGravity(Gravity.CENTER_HORIZONTAL);

				alert.setView(editText);

				alert.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						//setText(editText.getText().toString());
						AetherFlamesActivity.afa.mServerIP = editText.getText().toString();
						GameServer gs = null;
						try
						{
							gs = new GameServer(WifiUtils.getWifiIPv4Address(AetherFlamesActivity.afa), "???", 0);
							gs.setNumPlayers(1);
						}
						catch (UnknownHostException e)
						{
							e.printStackTrace();
						}
						AetherFlamesActivity.afa.initClient(gs);
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

				final AlertDialog dialog = alert.create();
				dialog.setOnShowListener(new OnShowListener() {
					@Override
					public void onShow(DialogInterface dialog) {
						editText.requestFocus();
						final InputMethodManager imm = (InputMethodManager) AetherFlamesActivity.afa.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
					}
				});
				dialog.show();
			}
		});
	}
	
	private void handleManualJoin()
	{
		showIPInput();
	}
	
	private void handleQuit()
	{
		AetherFlamesActivity.quit();
	}
	
	protected static void revertMenu()
	{
		//AetherFlamesActivity.mScene.clearChildScene();
		AetherFlamesActivity.afa.createMenu();
		//AetherFlamesActivity.destroyGame();
		//AetherFlamesActivity.mMenuScene.clearChildScene();
	}
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY)
	{
		switch (pMenuItem.getID())
		{
			case MENU_QUICK_MATCH:
				handleQuickMatch();
				return true;
			case MENU_SERVER_LIST:
				handleServerList();
				return true;
			case MENU_CREATE_SERVER:
				handleCreateServer();
				return true;
			case MENU_MANUAL_JOIN:
				handleManualJoin();
				return true;
			case MENU_QUIT:
				handleQuit();
				return true;
			default:
				return false;
		}
	}
}
