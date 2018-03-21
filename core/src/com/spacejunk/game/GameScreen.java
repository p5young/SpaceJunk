package com.spacejunk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;
import com.spacejunk.game.menus.RemainingLivesMenu;
import com.spacejunk.game.obstacles.Obstacle;

import org.w3c.dom.css.Rect;

import java.lang.Math;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class GameScreen implements Screen {

//	public static boolean DEBUG = true;
	public static boolean DEBUG = false;

	public enum State
	{
		PAUSE,
		RUN,
		CRASHED,
		RESUME,
		STOPPED
	}

	private ShapeRenderer shapeRenderer;
	private SpriteBatch canvas;

	private Texture background;
	private int backgroundImageIndex = 0;

	private Controller controller;

	private BitmapFont font;

	private Texture gameOver;

	private Boolean isGameActive = false;
	private Boolean isCrashed = false;

	private RemainingLivesMenu remainingLivesMenu;

	private SpaceJunk spaceJunk;

	private float elapsedTime;

	private State state;

	// this field is just for avoiding a local field instantiated every tap
	private Consumable.CONSUMABLES justPressed;

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
		shapeRenderer = new ShapeRenderer();
		canvas.enableBlending();

		background = new Texture("background_space_without_bar.jpg");
		background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

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
	private void renderAstronaut(boolean toAnimate) {

		spaceJunk.getCharacter().updateCharacterPosition();
		spaceJunk.getCharacter().updateCharacterShapeCoordinates();

		if(toAnimate) {
			// Accumulate elapsed animation time only if astronaut is being animated
			elapsedTime += Gdx.graphics.getDeltaTime();
		}

		spaceJunk.getCharacter().render(canvas, elapsedTime, shapeRenderer, toAnimate);
	}

	private void renderObstacles(boolean toMove) {
        spaceJunk.getLevel().renderObstacles(canvas, shapeRenderer, toMove);
        spaceJunk.getLevel().updateObstacleShapeCoordinates();
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
				renderCrashedScreenEssentials();
				// Game should be restarted now
				if(controller.isTouched()) {
					spaceJunk.getCharacter().resetLives();
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

	private void renderCrashedScreenEssentials() {
		canvas.begin();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.GREEN);
		// We are making use of the painters algorithm here
		drawBackground();
		renderController();
		renderAstronaut(false);
		shapeRenderer.setColor(Color.RED);
		renderObstacles(false);
		renderRemainingLives();
		displayScore();
		drawGameOverScreen();
		canvas.end();
		shapeRenderer.end();
	}

	private void renderScreenEssentials() {
		canvas.begin();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// We are making use of the painters algorithm here
		drawBackground();

		renderController();

        shapeRenderer.setColor(Color.GREEN);
		renderAstronaut(false);
		shapeRenderer.setColor(Color.RED);
		renderObstacles(false);

		renderRemainingLives();
		displayScore();

		canvas.end();
		shapeRenderer.end();
	}

	// Note :- Rendering each on screen component that 'moves' updates its internal coordinates
	private void renderScreen() {
		canvas.begin();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// We are making use of the painters algorithm here
		drawBackground();
		// We do this so that the obstacles are painted red
		shapeRenderer.setColor(Color.RED);
		gameLogic();            ///// THIS RENDERS OBSTACLES!!!!!!!!! just a note, leave the code
		// And the astronaut is painted green
		shapeRenderer.setColor(Color.GREEN);
		renderAstronaut(true);
		renderController();
		renderRemainingLives();
		displayScore();

		canvas.end();
		shapeRenderer.end();

		pickedConsumable();
		isCrashed = hasCharacterDied();
	}

	private boolean hasCharacterDied() {
		int numberOfObstacles = spaceJunk.getLevel().getObstaclesList().size();

		for(int i = 0; i < numberOfObstacles; i++) {

			Obstacle currentObstacle = this.spaceJunk.getLevel().getObstaclesList().get(i);

			if (currentObstacle.isBroken())
				continue;

			if(Intersector.overlaps(
					currentObstacle.getObstacleShape(),
					this.spaceJunk.getCharacter().getCharacterShape())) {
				////// MY SHITTT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if (collisionDetector(
						currentObstacle.getPixmap(),
						this.spaceJunk.getCharacter().getPixmap(),
                        currentObstacle.getCoordinates(),
                        this.spaceJunk.getCharacter().getCoordinates())) {

					if (currentObstacle.getBreaksOnConsumable()
							.equals(this.spaceJunk.getLevel().getEquippedConsumable())) {
						currentObstacle.setBroken(true);

						int count = this.spaceJunk.getLevel().getInventory().get(this.spaceJunk.getLevel().getEquippedConsumable());
						if (count > 0) {
							this.spaceJunk.getLevel().getInventory().put(this.spaceJunk.getLevel().getEquippedConsumable(), count - 1);
							if (count - 1 == 0) {
								this.spaceJunk.getLevel().setEquippedConsumable(null);
							}
						}

						return false;
					}

					currentObstacle.setBroken(true);

                    return spaceJunk.getCharacter().takesHit();
                }
			}
		}

		return false;

	}

	private boolean pickedConsumable() {
		boolean status = false;

		int numberOfConsumables = spaceJunk.getLevel().getConsumablesList().size();

		int indexToRemove = -1;

		for (int i = 0; i < numberOfConsumables; i++) {
			Consumable currentConsumable = this.spaceJunk.getLevel().getConsumablesList().get(i);

			if (Intersector.overlaps(
					currentConsumable.getConsumableShape(),
					this.spaceJunk.getCharacter().getCharacterShape()
			)) {
				indexToRemove = i;
				status = true;

				// have to deal with lives a little differently
				if(currentConsumable.getType() == Consumable.CONSUMABLES.LIFE) {
					this.spaceJunk.getCharacter().giveLife();
					break;
				}

				// increment the count
				int currentCount = this.spaceJunk.getLevel().getInventory().get(currentConsumable.getType());
				if (currentCount < 4) {
					this.spaceJunk.getLevel().getInventory().put(currentConsumable.getType(), currentCount + 1);

					Gdx.app.log("applog", new StringBuilder().append("Adding consumable ").append(currentConsumable.getType().toString()).append(". Count: ").append(currentCount + 1).toString());
				}

				break;
			}
		}

		if (status) {
			this.spaceJunk.getLevel().getConsumablesList().remove(indexToRemove);
		}

		return status;
	}

    // Returns true if pixmaps are found to overlap
    // takes in 2 pixmaps and 2 (x,y) coordinate pairs
    // ANTI-PLAGIARISM NOTE:
    // The design of this collision detector was taken from this page:
    // https://stackoverflow.com/questions/5914911/pixel-perfect-collision-detection-android
	private boolean collisionDetector(Pixmap obstacle, Pixmap astronaut, int[] obst, int[] astr) {
	    // Since no lives are lost when this returns false, announce it to logcat to see if this was called
        Gdx.app.log("applog", "COLLISION DETECTOR CALLED");

        // define intersection of obstacle and astronaut bounding boxes
	    int left = max(obst[0], astr[0]);
	    int right = min(obst[0] + obstacle.getWidth(), astr[0] + astronaut.getWidth());
	    int top = min(obst[1] + obstacle.getHeight(), astr[1] + astronaut.getHeight());
	    int bottom = max(obst[1], astr[1]);

	    // search intersection area only for pixels occupying same coordinate
        // only check every other column and every other row for 4 times the efficiency
        for (int x = left ; x < right - 1 ; x += 2) {
            for (int y = top ; y > bottom + 1 ; y -= 2) {
                // if a pixel == 0, it has no color (empty)
                // if co-located astronaut and obstacle pixels both != 0, then there's a collision
                if (obstacle.getPixel(x - obst[0], obst[1] + obstacle.getHeight() - y) != 0
                    && astronaut.getPixel(x - astr[0], astr[1] + astronaut.getHeight() - y) != 0) {
                    return true;
                }
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

				else if (controller.consumablesMenuPressed()) {
					this.justPressed = controller.getPressedConsumable();
					if (this.spaceJunk.getLevel().getInventory().get(this.justPressed) > 0) {
						this.spaceJunk.getLevel().setEquippedConsumable(this.justPressed);
					}
				}
				// If all checks fail, this means the user meant to move the character
				else {
					spaceJunk.getCharacter().moveCharacter(controller.getTouchYCoordinate());
				}
			}

			// Only render and move obstacles if the game is active
			renderObstacles(true);
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
		// canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// Using this method here implies that the background is suitable for wrap as a background
		// Using the width as the "u" parameter implies that image width is greater than screen width
		// If it's not the background is tiled.

		canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), backgroundImageIndex, 0, Gdx.graphics.getWidth(), Math.min(background.getHeight(), Gdx.graphics.getHeight()), false, false);

		if (this.state == State.RUN) {
			backgroundImageIndex += GameConstants.BACKGROUND_SPEED;
		}

		if (backgroundImageIndex > background.getWidth()) {
			backgroundImageIndex = 0;
		}
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
