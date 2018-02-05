package com.spacejunk.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.jvm.hotspot.memory.Space;

public class GameScreen implements Screen {

	int xMax, yMax;
	int initialY;

	int currentX, currentY;
	int targetY;

	SpriteBatch canvas;
	Texture background;
	ShapeRenderer shapeRenderer;
	int gameScore;
	int scoringTube;
	BitmapFont font;

	Texture gameOver;

	Texture[] birds;
	Boolean flapState = false;
	Boolean isGameActive = false;
	Boolean isCrashed = false;
	float velocity = 0;
	float gravity = 2;
	Circle birdCircle;

	Texture topTube;
	Texture bottomTube;
	Rectangle[] topRectangles;
	Rectangle[] bottomRectangles;
	float gap;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 5;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	OrthographicCamera camera;
	final SpaceJunk game;

	static private int WIDTH = 800;
	static private int HEIGHT = 480;

	public GameScreen(final SpaceJunk game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
	}


	@Override
	public void create () {

		xMax = Gdx.graphics.getWidth();
		yMax = Gdx.graphics.getHeight();

		Gdx.app.log("applog", String.valueOf(xMax));
		Gdx.app.log("applog", String.valueOf(yMax));


		canvas = new SpriteBatch();
		background = new Texture("space_background.jpg");
		gameScore = 0;
		scoringTube = 0;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
//		shapeRenderer = new ShapeRenderer();

		gameOver = new Texture("gameover.png");

		//BIRDS
		birdCircle = new Circle();
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");



		//TUBES
		topRectangles = new Rectangle[numberOfTubes];
		bottomRectangles = new Rectangle[numberOfTubes];
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gap = 500;
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 2/3;

		startGame();
	}


	public void startGame() {

		initialY = Gdx.graphics.getHeight() / 2 - birds[1].getWidth() / 2;
		currentY = initialY;

		for(int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + (i *distanceBetweenTubes) + Gdx.graphics.getWidth();
			topRectangles[i] = new Rectangle();
			bottomRectangles[i] = new Rectangle();
		}
	}

	private void moveAstronaut() {

		int y = Gdx.input.getY();


		// Top half of screen tapped
		if (y > (yMax / 2)) {
			// Making sure we don't go up a platform while already at the top most
			if (currentY != initialY - (yMax / 3)) {
				this.targetY = currentY - yMax / 3;
			}
		}
		// Bottom half is tapped
		else {
			// Making sure we don't go up a platform while already at the top most
			if (currentY != initialY + (yMax / 3)) {
				this.targetY +=  currentY + yMax / 3;
			}
		}

	}


	private void moveAstronautUpOnePlatform(int targetY) {
		if(currentY < targetY) {
			currentY += 1;
		}
	}

	private void renderAstronaut() {

		if(this.currentY < this.targetY) {
			this.currentY += 1;
		}
		else if (this.currentY > this.targetY){
			this.currentY -= 1;
		}

		if(this.flapState) {
			this.canvas.draw(birds[0], Gdx.graphics.getWidth() / 2 - birds[1].getWidth() / 2, currentY);
			this.flapState = false;
		}
		else {
			this.flapState = true;
			this.canvas.draw(birds[1], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, currentY);
		}
	}

	@Override
	public void show() {

	}


	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		canvas.dispose();
//		img.dispose();
	}

	@Override
	public void render (float delta) {

		canvas.begin();
		canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(isGameActive && !isCrashed) {

			if(birdCircle.x > tubeX[scoringTube]) {
				gameScore++;
				if(scoringTube < numberOfTubes-1) {
					scoringTube++;
				}
				else {
					scoringTube = 0;
				}
				Gdx.app.log("Score", Integer.toString(gameScore));
			}

			if(Gdx.input.justTouched()) {
				moveAstronaut();
			}



			for(int i = 0; i < numberOfTubes; i++) {
				if(tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
				}
				else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				canvas.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				canvas.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i]);

				//SETTING TOP TUBE SHAPE
				topRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());

			}

		}
		else if(!isGameActive && !isCrashed){
			if(Gdx.input.justTouched()) {
				isGameActive = true;
			}
		}


		//if statement ends here
		//Code for if crash occurs
		if(isCrashed) {
			canvas.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
			if(Gdx.input.justTouched()) {
				isGameActive = true;
				isCrashed = false;
				gameScore = 0;
				scoringTube = 0;
				velocity = 0;
				startGame();

			}
		}


		renderAstronaut();

		font.draw(canvas, String.valueOf(gameScore), 100, 200);
		canvas.end();


		//SETTING BIRD SHAPE
		birdCircle.set(Gdx.graphics.getWidth() / 2, currentY + birds[0].getHeight() / 2, birds[0].getWidth() / 2); //XY Coordinate and radius

		//SETTING BOTTOM TUBE SHAPE
		for(int i = 0; i < numberOfTubes; i++) {

			//CHECK FOR COLLISION
			if(Intersector.overlaps(birdCircle, topRectangles[i]) || Intersector.overlaps(birdCircle, bottomRectangles[i])) {
				//GAME OVER AFTER COLLISION
				isCrashed = true;
			}

		}

	}
}
