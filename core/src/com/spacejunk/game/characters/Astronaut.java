package com.spacejunk.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

        super.create();

    }


}
