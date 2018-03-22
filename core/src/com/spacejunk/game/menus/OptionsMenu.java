package com.spacejunk.game.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by vidxyz on 2/9/18.
 */

public class OptionsMenu {

    // BUTTON_0 <==> MAIN MENU
    // BUTTON_1 <==> PLAY/PAUSE
    // BUTTON_2 <==> RESTART

    public static final int MAX_BUTTONS = 3;

    private Texture[] optionsMenuTexures;

    public OptionsMenu() {

        optionsMenuTexures = new Texture[MAX_BUTTONS];

        optionsMenuTexures[0] = new Texture("main_menu.png");
        optionsMenuTexures[1] = new Texture("pause_button.png");
        optionsMenuTexures[2] = new Texture("restart_button.png");
    }

    public void render(SpriteBatch canvas) {

        for(int i = 0; i < MAX_BUTTONS; i++) {
            if(i != 0) {
                canvas.draw(optionsMenuTexures[i], 0,
                        Gdx.graphics.getHeight() - ((i+1) * optionsMenuTexures[i-1].getHeight()));
            }
            else {
                canvas.draw(optionsMenuTexures[i], 0, Gdx.graphics.getHeight() - ((i+1) * optionsMenuTexures[i].getHeight()));
            }
        }
    }

    public Texture[] getOptionsMenuTexures() {
        return optionsMenuTexures;
    }

    public void setMiddleButtonTextureToPlay() {
        optionsMenuTexures[1] = new Texture("play_button.png");
    }

    public void setMiddleButtonTextureToPause() {
        optionsMenuTexures[1] = new Texture("pause_button.png");
    }

}
