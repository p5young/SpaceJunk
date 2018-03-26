package com.spacejunk.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.menus.RemainingLivesMenu;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.obstacles.Obstacle;

import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.spacejunk.game.constants.GameConstants.BACKGROUND_MUSIC_VOLUME;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class GameScreen implements Screen {


    //	public static boolean DEBUG = true;
    public static boolean DEBUG = false;

    // These booleans specify the settings selected by user
    private boolean soundSetting;
    private boolean vibrationSetting;
    private boolean recordAudioSetting;

    public static final String GAME_START_PROMPT = "Press anywhere on the screen to begin playing";


    public static float SCALE_X_FACTOR = 1;
    public static float SCALE_Y_FACTOR = 1;

    public enum State {
        MAIN_MENU_SCREEN,
        ABOUT_SCREEN,
        HOW_TO_PLAY_SCREEN,
        PAUSE,
        RUN,
        CRASHED,
        RESUME,
        STOPPED
    }

    private ShapeRenderer shapeRenderer;
    private SpriteBatch canvas;

    private Texture background;
    private Texture mainMenu;
    private Texture mainMenuMiddle;

    // "how to play" and "about" screen variables
    private Texture howToPlay;
    private Texture about;
    private Texture back;
    private Texture play;
    private int aboutOrHowToPlayImageIndex = 0;
    private int scrollIndex = 0;
    private int autoScroll = 0; // autoScroll on when autoScroll = 0 or 1
    private boolean scrolling = false;

    private int backgroundImageIndex = 0;
    private int mainMenuImageIndex = 0;

    private Music backgroundMusic;

    private Controller controller;

    private BitmapFont scoreFont;
    private BitmapFont promptFont;

    private Texture gameOver;
    private Texture pauseScreen;
    private Texture settingsMenu;

    private Boolean isGameActive = false;
    private Boolean isCrashed = false;

    private boolean isRecordingInProgress = false;
    private boolean thisIsTheFirstTimeMainMenuIsAccessed = true;

    // This is only ever used in conjunction with STATE.PAUSE
    // If this is true, settings menu is shown while in pause state
    // If false, then we in pause state with the actual pause menu itself shown
    private boolean isSettingsMenuShownOnScreen;

    private RemainingLivesMenu remainingLivesMenu;

    private SpaceJunk spaceJunk;

    private float elapsedTime;

    private State state;

    // this field is just for avoiding a local field instantiated every tap
    private Consumable.CONSUMABLES justPressed;


    public static void setScaleFactor(int xMax, int yMax) {
        GameScreen.SCALE_X_FACTOR = (float) xMax / GameConstants.X_AXIS_CONSTANT;
        GameScreen.SCALE_Y_FACTOR = (float) yMax / GameConstants.Y_AXIS_CONSTANT;
        Gdx.app.log("applog", "XSCALEFACTOR: " + SCALE_X_FACTOR);
        Gdx.app.log("applog", "YSCALEFACTOR: " + SCALE_Y_FACTOR);
    }

    public static int getScaledTextureWidth(Texture texture) {
        return (int) (texture.getWidth() * GameScreen.SCALE_X_FACTOR);
    }

    public static int getScaledTextureHeight(Texture texture) {
        return (int) (texture.getHeight() * GameScreen.SCALE_Y_FACTOR);
    }

    public static int getScaledTextureRegionWidth(TextureRegion textureRegion) {
        return (int) (textureRegion.getRegionWidth() * GameScreen.SCALE_X_FACTOR);
    }

    public static int getScaledTextureRegionHeight(TextureRegion textureRegion) {
        return (int) (textureRegion.getRegionHeight() * GameScreen.SCALE_Y_FACTOR);
    }


    public GameScreen(final SpaceJunk game) {

        // Start the game off on the main menu
        this.state = State.MAIN_MENU_SCREEN;

        // Make all settings to be true
        soundSetting = true;
        recordAudioSetting = true;
        vibrationSetting = true;

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Retro-Frantic-bkg.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(BACKGROUND_MUSIC_VOLUME);

        startGame(game);
    }

    private void startGame(SpaceJunk game) {

        this.spaceJunk = game;

        this.controller = new Controller(this.spaceJunk, this);
        this.remainingLivesMenu = new RemainingLivesMenu(this.spaceJunk);

        create();
    }


    public void create() {

        Gdx.app.log("applog", "Create method of gamescreen.java called");

        canvas = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        canvas.enableBlending();


        GameScreen.setScaleFactor(spaceJunk.getxMax(), spaceJunk.getyMax());

        spaceJunk.getLevel().getLevelGenerator().setMinGapWithScaleFactor();

        background = new Texture("background.jpg");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
        updateBackgroundMusic();

        mainMenu = new Texture("main_menu_background.jpg");
        mainMenu.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        mainMenuMiddle = new Texture("main_menu_middle.png");

        howToPlay = new Texture("howToPlay.png");
        about = new Texture("about.png");
        back = new Texture("back.png");
        play = new Texture("play.png");

        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(7 * (GameScreen.SCALE_X_FACTOR));

        promptFont = new BitmapFont();
        promptFont.setColor(Color.WHITE);
        promptFont.getData().setScale(4 * (GameScreen.SCALE_X_FACTOR));

        gameOver = new Texture("gameover.jpg");
        pauseScreen = new Texture("pause_screen.png");

        updateSettingsMenuTexture();

        elapsedTime = 0f;

        controller.setupSwipeDetection();

    }


    private static double round(double value, int places) {

        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    @Override
    public void dispose() {
        canvas.dispose();
        backgroundMusic.dispose();
    }


    /**
     * Prepares an astronaut for rendering. Moves it 'forward' one frame and then draws the result on the canvas
     */
    private void renderAstronaut(boolean toAnimate) {

        spaceJunk.getCharacter().updateCharacterPosition(toAnimate);
        spaceJunk.getCharacter().updateCharacterShapeCoordinates();

        if (toAnimate) {
            // Accumulate elapsed animation time only if astronaut is being animated
            elapsedTime += Gdx.graphics.getDeltaTime();
        }

        spaceJunk.getCharacter().render(canvas, elapsedTime, shapeRenderer, toAnimate,
                spaceJunk.getLevel().getEquippedConsumable());
    }

    private void renderObstacles(boolean toMove) {
        spaceJunk.getLevel().renderObstacles(canvas, shapeRenderer, toMove);
        spaceJunk.getLevel().updateObstacleShapeCoordinates();
    }

    private void restartGame() {
        spaceJunk.getCharacter().resetLives();
        isGameActive = false;
        isCrashed = false;
        spaceJunk.setUpGame();
        startGame(spaceJunk);
    }

    @Override
    public void render(float delta) {

        switch (state) {
            case ABOUT_SCREEN:
                renderAboutOrHowToPlayScreen();
                break;
            case HOW_TO_PLAY_SCREEN:
                renderAboutOrHowToPlayScreen();
                break;
            case MAIN_MENU_SCREEN:
                // Display main menu here, on interaction, move on to different game state
                renderMainMenu();
                break;
            case RUN:
                renderRunningScreen();
                break;
            case CRASHED:
                renderCrashedScreenEssentials();
                // Game should be restarted now
                if (controller.isTouched()) {
                    restartGame();
                    this.state = State.RUN;
                }
                break;
            case PAUSE:
                renderPauseScreenEssentials();

                if (controller.isTouched()) {
                    if (isSettingsMenuShownOnScreen) {
                        // If back button is touched, flip this boolean around so the else case is run next time out
                        if (controller.settingsMenuBackButtonIsPressed()) {
                            isSettingsMenuShownOnScreen = false;
                        } else if (controller.settingsMenuSoundsSettingIsPressed()) {
                            soundSetting = !soundSetting;
                            updateBackgroundMusic();
                            updateSettingsMenuTexture();
                        } else if (controller.settingsMenuVibrateSettingIsPressed()) {
                            vibrationSetting = !vibrationSetting;
                            updateSettingsMenuTexture();
                        } else if (controller.settingsMenuRecordAudioSettingIsPressed()) {
                            recordAudioSetting = !recordAudioSetting;
                            updateSettingsMenuTexture();
                        }


                    } else {
                        if (controller.playPauseButtonisPressed() || controller.pauseScreenResumeButtonIsPressed()) {
                            resume();
                        } else if (controller.mainMenuButtonIsPressed() || controller.pauseScreenMainMenuButtonIsPressed()) {
                            goBackToMainMenu();
                        } else if (controller.settingsMenuButtonIsPressed() || controller.pauseScreenSettingsMenuButtonIsPressed()) {
                            showSettingsMenu();
                        }
                    }
                }
                break;
            default:
                break;
        }

    }

    private void updateSettingsMenuTexture() {

        if (soundSetting && recordAudioSetting && vibrationSetting) {
            settingsMenu = new Texture("settings_menu_all_selected.jpg");
        } else if (soundSetting && recordAudioSetting && !vibrationSetting) {
            settingsMenu = new Texture("settings_menu_sound_and_record_selected.jpg");
        } else if (soundSetting && !recordAudioSetting && vibrationSetting) {
            settingsMenu = new Texture("settings_menu_vibration_and_sound_selected.jpg");
        } else if (soundSetting && !recordAudioSetting && !vibrationSetting) {
            settingsMenu = new Texture("settings_menu_sound_selected.jpg");
        } else if (!soundSetting && recordAudioSetting && vibrationSetting) {
            settingsMenu = new Texture("settings_menu_vibration_record_selected.jpg");
        } else if (!soundSetting && recordAudioSetting && !vibrationSetting) {
            settingsMenu = new Texture("settings_menu_record_selected.jpg");
        } else if (!soundSetting && !recordAudioSetting && vibrationSetting) {
            settingsMenu = new Texture("settings_menu_vibration_selected.jpg");
        } else if (!soundSetting && !recordAudioSetting && !vibrationSetting) {
            settingsMenu = new Texture("settings_menu_none_selected.jpg");
        }


    }


    private void updateBackgroundMusic() {
        if (!soundSetting && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();

        } else if (soundSetting && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    private void renderCrashedScreenEssentials() {
        canvas.begin();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        // We are making use of the painters algorithm here
        drawBackground();
        renderAstronaut(false);
        shapeRenderer.setColor(Color.RED);
        renderObstacles(false);

        // All these are painted on top of the background/astronaut/obstacles
        renderRemainingLives();
        displayScore();
        renderController();
        drawGameOverScreen();

        canvas.end();
        shapeRenderer.end();
    }

    private void renderPauseScreenEssentials() {
        canvas.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // We are making use of the painters algorithm here
        drawBackground();

        shapeRenderer.setColor(Color.RED);
        renderObstacles(false);
        shapeRenderer.setColor(Color.GREEN);
        renderAstronaut(false);

        // Done at the end so that the pause screen is on top
        renderController();
        renderRemainingLives();
        displayScore();

        if (isSettingsMenuShownOnScreen) {
            drawSettingsMenu();
        } else {
            drawPauseScreenTexture();
        }

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

        if (isRecordingInProgress) {

            if ((int) elapsedTime % 2 == 0) {
                drawRecordingScreenBorder();
            }
        }

        pickedConsumable();
        isCrashed = hasCharacterDied();
    }


    private void renderAboutOrHowToPlayScreen() {
        Texture screenContents;
        if (state == State.HOW_TO_PLAY_SCREEN) {
            screenContents = howToPlay;
        } else {
            screenContents = about;
        }

        canvas.begin();

        // draw background
        canvas.draw(mainMenu, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainMenuImageIndex,
                0, Gdx.graphics.getWidth(), Math.min(mainMenu.getHeight(),
                        Gdx.graphics.getHeight()), false, false);

        // draw back button
        canvas.draw(back, Gdx.graphics.getWidth() / 9 - GameScreen.getScaledTextureWidth(back) / 2,
                Gdx.graphics.getHeight() / 2 - GameScreen.getScaledTextureHeight(back) / 2,
                GameScreen.getScaledTextureWidth(back), GameScreen.getScaledTextureHeight(back));

        // draw play button
        canvas.draw(play, (8 * Gdx.graphics.getWidth()) / 9 - GameScreen.getScaledTextureWidth(play) / 2,
                Gdx.graphics.getHeight() / 2 - GameScreen.getScaledTextureHeight(play) / 2,
                GameScreen.getScaledTextureWidth(play),
                GameScreen.getScaledTextureHeight(play));

        // draw how to play instructions
        canvas.draw(screenContents, Gdx.graphics.getWidth() / 2 - GameScreen.getScaledTextureWidth(screenContents) / 2,
                Gdx.graphics.getHeight() - GameScreen.getScaledTextureHeight(screenContents) + aboutOrHowToPlayImageIndex,
                GameScreen.getScaledTextureWidth(screenContents),
                GameScreen.getScaledTextureHeight(screenContents));

        canvas.end();

        mainMenuImageIndex += GameConstants.MAIN_MENU_BACKGROUND_SPEED;

        if (mainMenuImageIndex > mainMenu.getWidth()) {
            mainMenuImageIndex = 0;
        }

        if (autoScroll < 2) {
            aboutOrHowToPlayImageIndex += 1;
            if (aboutOrHowToPlayImageIndex > GameScreen.getScaledTextureHeight(screenContents) - Gdx.graphics.getHeight())
                aboutOrHowToPlayImageIndex = GameScreen.getScaledTextureHeight(screenContents) - Gdx.graphics.getHeight();
        }

        if (controller.touching()) {
            if (autoScroll == 1) autoScroll = 2;
            // !scrolling means this is the first touch (not dragging yet)
            if (!scrolling) {
                if (controller.howToPlayBackButtonPressed()) {
                    aboutOrHowToPlayImageIndex = 0;    // return scrolling image to top of screen
                    this.state = State.MAIN_MENU_SCREEN;
                } else if (controller.howToPlayPlayButtonPressed()) {
                    aboutOrHowToPlayImageIndex = 0;    // return scrolling image to top of screen
                    this.state = State.RUN;
                } else {
                    scrollIndex = controller.getTouchYCoordinate();
                    scrolling = true;   // no buttons pressed, start dragging (scrolling)
                }
            } else {
                aboutOrHowToPlayImageIndex += controller.getTouchYCoordinate() - scrollIndex;
                scrollIndex = controller.getTouchYCoordinate();
                // set boundaries
                if (aboutOrHowToPlayImageIndex < 0)
                    aboutOrHowToPlayImageIndex = 0;
                if (aboutOrHowToPlayImageIndex > GameScreen.getScaledTextureHeight(screenContents) - Gdx.graphics.getHeight())
                    aboutOrHowToPlayImageIndex = GameScreen.getScaledTextureHeight(screenContents) - Gdx.graphics.getHeight();
            }
        } else {
            if (autoScroll == 0) autoScroll = 1;
            if (scrolling) scrolling = false;  // no touch detected: stop scrolling
        }
    }

    private void renderMainMenu() {
        canvas.begin();

        // Draw main menu here
        canvas.draw(mainMenu, 0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), mainMenuImageIndex,
                0, GameScreen.getScaledTextureWidth(mainMenu), GameScreen.getScaledTextureHeight(mainMenu)
                , false, false);

        canvas.draw(mainMenuMiddle, Gdx.graphics.getWidth() / 2 - GameScreen.getScaledTextureWidth(mainMenuMiddle) / 2,
                Gdx.graphics.getHeight() / 2 - GameScreen.getScaledTextureHeight(mainMenuMiddle) / 2,
                GameScreen.getScaledTextureWidth(mainMenuMiddle),
                GameScreen.getScaledTextureHeight(mainMenuMiddle));

        canvas.end();


        mainMenuImageIndex += (int) (GameConstants.MAIN_MENU_BACKGROUND_SPEED * GameScreen.SCALE_X_FACTOR);

        if (mainMenuImageIndex > mainMenu.getWidth()) {
            mainMenuImageIndex = 0;
        }


        // Check for interactions with it
        if (controller.isTouched()) {

            if (controller.mainMenuPlayButtonIsTouched()) {
                this.state = State.RUN;

                Gdx.app.log("mainmenulog", "Play now button is pressed");
                if (!thisIsTheFirstTimeMainMenuIsAccessed) {
                    restartGame();
                } else {
                    thisIsTheFirstTimeMainMenuIsAccessed = false;
                }
            } else if (controller.aboutButtonIsTouched()) {
                autoScroll = 0;
                this.state = State.ABOUT_SCREEN;
            } else if (controller.howToPlayButtonIsTouched()) {
                autoScroll = 0;
                this.state = State.HOW_TO_PLAY_SCREEN;
            }


        }

    }


    private void beginRecordingScreen() {
        isRecordingInProgress = true;
        spaceJunk.getSystemServices().startRecording(spaceJunk.getxMax(), spaceJunk.getyMax(), recordAudioSetting);
    }


    private void stopRecordingScreen() {
        isRecordingInProgress = false;
        spaceJunk.getSystemServices().stopRecording();
    }

    public Texture getGameOver() {
        return gameOver;
    }

    public Texture getPauseScreen() {
        return pauseScreen;
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

        for (int i = 0; i < numberOfObstacles; i++) {

            Obstacle currentObstacle = this.spaceJunk.getLevel().getObstaclesList().get(i);

            // Move on if the obstacle is already broken, nothing to do here....
            if (currentObstacle.isBroken()) {
                continue;
            }

			/*
             *  Collision detection
			 */
            if (Intersector.overlaps(
                    currentObstacle.getObstacleShape(),
                    this.spaceJunk.getCharacter().getCharacterShape())) {


                if (collisionDetector(
                        currentObstacle.getPixmap(),
                        this.spaceJunk.getCharacter().getPixmap(),
                        currentObstacle.getCoordinates(),
                        this.spaceJunk.getCharacter().getCoordinates())) {


                    if (soundSetting) {
                        currentObstacle.playSound();
                    }

                    boolean passesObstacle = currentObstacle.getBreaksOnConsumable()
                            .equals(this.spaceJunk.getLevel().getEquippedConsumable());

                    this.spaceJunk.getLevel().getInventory().remove(this.spaceJunk.getLevel().getEquippedConsumable());
                    this.spaceJunk.getLevel().setEquippedConsumable(Consumable.CONSUMABLES.UNEQUIPPED);
                    currentObstacle.setBroken(true);

                    if (passesObstacle) {
                        this.spaceJunk.getLevel().incrementScoreRateMultiplier();
                        return false;
                    } else {
                        if (vibrationSetting) {
                            Gdx.input.vibrate(250);
                        }
                    }

                    // Character is about to take a hit so reset the multiplier
                    this.spaceJunk.getLevel().resetMultiplier();
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

                status = true;

                // have to deal with lives a little differently
                if (currentConsumable.getType() == Consumable.CONSUMABLES.LIFE) {
                    // this check is to see if getting a life made a difference
                    if (this.spaceJunk.getCharacter().giveLife()) {
                        indexToRemove = i;
                    }
                    break;
                }

                // put in set
                if (this.spaceJunk.getLevel().getInventory().add(currentConsumable.getType())) {
                    indexToRemove = i;
                }

                break;
            }
        }

        if (status && indexToRemove != -1) {
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

        // Modify xy coordinates so collisionDetector believes full-sized
        // Pixmaps are spaced further/closer than they actually are
        obst[0] /= GameScreen.SCALE_X_FACTOR;
        obst[1] /= GameScreen.SCALE_Y_FACTOR;
        astr[0] /= GameScreen.SCALE_X_FACTOR;
        astr[1] /= GameScreen.SCALE_Y_FACTOR;

        // define intersection of obstacle and astronaut bounding boxes
        int left = max(obst[0], astr[0]);
        int right = min(obst[0] + obstacle.getWidth(), astr[0] + astronaut.getWidth());
        int top = min(obst[1] + obstacle.getHeight(), astr[1] + astronaut.getHeight());
        int bottom = max(obst[1], astr[1]);

        // search intersection area only for pixels occupying same coordinate
        // only check every other column and every other row for 4 times the efficiency
        for (int x = left; x < right - 1; x += 2) {
            for (int y = top; y > bottom + 1; y -= 2) {
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


    private void goBackToMainMenu() {
        this.state = State.MAIN_MENU_SCREEN;
    }

    private void showSettingsMenu() {
        Gdx.app.log("settingslog", "Settings menu should be shown here");
        this.state = State.PAUSE;
        isSettingsMenuShownOnScreen = true;
    }

    private void gameLogic() {

        if (isGameActive && !isCrashed) {

            spaceJunk.incrementGameScore();

            // There has been on on screen touch action being done
            if (controller.isTouched()) {

                // Check if options menu is interacted with
                if (controller.playPauseButtonisPressed()) {
                    pause();
                }

                // Checking for main menu button press
                else if (controller.mainMenuButtonIsPressed()) {
                    goBackToMainMenu();
                }

                // Checking for settings menu button press
                else if (controller.settingsMenuButtonIsPressed()) {
                    showSettingsMenu();
                }

                // Checking for screen record tap
                else if (controller.screenRecordButtonIsPressed()) {
                    if (isRecordingInProgress) {
                        stopRecordingScreen();
                    } else {
                        beginRecordingScreen();
                    }
                }

                // Checking interaction with consumable menu
                else if (controller.consumablesMenuPressed()) {
                    this.justPressed = controller.getPressedConsumable();
                    if (this.spaceJunk.getLevel().getInventory().contains(this.justPressed)) {
                        this.spaceJunk.getLevel().setEquippedConsumable(this.justPressed);
                    }
                }

                // This is for unequipping the current consumable
                else if (controller.astronautTapped()) {
                    this.spaceJunk.getLevel().setEquippedConsumable(Consumable.CONSUMABLES.UNEQUIPPED);
                }

                // If all checks fail, this means the user meant to move the character
                else {
                    spaceJunk.getCharacter().moveCharacter(controller.getTouchYCoordinate());
                }
            }

            // Only render and move obstacles if the game is active
            renderObstacles(true);
        } else if (!isGameActive && !isCrashed) {
            // Game only starts IF user touches once more
            // If not, we display a prompt on screen telling them to tap anywhere to start
            drawOnScreenGameStartPrompt();
            if (controller.isTouched()) {
                Gdx.app.log("applog", "THE GAME STARTS NOW");
                isGameActive = true;
            }
        }

        //Code for if crash occurs
        if (isCrashed) {
            this.state = State.CRASHED;
            this.backgroundMusic.stop();
            drawGameOverScreen();
        }

    }


    private void drawOnScreenGameStartPrompt() {
        GlyphLayout layout = new GlyphLayout(promptFont, GAME_START_PROMPT);

        promptFont.draw(canvas, GAME_START_PROMPT,
                Gdx.graphics.getWidth() / 2 - layout.width / 2 - (100 * GameScreen.SCALE_X_FACTOR),
                (100 * GameScreen.SCALE_Y_FACTOR));
    }


    private void drawBackground() {
        // canvas.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Using this method here implies that the background is suitable for wrap as a background
        // Using the width as the "u" parameter implies that image width is greater than screen width
        // If it's not the background is tiled.

        canvas.draw(background, 0, 0, spaceJunk.getxMax(), spaceJunk.getyMax(),
                backgroundImageIndex, 0, spaceJunk.getxMax(), Math.min(background.getHeight(),
                        spaceJunk.getyMax()), false, false);

        // Only move background is game is currently running
        if (this.state == State.RUN) {
            backgroundImageIndex += (int) (GameConstants.BACKGROUND_SPEED * GameScreen.SCALE_X_FACTOR);
        }

        if (backgroundImageIndex > background.getWidth()) {
            backgroundImageIndex = 0;
        }
    }

    private void drawGameOverScreen() {
        canvas.draw(gameOver, Gdx.graphics.getWidth() / 2 - GameScreen.getScaledTextureWidth(gameOver) / 2,
                Gdx.graphics.getHeight() / 2 - GameScreen.getScaledTextureHeight(gameOver) / 2,
                GameScreen.getScaledTextureWidth(gameOver),
                GameScreen.getScaledTextureHeight(gameOver));
    }

    private void drawSettingsMenu() {
        canvas.draw(settingsMenu, Gdx.graphics.getWidth() / 2 - GameScreen.getScaledTextureWidth(settingsMenu) / 2,
                Gdx.graphics.getHeight() / 2 - GameScreen.getScaledTextureHeight(settingsMenu) / 2,
                GameScreen.getScaledTextureWidth(settingsMenu),
                GameScreen.getScaledTextureHeight(settingsMenu));

    }

    private void drawPauseScreenTexture() {
        canvas.draw(pauseScreen, Gdx.graphics.getWidth() / 2 - GameScreen.getScaledTextureWidth(pauseScreen) / 2,
                Gdx.graphics.getHeight() / 2 - GameScreen.getScaledTextureHeight(pauseScreen) / 2,
                GameScreen.getScaledTextureWidth(pauseScreen),
                GameScreen.getScaledTextureHeight(pauseScreen));
    }

    private void displayScore() {

        double currentGameScore = round(spaceJunk.getCurrentGameScore(), 2);
        int currentMultiplier = spaceJunk.getLevel().getScoreRateMultiplier();

        GlyphLayout layout = new GlyphLayout(scoreFont, String.valueOf(currentGameScore));
        GlyphLayout multiplierLayout = new GlyphLayout(scoreFont, String.valueOf(currentMultiplier));

        scoreFont.draw(canvas, String.valueOf(currentGameScore),
                Gdx.graphics.getWidth() / 2 - layout.width / 2,
                Gdx.graphics.getHeight());


        scoreFont.draw(canvas, "x" + String.valueOf((int) spaceJunk.getLevel().getScoreRateMultiplier()),
                Gdx.graphics.getWidth() / 2 - multiplierLayout.width / 2,
                multiplierLayout.height);

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
        if (spaceJunk.getCharacter().getRemainingLives() <= 0) {
            // player had no lives left
            this.state = State.CRASHED;
        } else {
            this.state = State.RUN;
            controller.getOptionsMenu().setMiddleButtonTextureToPause();
        }
    }

    @Override
    public void hide() {

    }

    public State getState() {
        return this.state;
    }

    public SpaceJunk getSpaceJunk() {
        return spaceJunk;
    }

    public Texture getSettingsMenu() {
        return settingsMenu;
    }

    public boolean isSoundSetting() {
        return soundSetting;
    }

    public boolean isVibrationSetting() {
        return vibrationSetting;
    }

    public boolean isRecordAudioSetting() {
        return recordAudioSetting;
    }
}
