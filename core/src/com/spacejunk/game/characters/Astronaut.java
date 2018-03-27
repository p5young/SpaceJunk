package com.spacejunk.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
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

        // Load the sprite sheet as a Texture
        defaultAnimationSheet = new Texture(Gdx.files.internal("astronaut_animation_sheet.png"));
        hammerAnimationSheet = new Texture(Gdx.files.internal("hammer_animation_sheet.png"));
        gasMaskAnimationSheet = new Texture(Gdx.files.internal("gasmask_animation_sheet.png"));
        fireSuitAnimationSheet = new Texture(Gdx.files.internal("firesuit_animation_sheet.png"));
        invisibilityAnimationSheet = new Texture(Gdx.files.internal("invisibility_animation_sheet.png"));

        FileHandle handle = Gdx.files.internal("astronaut_texture_1.png");
        this.pixmap = new Pixmap(handle);

        FRAME_COLS = 3;
        FRAME_ROWS = 1;

        super.create();

    }


}
