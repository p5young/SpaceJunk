package com.spacejunk.game;


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

    public boolean playPauseButtonisPressed() {
        return Gdx.input.getX() >= 0 &&
            Gdx.input.getX() <= optionsMenu.getOptionsMenuTexures()[0].getWidth() &&
                Gdx.input.getY() >= optionsMenu.getOptionsMenuTexures()[0].getHeight() &&
                Gdx.input.getY() <= optionsMenu.getOptionsMenuTexures()[0].getHeight() +
                        optionsMenu.getOptionsMenuTexures()[1].getHeight();
    }

    public boolean screenRecordButtonIsPressed() {
        Gdx.app.log("gdxlog", "(x,y) = " + Gdx.input.getX() + ":" + Gdx.input.getY());
        boolean toReturn = Gdx.input.getX() >= 0 &&
                Gdx.input.getX() <= optionsMenu.getScreenRecordButton().getWidth() +
                                (int) (optionsMenu.getScreenRecordButton().getWidth() * 0.1) &&
                /*Check y coorindates now*/
                Gdx.input.getY() <= currentGame.getyMax() &&
                Gdx.input.getY() >= currentGame.getyMax() - optionsMenu.getScreenRecordButton().getHeight();

        Gdx.app.log("gdxlog", "Going to return " + String.valueOf(toReturn));
        return toReturn;
    }


    public boolean pauseScreenResumeButtonIsPressed() {
        return Gdx.input.getX() >= (currentGame.getxMax() / 2) - 70 &&
                Gdx.input.getX() <= (currentGame.getxMax() / 2) + 70 &&
                /*Check y coordinates now*/
                Gdx.input.getY() >= (currentGame.getyMax() / 2) &&
                Gdx.input.getY() <=  (currentGame.getyMax() / 2) + 140;
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

    public void render(SpriteBatch canvas) {
        optionsMenu.render(canvas);
        consumablesMenu.render(canvas);
    }

    public void setupSwipeDetection() {
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                currentGame.getCharacter().moveCharacter(Gdx.graphics.getHeight());
                Gdx.app.log("swipelog", "onUp caled");
            }

            @Override
            public void onRight() {

            }

            @Override
            public void onLeft() {

            }

            @Override
            public void onDown() {
                currentGame.getCharacter().moveCharacter(0);
                Gdx.app.log("swipelog", "onDown caled");
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

}
