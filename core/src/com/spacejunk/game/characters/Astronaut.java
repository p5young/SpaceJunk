package com.spacejunk.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spacejunk.game.SpaceJunk;

/**
 * Created by vidxyz on 2/9/18.
 */

public class Astronaut extends Character  {

    public Astronaut(SpaceJunk game) {
        this.currentPlatform = PLATFORM.MIDDLE;
        this.currentGame = game;
    }

    public void create() {

        characterTextures[0] = new Texture("astronaut_texture_1.png");
        characterTextures[1] = new Texture("astronaut_texture_2.png");
        characterTextures[2] = new Texture("astronaut_texture_3.png");

        // Load the sprite sheet as a Texture
        animationSheet = new Texture(Gdx.files.internal("astronaut_animation_sheet.png"));

        FRAME_COLS = 5;
        FRAME_ROWS = 2;

        super.create();

    }


}
