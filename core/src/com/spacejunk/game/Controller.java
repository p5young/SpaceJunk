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
        this.optionsMenu = new OptionsMenu(currentGame);
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

            int yStart = Gdx.graphics.getHeight() / 2;
            boolean ySet = false;       // true when yStart has valid value


            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                if (gameScreen.getState() == GameScreen.State.RUN
                        && gameScreen.gameActive()
                        && noButtonsPressed()) {
                    //Gdx.app.log("applog", "TOUCHDOWN");
                    yStart = y;
                    ySet = true;
                    return true; // return true to indicate the event was handled
                }
                return false;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                if (!ySet) return false;
                if (gameScreen.getState() == GameScreen.State.RUN) {
                    //Gdx.app.log("applog", "TOUCHUP");
                    currentGame.getCharacter().moveCharacter(Gdx.graphics.getHeight() - y);
                    ySet = false;
                    return true; // return true to indicate the event was handled
                }
                yStart = Gdx.graphics.getHeight() / 2;
                ySet = false;
                return false;
            }

            @Override
            public boolean touchDragged (int x, int y, int pointer) {
                if (!ySet) return false;
                if (gameScreen.getState() == GameScreen.State.RUN) {
                    if ((yStart - y) > 50) {
                        currentGame.getCharacter().moveCharacter(Gdx.graphics.getHeight());
                        ySet = false;
                    } else if ((y - yStart) > 50) {
                        currentGame.getCharacter().moveCharacter(0);
                        ySet = false;
                    }
                    return true; // return true to indicate the event was handled
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
