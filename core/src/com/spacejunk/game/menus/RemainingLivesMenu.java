package com.spacejunk.game.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.GameScreen;
import com.spacejunk.game.SpaceJunk;

/**
 * Created by vidxyz on 2/12/18.
 */

public class RemainingLivesMenu {

    public static final int PADDING = 20;

    private SpaceJunk currentGame;
    Texture[] remainingLivesTextures;


    public RemainingLivesMenu(SpaceJunk currentGame) {

        this.currentGame = currentGame;

        // Must use spaceJunk.getLevel().getMaxLives() here
        remainingLivesTextures = new Texture[currentGame.getLevel().getMaxLives()];
        for(int i = 0; i < currentGame.getLevel().getMaxLives(); i++) {
            remainingLivesTextures[i] = this.currentGame.getManager().get("heart.png");
        }
    }

    public void render(SpriteBatch canvas) {
        int remainingLives = currentGame.getCharacter().getRemainingLives();
        for(int i = 0; i < remainingLives; i++) {
            canvas.draw(remainingLivesTextures[i], Gdx.graphics.getWidth() - ((i + 1) * GameScreen.getScaledTextureWidth(remainingLivesTextures[i])) - PADDING,
                    Gdx.graphics.getHeight() -  GameScreen.getScaledTextureHeight(remainingLivesTextures[i]) - PADDING,
                    GameScreen.getScaledTextureWidth(remainingLivesTextures[i]),
                    GameScreen.getScaledTextureHeight(remainingLivesTextures[i]));
        }
    }

}
