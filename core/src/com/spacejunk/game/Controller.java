package com.spacejunk.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.menus.ConsumablesMenu;
import com.spacejunk.game.menus.OptionsMenu;

/**
 * Created by vidxyz on 2/9/18.
 * This class handles user input
 */

public class Controller {

    private SpaceJunk currentGame;

    private OptionsMenu optionsMenu;
    private ConsumablesMenu consumablesMenu;

    private GameScreen gameScreen;

    private Consumable.CONSUMABLES pressedConsumable = null;

    public Controller(SpaceJunk currentGame, GameScreen gameScreen) {
        this.currentGame = currentGame;
        this.gameScreen = gameScreen;
        this.optionsMenu = new OptionsMenu();
        this.consumablesMenu = new ConsumablesMenu(currentGame);
    }


    public boolean isTouched() {
        return Gdx.input.justTouched();
    }


    // -------------------------------------------------
    // MAIN MENU INTERACTIONS
    // -------------------------------------------------


    public boolean mainMenuPlayButtonIsTouched() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (450 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (450 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - ((int) (10 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (120 * GameScreen.SCALE_Y_FACTOR));

    }


    public boolean howToPlayButtonIsTouched() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (450 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (450 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + ((int) (120 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (250 * GameScreen.SCALE_Y_FACTOR));

    }

    public boolean aboutButtonIsTouched() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (450 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (450 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + ((int) (250 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (380 * GameScreen.SCALE_Y_FACTOR));

    }

    // -------------------------------------------------
    // ABOUT SCREEN INTERACTIONS
    // -------------------------------------------------


    public boolean howToPlayBackButtonPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 9) - ((int) (150 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 9) + ((int) (150 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - ((int) (75 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (75 * GameScreen.SCALE_Y_FACTOR));
    }

    public boolean howToPlayPlayButtonPressed() {
        return Gdx.input.getX() >= ((8 * currentGame.getxMax()) / 9) - ((int) (150 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= ((8 *currentGame.getxMax()) / 9) + ((int) (150 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - ((int) (75 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (75 * GameScreen.SCALE_Y_FACTOR));
    }


    // -------------------------------------------------
    // OPTIONS MENU INTERACTIONS
    // -------------------------------------------------

    public boolean settingsMenuButtonIsPressed() {
        return Gdx.input.getX() >= 0 &&
                Gdx.input.getX() <= GameScreen.getScaledTextureWidth(optionsMenu.getOptionsMenuTexures()[0]) &&
                Gdx.input.getY() >= GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[0]) +
                        GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[1]) &&
                Gdx.input.getY() <= GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[0]) +
                        GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[1]) +
                        GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[2]);
    }


    public boolean mainMenuButtonIsPressed() {
        return Gdx.input.getX() >= 0 &&
                Gdx.input.getX() <= GameScreen.getScaledTextureWidth(optionsMenu.getOptionsMenuTexures()[0]) &&
                Gdx.input.getY() >= 0 &&
                Gdx.input.getY() <= GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[0]);
    }


    public boolean playPauseButtonisPressed() {
        return Gdx.input.getX() >= 0 &&
                Gdx.input.getX() <= GameScreen.getScaledTextureWidth(optionsMenu.getOptionsMenuTexures()[0]) &&
                Gdx.input.getY() >= GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[0]) &&
                Gdx.input.getY() <= GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[0]) +
                        GameScreen.getScaledTextureHeight(optionsMenu.getOptionsMenuTexures()[1]);
    }

    // ---------------------------------------------------------------------------



    // Returns true if none of the settings menu is interacted with
    public boolean settingsMenuBackButtonIsPressed() {
        return !(settingsMenuSoundsSettingIsPressed() || settingsMenuVibrateSettingIsPressed() || settingsMenuRecordAudioSettingIsPressed());
    }




    public boolean screenRecordButtonIsPressed() {
        return Gdx.input.getX() >= 0 &&
                Gdx.input.getX() <=  GameScreen.getScaledTextureWidth(optionsMenu.getScreenRecordButton()) +
                        (int) (GameScreen.getScaledTextureWidth(optionsMenu.getScreenRecordButton()) * 0.1) &&
                /*Check y coorindates now*/
                Gdx.input.getY() <= currentGame.getyMax() &&
                Gdx.input.getY() >= currentGame.getyMax() - GameScreen.getScaledTextureHeight(optionsMenu.getScreenRecordButton());
    }


    // -------------------------------------------------
    // PAUSE MENU INTERACTIONS
    // -------------------------------------------------


    public boolean pauseScreenMainMenuButtonIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) - ((int) (120 * GameScreen.SCALE_X_FACTOR)) &&
                /*Check y coordinates now*/
                Gdx.input.getY() >= (currentGame.getyMax() / 2) &&
                Gdx.input.getY() <=  (currentGame.getyMax() / 2) + ((int) (140 * GameScreen.SCALE_Y_FACTOR));
    }


    public boolean pauseScreenResumeButtonIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (80 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (80 * GameScreen.SCALE_X_FACTOR)) &&
                /*Check y coordinates now*/
                Gdx.input.getY() >= (currentGame.getyMax() / 2) &&
                Gdx.input.getY() <=  (currentGame.getyMax() / 2) + ((int) (140 * GameScreen.SCALE_Y_FACTOR));
    }


    public boolean pauseScreenSettingsMenuButtonIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) + ((int) (120 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                /*Check y coordinates now*/
                Gdx.input.getY() >= (currentGame.getyMax() / 2) &&
                Gdx.input.getY() <=  (currentGame.getyMax() / 2) + ((int) (140 * GameScreen.SCALE_Y_FACTOR));
    }


    // -------------------------------------------------
    // SETTINGS MENU INTERACTIONS
    // -------------------------------------------------


    public boolean settingsMenuSoundsSettingIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - ((int) (70 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (10 * GameScreen.SCALE_Y_FACTOR));
    }


    public boolean settingsMenuVibrateSettingIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + ((int) (10 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (90 * GameScreen.SCALE_Y_FACTOR));
    }

    public boolean settingsMenuRecordAudioSettingIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + ((int) (280 * GameScreen.SCALE_X_FACTOR)) &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + ((int) (90 * GameScreen.SCALE_Y_FACTOR)) &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + ((int) (170 * GameScreen.SCALE_Y_FACTOR));
    }

    // -------------------------------------------------
    // CONSUMABLES MENU INTERACTIONS
    // -------------------------------------------------



    public boolean consumablesMenuPressed() {
        if (Gdx.input.getX() > (currentGame.getxMax() - consumablesMenu.getInventoryWidth()) &&
                Gdx.input.getY() > (currentGame.getyMax() - consumablesMenu.getInventoryHeight())) {

            int inventoryXCoordinate = Gdx.input.getX() - (currentGame.getxMax() - consumablesMenu.getInventoryWidth());
            float xCoordFraction = inventoryXCoordinate / (float) consumablesMenu.getInventoryWidth();

            if (xCoordFraction < 0.25) {
                this.pressedConsumable = Consumable.CONSUMABLES.INVISIBILITY;
            } else if (xCoordFraction < 0.5) {
                this.pressedConsumable = Consumable.CONSUMABLES.GAS_MASK;
            } else if (xCoordFraction < 0.75) {
                this.pressedConsumable = Consumable.CONSUMABLES.FIRESUIT;
            } else if (xCoordFraction < 1) {
                this.pressedConsumable = Consumable.CONSUMABLES.SPACE_HAMMER;
            } else {
                // this else statement should never be reached
                return false;
            }
            return true;


        } else {
            return false;
        }

    }

    public boolean astronautTapped() {
        float x = Gdx.input.getX();
        float y = getTouchYCoordinate();

        return this.currentGame.getCharacter().getCharacterShape().contains(x, y);
    }

    public void render(SpriteBatch canvas) {
        optionsMenu.render(canvas);
        consumablesMenu.render(canvas);
    }


    public void setupSwipeDetection() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            int yStart;
            int xStart;
            boolean touched = false;       // true when yStart has valid value
            boolean dragged = false;    // true if a drag is detected


            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                if (gameScreen.getState() == GameScreen.State.RUN               // during gameplay
                        && gameScreen.gameActive()
                        && noButtonsPressed()) {
                    yStart = y;
                    touched = true;
                    dragged = false;
                    return true; // return true to indicate the event was handled
                } else if (gameScreen.getState() == GameScreen.State.RUN        // start game prompt
                        && !gameScreen.gameActive()) {
                    Gdx.app.log("applog", "TOUCHDOWN --- X: " + x + " Y: " + y);
                    Gdx.app.log("applog", "x BOUNDS --- MIN: " + (Gdx.graphics.getWidth() - (int)(300 * GameScreen.SCALE_X_FACTOR)) / 2 + " MAX: " + (Gdx.graphics.getWidth() + (int)(300 * GameScreen.SCALE_X_FACTOR)) / 2);
                    if (x >= (Gdx.graphics.getWidth() - (int)(300 * GameScreen.SCALE_X_FACTOR)) / 2     // play button pressed
                        && x <= (Gdx.graphics.getWidth() + (int)(300 * GameScreen.SCALE_X_FACTOR)) / 2
                        && y >= Gdx.graphics.getHeight() / 3 - (int)(150 * GameScreen.SCALE_X_FACTOR) / 2
                        && y <= Gdx.graphics.getHeight() / 3 + (int)(150 * GameScreen.SCALE_X_FACTOR) / 2) {
                            currentGame.getCharacter().setSpeedModifier(gameScreen.getVelocityMod());   // set player speed
                            currentGame.getSystemServices().setSpeed(gameScreen.getVelocityMod());      // save player speed in SharedPreferences
                            gameScreen.setGameActive(); // start game
                            return true;
                    } else {            // play button not pressed
                        xStart = x;
                        touched = true;
                        dragged = false;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (!touched) return false;

                if (gameScreen.getState() == GameScreen.State.RUN
                        && gameScreen.gameActive()) {
                    //Gdx.app.log("applog", "TOUCHUP");
                    if (!dragged) {
                        currentGame.getCharacter().moveCharacter(Gdx.graphics.getHeight() - y);
                    } else if (y < yStart) {
                        currentGame.getCharacter().moveCharacter(Gdx.graphics.getHeight());
                    } else {
                        currentGame.getCharacter().moveCharacter(0);
                    }
                    yStart = Gdx.graphics.getHeight() / 2;
                    touched = false;
                    dragged = false;
                    return true; // return true to indicate the event was handled
                } else if (gameScreen.getState() == GameScreen.State.RUN        // start game prompt
                        && !gameScreen.gameActive()) {
                    touched = false;
                    dragged = false;
                    return true;
                }
                touched = false;
                dragged = false;
                return false;
            }

            @Override
            public boolean touchDragged (int x, int y, int pointer) {
                if (!touched) return false;

                if (gameScreen.getState() == GameScreen.State.RUN &&
                        gameScreen.gameActive()) {
                    if (Math.abs(y - yStart) > 50)
                        dragged = true;
                    return true; // return true to indicate the event was handled
                } else if (gameScreen.getState() == GameScreen.State.RUN &&
                        !gameScreen.gameActive()){
                    if ((x - xStart) > (Gdx.graphics.getHeight() / 9)) {
                        int currentMod = gameScreen.getVelocityMod();
                        if (currentMod < 3)
                            gameScreen.setVelocityMod(currentMod + 1);
                        xStart = x;
                        return true; // return true to indicate the event was handled
                    } else if ((xStart - x) > (Gdx.graphics.getHeight() / 9)) {
                        int currentMod = gameScreen.getVelocityMod();
                        if (currentMod > -3)
                            gameScreen.setVelocityMod(currentMod - 1);
                        xStart = x;
                        return true; // return true to indicate the event was handled
                    }
                }
                return false;
            }

        });
    }

    private boolean noButtonsPressed() {
        if (playPauseButtonisPressed()) {
            return false;
        }

        // Checking for main menu button press
        else if (mainMenuButtonIsPressed()) {
            return false;
        }

        // Checking for settings menu button press
        else if (settingsMenuButtonIsPressed()) {
            return false;
        }

        // Checking for screen record tap
        else if (screenRecordButtonIsPressed()) {
            return false;
        }

        // Checking interaction with consumable menu
        else if (consumablesMenuPressed()) {
            return false;
        }

        // This is for unequipping the current consumable
        else if (astronautTapped()) {
            return false;
        }

        return true;
    }

    public OptionsMenu getOptionsMenu() {
        return optionsMenu;
    }

    public ConsumablesMenu getConsumablesMenu() {
        return consumablesMenu;
    }

    public int getTouchYCoordinate() {
        // Flip y coordinate to match LibGDX (origin in bottom left)
        return Gdx.graphics.getHeight() - Gdx.input.getY();
    }

    public int getTouchXCoordinate() {
        return Gdx.input.getX();
    }

    public Consumable.CONSUMABLES getPressedConsumable() {
        return pressedConsumable;
    }

    public boolean touching() {
        return Gdx.input.isTouched();
    }

}
