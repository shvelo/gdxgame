package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MyGdxGame extends ApplicationAdapter {
	ShapeRenderer shapeRenderer;
	int x;
	int y;
	int width;
	int height;
	int radius = 40;
	int xSpeed = 6;
	int ySpeed = 6;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		x = radius + 5;
		y = radius + 5;
	}

	@Override
	public void render () {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		if(x >= width - radius || x <= radius) xSpeed = xSpeed * -1;

		if(y >= height - radius || y <= radius) ySpeed = ySpeed * -1;

		x += xSpeed;
		y += ySpeed;

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.circle(x, y, radius);
		shapeRenderer.end();
	}
}
