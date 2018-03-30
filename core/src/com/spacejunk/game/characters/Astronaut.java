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

        characterTextures[0] = this.currentGame.getManager().get("astronaut_texture_1.png");
        characterTextures[1] = this.currentGame.getManager().get("astronaut_texture_2.png");
        characterTextures[2] = this.currentGame.getManager().get("astronaut_texture_3.png");

        // Load the sprite sheet as a Texture
        defaultAnimationSheet = this.currentGame.getManager().get("astronaut_animation_sheet.png");
        hammerAnimationSheet = this.currentGame.getManager().get("hammer_animation_sheet.png");
        gasMaskAnimationSheet = this.currentGame.getManager().get("gasmask_animation_sheet.png");
        fireSuitAnimationSheet = this.currentGame.getManager().get("firesuit_animation_sheet.png");
        invisibilityAnimationSheet = this.currentGame.getManager().get("invisibility_animation_sheet.png");

        this.pixmap = this.currentGame.getManager().getPixmap("astronaut_texture_1.png");

        FRAME_COLS = 3;
        FRAME_ROWS = 1;

        super.create();

    }


}
