package com.mygdx.space;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Constants;
public class PlayerShip implements Ship{

private Texture texture;
private Sprite sprite;
public boolean shotOneFired;
public boolean shotTwoFired;
private Sprite shotOne;
private Sprite shotTwo;
private boolean alive;
private boolean shotPressed;
Texture shotTexture;

/**
 * Creates player ship at a default position for debugging
 */
	public PlayerShip()
	{
		alive = true;
		texture = new Texture("ship.png");
		sprite = new Sprite(texture);
		sprite.setPosition(Gdx.graphics.getWidth()/2, 5);
		shotOneFired = false;
		shotTwoFired = false;
		shotTexture = new Texture("shot.png");
		shotOne = new Sprite(shotTexture);
		shotTwo = new Sprite(shotTexture);
	}
	
	/*
	 * Creates player ship at a given x position
	 */
	public PlayerShip(float xPos)
	{
		alive = true;
		texture = new Texture("ship.png");
		sprite = new Sprite(texture);
		sprite.setPosition(xPos, 5);
		shotOneFired = false;
		shotTwoFired = false;
		Texture shotTexture = new Texture("shot.png");
		shotOne = new Sprite(shotTexture);
		shotTwo = new Sprite(shotTexture);
	}
	/*
	 * Move ship according to input retrieved from keyboard
	 */
	public void move() {
		float sprMoveSpeed = 250 * Gdx.graphics.getDeltaTime();
		
		if ((Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) && sprite.getX() > 90) sprite.translate(-sprMoveSpeed,0);
		if ((Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) && sprite.getX() < Gdx.graphics.getWidth()-100) sprite.translate(sprMoveSpeed,0);
		if ((Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.DPAD_UP)) && sprite.getY() < 50) sprite.translate(0,sprMoveSpeed);
		if ((Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) && sprite.getY() > 5) sprite.translate(0,-sprMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.SPACE)) shotPressed = true;
	}

	@Override
	/*
	 * Returns sprite of player ship
	 */
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	/*
	 * Handles logic for ship's firing. Ship is allowed to have two active shots at any given time
	 * Shots become inactive when traveling off-screen and when colliding with an object
	 */
	public void shoot(SpriteBatch batch) {
		if (!shotOneFired && Gdx.input.isKeyPressed(Keys.SPACE)) //If shot one not fired and fire button is pressed
		{
			shotOneFired = true; //Shot one has been fired
			Space.shotsTaken++; //For accuracy calculation
			shotOne.setPosition(sprite.getX(), sprite.getY()+30); //Initial position for shot one
		}
		else if (shotOneFired && !shotTwoFired && Gdx.input.isKeyPressed(Keys.SPACE) && shotOne.getY() > sprite.getY()+200) //If shot one fired, but shot two not yet fired
		{
			Space.shotsTaken++; //For accuracy calculation
			shotTwoFired = true;
			shotTwo.setPosition(sprite.getX(), sprite.getY()+30); //Set position for second shot sprite
		}
		
		if (shotOne.getY() > Gdx.graphics.getHeight()) shotOneFired = false; //If shot one off screen
		if (shotTwo.getY() > Gdx.graphics.getHeight()) shotTwoFired = false; //If shot two off
		if (shotOne.getX() < 1) shotOneFired = false; //If shot one off screen (moved here when collided with ship)
		if (shotTwo.getX() < 1) shotTwoFired = false; //If shot two off screen (moved here when collided with ship)
		
		if (shotOneFired)  //If shot one fired, continue its movement upward
			{
			 shotOne.translateY(Gdx.graphics.getHeight()/75);
			 shotOne.draw(batch);
			}
		if (shotTwoFired)  //If shot two fired, continue its movement upward
			{
			shotTwo.translateY(Gdx.graphics.getHeight()/75);
			shotTwo.draw(batch);
			}
		
		
		
		
	}

	@Override
	/**
	 * Determines if player has collided with another sprite
	 */
	public void collision(Sprite coll) {
		if (Math.abs(sprite.getX() - coll.getX()) < 25 && Math.abs(sprite.getY() - coll.getY()) < 10) //If given sprite is within a tolerance of ship's position
		{
			destroy();
		}
	}
	/*
	 * Destroys shot if it connects with an object
	 */
	public void shotLanded(boolean whichShot)
	{
		if (!whichShot) shotOneFired = false;
		else if (whichShot) shotTwoFired = false;
		
	}
	/*
	 * Draws ship sprite
	 */
	public void draw(SpriteBatch batch)
	{
		sprite.draw(batch);
	}
	
	public float getX()
	{
		return sprite.getX();
	}
	
	public float getY()
	{
		return sprite.getY();
	}
	
	public void fireMove(int shotNum)
	{
		if (shotNum == 1) shotOne.translate(0, 5);
		if (shotNum == 2) shotTwo.translate(0, 5);
	}
	public boolean isShotFired()
	{
		return (shotOneFired || shotTwoFired);
	}
	public Sprite getShot() //This method does not apply to Ship objects that can fire multiple shots 
	{
		return null;
	}
	public boolean getShotOne()
	{
		return shotOneFired;
	}
	
	public boolean getShotTwo()
	{
		return shotTwoFired;
	}
	
	public void destroy()
	{
		alive = false;
		sprite.setAlpha(0);
		sprite.setPosition(0, 0);
		
		String scoreSend = "http://coms-309-tc-1.misc.iastate.edu:8080/newScore?score=" + Space.score + "&name=" + Constants.user;
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	public Sprite getShotOneSprite()
	{
		return shotOne;
	}
	
	public Sprite getShotTwoSprite()
	{
		return shotTwo;
	}
	
	public boolean getShotPressed()
	{
		return shotPressed;
	}
	
	public void dispose()
	{
		shotTexture.dispose();
	}
	
	public String getType()
	{
		return "player";
	}
	
}
