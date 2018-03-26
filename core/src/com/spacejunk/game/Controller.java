package com.spacejunk.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.menus.ConsumablesMenu;
import com.spacejunk.game.menus.OptionsMenu;
import com.spacejunk.game.utilities.SimpleDirectionGestureDetector;

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

    public boolean mainMenuPlayButtonIsTouched() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 450 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 450 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - 10 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 120;

    }

    public boolean aboutButtonIsTouched() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 450 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 450 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + 250 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 380;

    }

    public boolean howToPlayButtonIsTouched() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 450 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 450 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + 120 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 250;

    }

    public boolean howToPlayBackButtonPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 9) - 150 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 9) + 150 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - 75 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 75;
    }

    public boolean howToPlayPlayButtonPressed() {
        return Gdx.input.getX() >= ((8 * currentGame.getxMax()) / 9) - 150 &&
                Gdx.input.getX() <= ((8 *currentGame.getxMax()) / 9) + 150 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - 75 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 75;
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
                Gdx.input.getX() <= optionsMenu.getScreenRecordButton().getWidth() +
                        (int) (optionsMenu.getScreenRecordButton().getWidth() * 0.1) &&
                /*Check y coorindates now*/
                Gdx.input.getY() <= currentGame.getyMax() &&
                Gdx.input.getY() >= currentGame.getyMax() - optionsMenu.getScreenRecordButton().getHeight();
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
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 280 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 280 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) - 70 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 10;
    }


    public boolean settingsMenuVibrateSettingIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 280 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 280 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + 10 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 90;
    }

    public boolean settingsMenuRecordAudioSettingIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 280 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 280 &&
                Gdx.input.getY() >= (currentGame.getyMax() / 2) + 90 &&
                Gdx.input.getY() <= (currentGame.getyMax() / 2) + 170;
    }



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
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                if (gameScreen.getState() == GameScreen.State.RUN) {
                    currentGame.getCharacter().moveCharacter(Gdx.graphics.getHeight());
                    Gdx.app.log("swipelog", "onUp caled");
                }
            }

            @Override
            public void onRight() {

            }

            @Override
            public void onLeft() {

            }

            @Override
            public void onDown() {
                if (gameScreen.getState() == GameScreen.State.RUN) {
                    currentGame.getCharacter().moveCharacter(0);
                    Gdx.app.log("swipelog", "onDown caled");
                }
            }

        }));
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
