package com.spacejunk.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.menus.ConsumablesMenu;
import com.spacejunk.game.menus.OptionsMenu;

/**
 * Created by vidxyz on 2/9/18.
 */

public class Controller {

    private SpaceJunk currentGame;

    private OptionsMenu optionsMenu;
    private ConsumablesMenu consumablesMenu;

    public Controller(SpaceJunk currentGame) {
        this.currentGame = currentGame;
        this.optionsMenu = new OptionsMenu();
        this.consumablesMenu = new ConsumablesMenu();
    }

    public boolean isTouched() {
        return Gdx.input.justTouched();
    }

    public boolean playPauseButtonisPressed() {
        return Gdx.input.getX() >= optionsMenu.getOptionsMenuTexures()[0].getWidth() &&
            Gdx.input.getX() <= optionsMenu.getOptionsMenuTexures()[0].getWidth() +
                optionsMenu.getOptionsMenuTexures()[1].getWidth() &&
                Gdx.input.getY() <= optionsMenu.getOptionsMenuTexures()[0].getHeight() &&
                Gdx.input.getY() >= 0;
    }

    public void render(SpriteBatch canvas) {
        optionsMenu.render(canvas);
        consumablesMenu.render(canvas);
    }

    public OptionsMenu getOptionsMenu() {
        return optionsMenu;
    }

    public ConsumablesMenu getConsumablesMenu() {
        return consumablesMenu;
    }
}
