package com.spacejunk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.spacejunk.game.menus.RemainingLivesMenu;

import java.util.Random;

public class GameScreen implements Screen {
	
	public enum State
	{
		PAUSE,
		RUN,
		CRASHED,
		RESUME,
		STOPPED
	}

	private SpriteBatch canvas;
	private Texture background;
	private Controller controller;

	private BitmapFont font;

	private Texture gameOver;

	private Boolean isGameActive = false;
	private Boolean isCrashed = false;

	private RemainingLivesMenu remainingLivesMenu;

	private SpaceJunk spaceJunk;

	private float elapsedTime;

	private State state;

	public GameScreen(final SpaceJunk game) {
		startGame(game);
	}

	private void startGame(SpaceJunk game) {

		this.spaceJunk = game;
		this.state =  State.RUN;

		this.controller = new Controller(this.spaceJunk);
		this.remainingLivesMenu = new RemainingLivesMenu(this.spaceJunk);

		create();
	}


	public void create () {

		Gdx.app.log("applog", "Create method of gamescreen.java called");

		canvas = new SpriteBatch();
		canvas.enableBlending();
		background = new Texture("space_background.jpg");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(7);

		gameOver = new Texture("gameover.png");

		elapsedTime = 0f;
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
		spaceJunk.getCharacter().updateCharacterShapeCoordinates();

		elapsedTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time


		TextureRegion currentFrame = spaceJunk.getCharacter().getCharacterAnimation().getKeyFrame(elapsedTime, true);

		this.canvas.draw(currentFrame,
				spaceJunk.getCharacter().getInitialX() - currentFrame.getRegionWidth() / 2,
				spaceJunk.getCharacter().getCurrentY() - currentFrame.getRegionHeight() / 2);
	}

	private void renderObstacles() {
		this.spaceJunk.getLevel().renderObstacles(canvas);
		this.spaceJunk.getLevel().updateObstacleShapeCoordinates();
	}

	private void restartGame() {
		spaceJunk.setUpGame();
		startGame(spaceJunk);
	}

	@Override
	public void render(float delta) {

		switch (state) {
			case RUN:
				renderScreen();
				break;
			case CRASHED:
				// Game should be restarted now
				if(controller.isTouched()) {
					isGameActive = true;
					isCrashed = false;
					restartGame();
					this.state = State.RUN;
				}
				break;
			case PAUSE:
				renderScreenEssentials();
				if(controller.isTouched() && controller.playPauseButtonisPressed()) {
					resume();
				}
				break;
			default:
				break;
		}

	}

	private void renderScreenEssentials() {
		canvas.begin();
		renderController();
		canvas.end();
	}

	// Note :- Rendering each on screen component that 'moves' updates its internal coordinates
	private void renderScreen() {
		canvas.begin();

		// We are making use of the painters algorithm here
		drawBackground();
		gameLogic();
		renderAstronaut();
		renderController();
		renderRemainingLives();
		displayScore();

		canvas.end();

		isCrashed = hasCollisionOccured();
	}

	private boolean hasCollisionOccured() {

		int numberOfObstacles = spaceJunk.getLevel().getObstaclesList().size();

		for(int i = 0; i < numberOfObstacles; i++) {
			if(Intersector.overlaps(this.spaceJunk.getCharacter().getCharacterShape(),
					this.spaceJunk.getLevel().getObstaclesList().get(i).getObstacleShape())) {
				return true;
			}
		}

		return false;
	}

	private void renderController() {
		controller.render(canvas);
	}

	private void renderRemainingLives() {
		remainingLivesMenu.render(canvas);
	}

	private void gameLogic() {

		if(isGameActive && !isCrashed) {

			spaceJunk.incrementGameScore();

			// There has been on on screen touch action being done
			if(controller.isTouched()) {
				// Check if options menu is interacted with
				if(controller.playPauseButtonisPressed()) {
					pause();
				}
				// If all checks fail, this means the user meant to move the character
				else {
					spaceJunk.getCharacter().moveCharacter(controller.getTouchYCoordinate());
				}
			}

			// Only render obstacles if the game is active
			renderObstacles();
		}

		else if(!isGameActive && !isCrashed){
			if(controller.isTouched()) {
				isGameActive = true;
			}
		}

		//Code for if crash occurs
		if(isCrashed) {
			this.state = State.CRASHED;
			drawGameOverScreen();
		}

	}


	private void drawBackground() {
		canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void drawGameOverScreen() {
		canvas.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
	}

	private void displayScore() {
		GlyphLayout layout = new GlyphLayout(font, String.valueOf(spaceJunk.getCurrentGameScore()));
		font.draw(canvas, String.valueOf(spaceJunk.getCurrentGameScore()),
				Gdx.graphics.getWidth() / 2 - layout.width / 2,
				Gdx.graphics.getHeight());
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
		controller.getOptionsMenu().setMiddleButtonTextureToPlay();
	}

	@Override
	public void resume() {
		this.state = State.RUN;
		controller.getOptionsMenu().setMiddleButtonTextureToPause();
	}

	@Override
	public void hide() {

	}

}
