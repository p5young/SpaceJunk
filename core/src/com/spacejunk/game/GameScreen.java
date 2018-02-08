package com.spacejunk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class GameScreen implements Screen {

	SpriteBatch canvas;
	Texture background;

	int gameScore;
	int scoringTube;
	BitmapFont font;

	Texture gameOver;

	Texture[] astronauts;
	Boolean flapState = false;
	Boolean isGameActive = false;
	Boolean isCrashed = false;
	float velocity = 0;
	Ellipse astronautShape;

	Texture topTubeTexture;
	Texture bottomTubeTexture;

	Rectangle[] topRectangles;
	Rectangle[] bottomRectangles;

	float gap;
	float maxTubeOffset;
	Random randomGenerator;

	float tubeVelocity = 8;
	int numberOfTubes = 4;

	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	OrthographicCamera camera;
	final SpaceJunk spaceJunk;

	static private int WIDTH = 800;
	static private int HEIGHT = 480;

	Animation<TextureRegion> astronautAnimation;
	float stateTime;


	public GameScreen(final SpaceJunk game) {
		this.spaceJunk = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		create();
	}


	public void create () {

		canvas = new SpriteBatch();
		background = new Texture("space_background.jpg");
		gameScore = 0;
		scoringTube = 0;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		gameOver = new Texture("gameover.png");

		//BIRDS
		astronautShape = new Ellipse();
		astronauts = new Texture[3];
		astronauts[0] = new Texture("astronaut_texture_1.png");
		astronauts[1] = new Texture("astronaut_texture_2.png");
		astronauts[2] = new Texture("astronaut_texture_3.png");


		//TUBES
		topRectangles = new Rectangle[numberOfTubes];
		bottomRectangles = new Rectangle[numberOfTubes];
		topTubeTexture = new Texture("toptube.png");
		bottomTubeTexture = new Texture("bottomtube.png");
		gap = 500;
		randomGenerator = new Random();

		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		distanceBetweenTubes = Gdx.graphics.getWidth() * 2/3;

		stateTime = 0f;

		startGame();
	}


	public void startGame() {

		for(int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTubeTexture.getWidth()/2 + (i * distanceBetweenTubes) + Gdx.graphics.getWidth();
			topRectangles[i] = new Rectangle();
			bottomRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void dispose () {
		canvas.dispose();
//		img.dispose();
	}


	private void renderAstronaut() {

		spaceJunk.updateAstronautPosition();
		this.canvas.draw(astronauts[0], spaceJunk.getInitialX(), spaceJunk.getCurrentY());
	}

	private void renderObstacles() {

		for(int i = 0; i < numberOfTubes; i++) {
//			Gdx.app.log("testlog", "Initially, i is " + i + " and tubeX is " + tubeX[i]);
			if(tubeX[i] < -topTubeTexture.getWidth()) {
				Gdx.app.log("testlog", "Finally, i is " + i + " and tubeX is " + tubeX[i]);
				tubeX[i] += numberOfTubes * distanceBetweenTubes;
				Gdx.app.log("testlog", "The other thing is " + topTubeTexture.getWidth());
				tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
			}
			else {
				tubeX[i] = tubeX[i] - tubeVelocity;
			}

			canvas.draw(topTubeTexture, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
			canvas.draw(bottomTubeTexture, tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTubeTexture.getHeight() - gap / 2 + tubeOffset[i]);

			//SETTING TOP TUBE SHAPE
			topRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i], topTubeTexture.getWidth(), topTubeTexture.getHeight());
			bottomRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTubeTexture.getHeight() - gap / 2 + tubeOffset[i], topTubeTexture.getWidth(), topTubeTexture.getHeight());
		}
	}

	@Override
	public void render(float delta) {

		canvas.begin();
		canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(isGameActive && !isCrashed) {

			if(astronautShape.x > tubeX[scoringTube]) {
				gameScore++;
				if(scoringTube < numberOfTubes-1) {
					scoringTube++;
				}
				else {
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()) {
				spaceJunk.moveAstronaut(Gdx.input.getY());
			}

			renderObstacles();


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
		astronautShape.set(spaceJunk.getInitialX(), spaceJunk.getCurrentY(),
				astronauts[0].getWidth() / 2, astronauts[0].getHeight() / 2); //XY Coordinate and radius

		//SETTING BOTTOM TUBE SHAPE
		for(int i = 0; i < numberOfTubes; i++) {

			//CHECK FOR COLLISION
//			if(Intersector.overlaps(astronautShape, topRectangles[i]) || Intersector.overlaps(astronautShape, bottomRectangles[i])) {
//				GAME OVER AFTER COLLISION
//				isCrashed = true;
//			}

		}

	}


	// Auto generated method stubs
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

}
