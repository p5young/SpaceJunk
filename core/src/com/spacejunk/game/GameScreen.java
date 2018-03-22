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
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.menus.RemainingLivesMenu;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.obstacles.Obstacle;
import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.GL20.GL_RGBA;
import static com.badlogic.gdx.graphics.GL20.GL_UNSIGNED_BYTE;
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

	private boolean isRecordingComplete = false;

	private RemainingLivesMenu remainingLivesMenu;


	private SpaceJunk spaceJunk;

	private float elapsedTime;

	private State state;

	private ArrayList<ArrayList<Integer>> screenShots = new ArrayList<ArrayList<Integer>>();
	private List<Pixmap> frames = new ArrayList<Pixmap>();
	private GifEncoder gifEncoder;

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

		controller.setupSwipeDetection();

	}



	@Override
	public void dispose () {
		canvas.dispose();
	}


	/**
	 * Prepares an astronaut for rendering. Moves it 'forward' one frame and then draws the result on the canvas
	 * */
	private void renderAstronaut(boolean toAnimate) {

		spaceJunk.getCharacter().updateCharacterPosition(toAnimate);
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
				renderRunningScreen();
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
				renderPauseScreenEssentials();
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

	private void renderPauseScreenEssentials() {
		canvas.begin();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// We are making use of the painters algorithm here
		drawBackground();
		drawPauseScreenTexture();
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
	private void renderRunningScreen() {
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

		recordScreen();

		pickedConsumable();
		isCrashed = hasCharacterDied();
	}

	private void recordScreen() {

		if(elapsedTime < 7 && !isRecordingComplete) {
			spaceJunk.getSystemServices().startRecording("Test_path");
		}
		else {
			isRecordingComplete = true;
			spaceJunk.getSystemServices().stopRecording();
		}

//		if(isRecordingComplete) {
//
//		}
	}

	/*
	private void recordScreen() {

		if(elapsedTime < 1) {
			drawRecordingScreenBorder();

//		Runtime rt = Runtime.getRuntime();
//		rt.exec()

//		recorder.update();
//		Gdx.app.log("recorderlog", recorder.getExportFileHandle().file().getAbsolutePath());
			takeScreenshotOfScreen();
		}
		else {
			if(!isRecordingComplete) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							saveGif();
						}
						catch (Exception e) {
							e.printStackTrace();
							Gdx.app.log("errorlog", "ERROR ERROR");
						}

					}
				}).start();




				Gdx.app.log("giflog", "File has been saved to : " + Gdx.files.local("testgif.gif").file().getAbsolutePath());

				isRecordingComplete = true;
			}
		}
	}
	*/

	public static int[] convertIntegers(ArrayList<Integer> integers)
	{
		int[] ret = new int[integers.size()];
		for (int i=0; i < ret.length; i++)
		{
			ret[i] = integers.get(i);
		}
		return ret;
	}

	private void saveGif() throws Exception {

		Gdx.app.log("giflog", "Start of save gif");

		gifEncoder = new GifEncoder(Gdx.files.local("testgif.gif").write(false),
				spaceJunk.getxMax()	, spaceJunk.getyMax(), 1);

		ImageOptions options = new ImageOptions();

		// Frame rate / Time between each frame
		options.setDelay(50, TimeUnit.MILLISECONDS);

		Gdx.app.log("giflog", "Before for loop");
		Gdx.app.log("giflog", "NBumber of frames is " + frames.size());



		for (Pixmap pixmap : frames) {
			Gdx.app.log("giflog", "Loop iteration here");

			int width = pixmap.getWidth();
			int height = pixmap.getHeight();


			Gdx.app.log("giflog", "Width of pixmap is  : " + width);
			Gdx.app.log("giflog", "Height of pixamp is : " + height);


			int[][] pixels = new int[height][width];

			for (int x = 0; x < width; ++x) {
//				Gdx.app.log("giflog", "In first inner loop");

				for (int y = 0; y < height; ++y) {
//					Gdx.app.log("giflog", "In Second inner loop");
					int pixel = pixmap.getPixel(x, y);


					pixels[y][x] = pixel;
				}
			}

			Gdx.app.log("giflog", "Inner for loops done");


			gifEncoder.addImage(pixels, options);
		}

		Gdx.app.log("giflog", "Finish Encoding method being called here");

		gifEncoder.finishEncoding();

		for (Pixmap pixmap : frames)
			pixmap.dispose();

		Gdx.app.log("giflog", "Disposed of the pixmap");


		frames.clear();

	}


	private void takeScreenshotOfScreen() {

//		gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
//
//		Pixmap pixmap = new Pixmap(spaceJunk.getxMax(), spaceJunk.getyMax(), Pixmap.Format.RGBA8888);
//
//		gl.glReadPixels(0, 0, spaceJunk.getxMax(), spaceJunk.getyMax(), GL_RGBA, GL_UNSIGNED_BYTE, pixels);
//
//		int width = pixmap.getWidth(), height = pixmap.getHeight();
//		Pixmap flipped = new Pixmap(width, height, pixmap.getFormat());
//
//		for (int x = 0; x < width; ++x)
//			for (int y = 0; y < height; ++y)
//				flipped.drawPixel(x, y, pixmap.getPixel(x, height - y - 1));
//
//		pixmap.dispose();
//		pixmap = flipped;
//
//
//		ByteBuffer pixels = pixmap.getPixels();
//		byte[] bytes = new byte[pixels.remaining()];
//
//		pixels.get(bytes);
//
//		int bytesSize = bytes.length;
////		int[] arrayWitoutAlpha = new int[bytesSize/4];
//		ArrayList<Integer> arrayWithoutAlpha = new ArrayList<Integer>();
//
//		for(int i = 0; i < bytesSize; i+=4) {
//			arrayWithoutAlpha.add(((bytes[i]&0x0ff) << 16) | ((bytes[i+1]&0x0ff) << 8) | (bytes[i+2]&0x0ff));
//		}

		int width = spaceJunk.getxMax();
		int height = spaceJunk.getyMax();

		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		ByteBuffer pixels = pixmap.getPixels();
		gl.glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		frames.add(pixmap);

//		screenShots.add(arrayWithoutAlpha);
	}

	private void drawRecordingScreenBorder() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);

		Gdx.gl20.glLineWidth(GameConstants.BORDER_WIDTH);

		float w = spaceJunk.getxMax();
		float h = spaceJunk.getyMax();
		shapeRenderer.line(0, 0, w, 0);
		shapeRenderer.line(0, h, w, h);
		shapeRenderer.line(0, 0, 0, h);
		shapeRenderer.line(w, 0, w, h);
		shapeRenderer.end();
	}

	private boolean hasCharacterDied() {
		int numberOfObstacles = spaceJunk.getLevel().getObstaclesList().size();

		for(int i = 0; i < numberOfObstacles; i++) {

			Obstacle currentObstacle = this.spaceJunk.getLevel().getObstaclesList().get(i);

			// Move on if the obstacle is already broken, nothing to do here....
			if (currentObstacle.isBroken()) {
				continue;
			}

			/*
			 *  Collision detection
			 */
			if(Intersector.overlaps(
					currentObstacle.getObstacleShape(),
					this.spaceJunk.getCharacter().getCharacterShape())) {


				if (collisionDetector(
						currentObstacle.getPixmap(),
						this.spaceJunk.getCharacter().getPixmap(),
                        currentObstacle.getCoordinates(),
                        this.spaceJunk.getCharacter().getCoordinates())) {

					currentObstacle.playSound();

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
				Gdx.app.log("applog", "ELSE IF CASE OF GAME LOGIC METHOD");
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

	private void drawPauseScreenTexture() {
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

	public SpaceJunk getSpaceJunk() {
		return spaceJunk;
	}

}
