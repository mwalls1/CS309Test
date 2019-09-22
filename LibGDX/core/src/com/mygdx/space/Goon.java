package com.mygdx.space;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Goon implements Ship{
	private Sprite sprite;
	private Texture texture;
	private boolean alive;
	private float xOrigin;
	private float dir = 1;
	private boolean shotFired;
	private Sprite shotSprite;
	private Texture shotTexture;
	
	public Goon()
	{
		texture = new Texture("enemy1.png");
		shotTexture = new Texture("shot.png");
		sprite = new Sprite(texture);
		sprite.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - 100);
		sprite.setScale(100);
		alive = true;
		xOrigin = sprite.getX();
		shotSprite = new Sprite(shotTexture);
		
	}
	
	public Goon(float xPos)
	{
		texture = new Texture("enemy1.png");
		shotTexture = new Texture("shot.png");
		sprite = new Sprite(texture);
		sprite.setPosition(xPos, Gdx.graphics.getHeight() - 100);
		alive = true;
		xOrigin = sprite.getX();
		shotSprite = new Sprite(shotTexture);
	}

	public void move(float x, float y) {
		if (sprite.getX() > xOrigin + 50 || sprite.getX() > Gdx.graphics.getWidth()-100) dir = -1;
		else if (sprite.getX() < xOrigin - 50 || sprite.getX() < 90) dir = 1;
		
		sprite.translateX(dir*5);
		
		
		
		
		
		if (sprite.getY() < 0 && alive) sprite.setY(Gdx.graphics.getHeight());
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public void shoot(SpriteBatch batch) {
		if (!shotFired && Gdx.input.isKeyPressed(Keys.SPACE))
		{
			shotFired = true;
			shotSprite.setPosition(sprite.getX(), sprite.getY()+30);
		}
		
		
		if (shotSprite.getY() > Gdx.graphics.getHeight()) shotFired = false;
		if (shotSprite.getX() < 1) shotFired = false;
		
		if (shotFired) 
			{
			 shotSprite.translateY(Gdx.graphics.getHeight()/-75);
			 shotSprite.draw(batch);
			}
		
	}

	@Override
	public void collision(Sprite coll) {
		if (Math.abs(sprite.getX() - coll.getX()) < 25 && Math.abs(sprite.getY() - coll.getY()) < 10 && isAlive())
		{
			coll.setPosition(0, 0);
			destroy();
		}		
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return sprite.getX();
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return sprite.getY();
	}
	
	public void draw(SpriteBatch batch)
	{
		sprite.draw(batch);
	}
	
	public void destroy()
	{
		alive = false;
		sprite.setPosition(0, 0);
		Space.score += 10;
		//sprite.setAlpha(0);
	}

	public boolean isAlive()
	{
		return alive;
	}
}
