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

	public enum State
	{
		PAUSE,
		RUN,
		RESUME,
		STOPPED
	}

	SpriteBatch canvas;
	Texture background;

	BitmapFont font;

	Texture gameOver;

	Texture[] astronauts;

	Boolean isGameActive = false;
	Boolean isCrashed = false;
	Ellipse astronautShape;

	Rectangle[] topRectangles;
	Rectangle[] bottomRectangles;

	Random randomGenerator;


	final SpaceJunk spaceJunk;


	float stateTime;

	private State state;


	public GameScreen(final SpaceJunk game) {
		this.spaceJunk = game;
		this.state =  State.RUN;

		create();
	}


	public void create () {

		canvas = new SpriteBatch();
		background = new Texture("space_background.jpg");
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
		topRectangles = new Rectangle[4];
		bottomRectangles = new Rectangle[4];
		randomGenerator = new Random();

		stateTime = 0f;

	}


	@Override
	public void dispose () {
		canvas.dispose();
	}


	/**
	 * Prepares an astronaut for rendering. Moves it 'forward' one frame and then draws the result on the canvas
	 * */
	private void renderAstronaut() {
		spaceJunk.getCharacter().updateCharacterPosition();

		this.canvas.draw(astronauts[0], spaceJunk.getCharacter().getInitialX() - astronauts[0].getWidth() / 2,
				spaceJunk.getCharacter().getCurrentY() - astronauts[0].getHeight() / 2);
	}

	private void renderObstacles() {

		this.spaceJunk.getLevel().renderObstacles(canvas);


//		for(int i = 0; i < numberOfTubes; i++) {
////			Gdx.app.log("testlog", "Initially, i is " + i + " and tubeX is " + tubeX[i]);
//			if(tubeX[i] < -topTubeTexture.getWidth()) {
//				Gdx.app.log("testlog", "Finally, i is " + i + " and tubeX is " + tubeX[i]);
//				tubeX[i] += numberOfTubes * distanceBetweenTubes;
//				Gdx.app.log("testlog", "The other thing is " + topTubeTexture.getWidth());
//				tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
//			}
//			else {
//				tubeX[i] = tubeX[i] - tubeVelocity;
//			}
//
//			canvas.draw(topTubeTexture, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
//			canvas.draw(bottomTubeTexture, tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTubeTexture.getHeight() - gap / 2 + tubeOffset[i]);
//
//			//SETTING TOP TUBE SHAPE
//			topRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i], topTubeTexture.getWidth(), topTubeTexture.getHeight());
//			bottomRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - bottomTubeTexture.getHeight() - gap / 2 + tubeOffset[i], topTubeTexture.getWidth(), topTubeTexture.getHeight());
//		}
	}

	@Override
	public void render(float delta) {

		switch (state) {
			case RUN:
				renderScreen();
				break;
			case PAUSE:
				Gdx.app.log("applog", "In pause state now");
				if(Gdx.input.justTouched() &&  playPauseButtonisPressed()) {
					resume();
				}
				break;
			default:
				break;
		}

	}


	private boolean playPauseButtonisPressed() {
		return Gdx.input.getX() >= 0 && Gdx.input.getX() <= 81 && Gdx.input.getY() >= 0 && Gdx.input.getY() <= 81;
	}

	private void renderScreen() {
		canvas.begin();

		drawBackground();
		renderController();
		eventLoop();
		renderRemainingLives();
		renderAstronaut();
		displayScore();

		canvas.end();


		//SETTING BIRD SHAPE
		astronautShape.set(spaceJunk.getCharacter().getInitialX(), spaceJunk.getCharacter().getCurrentY(),
				astronauts[0].getWidth() / 2, astronauts[0].getHeight() / 2); //XY Coordinate and radius

		//SETTING BOTTOM TUBE SHAPE
		for(int i = 0; i < 4; i++) {

			//CHECK FOR COLLISION
//			if(Intersector.overlaps(astronautShape, topRectangles[i]) || Intersector.overlaps(astronautShape, bottomRectangles[i])) {
//				GAME OVER AFTER COLLISION
//				isCrashed = true;
//			}

		}
	}

	private void renderController() {

	}

	private void renderRemainingLives() {

	}

	private void eventLoop() {
		if(isGameActive && !isCrashed) {

			spaceJunk.incrementGameScore();

			if(Gdx.input.justTouched()) {
				if(playPauseButtonisPressed()) {
					Gdx.app.log("applog", "PAUSE BUTTON PRESSED");
					pause();
				}

				spaceJunk.getCharacter().moveCharacter(Gdx.input.getY());
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
			drawGameOverScreen();
		}

	}


	private void drawBackground() {
		canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawGameOverScreen() {
		canvas.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
		if(Gdx.input.justTouched()) {
			isGameActive = true;
			isCrashed = false;

		}
	}

	private void displayScore() {
		font.draw(canvas, String.valueOf(spaceJunk.getCurrentGameScore()), 100, 200);
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
		this.state = State.PAUSE;
	}

	@Override
	public void resume() {
		this.state = State.RUN;
	}

	@Override
	public void hide() {

	}

}
